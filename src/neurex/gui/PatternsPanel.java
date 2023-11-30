package neurex.gui;

import neurex.ann.Attribute;
import neurex.ann.AttributePair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PatternsPanel extends JPanel implements ANNUpdateListener {
    MainFrame main;
    private final DefaultListModel<AttributePair> inputListModel;
    private final DefaultListModel<AttributePair> outputListModel;
    private final JList<AttributePair> inputList;
    private final JList<AttributePair> outputList;
    public final JSlider slider;
    private final JLabel indexLabel;

    public PatternsPanel(MainFrame main) {
        this.main = main;
        this.main.addUpdateListener(this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("Training Set Patterns");
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
        inputScrollPanel.setPreferredSize(new Dimension(250, 350));

        outputListModel = new DefaultListModel<>();
        outputList = new JList<>(outputListModel);

        createListRenderer(outputList);

        JScrollPane outputScrollPanel = new JScrollPane(outputList);
        outputScrollPanel.setPreferredSize(new Dimension(250, 350));
        attributesPanel.add(inputScrollPanel);
        attributesPanel.add(outputScrollPanel);

        JPanel sliderPanel = new JPanel(new BorderLayout());
        slider = new JSlider(0, main.ann.trainingSet.patterns.length-1);
        slider.setValue(0);
        JPanel flowPanel = new JPanel(new FlowLayout());
        JLabel patternLabel = new JLabel("Pattern:");
        indexLabel = new JLabel(String.valueOf(slider.getValue()+1));
        flowPanel.add(patternLabel);
        flowPanel.add(indexLabel);
        sliderPanel.add(BorderLayout.NORTH, flowPanel);
        sliderPanel.add(BorderLayout.CENTER, slider);

        slider.addChangeListener(e -> {
            int index = slider.getValue();
            indexLabel.setText(String.valueOf(index+1));
            updateInputList();
            updateOutputList();
        });

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        JButton addButton = new JButton("Add Pattern");
        addButton.addActionListener(e -> {
            addPattern();
            updateInputList();
            updateOutputList();
        });
        JButton removeButton = new JButton("Remove Pattern");
        removeButton.addActionListener(e -> {
            removePattern();
            updateInputList();
            updateOutputList();
        });
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);

        JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
        separator.setPreferredSize(new Dimension(550, 10));

        updateInputList();
        updateOutputList();

        centerPanel.add(attributesPanel, gbc);
        centerPanel.add(sliderPanel, gbc);
        centerPanel.add(separator, gbc);
        centerPanel.add(buttonPanel, gbc);

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

        outputList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int index = outputList.locationToIndex(evt.getPoint());
                    editOutput(index);
                }
            }
        });
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
                progress.setValue((int) (pair.value * 100));
                panel.add(BorderLayout.SOUTH, progress);

                return panel;
            }
        });
    }

    private void updateInputList() {
        inputListModel.clear();
        int index = slider.getValue();
        int i = 0;
        for (Attribute attribute : main.ann.attributes[0]) {
            double value = main.ann.trainingSet.patterns[index].input[i];
            AttributePair pair = new AttributePair(attribute, value);
            inputListModel.addElement(pair);
            i += 1;
        }
    }

    private void updateOutputList() {
        outputListModel.clear();
        int index = slider.getValue();
        int i = 0;
        for (Attribute attribute : main.ann.attributes[1]) {
            double value = main.ann.trainingSet.patterns[index].output[i];
            AttributePair pair = new AttributePair(attribute, value);
            outputListModel.addElement(pair);
            i += 1;
        }
    }

    public void addPattern() {
        main.ann.trainingSet.addPattern();
        slider.setMaximum(main.ann.trainingSet.patterns.length-1);
        slider.setValue(main.ann.trainingSet.patterns.length-1);
        int index = slider.getValue();
        indexLabel.setText(String.valueOf(index+1));
    }

    private void removePattern() {
        if (slider.getMaximum() > 1) {
            int index = slider.getValue();
            main.ann.trainingSet.deleteAt(index);
            slider.setMaximum(main.ann.trainingSet.patterns.length - 1);
            slider.setValue(0);
            indexLabel.setText(String.valueOf(1));
        } else {
            JOptionPane.showMessageDialog(null, "Number of patterns must be bigger than 2.", "Delete Pattern Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editInput(int index) {
        if (index >= 0) {
            AttributePair pair = inputListModel.getElementAt(index);
            ValueEditDialog editDialog = new ValueEditDialog(main, pair);
            editDialog.setVisible(true);

            if (editDialog.isConfirmed()) {
                pair = editDialog.getAttributePair();
                inputListModel.set(index, pair);
                main.ann.trainingSet.patterns[slider.getValue()].input[index] = pair.value;
            }
        }
    }

    private void editOutput(int index) {
        if (index >= 0) {
            AttributePair pair = outputListModel.getElementAt(index);
            ValueEditDialog editDialog = new ValueEditDialog(main, pair);
            editDialog.setVisible(true);

            if (editDialog.isConfirmed()) {
                pair = editDialog.getAttributePair();
                outputListModel.set(index, pair);
                main.ann.trainingSet.patterns[slider.getValue()].output[index] = pair.value;
            }
        }

    }

    public void onANNUpdated() {
        slider.setMaximum(main.ann.trainingSet.patterns.length-1);
        updateInputList();
        updateOutputList();
    }
}
