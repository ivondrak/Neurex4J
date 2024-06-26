package neurex.gui;

import neurex.ann.NeuralNet;
import javax.swing.*;
import java.io.*;

public class OpenFileDialog {
	
    public void openANN(MainFrame main) {

        JFileChooser fileChooser = new JFileChooser();
        //fileChooser.setCurrentDirectory(new File(System.getProperty(".")));
        fileChooser.setCurrentDirectory(new File("."));
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("neurex files", "neux"));
        fileChooser.setDialogTitle("Open Project");
        fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);

        int userSelection = fileChooser.showOpenDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToOpen = fileChooser.getSelectedFile();
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileToOpen))) {
                main.ann = (NeuralNet) ois.readObject();
                main.filename = fileToOpen.getAbsolutePath();
            } catch (IOException | ClassNotFoundException e) {
                //noinspection CallToPrintStackTrace
                e.printStackTrace();
            }
        }
    }
}