package neurex.gui;

import neurex.ann.NeuralNetJson;
import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class OpenFileDialog {

    enum OpenResult {
        SUCCESS,
        FAILURE,
        CANCELLED
    }
	
    public OpenResult openANN(MainFrame main) {

        JFileChooser fileChooser = new JFileChooser();
        //fileChooser.setCurrentDirectory(new File(System.getProperty(".")));
        fileChooser.setCurrentDirectory(new File("."));
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(I18n.text("file.filter.neurex"), "ann", "json"));
        fileChooser.setDialogTitle(I18n.text("file.open.title"));
        fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
        fileChooser.setApproveButtonText(I18n.text("file.open.button"));
        fileChooser.setApproveButtonToolTipText(I18n.text("file.open.tooltip"));

        int userSelection = fileChooser.showOpenDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToOpen = fileChooser.getSelectedFile();
            try (Reader reader = new InputStreamReader(new FileInputStream(fileToOpen), StandardCharsets.UTF_8)) {
                main.ann = NeuralNetJson.read(reader);
                main.filename = fileToOpen.getAbsolutePath();
                return OpenResult.SUCCESS;
            } catch (IOException e) {
                //noinspection CallToPrintStackTrace
                e.printStackTrace();
                return OpenResult.FAILURE;
            }
        }
        return OpenResult.CANCELLED;
    }
}
