package neurex.gui;

import javax.swing.*;
import java.awt.*;

public class TopologyPanel extends JPanel {
    MainFrame main;

    public TopologyPanel(MainFrame main) {
        this.main = main;
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
        panel.add(new JLabel(String.valueOf(topology)));
        return panel;
    }

    private JPanel createPatternsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2));
        panel.add(new JLabel("Number of Patterns"));
        panel.add(new JLabel(String.valueOf(main.ann.trainingSet.patterns.length)));
        return panel;
    }

    private JPanel createErrorPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2));
        panel.add(new JLabel("Mean Squared Error"));
        double error = (double) main.ann.meanSquaredError()[0];
        float rounded = Math.round(error * 100.0) / 100f;
        panel.add(new JLabel(String.valueOf(rounded)));
        return panel;
    }
}
