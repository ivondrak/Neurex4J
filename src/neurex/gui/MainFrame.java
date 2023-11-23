package neurex.gui;
import neurex.ann.NeuralNet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Objects;

public class MainFrame extends JFrame {

    public NeuralNet ann;
    public String filename = "undefined.neux";

    public CardLayout cardLayout = new CardLayout();
    public JPanel mainPanel = new JPanel(cardLayout);

    public MainFrame() {
        createMenuBar();
        createViewPanels();
        setTitle("ANN: "+filename);
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        add(mainPanel);
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // File Menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem newItem = new JMenuItem("New");
        JMenuItem openItem = new JMenuItem("Open");
        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem saveAsItem = new JMenuItem("Save As");
        JMenuItem exitItem = new JMenuItem("Exit");

        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(saveAsItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        // View Menu
        JMenu viewMenu = new JMenu("View");
        JMenuItem topologyItem = new JMenuItem("Topology");
        JMenuItem attributesItem = new JMenuItem("Attributes");
        JMenuItem patternItem = new JMenuItem("Pattern");
        JMenuItem learningItem = new JMenuItem("Learning");
        JMenuItem consultItem = new JMenuItem("Consult");

        viewMenu.add(topologyItem);
        viewMenu.add(attributesItem);
        viewMenu.add(patternItem);
        viewMenu.add(learningItem);
        viewMenu.addSeparator();
        viewMenu.add(consultItem);

        // Help Menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem credentialsItem = new JMenuItem("Credentials");

        helpMenu.add(credentialsItem);

        // Add action listeners for menu items (example actions)
        newItem.addActionListener(e -> cardLayout.show(mainPanel, "Init"));
        openItem.addActionListener(e -> openFile());
        saveItem.addActionListener(e -> save());
        saveAsItem.addActionListener(e -> {
            SaveFileDialog toSave = new SaveFileDialog();
            toSave.saveANN(this);
            setTitle("ANN: "+filename);
        });
        exitItem.addActionListener(e -> System.exit(0)); // This will exit the application
        topologyItem.addActionListener(e -> cardLayout.show(mainPanel, "Topology"));
        attributesItem.addActionListener(e -> cardLayout.show(mainPanel, "Attributes"));
        patternItem.addActionListener(e -> cardLayout.show(mainPanel, "Patterns"));
        learningItem.addActionListener(e -> cardLayout.show(mainPanel, "Learning"));
        consultItem.addActionListener(e -> cardLayout.show(mainPanel, "Consult"));
        credentialsItem.addActionListener(e -> cardLayout.show(mainPanel, "Credentials"));

        // Add menus to menu bar
        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        menuBar.add(helpMenu);

        // Set the menu bar
        setJMenuBar(menuBar);
    }

    private void createViewPanels() {
        JPanel initPanel = new InitPanel(this);
        mainPanel.add(initPanel, "Init");

        JPanel topologyPanel = new JPanel();
        topologyPanel.add(new JLabel("Topology View"));
        mainPanel.add(topologyPanel, "Topology");

        JPanel attributesPanel = new JPanel();
        attributesPanel.add(new JLabel("Attributes View"));
        mainPanel.add(attributesPanel, "Attributes");

        JPanel patternPanel = new JPanel();
        patternPanel.add(new JLabel("Pattern View"));
        mainPanel.add(patternPanel, "Patterns");

        JPanel learningPanel = new JPanel();
        learningPanel.add(new JLabel("Learning View"));
        mainPanel.add(learningPanel, "Learning");

        JPanel consultPanel = new JPanel();
        consultPanel.add(new JLabel("Consult View"));
        mainPanel.add(consultPanel, "Consult");

        JPanel credentialPanel = new CredentialsPanel();
        mainPanel.add(credentialPanel, "Credentials");
    }

    void openFile() {
        OpenFileDialog toOpen = new OpenFileDialog();
        toOpen.openANN(this);
        setTitle("ANN: "+filename);
    }

    void save() {
        if (Objects.equals(filename, "undefined.neux")) {
            SaveFileDialog toSave = new SaveFileDialog();
            toSave.saveANN(this);
            setTitle("ANN: "+filename);
        } else {
            File fileToSave = new File(filename);
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileToSave))) {
                oos.writeObject(ann);
                System.out.println("Object saved to: " + fileToSave.getAbsolutePath());
                filename = fileToSave.getAbsolutePath();
            } catch (IOException e) {
                //noinspection CallToPrintStackTrace
                e.printStackTrace();
            }
        }
    }
}
