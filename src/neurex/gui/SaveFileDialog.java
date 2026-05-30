package neurex.gui;


import neurex.ann.NeuralNetJson;

import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class SaveFileDialog {
	
    public void saveANN(MainFrame main) {

        JFileChooser fileChooser = new JFileChooser();
        //fileChooser.setCurrentDirectory(new File(System.getProperty(".")));
        fileChooser.setCurrentDirectory(new File("."));
        fileChooser.setSelectedFile(new File(main.filename));
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(I18n.text("file.filter.neurex"), "ann", "json"));
        fileChooser.setDialogTitle(I18n.text("file.save.title"));
        fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
        fileChooser.setApproveButtonText(I18n.text("file.save.button"));
        fileChooser.setApproveButtonToolTipText(I18n.text("file.save.tooltip"));

        int userSelection = fileChooser.showSaveDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = withDefaultExtension(fileChooser.getSelectedFile());
            try (Writer writer = new OutputStreamWriter(new FileOutputStream(fileToSave), StandardCharsets.UTF_8)) {
                NeuralNetJson.write(main.ann, writer);
                main.filename = fileToSave.getAbsolutePath();
            } catch (IOException e) {
                //noinspection CallToPrintStackTrace
                e.printStackTrace();
            }
        }
    }

    private File withDefaultExtension(File file) {
        String name = file.getName();
        String lowerName = name.toLowerCase();
        if (lowerName.endsWith(".ann") || lowerName.endsWith(".json")) {
            return file;
        }
        File parent = file.getParentFile();
        if (parent == null) {
            return new File(name + ".ann");
        }
        return new File(parent, name + ".ann");
    }
}
