package neurex.gui;
import neurex.ann.Attribute;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class AttributesPanel extends JPanel implements ANNUpdateListener {
    MainFrame main;
    private final DefaultListModel<Attribute> inputListModel;
    private final DefaultListModel<Attribute> outputListModel;
    private final JList<Attribute> inputAttributeList;
    private final JList<Attribute> outputAttributeList;

    public AttributesPanel(MainFrame main) {
        this.main = main;
        this.main.addUpdateListener(this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("Input and Output Attributes");
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
        inputAttributeList = new JList<>(inputListModel);
        JScrollPane inputScrollPanel = new JScrollPane(inputAttributeList);
        inputScrollPanel.setPreferredSize(new Dimension(250, 400));
        outputListModel = new DefaultListModel<>();
        outputAttributeList = new JList<>(outputListModel);
        JScrollPane outputScrollPanel = new JScrollPane(outputAttributeList);
        outputScrollPanel.setPreferredSize(new Dimension(250, 400));
        attributesPanel.add(inputScrollPanel);
        attributesPanel.add(outputScrollPanel);

        updateInputList();
        updateOutputList();

        centerPanel.add(attributesPanel, gbc);

        add(Box.createRigidArea(new Dimension(0, 10)));
        add(titleLabel);
        add(centerPanel);
        add(Box.createVerticalStrut(10));

        inputAttributeList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int index = inputAttributeList.locationToIndex(evt.getPoint());
                    editInputAttribute(index);
                }
            }
        });

        outputAttributeList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int index = outputAttributeList.locationToIndex(evt.getPoint());
                    editOutputAttribute(index);
                }
            }
        });

    }

    private void editInputAttribute(int index) {
        if (index >= 0) {
            Attribute attribute = inputListModel.getElementAt(index);
            AttributeEditDialog editDialog = new AttributeEditDialog(main, attribute);
            editDialog.setVisible(true);

            if (editDialog.isConfirmed()) {
                inputListModel.set(index, editDialog.getAttribute());
            }
        }
    }

    private void editOutputAttribute(int index) {
        if (index >= 0) {
            Attribute attribute = outputListModel.getElementAt(index);
            AttributeEditDialog editDialog = new AttributeEditDialog(main, attribute);
            editDialog.setVisible(true);

            if (editDialog.isConfirmed()) {
                outputListModel.set(index, editDialog.getAttribute());
            }
        }
    }

    private void updateInputList() {
        inputListModel.clear();
        for (Attribute attribute : main.ann.attributes[0]) {
            inputListModel.addElement(attribute);
        }
    }

    private void updateOutputList() {
        outputListModel.clear();
        for (Attribute attribute : main.ann.attributes[1]) {
            outputListModel.addElement(attribute);
        }
    }

    public void onANNUpdated() {
        updateInputList();
        updateOutputList();
    }
}
