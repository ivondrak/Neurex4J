package neurex.gui;

import javax.swing.*;
import java.awt.*;

public class ConsultPanel extends JPanel implements ANNUpdateListener {
    MainFrame main;

    public ConsultPanel(MainFrame main) {
        this.main = main;
        this.main.addUpdateListener(this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("Neural Net Inferrencing");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font(titleLabel.getFont().getName(), Font.BOLD, 18));

        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1;

        add(Box.createRigidArea(new Dimension(0, 10)));
        add(titleLabel);
        add(centerPanel);
        add(Box.createVerticalStrut(250));
    }

    public void onANNUpdated() {
    }
}
