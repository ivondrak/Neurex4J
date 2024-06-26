package neurex.gui;
import neurex.ann.NeuralNet;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.List;

public class MainFrame extends JFrame {

    public NeuralNet ann;
    public String filename = "undefined.neux";

    public CardLayout cardLayout = new CardLayout();
    public JPanel mainPanel = new JPanel(cardLayout);
    public Map<String, JPanel> cardMap;

    private final List<ANNUpdateListener> listeners = new ArrayList<>();

    public MainFrame(NeuralNet ann) {
        this.ann = ann;
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
        JMenuItem patternsItem = new JMenuItem("Patterns");
        JMenuItem learningItem = new JMenuItem("Learning");
        JMenuItem consultItem = new JMenuItem("Consult");

        viewMenu.add(topologyItem);
        viewMenu.add(attributesItem);
        viewMenu.add(patternsItem);
        viewMenu.add(learningItem);
        viewMenu.addSeparator();
        viewMenu.add(consultItem);

        // Help Menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem credentialsItem = new JMenuItem("Credentials");

        helpMenu.add(credentialsItem);

        // Add action listeners for menu items (example actions)
        newItem.addActionListener(e -> {
            changeModel();
            cardLayout.show(mainPanel, "Init");
        });
        openItem.addActionListener(e -> openFile());
        saveItem.addActionListener(e -> save());
        saveAsItem.addActionListener(e -> {
            SaveFileDialog toSave = new SaveFileDialog();
            toSave.saveANN(this);
            setTitle("ANN: "+filename);
        });
        exitItem.addActionListener(e -> System.exit(0)); // This will exit the application
        topologyItem.addActionListener(e -> {
            changeModel();
            cardLayout.show(mainPanel, "Topology");
        });
        attributesItem.addActionListener(e -> {
            changeModel();
            cardLayout.show(mainPanel, "Attributes");
        });
        patternsItem.addActionListener(e -> {
            changeModel();
            cardLayout.show(mainPanel, "Patterns");
        });
        learningItem.addActionListener(e -> {
            changeModel();
            cardLayout.show(mainPanel, "Learning");
        });
        consultItem.addActionListener(e -> {
            changeModel();
            cardLayout.show(mainPanel, "Consult");
        });
        credentialsItem.addActionListener(e -> cardLayout.show(mainPanel, "Credentials"));

        // Add menus to menu bar
        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        menuBar.add(helpMenu);

        // Set the menu bar
        setJMenuBar(menuBar);
    }

    private void createViewPanels() {
        cardMap = new HashMap<>();

        JPanel initPanel = new InitPanel(this);
        mainPanel.add(initPanel, "Init");
        cardMap.put("Init",initPanel);

        JPanel topologyPanel = new TopologyPanel(this);
        mainPanel.add(topologyPanel, "Topology");
        cardMap.put("Topology",topologyPanel);

        JPanel attributesPanel = new AttributesPanel(this);
        mainPanel.add(attributesPanel, "Attributes");
        cardMap.put("Attributes",attributesPanel);

        JPanel patternsPanel = new PatternsPanel(this);
        mainPanel.add(patternsPanel, "Patterns");
        cardMap.put("Patterns",patternsPanel);

        JPanel learningPanel = new LearningPanel(this);
        mainPanel.add(learningPanel, "Learning");
        cardMap.put("Learning",learningPanel);

        JPanel consultPanel = new ConsultPanel(this);
        mainPanel.add(consultPanel, "Consult");
        cardMap.put("Consult",consultPanel);

        JPanel credentialPanel = new CredentialsPanel();
        mainPanel.add(credentialPanel, "Credentials");
        cardMap.put("Credential",credentialPanel);
    }

    void openFile() {
        OpenFileDialog toOpen = new OpenFileDialog();
        toOpen.openANN(this);
        setTitle("ANN: "+filename);
        changeModel();
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

    public void addUpdateListener(ANNUpdateListener listener) {
        listeners.add(listener);
    }

    @SuppressWarnings("unused")
    public void removeUpdateListener(ANNUpdateListener listener) {
        listeners.remove(listener);
    }

    private void notifyListeners() {
        for (ANNUpdateListener listener : listeners) {
            listener.onANNUpdated();
        }
    }

    public void changeModel() {
        notifyListeners();
    }
}

