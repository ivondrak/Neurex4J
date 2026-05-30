package neurex.gui;

import neurex.ann.Attribute;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.Serial;

class AttributeEditDialog extends JDialog {
    @Serial
    private static final long serialVersionUID = 1L;

    private final JTextField attributeNameField;
    private final JTextField minValueField;
    private final JTextField maxValueField;
    private boolean confirmed = false;

    public AttributeEditDialog(JFrame parent, Attribute attribute) {
        super(parent, I18n.text("dialog.attribute.title"), true);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2));

        attributeNameField = new JTextField(attribute.attribute);
        minValueField = new JTextField(String.valueOf(attribute.minValue));
        maxValueField = new JTextField(String.valueOf(attribute.maxValue));

        panel.add(new JLabel(I18n.text("dialog.attribute.name")));
        panel.add(attributeNameField);
        panel.add(new JLabel(I18n.text("dialog.attribute.min")));
        panel.add(minValueField);
        panel.add(new JLabel(I18n.text("dialog.attribute.max")));
        panel.add(maxValueField);

        JButton okButton = new JButton(I18n.text("dialog.ok"));
        JButton cancelButton = new JButton(I18n.text("dialog.cancel"));

        okButton.addActionListener(e -> {
            try {
                String minValueText = minValueField.getText();
                String maxValueText = maxValueField.getText();

                double minValue = Double.parseDouble(minValueText);
                double maxValue = Double.parseDouble(maxValueText);

                if (minValue >= maxValue) {
                    minValueField.setText(maxValueText);
                    maxValueField.setText(minValueText);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, I18n.text("dialog.attribute.numberError"), I18n.text("dialog.inputError.title"), JOptionPane.ERROR_MESSAGE);
                return;
            }
            confirmed = true;
            dispose();
        });

        cancelButton.addActionListener(e -> dispose());

        panel.add(okButton);
        panel.add(cancelButton);

        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        add(panel);
        setSize(400,200);
        setResizable(false);
        setLocationRelativeTo(parent);
        this.getRootPane().setDefaultButton(okButton);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public Attribute getAttribute() {
        return new Attribute(attributeNameField.getText(), Double.parseDouble(minValueField.getText()), Double.parseDouble(maxValueField.getText()));
    }
}
