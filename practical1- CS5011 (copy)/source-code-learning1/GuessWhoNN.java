import java.io.File;

import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.back.Backpropagation;
import org.encog.persist.EncogDirectoryPersistence;

public class GuessWhoNN {

    final static double[] Alex = { 1, 0, 0, 0, 0 };
    final static double[] Alfred = { 0, 1, 0, 0, 0 };
    final static double[] Anita = { 0, 0, 0, 1, 0 };
    final static double[] Anne = { 0, 0, 0, 1, 0 };
    final static double[] Bernard = { 0, 0, 0, 0, 1 };

    public static void main(String[] args) {

        String[] paragraphs = FileUtil.readFile("char.csv");
        double[][] INPUT = createTrainingTable(paragraphs);
        double[][] OUTPUT = { Alex, Alfred, Anita, Anne, Bernard, Alex, Alfred, Anita, Anne, Bernard, Alex, Alfred,
                Anita, Anne, Bernard, Alex, Alfred, Anita, Anne, Bernard, Alex, Alfred, Anita, Anne, Bernard, Alex,
                Alfred, Anita, Anne, Bernard, Alex, Alfred, Anita, Anne, Bernard, };

        // Create Neural Network
        int input_units = 7;
        int hidden_units = 6;
        int output_units = 5;

        BasicNetwork network = new BasicNetwork();
        network.addLayer(new BasicLayer(null, false, input_units)); // Input
                                                                    // Layer
        network.addLayer(new BasicLayer(new ActivationSigmoid(), true, hidden_units)); // Hidden
                                                                                       // Layer
        network.addLayer(new BasicLayer(new ActivationSigmoid(), false, output_units)); // Output
                                                                                        // Layer

        network.getStructure().finalizeStructure();
        network.reset();

        // Train the neural network
        MLDataSet trainingSet = new BasicMLDataSet(INPUT, OUTPUT);
        Backpropagation train = new Backpropagation(network, trainingSet, 0.5, 0.3);

        // Train the neural network until the error smaller than 0.01
        int epoch = 1;
        do {
            train.iteration();
            System.out.println("Epoch #" + epoch + " Error:" + train.getError());
            epoch++;
        } while (train.getError() > 0.01);
        train.finishTraining();

        // Save the neural network
        System.out.println("Saving network");
        EncogDirectoryPersistence.saveObject(new File("my_network1.eg"), network);

        // Compute the output from the neural network and compare them with the
        // output of the training set
        double[] answer=new double[]{0,1,0,1,0,0,0};
        MLData data=new BasicMLData(answer);
        MLData output = network.compute(data);
        System.out.println("input ="+data.getData(0)+" "+ data.getData(1)+" "+ data.getData(2)+" "+ data.getData(3)+" "+ data.getData(4)+" "+ data.getData(5)+" "+ data.getData(6));
        System.out.println("actual = " + output.getData(0)+" "+ output.getData(1)+" " + output.getData(2)+" " + output.getData(3)+" " + output.getData(4));
    }

    /**
     * transfer "Yes/No" to "1/0" according to "char.csv"
     * 
     * @param _paragraphs
     *            is used to store each line of "char.csv"
     * @return
     */
    public static double[][] createTrainingTable(String[] _paragraphs) {
        double[][] table = new double[35][7];
        for (int i = 0; i < _paragraphs.length; i++) {
            String[] elements = _paragraphs[i].split(",");
            for (int j = 0; j < elements.length - 1; j++) {
                if (elements[j].equals("Yes")) {
                    elements[j] = "1";
                }
                if (elements[j].equals("No")) {
                    elements[j] = "0";
                }
                table[i][j] = Double.valueOf(elements[j]);
            }
        }

        return table;
    }
}
