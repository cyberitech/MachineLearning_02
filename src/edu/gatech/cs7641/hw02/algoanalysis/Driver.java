package edu.gatech.cs7641.hw02.algoanalysis;

import edu.gatech.cs7641.hw02.neuralnetwork.NeuralNetworkTrainer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.*;


public class Driver {
    public static void main(String[] args) throws Exception {
        //RunBenchmarkingAlgos();
        RunNerualNetTrain();
    }

    private static void RunNerualNetTrain() {
        NeuralNetworkTrainer.main(null);

    }
    private static void RunBenchmarkingAlgos() throws InterruptedException, IOException {

            final String csv_out = "count_ones_results.csv";
            final int PROBLEM_SIZE_MAX=250;
            final int PROBLEM_SIZE_STEP=25;
            final int TRAIN_ITERATIONS_MAX = 1000;
            final int TRAIN_ITERATIONS_STEP = 100;
            final int TOTAL_TESTS = (PROBLEM_SIZE_MAX/PROBLEM_SIZE_STEP)*(TRAIN_ITERATIONS_MAX/TRAIN_ITERATIONS_STEP)*4;
            System.out.printf("[!] **Executing %d tests for benchmarking**\n",TOTAL_TESTS);

            final int MAX_THREADS=Runtime.getRuntime().availableProcessors();
            ResultList all_results = new ResultList();
            ArrayList<FutureBenchmark> futures = new ArrayList<>();


            ExecutorService pool = Executors.newFixedThreadPool(MAX_THREADS);
            for (int ii=1; ii<=PROBLEM_SIZE_MAX/PROBLEM_SIZE_STEP; ii+=1)
            {
                int problem_size = ii*PROBLEM_SIZE_STEP;

                BenchmarkNQueens nqueens = new BenchmarkNQueens(problem_size, TRAIN_ITERATIONS_MAX, TRAIN_ITERATIONS_STEP);
                BenchmarkFourPeaks fourpeaks=new BenchmarkFourPeaks(problem_size, TRAIN_ITERATIONS_MAX, TRAIN_ITERATIONS_STEP);
                BenchmarkCountOnes countones = new BenchmarkCountOnes(problem_size*10, TRAIN_ITERATIONS_MAX/10, TRAIN_ITERATIONS_STEP/10);
                BenchmarkTravelingSalesman salesman = new BenchmarkTravelingSalesman(problem_size, TRAIN_ITERATIONS_MAX, TRAIN_ITERATIONS_STEP);

                //futures.add( new FutureBenchmark(nqueens));
                //futures.add(new FutureBenchmark(fourpeaks));
                futures.add(new FutureBenchmark(countones));
                //futures.add(new FutureBenchmark(salesman));
            }
            ArrayList<Future<ResultList>> future_results = null;
            try {
                future_results= (ArrayList<Future<ResultList>>) pool.invokeAll(futures);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            pool.shutdown();

            if (pool.awaitTermination(8, TimeUnit.HOURS)) {
                for (int ii = 0; ii < future_results.size(); ii++) {
                    Future<ResultList> future = future_results.get(ii);
                    try {
                        ResultList result = future.get();
                        all_results.addAll(result);
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                String csv_data = all_results.GetAsCSV();
                BufferedWriter writer = new BufferedWriter(new FileWriter(csv_out));
                writer.write(csv_data);
                writer.flush();
                writer.close();
            }
            else{
                System.err.println("Exiting with error.  Benchmark did not successfully complete execution. (SIGINTERRUPTed?)");
            }
            return;
        }
    }

class FutureBenchmark implements Callable<ResultList> {
    private GenericProblemBenchmarkUsingAlgo p;

    public FutureBenchmark(GenericProblemBenchmarkUsingAlgo problemset){
        p=problemset;
    }

    @Override
    public ResultList call() throws Exception {
        ResultList res = p.DoBenchmarks();
        return (res==null)?new ResultList():res;
    }
}