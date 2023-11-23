// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.

import neurex.ann.Attribute;
import neurex.ann.NeuralNet;
import neurex.ann.Pattern;
import neurex.ann.TrainingSet;
import neurex.gui.MainFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        /*
        Attribute[][] attributes = getAttributes();
        double[][][] data = {
                {{0.0, 1.0, 1.0, 0.2, 0.0},{0.0, 0.0, 1.0}},
                {{1.0, 1.0, 1.0, 1.0, 0.0},{0.0, 1.0, 0.0}},
                {{0.5, 1.0, 0.0, 1.0, 1.0},{1.0, 0.0, 0.0}},
        };

        Pattern[] patterns = new Pattern[3];
        for (int i=0; i < patterns.length; i++) {
            patterns[i] = new Pattern(data[i][0], data[i][1]);
        }

        TrainingSet training = new TrainingSet(patterns);
        training.dump();

        NeuralNet ann = new NeuralNet(attributes, training, 2);
        ann.dump();



        double[] input1 = {0, 1, 1, 0.2, 0};
        double[] input2 = {1, 1, 1, 1, 0};
        double[] input3 = {0.5, 1, 0, 1, 1};


        ann.learn(0.3, 10000);
        ann.dump();
        ann.run(input1);

        System.out.println("Input:");
        for (int i=0; i < ann.input().length; i++) {
            System.out.print(" "+ann.input()[i]+" ");
        }
        System.out.println();
        System.out.println("Output:");
        for (int i=0; i < ann.output().length; i++) {
            System.out.print(" "+String.format("%.5f",ann.output()[i])+" ");
        }
        System.out.println();

        ann.run(input2);

        System.out.println("Input:");
        for (int i=0; i < ann.input().length; i++) {
            System.out.print(" "+ann.input()[i]+" ");
        }
        System.out.println();
        System.out.println("Output:");
        for (int i=0; i < ann.output().length; i++) {
            System.out.print(" "+String.format("%.5f",ann.output()[i])+" ");
        }
        System.out.println();

        ann.run(input3);

        System.out.println("Input:");
        for (int i=0; i < ann.input().length; i++) {
            System.out.print(" "+ann.input()[i]+" ");
        }
        System.out.println();
        System.out.println("Output:");
        for (int i=0; i < ann.output().length; i++) {
            System.out.print(" "+String.format("%.5f",ann.output()[i])+" ");
        }
        System.out.println();

        Number[] error = ann.meanSquaredError();
        System.out.println();
        System.out.println("Total error[%]: "+String.format("%.2f",error[0].floatValue()));
        System.out.println("Worst pattern error[%]: "+String.format("%.2f",error[1].floatValue()));
        System.out.println("Worst pattern index: "+error[2]);

        SaveFileDialog toSave = new SaveFileDialog();
        toSave.saveANN(ann);
        OpenFileDialog toOpen = new OpenFileDialog();
        NeuralNet annLoaded = toOpen.openANN();
        annLoaded.dump();
         */

        Attribute[][] attributes = getAttributes();
        Pattern[] patterns = getPatterns();
        TrainingSet training = new TrainingSet(patterns);
        NeuralNet ann = new NeuralNet(attributes, training, 2);

        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame(ann);
            mainFrame.setVisible(true);
            mainFrame. cardLayout.show(mainFrame.mainPanel, "Credentials");
        });
    }

    private static Attribute[][] getAttributes() {
        Attribute in1 = new Attribute("Fever",37.0,42.0);
        Attribute in2 = new Attribute("Cough",0,1);
        Attribute in3 = new Attribute("Headache",0,100);
        Attribute in4 = new Attribute("Fatigue",0,1);
        Attribute in5 = new Attribute("Night Sweat",0,1);
        Attribute out1 = new Attribute("Pneumonia",0,100);
        Attribute out2 = new Attribute("Flu",0,100);
        Attribute out3 = new Attribute("Cold",0,100);

        return new Attribute[][]{
                {in1, in2, in3, in4, in5},
                {out1, out2, out3}
        };
    }

    private static Pattern[] getPatterns() {
        double [][][] data = {
                {{0.0, 1.0, 1.0, 0.2, 0.0},{0.0, 0.0, 1.0}},
                {{1.0, 1.0, 1.0, 1.0, 0.0},{0.0, 1.0, 0.0}},
                {{0.5, 1.0, 0.0, 1.0, 1.0},{1.0, 0.0, 0.0}},
        };

        Pattern[] patterns = new Pattern[3];
        for (int i=0; i < patterns.length; i++) {
            patterns[i] = new Pattern(data[i][0], data[i][1]);
        }

        return patterns;
    }
}