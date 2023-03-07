package com.project.stressAI.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.datavec.api.writable.Writable;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.datasets.iterator.impl.ListDataSetIterator;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.conf.BackpropType;
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
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.io.ClassPathResource;
import org.nd4j.linalg.lossfunctions.LossFunctions;

public class StressDetectorServiceImpl {

	public static void main(String[] args) {
		try {
			DataSet dataSet = loadHeartRateData();
			double Stress = 0;

			List<Double> stressList = new ArrayList<>();
			for (int i = 0; i <= dataSet.numExamples() - 1; i++) {
				Stress = calculateStress(dataSet.getFeatures().getRow(i).getColumn(0).getDouble(0),
						dataSet.getFeatures().getRow(i).getColumn(1).getDouble(0),
						dataSet.getFeatures().getRow(i).getColumn(2).getDouble(0),
						dataSet.getFeatures().getRow(i).getColumn(3).getDouble(0));
				stressList.add(Stress);
			}

			System.out.println(stressList);

			for (Double d : stressList) {
				if (d >= 20000) {
					System.out.println("High Stress");
				}
			}

			dataSet.shuffle(42);

			DataNormalization normalizer = new NormalizerStandardize();
			normalizer.fit(dataSet);
			normalizer.transform(dataSet);

			SplitTestAndTrain testAndTrain = dataSet.splitTestAndTrain(0.65);
			DataSet trainingData = testAndTrain.getTrain();
			DataSet testData = testAndTrain.getTest();

			DataSetIterator dataSetIterator = new ListDataSetIterator(dataSet.asList());
			// Define the model
			MultiLayerConfiguration configuration = new NeuralNetConfiguration.Builder().iterations(1000)
					.activation(Activation.TANH).weightInit(WeightInit.XAVIER).regularization(true).learningRate(0.1)
					.l2(0.0001).list().layer(0, new DenseLayer.Builder().nIn(4).nOut(3).build())
					.layer(1, new DenseLayer.Builder().nIn(3).nOut(3).build())
					.layer(2,
							new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
									.activation(Activation.SOFTMAX).nIn(3).nOut(3).build())
					.backpropType(BackpropType.Standard).pretrain(false).build();

			MultiLayerNetwork model = new MultiLayerNetwork(configuration);
			model.init();
//	        model.fit(dataSetIterator);
//	        INDArray output = model.output(dataSet.getFeatures());
			Evaluation evaluation = new Evaluation(5);
			while (dataSetIterator.hasNext()) {
				DataSet testDataSet = dataSetIterator.next();
				INDArray features = testDataSet.getFeatures();
//	            INDArray labels = testDataSet.getFeatures();
				INDArray labels = Nd4j.create(new double[][] { { 0.40, 0.33, 0.27 }, { 0.24, 0.38, 0.37 },
						{ 0.28, 0.34, 0.39 }, { 0.42, 0.32, 0.26 }, { 0.37, 0.27, 0.37 } });
				INDArray predicted = model.output(features, false);
				System.out.println(labels);
				System.out.println(predicted);
				evaluation.eval(labels, predicted);
			}
			System.out.println(evaluation.stats());

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static double calculateStress(double heartRate, double hrv, double heartRateWeight, double hrvWeight) {
		// Calculate stress
		double stress = (heartRateWeight * heartRate) + (hrvWeight * hrv);
		return stress;
	}

	public static DataSet loadHeartRateData() throws IOException, InterruptedException {
		DataSet dataSet;
		try (RecordReader recordReader = new CSVRecordReader(0, ',')) {
			recordReader.initialize(new FileSplit(new ClassPathResource("HRV-DataSet.txt").getFile()));
			DataSetIterator iterator = new RecordReaderDataSetIterator(recordReader, 5, 4, 186);
			dataSet = iterator.next();
		}

//		dataSet.shuffle(42);
		List<List<Writable>> allData = new ArrayList<>();

		List<List<String>> stringListList = new ArrayList<>();

		for (List<Writable> innerList : allData) {
			List<String> result = new ArrayList<>();
			for (Writable w : innerList) {
				result.add(w.toString());
			}
			stringListList.add(result);
		}
		return dataSet;

	}
}