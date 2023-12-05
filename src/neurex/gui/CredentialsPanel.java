package neurex.gui;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class CredentialsPanel extends JPanel {
    BufferedImage img = null;
    public CredentialsPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        try {
            InputStream stream = getClass().getResourceAsStream("/resources/images/neurex.png");
            img = ImageIO.read(Objects.requireNonNull(stream));
        } catch (IOException e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }

        JLabel titleLabel = new JLabel("Credentials");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font(titleLabel.getFont().getName(), Font.BOLD, 18));

        ImageIcon imageIcon = new ImageIcon(img);
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
