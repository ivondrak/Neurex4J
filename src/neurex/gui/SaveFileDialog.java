package neurex.gui;

import neurex.ann.NeuralNet;
import javax.swing.*;
import java.io.*;

public class SaveFileDialog {
	
    public void saveANN(NeuralNet ann) {

        JFileChooser fileChooser = new JFileChooser();
        //fileChooser.setCurrentDirectory(new File(System.getProperty(".")));
        fileChooser.setCurrentDirectory(new File("."));
        fileChooser.setSelectedFile(new File("undefined.neux"));
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("neurex files", "neux"));
        fileChooser.setDialogTitle("Save Project");
        fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);

        int userSelection = fileChooser.showSaveDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileToSave))) {
                oos.writeObject(ann);
                System.out.println("Object saved to: " + fileToSave.getAbsolutePath());
            } catch (IOException e) {
                //noinspection CallToPrintStackTrace
                e.printStackTrace();
            }
        }
    }
}

