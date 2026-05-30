package neurex.gui;

import javax.swing.*;
import java.awt.*;
import java.io.Serial;

public final class ToastBar extends JPanel {
    @Serial
    private static final long serialVersionUID = 1L;

    private static final Color SUCCESS_BACKGROUND = new Color(46, 125, 50);
    private static final Color ERROR_BACKGROUND = new Color(183, 28, 28);
    private static final int DISPLAY_TIME_MS = 3500;

    private final JLabel messageLabel = new JLabel();
    private final Timer hideTimer;

    public ToastBar() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        setVisible(false);

        messageLabel.setForeground(Color.WHITE);
        messageLabel.setFont(messageLabel.getFont().deriveFont(Font.BOLD));
        add(messageLabel, BorderLayout.CENTER);

        hideTimer = new Timer(DISPLAY_TIME_MS, _ -> setVisible(false));
        hideTimer.setRepeats(false);
    }

    public void showSuccess(String message) {
        showMessage(message, SUCCESS_BACKGROUND);
    }

    public void showError(String message) {
        showMessage(message, ERROR_BACKGROUND);
    }

    private void showMessage(String message, Color background) {
        hideTimer.stop();
        setBackground(background);
        messageLabel.setText(message);
        setVisible(true);
        revalidate();
        repaint();
        hideTimer.start();
    }
}
