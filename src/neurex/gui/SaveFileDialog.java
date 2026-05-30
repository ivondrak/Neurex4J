package neurex.gui;


import javax.swing.*;
import java.io.*;

public class SaveFileDialog {
	
    public void saveANN(MainFrame main) {

        JFileChooser fileChooser = new JFileChooser();
        //fileChooser.setCurrentDirectory(new File(System.getProperty(".")));
        fileChooser.setCurrentDirectory(new File("."));
        fileChooser.setSelectedFile(new File(main.filename));
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(I18n.text("file.filter.neurex"), "neux"));
        fileChooser.setDialogTitle(I18n.text("file.save.title"));
        fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
        fileChooser.setApproveButtonText(I18n.text("file.save.button"));
        fileChooser.setApproveButtonToolTipText(I18n.text("file.save.tooltip"));

        int userSelection = fileChooser.showSaveDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileToSave))) {
                oos.writeObject(main.ann);
                main.filename = fileToSave.getAbsolutePath();
            } catch (IOException e) {
                //noinspection CallToPrintStackTrace
                e.printStackTrace();
            }
        }
    }
}
