package edu.gatech.cs7641.hw02.algoanalysis;

import dist.DiscreteDependencyTree;
import dist.DiscreteUniformDistribution;
import dist.Distribution;

import opt.example.CountOnesEvaluationFunction;

import opt.prob.GenericProbabilisticOptimizationProblem;
import opt.prob.MIMIC;
import opt.prob.ProbabilisticOptimizationProblem;


import java.util.Arrays;

public class BenchmarkCountOnes extends GenericProblemBenchmarkUsingAlgo {

    private final CountOnesEvaluationFunction fitness_func;
    private final Distribution dist;
    private final int[] topology;
    private final int sz;
    public BenchmarkCountOnes(int sz, int iteration_max, int iteration_step){
        this.sz=sz;
        this.n_iter_max = iteration_max;
        this.n_iter_step = iteration_step;
        topology = new int[sz];
        Arrays.fill(topology,2);
        fitness_func = new CountOnesEvaluationFunction();
        dist = new DiscreteUniformDistribution(topology);
    }


     protected ResultList test_rhc(int n_iter, int iter_step){
        NeighborFunction neigh_func = new SwapNeighbor();
        RandomizedHillClimbing hill_climber = new  RandomizedHillClimbing(new GenericHillClimbingProblem(fitness_func,dist,neigh_func));
        ResultList result_list = this.TestAlgo_N_Iterations(hill_climber,"rhc",fitness_func,"count-ones",n_iter,iter_step,sz);
        System.out.printf("[+] DONE: problem=count-ones algo=rhc psize=%d n_iter=%d iter_step=%d\n",sz,n_iter,iter_step);
        return result_list;


    }
    protected  ResultList test_annealing(int n_iter, int iter_step){
        NeighborFunction neigh_func = new DiscreteChangeOneNeighbor(topology);
        SimulatedAnnealing annealer = new  SimulatedAnnealing(1E1,1,new GenericHillClimbingProblem(fitness_func,dist,neigh_func));
        ResultList result_list = this.TestAlgo_N_Iterations(annealer,"annealing",fitness_func,"count-ones",n_iter,iter_step,sz);
        System.out.printf("[+] DONE: problem=count-ones algo=annealing psize=%d n_iter=%d iter_step=%d\n",sz,n_iter,iter_step);
        return result_list;

    }
    protected  ResultList test_genetic(int n_iter, int iter_step){
        MutationFunction mutator_func = new DiscreteChangeOneMutation(topology);
        CrossoverFunction crossover_func = new UniformCrossOver();
        GeneticAlgorithmProblem genetic = new GenericGeneticAlgorithmProblem(fitness_func,dist,mutator_func,crossover_func);
        StandardGeneticAlgorithm genetic_algo = new StandardGeneticAlgorithm(sz*4,sz,sz/10,genetic);
        ResultList result_list = this.TestAlgo_N_Iterations(genetic_algo,"genetic",fitness_func,"count-ones",n_iter,iter_step,sz);
        System.out.printf("[+] DONE: problem=count-ones algo=genetic psize=%d n_iter=%d iter_step=%d\n",sz,n_iter,iter_step);

    }
    protected  ResultList test_mimic(int n_iter, int iter_step){
        Distribution dist_tree = new DiscreteDependencyTree(.1);
        ProbabilisticOptimizationProblem prob_optimizer = new GenericProbabilisticOptimizationProblem(fitness_func,dist,dist_tree);
        MIMIC mimic_algo = new MIMIC(100, 10, prob_optimizer);
        ResultList result_list = this.TestAlgo_N_Iterations(mimic_algo,"mimic",fitness_func,"count-ones",n_iter,iter_step,sz);
        System.out.printf("[+] DONE: problem=count-ones algo=mimic psize=%d n_iter=%d iter_step=%d\n",sz,n_iter,iter_step);
        return result_list;
    }

}
