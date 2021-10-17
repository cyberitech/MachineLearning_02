package edu.gatech.cs7641.hw02.neuralnetwork;


import func.nn.backprop.BackPropagationNetwork;
import func.nn.backprop.BackPropagationNetworkFactory;
import opt.OptimizationAlgorithm;
import opt.RandomizedHillClimbing;
import opt.SimulatedAnnealing;
import opt.example.NeuralNetworkOptimizationProblem;
import opt.ga.StandardGeneticAlgorithm;
import shared.DataSet;
import shared.ErrorMeasure;
import shared.Instance;
import shared.SumOfSquaresError;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.Scanner;



/*
*   https://github.com/pushkar/ABAGAIL/blob/438eacc5bfdacbf066bf5298a1e001a8127e6e72/src/opt/test/AbaloneTest.java#L45
*   Heavily modified from original to do the following:
*       Implement train/test samples
*       Implement train/test cycles
*       Implement reading from the correct file
*       Implement suitable real-value to classification function
*       Alter Neural Network hidden layer parameters
*       Alter searcg algorithm parameters
*       Display itemized prediction/actual classification results
*       Other minor things
*/
/**
 * Implementation of randomized hill climbing, simulated annealing, and genetic algorithm to
 * find optimal weights to a neural network that is classifying abalone as having either fewer
 * or more than 15 rings.
 *
 *
 * @author Hannah Lau
 * @version 1.0
 */
public class NeuralNetworkTrainer {
    /* NN from HW 01 -- MLPClassifier(learning_rate='adaptive',hidden_layer_sizes=(150,))  */
    private static Instance[] instances = initializeInstances();
    private static Instance[] TestingInstances;

    private static final int inputLayer = 19;
    private static final int hiddenLayer = 150;
    private static final int outputLayer = 1;
    private static final int trainingIterations = 1000;
    private static final BackPropagationNetworkFactory factory = new BackPropagationNetworkFactory();

    private static ErrorMeasure measure = new SumOfSquaresError();

    private static DataSet set = new DataSet(instances);

    private static BackPropagationNetwork networks[] = new BackPropagationNetwork[3];
    private static NeuralNetworkOptimizationProblem[] nnop = new NeuralNetworkOptimizationProblem[3];

    private static OptimizationAlgorithm[] oa = new OptimizationAlgorithm[3];
    private static String[] oaNames = {"RHC", "SA", "GA"};
    private static String results = "";

    private static DecimalFormat df = new DecimalFormat("0.000");

