package edu.gatech.cs7641.hw02.algoanalysis;

import opt.EvaluationFunction;
import opt.OptimizationAlgorithm;
import shared.FixedIterationTrainer;


public abstract class GenericProblemBenchmarkUsingAlgo {

    protected int sz;
    protected int n_iter_max;
    protected int n_iter_step;

    protected ResultList TestAlgo_N_Iterations(OptimizationAlgorithm problem,String optimization_algo_name, EvaluationFunction eval_algo, String eval_algo_name, int n_iter, int step, int d) {
        ResultList results = new ResultList();
        for (int ii=step; ii<=n_iter; ii+=step){
            long start = System.nanoTime();
            FixedIterationTrainer fit = new FixedIterationTrainer(problem, ii);
            fit.train();
            double scored_result = eval_algo.value(problem.getOptimal());
            long end = System.nanoTime();
            long elapsed_time = end -start;
            ResultSet res = new ResultSet(elapsed_time,scored_result,eval_algo_name,optimization_algo_name,ii,d);
            results.add(res);
            System.out.println("[+] completed a set");
        }
        return results;
    }
    public  ResultList DoBenchmarks(){
        ResultList res = new ResultList();
        res.addAll(test_rhc(n_iter_max,n_iter_step));
        res.addAll(test_annealing(n_iter_max,n_iter_step));
        res.addAll(test_genetic(n_iter_max,n_iter_step));
        res.addAll(test_mimic(n_iter_max,n_iter_step));
        return res;
    }
    protected abstract ResultList test_rhc(int n_iter, int iter_step);
    protected abstract ResultList test_genetic(int n_iter, int iter_step);
    protected abstract ResultList test_annealing(int n_iter, int iter_step);
    protected abstract ResultList test_mimic(int n_iter, int iter_step);
}
