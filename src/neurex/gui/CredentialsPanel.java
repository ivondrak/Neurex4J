package neurex.gui;
import javax.swing.*;
import java.awt.*;

public class CredentialsPanel extends JPanel {
    public CredentialsPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("Credentials");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font(titleLabel.getFont().getName(), Font.BOLD, 18));

        ImageIcon imageIcon = new ImageIcon("Neurex.png");
        JLabel imageLabel = new JLabel(imageIcon);
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel textLabel = new JLabel("(c) Ivo Vondrak, 2023");
        textLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(Box.createRigidArea(new Dimension(0, 10)));
        add(titleLabel);
        add(Box.createRigidArea(new Dimension(0, 100)));
        add(imageLabel);
        add(Box.createRigidArea(new Dimension(0, 5)));
        add(textLabel);
    }
}
