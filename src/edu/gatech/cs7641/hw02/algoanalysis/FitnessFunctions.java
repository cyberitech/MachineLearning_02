package edu.gatech.cs7641.hw02.algoanalysis;
import java.lang.Math;
import java.util.BitSet;

public class FitnessFunctions {

    public double func_one_get_log_fitness(double x){
        /*
            Fit(x) = (x^sin(x))/log(x)

            This one is interesting.  It increases without bound as x -> +inf...
            but it has its maximum at x=1, where y=inf

            So if the x has to keep increasing to increase fitness..
            or, if x approaches 1, you increase fitness even quicker.

            https://www.desmos.com/calculator/zgj2oehubq
       */
        double operand_one = x*Math.sin(x);
        double operand_two = Math.log(x);
        return operand_one/operand_two;
    }

    public int func_two_get_xor_bit_fitness(BitSet p1){
        /*
            Ok, so this is how this fitness function will work
            it accepts a bit set which is divisible by 2 in length.
            It then splits the input BitSet in half at the middle, into two new sets, and XOR's the new two bitsets.
            The resultant BitSet is counted for 1's, and this sum is returned.
            Thus, the most 'fit' something can be, is len(BitSet), and this occurs when XOR(a,b) = 111111...
        */
        assert (p1.size()%2==0);

        BitSet half_set_one = new BitSet(p1.size()/2);
        BitSet half_set_two = new BitSet(p1.size()/2);
        for (int i=0; i<p1.size()/2; i++){  // This splits the input BitSet into two half-size bit sets
            int j = i+p1.size()/2;
            boolean is_bit_set = p1.get(i);
            half_set_one.set(i,is_bit_set);
            is_bit_set = p1.get(j);
            half_set_two.set(i,is_bit_set);
        }
        half_set_one.xor(half_set_two);
        return half_set_one.cardinality();
    }

    public double func_three_get_wavy_fitness(double x){
        /*
        This is a super wavy function of x with a lot of peaks and valleys, it should be fun to search randomly.
        The function increases in fitness until somewhere around +/- pi is hit, and then it decreases in fitness
        The optimum is somewhere around -3.123, but many other close peaks exist.

        if (|x| > pi):	y = x*sin(3*sin(2x*sin(4x*sin(x)-0.2)-0.5)+0.4)
        else : 			y = (pi^2/x)*sin(3*sin(2x*sin(4x*sin(x)-0.2)-0.5)+0.4)

        https://www.desmos.com/calculator/vx7ptv8aww
        */
        final double operand_one = (Math.sin(3*Math.sin(2*x*Math.sin(4*x*Math.sin(x)-0.2)-0.5)-0.4));
        final double operand_two = (Math.abs(x) > Math.PI) ? x : Math.pow(Math.PI,2)/x;
        return operand_one * operand_two;
    }

}
