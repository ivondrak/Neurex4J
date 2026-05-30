package neurex.gui;
import neurex.ann.NeuralNet;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.Serial;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.List;

public class MainFrame extends JFrame {
    @Serial
    private static final long serialVersionUID = 1L;

    public NeuralNet ann;
    public String filename = "undefined.neux";

    public CardLayout cardLayout = new CardLayout();
    public JPanel mainPanel = new JPanel(cardLayout);
    public HashMap<String, JPanel> cardMap;

    private final ToastBar toastBar = new ToastBar();
    private final transient List<ANNUpdateListener> listeners = new ArrayList<>();

    @SuppressWarnings("this-escape")
    public MainFrame(NeuralNet ann) {
        this.ann = ann;
        setApplicationIcon();
        createMenuBar();
        createViewPanels();
        updateTitle();
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        add(mainPanel, BorderLayout.CENTER);
        add(toastBar, BorderLayout.SOUTH);
    }

    @SuppressWarnings("this-escape")
    private void setApplicationIcon() {
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream("images/neurex.png")) {
            Image icon = ImageIO.read(Objects.requireNonNull(stream));
            setIconImage(icon);
            setTaskbarIcon(icon);
        } catch (IOException e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
    }

    private void setTaskbarIcon(Image icon) {
        if (!Taskbar.isTaskbarSupported()) {
            return;
        }

        Taskbar taskbar = Taskbar.getTaskbar();
        if (taskbar.isSupported(Taskbar.Feature.ICON_IMAGE)) {
            taskbar.setIconImage(icon);
        }
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // File Menu
        JMenu fileMenu = new JMenu(I18n.text("menu.file"));
        JMenuItem newItem = new JMenuItem(I18n.text("menu.file.new"));
        JMenuItem openItem = new JMenuItem(I18n.text("menu.file.open"));
        JMenuItem saveItem = new JMenuItem(I18n.text("menu.file.save"));
        JMenuItem saveAsItem = new JMenuItem(I18n.text("menu.file.saveAs"));
        JMenuItem exitItem = new JMenuItem(I18n.text("menu.file.exit"));

        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(saveAsItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        // View Menu
        JMenu viewMenu = new JMenu(I18n.text("menu.view"));
        JMenuItem topologyItem = new JMenuItem(I18n.text("menu.view.topology"));
        JMenuItem attributesItem = new JMenuItem(I18n.text("menu.view.attributes"));
        JMenuItem patternsItem = new JMenuItem(I18n.text("menu.view.patterns"));
        JMenuItem learningItem = new JMenuItem(I18n.text("menu.view.learning"));
        JMenuItem consultItem = new JMenuItem(I18n.text("menu.view.consult"));

        viewMenu.add(topologyItem);
        viewMenu.add(attributesItem);
        viewMenu.add(patternsItem);
        viewMenu.add(learningItem);
        viewMenu.addSeparator();
        viewMenu.add(consultItem);

        // Help Menu
        JMenu helpMenu = new JMenu(I18n.text("menu.help"));
        JMenuItem credentialsItem = new JMenuItem(I18n.text("menu.help.credentials"));

        helpMenu.add(credentialsItem);

        // Add action listeners for menu items (example actions)
        newItem.addActionListener(_ -> {
            changeModel();
            cardLayout.show(mainPanel, "Init");
        });
        openItem.addActionListener(_ -> openFile());
        saveItem.addActionListener(_ -> save());
        saveAsItem.addActionListener(_ -> {
            SaveFileDialog toSave = new SaveFileDialog();
            toSave.saveANN(this);
            updateTitle();
        });
        exitItem.addActionListener(_ -> System.exit(0)); // This will exit the application
        topologyItem.addActionListener(_ -> {
            changeModel();
            cardLayout.show(mainPanel, "Topology");
        });
        attributesItem.addActionListener(_ -> {
            changeModel();
            cardLayout.show(mainPanel, "Attributes");
        });
        patternsItem.addActionListener(_ -> {
            changeModel();
            cardLayout.show(mainPanel, "Patterns");
        });
        learningItem.addActionListener(_ -> {
            changeModel();
            cardLayout.show(mainPanel, "Learning");
        });
        consultItem.addActionListener(_ -> {
            changeModel();
            cardLayout.show(mainPanel, "Consult");
        });
        credentialsItem.addActionListener(_ -> cardLayout.show(mainPanel, "Credentials"));

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
        addCard("Init", initPanel);

        JPanel topologyPanel = new TopologyPanel(this);
        addCard("Topology", topologyPanel);

        JPanel attributesPanel = new AttributesPanel(this);
        addCard("Attributes", attributesPanel);

        JPanel patternsPanel = new PatternsPanel(this);
        addCard("Patterns", patternsPanel);

        JPanel learningPanel = new LearningPanel(this);
        addCard("Learning", learningPanel);

        JPanel consultPanel = new ConsultPanel(this);
        addCard("Consult", consultPanel);

        JPanel credentialPanel = new CredentialsPanel();
        addCard("Credentials", credentialPanel);
    }

    private void addCard(String name, JPanel panel) {
        mainPanel.add(panel, name);
        cardMap.put(name, panel);
        if (panel instanceof ANNUpdateListener listener) {
            addUpdateListener(listener);
        }
    }

    void openFile() {
        OpenFileDialog toOpen = new OpenFileDialog();
        OpenFileDialog.OpenResult result = toOpen.openANN(this);
        if (result == OpenFileDialog.OpenResult.SUCCESS) {
            updateTitle();
            changeModel();
            toastBar.showSuccess(I18n.text("toast.open.success"));
        } else if (result == OpenFileDialog.OpenResult.FAILURE) {
            toastBar.showError(I18n.text("toast.open.failure"));
        }
    }

    void save() {
        if (Objects.equals(filename, "undefined.neux")) {
            SaveFileDialog toSave = new SaveFileDialog();
            toSave.saveANN(this);
            updateTitle();
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

    private void updateTitle() {
        setTitle(I18n.text("app.title.prefix") + filename);
    }
}
