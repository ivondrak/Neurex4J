package neurex.gui;

import javax.swing.*;
import java.awt.*;

public class LearningPanel extends JPanel implements ANNUpdateListener {
    MainFrame main;
    private JTextField learningCoeffField;
    private JTextField cyclesField;
    private JTextField errorField;
    private JTextField worstField;
    private JTextField worstErrorField;


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

        centerPanel.add(subtitleLabel, gbc);
        centerPanel.add(learningCoeffPanel, gbc);
        centerPanel.add(cyclesPanel, gbc);
        centerPanel.add(errorPanel, gbc);
        centerPanel.add(worstPanel, gbc);
        centerPanel.add(worstErrorPanel, gbc);

        add(Box.createRigidArea(new Dimension(0, 10)));
        add(titleLabel);
        add(centerPanel);
        add(Box.createVerticalStrut(250));
    }

    private JPanel createLearningCoeffPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2));
        panel.add(new JLabel("Learning Coefficient"));
        learningCoeffField = new JTextField(15);
        learningCoeffField.setText(String.valueOf("0,3"));
        panel.add(learningCoeffField);
        return panel;
    }

    private JPanel createCyclesPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2));
        panel.add(new JLabel("Learning Cycles"));
        cyclesField = new JTextField(15);
        cyclesField.setText(String.valueOf("10000"));
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

    public void onANNUpdated() {
        learningCoeffField.setText(String.valueOf("0,3"));
        cyclesField.setText(String.valueOf("10000"));
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
