package neurex.gui;

import neurex.ann.Attribute;
import neurex.ann.AttributePair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;

public class ConsultPanel extends JPanel implements ANNUpdateListener {
    MainFrame main;
    private final DefaultListModel<AttributePair> inputListModel;
    private final DefaultListModel<AttributePair> outputListModel;
    private final JList<AttributePair> inputList;
    @SuppressWarnings("FieldCanBeLocal")
    private final JList<AttributePair> outputList;

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

        JLabel inputLabel = new JLabel("Input");
        inputLabel.setFont(new Font(inputLabel.getFont().getName(), Font.BOLD, 14));
        JLabel outputLabel = new JLabel("Output");
        outputLabel.setFont(new Font(outputLabel.getFont().getName(), Font.BOLD, 14));
        JPanel settingsPanel = new JPanel(new GridLayout(1, 2));
        settingsPanel.add(inputLabel);
        settingsPanel.add(outputLabel);

        centerPanel.add(settingsPanel, gbc);

        JPanel attributesPanel = new JPanel(new GridLayout(1, 2));
        inputListModel = new DefaultListModel<>();
        inputList = new JList<>(inputListModel);

        createListRenderer(inputList);

        JScrollPane inputScrollPanel = new JScrollPane(inputList);
        inputScrollPanel.setPreferredSize(new Dimension(250, 400));
        outputListModel = new DefaultListModel<>();
        outputList = new JList<>(outputListModel);

        createListRenderer(outputList);

        JScrollPane outputScrollPanel = new JScrollPane(outputList);
        outputScrollPanel.setPreferredSize(new Dimension(250, 400));
        attributesPanel.add(inputScrollPanel);
        attributesPanel.add(outputScrollPanel);

        JButton runButton = new JButton("Reset Input");
        runButton.addActionListener(e -> {
            updateInputList();
            consult();
        });
        JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
        separator.setPreferredSize(new Dimension(550, 10));

        updateInputList();
        consult();

        centerPanel.add(attributesPanel, gbc);
        centerPanel.add(separator, gbc);
        centerPanel.add(runButton, gbc);

        add(Box.createRigidArea(new Dimension(0, 10)));
        add(titleLabel);
        add(centerPanel);
        add(Box.createVerticalStrut(10));

        inputList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int index = inputList.locationToIndex(evt.getPoint());
                    editInput(index);
                }
            }
        });


        //this.main.getRootPane().setDefaultButton(runButton);

    }

    private void createListRenderer(JList<AttributePair> list) {
        list.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JPanel panel = new JPanel();
                panel.setLayout(new BorderLayout());
                panel.setOpaque(true);

                AttributePair pair = (AttributePair) value;
                String textPart = pair.attribute.attribute;
                double realValue = pair.attribute.minValue + (pair.attribute.maxValue - pair.attribute.minValue) * pair.value;
                double rounded = Math.round(realValue * 100.0) / 100.0;
                String numberPart = String.valueOf(rounded);

                JLabel textLabel = new JLabel(textPart);
                textLabel.setOpaque(true);
                textLabel.setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
                textLabel.setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());

                JLabel emptyLabel = new JLabel("");
                emptyLabel.setOpaque(true);
                emptyLabel.setHorizontalAlignment(JLabel.CENTER);
                emptyLabel.setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
                emptyLabel.setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());

                JLabel numberLabel = new JLabel(numberPart);
                numberLabel.setOpaque(true);
                numberLabel.setHorizontalAlignment(JLabel.RIGHT);
                numberLabel.setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
                numberLabel.setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());

                panel.add(BorderLayout.WEST, textLabel);
                panel.add(BorderLayout.CENTER, emptyLabel);
                panel.add(BorderLayout.EAST, numberLabel);

                JProgressBar progress = new JProgressBar(0, 100);
                progress.setOpaque(true);
                progress.setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
                progress.setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
                progress.setValue((int) (pair.value*100));
                panel.add(BorderLayout.SOUTH, progress);

                return panel;
            }
        });
    }

    private void consult() {
        double[] input = new double[main.ann.inputSize];
        int index = 0;
        for (AttributePair pair: Collections.list(inputListModel.elements())) {
            input[index] = pair.value;
            index += 1;
        }
        @SuppressWarnings("unused")
        double[] output = main.ann.run(input);
        updateOutputList();
    }

    private void updateInputList() {
        inputListModel.clear();
        for (Attribute attribute : main.ann.attributes[0]) {
            AttributePair pair = new AttributePair(attribute, 0.0);
            inputListModel.addElement(pair);
        }
    }

    private void updateOutputList() {
        outputListModel.clear();
        int index = 0;
        double[] output = main.ann.output();
        for (Attribute attribute : main.ann.attributes[1]) {
            AttributePair pair = new AttributePair(attribute, output[index]);
            outputListModel.addElement(pair);
            index += 1;
        }
    }

    private void editInput(int index) {
        if (index >= 0) {
            AttributePair pair = inputListModel.getElementAt(index);
            ValueEditDialog editDialog = new ValueEditDialog(main, pair);
            editDialog.setVisible(true);

            if (editDialog.isConfirmed()) {
                inputListModel.set(index, editDialog.getAttributePair());
                consult();
            }
        }
    }

    public void onANNUpdated() {
        updateInputList();
        consult();
    }
}
