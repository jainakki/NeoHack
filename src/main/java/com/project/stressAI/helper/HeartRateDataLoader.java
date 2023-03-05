package com.project.stressAI.helper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.datavec.api.writable.Writable;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.nd4j.linalg.dataset.DataSet;

public class HeartRateDataLoader {
    public static void main(String[] args) {
        try {
            RecordReader recordReader = new CSVRecordReader();
            recordReader.initialize(new FileSplit(new File("C:\\NeoHack\\src\\main\\resources\\HRV_dataset.csv")));
            List<List<Writable>> allData = new ArrayList<>();
            while (recordReader.hasNext()) {
                List<Writable> next = recordReader.next();
                allData.add(next);
            }
            
            List<List<String>> stringListList = new ArrayList<>();
            
            for (List<Writable> innerList : allData) {
            	List<String> result = new ArrayList<>();
                for (Writable w : innerList) {
                	result.add(w.toString());
                }
                System.out.println(result);
                stringListList.add(result);
            }
            
            int batchSize = 10;
            int numClasses = 2;
            RecordReaderDataSetIterator dataSetIterator = new RecordReaderDataSetIterator(recordReader, batchSize, 0, numClasses);
            while (dataSetIterator.hasNext()) {
                DataSet dataSet = dataSetIterator.next();
            }
            recordReader.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
