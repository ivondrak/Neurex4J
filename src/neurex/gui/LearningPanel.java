package neurex.gui;

import neurex.ann.Attribute;
import neurex.ann.NeuralNet;
import neurex.ann.TrainingSet;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;
import java.text.ParseException;

public class LearningPanel extends JPanel implements ANNUpdateListener {
    MainFrame main;
    private JTextField learningCoeffField;
    private JTextField cyclesField;
    private JTextField errorField;
    private JTextField worstField;
    private JTextField worstErrorField;
    @SuppressWarnings("FieldMayBeFinal")
    private JLabel statusField;


    public LearningPanel(MainFrame main) {
        this.main = main;
        this.main.addUpdateListener(this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("Neural Net Topology");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font(titleLabel.getFont().getName(), Font.BOLD, 18));

        JLabel subtitleLabel = new JLabel("Specification");
        subtitleLabel.setFont(new Font(subtitleLabel.getFont().getName(), Font.BOLD, 14));

        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1;

        JPanel learningCoeffPanel = createLearningCoeffPanel();
        JPanel cyclesPanel = createCyclesPanel();
        JPanel errorPanel = createErrorPanel();
        JPanel worstPanel = createWorstPanel();
        JPanel worstErrorPanel = createWorstErrorPanel();

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        JButton learnButton = new JButton("Start Learning");
        learnButton.addActionListener(e -> {
            learnNetwork();
            main.changeModel();
        });
        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> {
            resetNetwork();
            main.changeModel();
        });
        buttonPanel.add(learnButton);
        buttonPanel.add(resetButton);

        statusField = new JLabel("Waiting to be learned.");

        JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
        separator.setPreferredSize(new Dimension(450, 10));

        centerPanel.add(subtitleLabel, gbc);
        centerPanel.add(learningCoeffPanel, gbc);
        centerPanel.add(cyclesPanel, gbc);
        centerPanel.add(errorPanel, gbc);
        centerPanel.add(worstPanel, gbc);
        centerPanel.add(worstErrorPanel, gbc);
        centerPanel.add(separator, gbc);
        centerPanel.add(buttonPanel, gbc);
        centerPanel.add(statusField);


        add(Box.createRigidArea(new Dimension(0, 10)));
        add(titleLabel);
        add(centerPanel);
        add(Box.createVerticalStrut(250));
    }

    private JPanel createLearningCoeffPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2));
        panel.add(new JLabel("Learning Coefficient"));
        learningCoeffField = new JTextField(15);
        learningCoeffField.setText("0.3");
        panel.add(learningCoeffField);
        return panel;
    }

    private JPanel createCyclesPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2));
        panel.add(new JLabel("Learning Cycles"));
        cyclesField = new JTextField(15);
        cyclesField.setText("10000");
        panel.add(cyclesField);
        return panel;
    }

    private JPanel createErrorPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2));
        panel.add(new JLabel("Mean Squared Error"));
        double error = (double) main.ann.meanSquaredError()[0];
        float rounded = Math.round(error * 100.0) / 100f;
        errorField = new JTextField(15);
        errorField.setText(String.valueOf(rounded));
        errorField.setEnabled(false);
        panel.add(errorField);
        return panel;
    }

    private JPanel createWorstPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2));
        panel.add(new JLabel("Worst Pattern Index"));
        int worst = (int) main.ann.meanSquaredError()[2]+1;
        worstField = new JTextField(15);
        worstField.setText(String.valueOf(worst));
        worstField.setEnabled(false);
        panel.add(worstField);
        return panel;
    }

    private JPanel createWorstErrorPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2));
        panel.add(new JLabel("Worst Pattern Error [%]"));
        double error = (double) main.ann.meanSquaredError()[1];
        float rounded = Math.round(error * 100.0) / 100f;
        worstErrorField = new JTextField(15);
        worstErrorField.setText(String.valueOf(rounded));
        worstErrorField.setEnabled(false);
        panel.add(worstErrorField);
        return panel;
    }

    public void learnNetwork() {
        statusField.setText("Neural network learning ...");
        double learningCoefficient;
        int cycles;
        try {
            //learningCoefficient = readLocalizedNumber(learningCoeffField.getText());
            learningCoefficient = Double.parseDouble(learningCoeffField.getText());
            if (learningCoefficient <= 0) {
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Learning coefficient must be bigger than 0 and should smaller that 1.", "Input data error.", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            cycles = Integer.parseInt(cyclesField.getText());
            if (cycles < 1) {
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Number of cycles must be bigger than 1.", "Input data error.", JOptionPane.ERROR_MESSAGE);
            return;
        }
        main.ann.learn(learningCoefficient, cycles);
        statusField.setText("Neural network learned.");
    }

    @SuppressWarnings("unused")
    private static double readLocalizedNumber(String text) {
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.getDefault());
        double result = 0;
        try {
            result = numberFormat.parse(text).doubleValue();
        } catch (ParseException e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
        return result;
    }

    public void resetNetwork() {
        Attribute[][] attributes = main.ann.attributes;
        TrainingSet trainingSet = main.ann.trainingSet;
        int hidden = main.ann.hidden;
        main.ann = new NeuralNet(attributes, trainingSet, hidden);
        statusField.setText("Waiting to be learned.");
    }

    public void onANNUpdated() {
        learningCoeffField.setText("0.3");
        cyclesField.setText("10000");
        double error = (double) main.ann.meanSquaredError()[0];
        float rounded = Math.round(error * 100.0) / 100f;
        errorField.setText(String.valueOf(rounded));
        int worst = (int) main.ann.meanSquaredError()[2]+1;
        worstField.setText(String.valueOf(worst));
        error = (double) main.ann.meanSquaredError()[1];
        rounded = Math.round(error * 100.0) / 100f;
        worstErrorField.setText(String.valueOf(rounded));
    }
}
