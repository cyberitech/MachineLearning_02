package edu.gatech.cs7641.hw02.algoanalysis;

import java.util.ArrayList;

public class ResultList extends ArrayList<ResultSet> {

    public String GetAsCSV(){
        StringBuilder csv_out = new StringBuilder("\"problem\",\"algo\",\"score\",\"time-ns\",\"train-iters\",\"problem-size\"\n");
        for (ResultSet rs:this){
            csv_out.append(rs.toCSVString()+'\n');
        }
        return csv_out.toString();
    }

}
