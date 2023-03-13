package com.project.stressAI.service;

import java.io.IOException;

import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
//import org.deeplearning4j.eval.;
//import org.deeplearning4j.datasets.iterator.utilty.ListDataSetIterator;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.SplitTestAndTrain;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.NormalizerStandardize;
import org.nd4j.linalg.io.ClassPathResource;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.springframework.stereotype.Service;

@Service
public class StressDetectorServiceImpl implements StressDetectorService {

	@Override
	public String getStress() {
		try {
			DataSet dataSet = loadHeartRateData();

			DataSet dataInput = loadHeartRateInput();
			double Stress = 0;
			String evaluationJson;

//			dataSet.shuffle(42);

			DataNormalization normalizer = new NormalizerStandardize();
			normalizer.fit(dataSet);
			normalizer.transform(dataSet);

			normalizer.fit(dataInput);
			normalizer.transform(dataInput);

			SplitTestAndTrain testAndTrain = dataSet.splitTestAndTrain(0.65);
			DataSet trainingData = testAndTrain.getTrain();
			DataSet testData = testAndTrain.getTest();

//			DataSetIterator dataSetIterator = new ListDataSetIterator(dataSet.asList());
			// Define the model
			MultiLayerConfiguration configuration = new NeuralNetConfiguration.Builder().iterations(1000)
					.activation(Activation.TANH).weightInit(WeightInit.XAVIER)
//					.regularization(true)
//					.learningRate(0.1).l2(0.0001)
					.list().layer(0, new DenseLayer.Builder().nIn(2).nOut(3).build())
					.layer(1, new DenseLayer.Builder().nIn(3).nOut(3).build())
					.layer(2, new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
							.activation(Activation.SOFTMAX).nIn(3).nOut(3).build())
					.build();

			MultiLayerNetwork model = new MultiLayerNetwork(configuration);
			model.init();

			model.fit(trainingData);
			model.fit(dataInput);

			INDArray labels = dataInput.getLabels();
			INDArray output = model.output(dataSet.getFeatures());
			System.out.println(output);

			Evaluation eval = new Evaluation(3);
			eval.eval(labels, output);
			System.out.println(eval.stats());

			String status="";
			for(int j=0;j<500;j++) {
		        for(int i=0;i<2;i++) {
		        	
		        	double hrvStressPredictions = output.getRow(j).getDouble(i);
		        	double stressScore = output.getDouble(i, i) - output.getDouble(i, i+1);
		        	System.out.println(stressScore);
		            if(stressScore > 0.001) {
		            	return "stressed";
//		            	if(i==0)
//		            		return "not stressed";
//		            	if(i==1)
//		            		return "mild stress";
//		            	if(i==2)
//		            		return "stressed";
		            	
		            }
		        }
		        }
			// JSON Object

//		ObjectMapper objectMapper = new ObjectMapper();
//		evaluationJson = objectMapper.writeValueAsString(eval);
//		System.out.println(evaluationJson);

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		return null;

	}

	public static double calculateStress(double heartRate, double hrv, double heartRateWeight, double hrvWeight) {
		// Calculate stress
		double stress = (heartRateWeight * heartRate) + (hrvWeight * hrv);
		return stress;
	}

	public static DataSet loadHeartRateData() throws IOException, InterruptedException {
		DataSet dataSet = null;
		try (RecordReader recordReader = new CSVRecordReader(0, ',')) {
			recordReader.initialize(new FileSplit(new ClassPathResource("HRVDataSet.txt").getFile()));
			DataSetIterator iterator = new RecordReaderDataSetIterator(recordReader, 501, 2, 3);
			dataSet = iterator.next();

		}

		return dataSet;
	}

	public static DataSet loadHeartRateInput() throws IOException, InterruptedException {
		DataSet dataSet;
		try (RecordReader recordReader = new CSVRecordReader(0, ',')) {
			recordReader.initialize(new FileSplit(new ClassPathResource("HRV-InputData.txt").getFile()));
			DataSetIterator iterator = new RecordReaderDataSetIterator(recordReader, 501, 2, 3);
			dataSet = iterator.next();
		}
		return dataSet;
	}

}
