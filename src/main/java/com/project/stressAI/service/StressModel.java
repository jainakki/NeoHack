package com.project.stressAI.service;// Import necessary libraries
import java.io.File;
import java.io.IOException;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class StressModel {

  public static void main(String[] args) {
    try {
      // Load dataset
      DataSource source = new DataSource("path/to/dataset.arff");
      Instances data = source.getDataSet();
      if (data.classIndex() == -1) {
        data.setClassIndex(data.numAttributes() - 1);
      }

      // Split dataset into training and testing sets
      Instances trainingData = data.trainCV(2, 0);
      Instances testingData = data.testCV(2, 0);

      // Build and train model
      Classifier classifier = new weka.classifiers.trees.RandomForest();
      classifier.buildClassifier(trainingData);

      // Evaluate model
      Evaluation eval = new Evaluation(trainingData);
      eval.evaluateModel(classifier, testingData);
      System.out.println(eval.toSummaryString("\nResults\n========\n", false));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
