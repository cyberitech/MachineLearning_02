package edu.gatech.cs7641.hw02.algoanalysis;

import dist.DiscreteDependencyTree;
import dist.DiscretePermutationDistribution;
import dist.Distribution;


import opt.*;
import opt.example.TravelingSalesmanRouteEvaluationFunction;
import opt.ga.*;
import opt.prob.GenericProbabilisticOptimizationProblem;
import opt.prob.MIMIC;
import opt.prob.ProbabilisticOptimizationProblem;

import java.util.Random;

public class BenchmarkTravelingSalesman extends GenericProblemBenchmarkUsingAlgo {

    private final TravelingSalesmanRouteEvaluationFunction fitness_func;
    private final Distribution dist;
    private final int sz;

    private final double edge_weights[][];

    public BenchmarkTravelingSalesman(int sz, int iteration_max, int iteration_step){
        this.sz=sz;
        this.n_iter_max = iteration_max;
        this.n_iter_step = iteration_step;
        Random r = new Random();
        edge_weights = new double[sz][2];
        for (int ii=0; ii < sz; ii++){
            edge_weights[ii][0]=r.nextDouble();
            edge_weights[ii][1]=r.nextDouble();
        }
        fitness_func = new TravelingSalesmanRouteEvaluationFunction(edge_weights);
        dist = new DiscretePermutationDistribution(sz);
    }

    @Override
    protected ResultList test_rhc(int n_iter, int iter_step) {
        NeighborFunction neigh_func = new SwapNeighbor();
        RandomizedHillClimbing hill_climber = new  RandomizedHillClimbing(new GenericHillClimbingProblem(fitness_func,dist,neigh_func));
        ResultList result_list = this.TestAlgo_N_Iterations(hill_climber,"rhc",fitness_func,"traveling-salesman",n_iter,iter_step,sz);
        System.out.printf("[+] DONE: problem=traveling-salesman algo=rhc psize=%d n_iter=%d iter_step=%d\n",sz,n_iter,iter_step);
        return result_list;
    }

    @Override
    protected ResultList test_genetic(int n_iter, int iter_step) {
        SwapMutation mutator_func = new SwapMutation();
        CrossoverFunction crossover_func = new UniformCrossOver();
        GeneticAlgorithmProblem genetic = new GenericGeneticAlgorithmProblem(fitness_func,dist,mutator_func,crossover_func);
        StandardGeneticAlgorithm genetic_algo = new StandardGeneticAlgorithm(100,10,10,genetic);
        ResultList result_list = this.TestAlgo_N_Iterations(genetic_algo,"genetic",fitness_func,"traveling-salesman",n_iter,iter_step,sz);
        System.out.printf("[+] DONE: problem=traveling-salesman algo=genetic psize=%d n_iter=%d iter_step=%d\n",sz,n_iter,iter_step);
        return result_list;
    }

    @Override
    protected ResultList test_annealing(int n_iter, int iter_step) {
        NeighborFunction neigh_func = new SwapNeighbor();
        SimulatedAnnealing annealer = new  SimulatedAnnealing(1E1,0.1,new GenericHillClimbingProblem(fitness_func,dist,neigh_func));
        ResultList result_list = this.TestAlgo_N_Iterations(annealer,"annealing",fitness_func,"traveling-salesman",n_iter,iter_step,sz);
        System.out.printf("[+] DONE: problem=traveling-salesman algo=annealing psize=%d n_iter=%d iter_step=%d\n",sz,n_iter,iter_step);
        return result_list;
    }

    @Override
    protected ResultList test_mimic( int n_iter, int iter_step) {
        Distribution dist_tree = new DiscreteDependencyTree(.1);
        ProbabilisticOptimizationProblem prob_optimizer = new GenericProbabilisticOptimizationProblem(fitness_func,dist,dist_tree);
        MIMIC mimic_algo = new MIMIC(100, 10, prob_optimizer);
        ResultList result_list = this.TestAlgo_N_Iterations(mimic_algo,"mimic",fitness_func,"traveling-salesman",n_iter,iter_step,sz);
        System.out.printf("[+] DONE: problem=traveling-salesman algo=mimic psize=%d n_iter=%d iter_step=%d\n",sz,n_iter,iter_step);
        return result_list;
    }
}
