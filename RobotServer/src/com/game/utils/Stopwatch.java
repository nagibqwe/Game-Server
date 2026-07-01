/**
 * @author sb
 */
package com.game.utils;

import java.util.ArrayList;

/*-
 * Simple StopWatch
 * 
 * 1. Prints time in desc order. 
 * 2. Simple to use
 * 
 * Usages
 * Stopwatch sw = new Stopwatch(); 
 * sw.start(); 
 * After Task 1 completion 
 * sw.measure("Task1"); 
 * After Task 2 completion 
 * sw.measure("Task2");
 * After Task 3 completion  
 * sw.measure("Task3 Complete"); 
 * sw.stop(); // It will print time consumed by task A, B and C in descending order
 */
public class Stopwatch {

    private long startTime;
    private long stopTime;
    private final ArrayList<Pair<String, Double>> measurementMap = new ArrayList<>();
    private String watchName;
    private double totalTime;

    /**
     */
    public void start(String title) {
        watchName = title;
        startTime = System.nanoTime();
    }

    /**
     * @return
     */
    public long elapsedTimeNanos() {
        return stopTime - startTime;
    }

    /**
     * @return
     */
    public double elapsedTimeMicros() {
        return elapsedTimeNanos() / 1000d;
    }

    /**
     * @return
     */
    public double elapsedTimeMills() {
        return elapsedTimeMicros() / 1000d;
    }

    /**
     * @return
     */
    public double elapsedTimeSeconds() {
        return elapsedTimeMills() / 1000d;
    }

    /**
     * @return
     */
    public double elapsedTimeMinutes() {
        return elapsedTimeSeconds() / 60d;
    }

    /**
     * @param measurePoint
     * @param outlast      second
     */
    public void measure(String measurePoint, double outlast) {
        // Stop the time first
        stopTime = System.nanoTime();
        // Housekeeping while the stopWatch is not running - Important
        double elapsedTime = elapsedTimeSeconds();
        if (elapsedTime >= outlast) {
            measurementMap.add(new Pair<String, Double>(measurePoint, new Double(elapsedTime)));
        }
        totalTime += elapsedTime;
        //measurementMap.put(measurePoint, new Double(elapsedTime));
        // Restart the stop watch
        startTime = System.nanoTime();
    }

    /**
     * @param printAllMeasurements
     */
    public StringBuilder stop(boolean printAllMeasurements, double outlast) {
        // Stop the time first
        stopTime = System.nanoTime();
        // If no measurement was ever called, total time is stopTime -
        // startTime, otherwise sum or all measurements
        totalTime = measurementMap.size() == 0 ? (elapsedTimeSeconds()) : totalTime;
        StringBuilder ret = new StringBuilder();
        if (totalTime < outlast)
            return ret;

        ret.append(watchName + " 		-> time (seconds)\n");

        for (Pair<String, Double> kv : measurementMap) {
            double time = kv.getValue();
            String task = kv.getKey();

            if (printAllMeasurements) {
                ret.append(String.format("%s 		-> %f\n", task, time));
            }
        }
        ret.append(String.format("Sum(Tasks)	-> %f\n", totalTime));
        return ret;
    }

//	/**
//	 * @param args
//	 * @throws InterruptedException
//	 */
//	public static void main(String[] args) throws InterruptedException {
//		Stopwatch sw = new Stopwatch();
//		sw.start();
//		Thread.sleep(100);
//		sw.measure("A");
//		Thread.sleep(1000);
//		sw.measure("B");
//		Thread.sleep(10);
//		sw.measure("C");
//		sw.stop(true);
//		sw.stop(false);
//	}

}
