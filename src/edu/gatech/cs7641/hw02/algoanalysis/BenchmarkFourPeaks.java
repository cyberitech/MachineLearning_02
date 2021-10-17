package edu.gatech.cs7641.hw02.algoanalysis;

import dist.DiscreteDependencyTree;

import dist.DiscreteUniformDistribution;
import dist.Distribution;
import opt.example.FourPeaksEvaluationFunction;
import opt.prob.GenericProbabilisticOptimizationProblem;
import opt.prob.MIMIC;
import opt.prob.ProbabilisticOptimizationProblem;
import opt.*;
import opt.ga.*;
import java.util.Arrays;

public class BenchmarkFourPeaks extends GenericProblemBenchmarkUsingAlgo {

    private final FourPeaksEvaluationFunction fitness_func;
    private final Distribution dist;
    private final int[] topology;
    private final int sz;

    public BenchmarkFourPeaks(int sz, int iteration_max, int iteration_step){
        this.sz=sz;
        this.n_iter_max = iteration_max;
        this.n_iter_step = iteration_step;
        topology = new int[sz];
        Arrays.fill(topology,2);
        fitness_func = new FourPeaksEvaluationFunction(sz/5);
        dist = new DiscreteUniformDistribution(topology);
    }


     protected ResultList test_rhc(int n_iter, int iter_step){
        NeighborFunction neigh_func = new SwapNeighbor();
        RandomizedHillClimbing hill_climber = new  RandomizedHillClimbing(new GenericHillClimbingProblem(fitness_func,dist,neigh_func));
        ResultList result_list = this.TestAlgo_N_Iterations(hill_climber,"rhc",fitness_func,"four-peaks",n_iter,iter_step,sz);
        System.out.printf("[+] DONE: problem=four-peaks algo=rhc psize=%d n_iter=%d iter_step=%d\n",sz,n_iter,iter_step);
        return result_list;
    }
    protected  ResultList test_annealing( int n_iter, int iter_step){
        NeighborFunction neigh_func = new DiscreteChangeOneNeighbor(topology);
        SimulatedAnnealing annealer = new  SimulatedAnnealing(1E1,0.1,new GenericHillClimbingProblem(fitness_func,dist,neigh_func));
        ResultList result_list = this.TestAlgo_N_Iterations(annealer,"annealing",fitness_func,"four-peaks",n_iter,iter_step,sz);
        System.out.printf("[+] DONE: problem=four-peaks algo=annealing psize=%d n_iter=%d iter_step=%d\n",sz,n_iter,iter_step);
        return result_list;
    }
    protected  ResultList test_genetic(int n_iter, int iter_step){
        MutationFunction mutator_func = new DiscreteChangeOneMutation(topology);
        CrossoverFunction crossover_func = new UniformCrossOver();
        GeneticAlgorithmProblem genetic = new GenericGeneticAlgorithmProblem(fitness_func,dist,mutator_func,crossover_func);
        StandardGeneticAlgorithm genetic_algo = new StandardGeneticAlgorithm(100,10,10,genetic);
        ResultList result_list = this.TestAlgo_N_Iterations(genetic_algo,"genetic",fitness_func,"four-peaks",n_iter,iter_step,sz);
        System.out.printf("[+] DONE: problem=four-peaks algo=genetic psize=%d n_iter=%d iter_step=%d\n",sz,n_iter,iter_step);
        return result_list;
    }
    protected  ResultList test_mimic(int n_iter, int iter_step){
        Distribution dist_tree = new DiscreteDependencyTree(.1);
        ProbabilisticOptimizationProblem prob_optimizer = new GenericProbabilisticOptimizationProblem(fitness_func,dist,dist_tree);
        MIMIC mimic_algo = new MIMIC(100, 100, prob_optimizer);
        ResultList result_list = this.TestAlgo_N_Iterations(mimic_algo,"mimic",fitness_func,"four-peaks",n_iter,iter_step,sz);
        System.out.printf("[+] DONE: problem=four-peaks algo=mimic psize=%d n_iter=%d iter_step=%d\n",sz,n_iter,iter_step);
        return result_list;
    }
}
