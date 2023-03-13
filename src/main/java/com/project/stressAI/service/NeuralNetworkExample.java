package com.project.stressAI.service;

import org.deeplearning4j.datasets.iterator.utilty.ListDataSetIterator;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.nd4j.linalg.learning.config.Nesterovs;

import org.deeplearning4j.datasets.iterator.impl.ListDataSetIterator;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;

import java.util.ArrayList;
import java.util.List;

public class NeuralNetworkExample {
    public static void main(String[] args) {
        // Define the input and output values
        INDArray input = Nd4j.create(new double[][] {{190,100},{100,80}});
        INDArray output = Nd4j.create(new double[][] {{1,0}});
        
        // Define the neural network configuration
        int numInputs = input.shape()[1];
        int numOutputs = output.shape()[1];
        int numHiddenNodes = 10;
        int numEpochs = 1000;
        double learningRate = 0.1;
        
        MultiLayerConfiguration config = new NeuralNetConfiguration.Builder()
                .weightInit(WeightInit.XAVIER)
                .updater(new Nesterovs(learningRate, Nesterovs.DEFAULT_NESTEROV_MOMENTUM))
                .list()
                .layer(new DenseLayer.Builder()
                        .nIn(numInputs)
                        .nOut(numHiddenNodes)
                        .activation(Activation.RELU)
                        .build())
                .layer(new OutputLayer.Builder(LossFunctions.LossFunction.MSE)
                        .nIn(numHiddenNodes)
                        .nOut(numOutputs)
                        .activation(Activation.SIGMOID)
                        .build())
                .build();

        // Create the neural network and set up a listener for training progress
        MultiLayerNetwork model = new MultiLayerNetwork(config);
        model.init();
        model.setListeners(new ScoreIterationListener(100));

        // Train the neural network
        List<DataSet> data = new ArrayList<>();
        data.add(new DataSet(input, output));
        ListDataSetIterator<DataSet> dataIter = new ListDataSetIterator<>(data);
        for (int i = 0; i < numEpochs; i++) {
            model.fit(dataIter);
        }

        // Use the trained neural network to predict the output for the input
        INDArray predictedOutput = model.output(input);
        System.out.println(predictedOutput);
    }
}
