package edu.gatech.cs7641.hw02.algoanalysis;


public class ResultSet {

    private final long elapsed_time;
    private final int distribution_n_value;
    private final double score;
    private final int training_iterations;
    private final String problem,algo;

    public ResultSet(long elapsed_time, double score, String problem, String algo, int train_iterations, int distribution_n_value){
        this.elapsed_time=elapsed_time;
        this.score=score;
        this.problem=(problem==null)?"":problem;
        this.algo=(algo==null)?"":algo;
        this.training_iterations=train_iterations;
        this.distribution_n_value=distribution_n_value;
    }
    public double get_time(){return this.elapsed_time;}
    public double get_score(){return this.score;}
    public String get_problem(){return this.problem;}
    public String get_algo(){return this.algo;}
    public int get_iter(){return this.training_iterations;}
    public String toPrettyString(){return String.format("Problem: %s, Algo: %s, Score: %.5f, Time-nano: %d, Iterations: %d, Distribution-n: %d",this.problem,this.algo,this.score,this.elapsed_time,this.training_iterations,this.distribution_n_value);}
    public String toCSVString(){return String.format("\"%s\",\"%s\",%f,%d,%d,%d",this.problem,this.algo,this.score,this.elapsed_time,this.training_iterations,this.distribution_n_value);}

}