    public static void main(String[] args) {
        for(int i = 0; i < oa.length; i++) {
            networks[i] = factory.createClassificationNetwork(
                    new int[] {inputLayer, hiddenLayer, outputLayer});
            nnop[i] = new NeuralNetworkOptimizationProblem(set, networks[i], measure);
        }

        oa[0] = new RandomizedHillClimbing(nnop[0]);
        oa[1] = new SimulatedAnnealing(10, 0.1, nnop[1]);
        oa[2] = new StandardGeneticAlgorithm(40, 10, 5, nnop[2]);
        double[] min_errors = new double[3];


        for(int i = 0; i < oa.length; i++) {  //training
            double start = System.nanoTime(), end=0, trainingTime=0, correct=0, incorrect=0;
            min_errors[i] = train(oa[i], networks[i], oaNames[i]); //trainer.train();
            end = System.nanoTime();
            trainingTime = end - start;
            trainingTime /= Math.pow(10, 9);
            Instance optimalInstance = oa[i].getOptimal();
            networks[i].setWeights(optimalInstance.getData());
            System.out.println("Trained in "+trainingTime);
            for(int j = 0; j < instances.length; j++) {
                networks[i].setInputValues(instances[j].getData());
                networks[i].run();
                double actual = Double.parseDouble(instances[j].getLabel().toString());
                double predicted = Double.parseDouble(networks[i].getOutputValues().toString());

                System.out.printf("Training Result Actual: %f, Training Result Predicted: %f\n",actual,to_label(predicted));
                if (is_correct_classification(predicted,actual))
                    correct++;
                else
                    incorrect++;

            }
            end = System.nanoTime();
            trainingTime = end - start;
            trainingTime /= Math.pow(10, 9);
            results +=  "\nResults for training " + oaNames[i] + ": \nCorrectly train classified " + correct + " instances." +
                    "\nIncorrectly train classified " + incorrect + " instances.\nPercent train correctly classified: "
                    + df.format(correct/(correct+incorrect)*100)  + "\nTraining time: " + df.format(trainingTime) + " seconds\n";
        }


        for (int i = 0; i<oa.length; i++){  //testing
            double start = System.nanoTime(), end, trainingTime, testingTime, correct = 0, incorrect = 0;
            double predicted, actual;
            start = System.nanoTime();
            for(int j = 0; j < TestingInstances.length; j++) {
                networks[i].setInputValues(TestingInstances[j].getData());
                networks[i].run();

                predicted = Double.parseDouble(networks[i].getOutputValues().toString());
                actual =  Double.parseDouble(TestingInstances[j].getLabel().toString());
                System.out.printf("Testing Result Actual: %f, Testing Predicted: %f\n",actual,to_label(predicted));
                if (is_correct_classification(predicted,actual))
                    correct++;
                else
                    incorrect++;
            }


            end = System.nanoTime();
            testingTime = end - start;
            testingTime /= Math.pow(10,9);

            results +=  "\nResults for testing " + oaNames[i] + ": \nCorrectly test classified " + correct + " instances." +
                    "\nIncorrectly test classified " + incorrect + " instances.\nPercent correctly test classified: "
                    + df.format(correct/(correct+incorrect)*100)  + "\nTesting time: " + df.format(testingTime) + " seconds\n";
        }

        System.out.println(results);
    }

    private static double to_label(double predicted){
     return (predicted >= 0.5)?1:0;
    }
    private static boolean is_correct_classification(double predicted, double actual){
        return actual == to_label(predicted);
    }
    private static double train(OptimizationAlgorithm oa, BackPropagationNetwork network, String oaName) {
        System.out.println("\nError results for " + oaName + "\n---------------------------");
        double error=0;
        for(int i = 0; i < trainingIterations; i++) {
            oa.train();

            error=0;
            for(int j = 0; j < instances.length; j++) {
                network.setInputValues(instances[j].getData());
                network.run();

                Instance output = instances[j].getLabel(), example = new Instance(network.getOutputValues());
                example.setLabel(new Instance(Double.parseDouble(network.getOutputValues().toString())));
                error += measure.value(output, example);
            }

            System.out.println(df.format(error));
        }
        return error;
    }

    private static Instance[] initializeInstances() {

        double[][][] attributes = new double[3000][][];

        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("balanced.csv")));

            for(int i = 0; i < attributes.length; i++) {
                Scanner scan = new Scanner(br.readLine());
                scan.useDelimiter(",");

                attributes[i] = new double[2][];
                attributes[i][0] = new double[19]; // 19 attributes
                attributes[i][1] = new double[1];

                for(int j = 0; j < 19; j++)
                    attributes[i][0][j] = Double.parseDouble(scan.next());

                attributes[i][1][0] = Double.parseDouble(scan.next());
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }


        int sz = attributes.length;
        double split = 0.66;
        int train_size = (int) (sz*split);
        int test_size = sz-train_size;

        Instance[] instances = new Instance[train_size];
        TestingInstances = new Instance[test_size];
        for(int i = 0; i < instances.length; i++) {
            instances[i] = new Instance(attributes[i][0]);
            instances[i].setLabel(new Instance(attributes[i][1][0]));
        }
        for (int i = 0; i < TestingInstances.length; i++){
            TestingInstances[i] = new Instance(attributes[i+train_size][0]);
            TestingInstances[i].setLabel(new Instance(attributes[i+train_size][1][0]));
        }

        //ShuffleInstances(instances);
        //ShuffleInstances(TestingInstances);
        return instances;
    }
}

