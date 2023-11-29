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
        try {
            // Set Nimbus Look and Feel
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("".equals(info.getName())) { //Metal, Nimbus, Aqua, Windows
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, fall back to the default Look and Feel.
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ex) {
                // Handle exception
            }
        }

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