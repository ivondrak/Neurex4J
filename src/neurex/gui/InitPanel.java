package neurex.gui;

import neurex.ann.Attribute;
import neurex.ann.NeuralNet;
import neurex.ann.Pattern;
import neurex.ann.TrainingSet;

import javax.swing.*;
import java.awt.*;
import java.io.Serial;

public class InitPanel extends JPanel implements ANNUpdateListener {
    @Serial
    private static final long serialVersionUID = 1L;

    MainFrame main;
    private JTextField numberOfInputsField;
    private JTextField numberOfOutputsField;
    private JTextField hiddenLayersField;


    @SuppressWarnings("this-escape")
    public InitPanel(MainFrame main) {
        this.main = main;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel(I18n.text("panel.init.title"));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font(titleLabel.getFont().getName(), Font.BOLD, 18));

        JLabel subtitleLabel = new JLabel(I18n.text("panel.init.subtitle"));
        subtitleLabel.setFont(new Font(subtitleLabel.getFont().getName(), Font.BOLD, 14));

        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1;

        JPanel inputPanel = createInputPanel(I18n.text("panel.init.inputs"), 0);
        JPanel outputPanel = createInputPanel(I18n.text("panel.init.outputs"), 1);
        JPanel layersPanel = createInputPanel(I18n.text("panel.init.hiddenLayers"), 2);

        JButton initButton = new JButton(I18n.text("panel.init.initialize"));
        initButton.addActionListener(e -> initializeNetwork());

        JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
        separator.setPreferredSize(new Dimension(350, 10));

        centerPanel.add(subtitleLabel, gbc);
        centerPanel.add(inputPanel, gbc);
        centerPanel.add(outputPanel, gbc);
        centerPanel.add(layersPanel, gbc);
        centerPanel.add(separator, gbc);
        centerPanel.add(initButton, gbc);

        add(Box.createRigidArea(new Dimension(0, 10)));
        add(titleLabel);
        add(centerPanel);
        add(Box.createVerticalStrut(250));

        this.main.getRootPane().setDefaultButton(initButton);
    }

    private JPanel createInputPanel(String label, int fieldType) {
        JPanel panel = new JPanel(new GridLayout(1, 2));
        panel.add(new JLabel(label));
        switch (fieldType) {
            case 0: {
                numberOfInputsField = new JTextField(10);
                numberOfInputsField.setText("1");
                panel.add(numberOfInputsField);
                break;
            }
            case 1: {
                numberOfOutputsField = new JTextField(10);
                numberOfOutputsField.setText("1");
                panel.add(numberOfOutputsField);
                break;
            }
            case 2: {
                hiddenLayersField = new JTextField(10);
                hiddenLayersField.setText("2");
                panel.add(hiddenLayersField);
                break;
            }
            default:
                throw new IllegalStateException("Unexpected value: " + label);
        }
        return panel;
    }

    private void initializeNetwork() {
        int numberOfInputs;
        int numberOfOutputs;
        int hiddenLayers;
        try {
            numberOfInputs = Integer.parseInt(numberOfInputsField.getText());
            if (numberOfInputs < 1) return;
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, I18n.text("validation.inputs"), I18n.text("dialog.inputError.title"), JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            numberOfOutputs = Integer.parseInt(numberOfOutputsField.getText());
            if (numberOfOutputs < 1) return;
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, I18n.text("validation.outputs"), I18n.text("dialog.inputError.title"), JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            hiddenLayers = Integer.parseInt(hiddenLayersField.getText());
            if (hiddenLayers < 0) return;
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, I18n.text("validation.hiddenLayers"), I18n.text("dialog.inputError.title"), JOptionPane.ERROR_MESSAGE);
            return;
        }
        Attribute[] inputAttributes = new Attribute[numberOfInputs];
        for (int i=0; i < numberOfInputs; i++) {
            Attribute attribute = new Attribute();
            attribute.attribute = I18n.text("panel.init.defaultInput") + " #" + i;
            inputAttributes[i] = attribute;
        }
        Attribute[] outputAttributes = new Attribute[numberOfOutputs];
        for (int i=0; i < numberOfOutputs; i++) {
            Attribute attribute = new Attribute();
            attribute.attribute = I18n.text("panel.init.defaultOutput") + " #" + i;
            outputAttributes[i] = attribute;
        }
        Attribute[][] attributes = {inputAttributes, outputAttributes};
        double[][][] data = new double[2][2][];
        for (int i=0; i < 2; i++) {
            double[] inData = new double[numberOfInputs];
            for (int j=0; j < numberOfInputs; j++) {
                inData[j] = 0.0;
            }
            data[i][0] = inData;
            double[] outData = new double[numberOfOutputs];
            for (int j=0; j < numberOfOutputs; j++) {
                outData[j] = 0.0;
            }
            data[i][1] = outData;
        }
        Pattern[] patterns = new Pattern[2];
        for (int i=0; i < patterns.length; i++) {
            patterns[i] = new Pattern(data[i][0], data[i][1]);
        }
        TrainingSet trainingSet = new TrainingSet(patterns);
        main.ann = new NeuralNet(attributes, trainingSet, hiddenLayers);
        main.filename = "undefined.neux";

        main.changeModel();
        main.cardLayout.show(main.mainPanel, "Topology");
    }

    public void onANNUpdated() {
        numberOfInputsField.setText("1");
        numberOfOutputsField.setText("1");
        hiddenLayersField.setText("2");
    }

}
