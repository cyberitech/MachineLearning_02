package edu.gatech.cs7641.hw02.algoanalysis;

import dist.DiscreteDependencyTree;
import dist.DiscretePermutationDistribution;
import dist.Distribution;
import opt.*;
import opt.ga.*;
import opt.prob.GenericProbabilisticOptimizationProblem;
import opt.prob.MIMIC;
import opt.prob.ProbabilisticOptimizationProblem;

public class BenchmarkNQueens extends GenericProblemBenchmarkUsingAlgo {

        private final NQueensFitnessFunction fitness_func;
        private final Distribution dist;


    public BenchmarkNQueens(int sz, int iteration_max, int iteration_step){
        this.sz=sz;
        this.n_iter_max = iteration_max;
        this.n_iter_step = iteration_step;
        fitness_func = new NQueensFitnessFunction();
        dist = new DiscretePermutationDistribution(sz);
    }

    protected ResultList test_rhc( int n_iter, int iter_step) {
        NeighborFunction neigh_func = new SwapNeighbor();
        RandomizedHillClimbing hill_climber = new  RandomizedHillClimbing(new GenericHillClimbingProblem(fitness_func,dist,neigh_func));
        ResultList result_list = this.TestAlgo_N_Iterations(hill_climber,"rhc",fitness_func,"n-queens",n_iter,iter_step,sz);
        System.out.printf("[+] DONE: problem=n-queens algo=rhc psize=%d n_iter=%d iter_step=%d\n",sz,n_iter,iter_step);
        return result_list;
    }


    protected ResultList test_genetic(int n_iter, int iter_step) {
        SwapMutation mutator_func = new SwapMutation();
        CrossoverFunction crossover_func = new UniformCrossOver();
        GeneticAlgorithmProblem genetic = new GenericGeneticAlgorithmProblem(fitness_func,dist,mutator_func,crossover_func);
        StandardGeneticAlgorithm genetic_algo = new StandardGeneticAlgorithm(100,10,10,genetic);
        ResultList result_list = this.TestAlgo_N_Iterations(genetic_algo,"genetic",fitness_func,"n-queens",n_iter,iter_step,sz);
        System.out.printf("[+] DONE: problem=n-queens algo=genetic psize=%d n_iter=%d iter_step=%d\n",sz,n_iter,iter_step);
        return result_list;
    }


    protected ResultList test_annealing(int n_iter, int iter_step) {
        NeighborFunction neigh_func = new SwapNeighbor();
        SimulatedAnnealing annealer = new  SimulatedAnnealing(1E1,0.1,new GenericHillClimbingProblem(fitness_func,dist,neigh_func));
        ResultList result_list = this.TestAlgo_N_Iterations(annealer,"annealing",fitness_func,"n-queens",n_iter,iter_step,sz);
        System.out.printf("[+] DONE: problem=n-queens algo=annealing psize=%d n_iter=%d iter_step=%d\n",sz,n_iter,iter_step);
        return result_list;
    }


    protected ResultList test_mimic(int n_iter, int iter_step) {
        Distribution dist_tree = new DiscreteDependencyTree(.1);
        ProbabilisticOptimizationProblem prob_optimizer = new GenericProbabilisticOptimizationProblem(fitness_func,dist,dist_tree);
        MIMIC mimic_algo = new MIMIC(1000, 10, prob_optimizer);
        ResultList result_list = this.TestAlgo_N_Iterations(mimic_algo,"mimic",fitness_func,"n-queens",n_iter,iter_step,sz);
        System.out.printf("[+] DONE: problem=n-queens algo=mimic psize=%d n_iter=%d iter_step=%d\n",sz,n_iter,iter_step);
        return result_list;
    }
}
