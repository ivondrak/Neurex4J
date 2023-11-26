package neurex.gui;

import javax.swing.*;
import java.awt.*;

public class TopologyPanel extends JPanel implements ANNUpdateListener {
    MainFrame main;

    JTextField topologyLabel;
    JTextField patternsLabel;
    JTextField errorLabel;

    public TopologyPanel(MainFrame main) {
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

        JPanel topologyPanel = createTopologyPanel();
        JPanel patternsPanel = createPatternsPanel();
        JPanel errorPanel = createErrorPanel();

        centerPanel.add(subtitleLabel, gbc);
        centerPanel.add(topologyPanel, gbc);
        centerPanel.add(patternsPanel, gbc);
        centerPanel.add(errorPanel, gbc);


        add(Box.createRigidArea(new Dimension(0, 10)));
        add(titleLabel);
        add(centerPanel);
        add(Box.createVerticalStrut(250));
    }

    private JPanel createTopologyPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2));
        panel.add(new JLabel("Topology"));
        StringBuilder topology;
        topology = new StringBuilder(String.valueOf(main.ann.inputSize));
        for (int i=0; i < (main.ann.hidden + 1); i++) {
            int num = main.ann.neurons[i+1].length;
            topology.append(" -> ").append(num);
        }
        topologyLabel = new JTextField(12);
        topologyLabel.setText(String.valueOf(topology));
        topologyLabel.setEnabled(false);
        //topologyLabel = new JLabel(String.valueOf(topology));
        panel.add(topologyLabel);
        return panel;
    }

    private JPanel createPatternsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2));
        panel.add(new JLabel("Number of Patterns"));
        patternsLabel = new JTextField(12);
        patternsLabel.setText(String.valueOf(main.ann.trainingSet.patterns.length));
        patternsLabel.setEnabled(false);
        panel.add(patternsLabel);
        return panel;
    }

    private JPanel createErrorPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2));
        panel.add(new JLabel("Mean Squared Error"));
        double error = (double) main.ann.meanSquaredError()[0];
        double rounded = Math.round(error * 100.0) / 100.0;
        errorLabel = new JTextField(12);
        errorLabel.setText(String.valueOf(rounded));
        errorLabel.setEnabled(false);
        panel.add(errorLabel);
        return panel;
    }

    public void onANNUpdated() {
        StringBuilder topology;
        topology = new StringBuilder(String.valueOf(main.ann.inputSize));
        for (int i=0; i < (main.ann.hidden + 1); i++) {
            int num = main.ann.neurons[i+1].length;
            topology.append(" -> ").append(num);
        }
        topologyLabel.setText(String.valueOf(topology));
        patternsLabel.setText(String.valueOf(main.ann.trainingSet.patterns.length));
        double error = (double) main.ann.meanSquaredError()[0];
        float rounded = Math.round(error * 100.0) / 100f;
        errorLabel.setText(String.valueOf(rounded));
    }
}
