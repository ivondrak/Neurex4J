package neurex.gui;

import neurex.ann.AttributePair;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ValueEditDialog extends JDialog {
    public AttributePair pair;
    private final JLabel valueField;
    private final JSlider slider;
    private boolean confirmed = false;

    public ValueEditDialog(JFrame parent, AttributePair pair) {
        super(parent, "Edit Fact Value", true);

        this.pair = new AttributePair(pair.attribute, pair.value);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        JLabel attributeField = new JLabel(pair.attribute.attribute);
        double realValue = this.pair.attribute.minValue + (this.pair.attribute.maxValue - this.pair.attribute.minValue) * this.pair.value;
        double rounded = Math.round(realValue * 100.0) / 100.0;
        valueField = new JLabel(String.valueOf(rounded));
        slider = new JSlider(0, 100);
        slider.setValue((int) (this.pair.value*100));

        slider.addChangeListener(e -> {
            double value = slider.getValue()/100.0;
            double real = this.pair.attribute.minValue + (this.pair.attribute.maxValue - this.pair.attribute.minValue) * value;
            double round = Math.round(real * 100.0) / 100.0;
            valueField.setText(String.valueOf(round));
        });

        panel.add(attributeField);
        panel.add(valueField);
        panel.add(new JLabel("Set the value of the fact:"));
        panel.add(slider);

        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");

        okButton.addActionListener(e -> {
            this.pair.value = slider.getValue()/100.0;
            confirmed = true;
            dispose();
        });

        cancelButton.addActionListener(e -> dispose());

        panel.add(okButton);
        panel.add(cancelButton);

        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        add(panel);
        setSize(400,170);
        setResizable(false);
        setLocationRelativeTo(parent);
        this.getRootPane().setDefaultButton(okButton);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public AttributePair getAttributePair() {
        return this.pair;
    }
}
