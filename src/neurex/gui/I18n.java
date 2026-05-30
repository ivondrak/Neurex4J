package neurex.gui;

import javax.swing.*;
import java.util.Locale;
import java.util.ResourceBundle;

public final class I18n {
    private static final Locale SUPPORTED_LOCALE = Locale.getDefault().getLanguage().equals("cs")
            ? Locale.forLanguageTag("cs")
            : Locale.ENGLISH;
    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("neurex.gui.Messages", SUPPORTED_LOCALE);

    private I18n() {
    }

    public static String text(String key) {
        return BUNDLE.getString(key);
    }

    public static void configureSwingDefaults() {
        UIManager.put("FileChooser.openDialogTitleText", text("file.open.title"));
        UIManager.put("FileChooser.saveDialogTitleText", text("file.save.title"));
        UIManager.put("FileChooser.saveDialogFileNameLabelText", text("file.saveAs"));
        UIManager.put("FileChooser.saveAsLabelText", text("file.saveAs"));
        UIManager.put("FileChooser.lookInLabelText", text("file.lookIn"));
        UIManager.put("FileChooser.saveInLabelText", text("file.saveIn"));
        UIManager.put("FileChooser.fileNameLabelText", text("file.fileName"));
        UIManager.put("FileChooser.filesOfTypeLabelText", text("file.filesOfType"));
        UIManager.put("FileChooser.openButtonText", text("file.open.button"));
        UIManager.put("FileChooser.saveButtonText", text("file.save.button"));
        UIManager.put("FileChooser.cancelButtonText", text("file.cancel.button"));
        UIManager.put("FileChooser.updateButtonText", text("file.update.button"));
        UIManager.put("FileChooser.helpButtonText", text("file.help.button"));
        UIManager.put("FileChooser.directoryOpenButtonText", text("file.open.button"));
        UIManager.put("FileChooser.openButtonToolTipText", text("file.open.tooltip"));
        UIManager.put("FileChooser.saveButtonToolTipText", text("file.save.tooltip"));
        UIManager.put("FileChooser.cancelButtonToolTipText", text("file.cancel.tooltip"));
        UIManager.put("FileChooser.updateButtonToolTipText", text("file.update.tooltip"));
        UIManager.put("FileChooser.helpButtonToolTipText", text("file.help.tooltip"));
        UIManager.put("FileChooser.directoryOpenButtonToolTipText", text("file.open.tooltip"));
        UIManager.put("FileChooser.acceptAllFileFilterText", text("file.allFiles"));
        UIManager.put("FileChooser.upFolderToolTipText", text("file.upFolder.tooltip"));
        UIManager.put("FileChooser.homeFolderToolTipText", text("file.homeFolder.tooltip"));
        UIManager.put("FileChooser.newFolderButtonText", text("file.newFolder.button"));
        UIManager.put("FileChooser.newFolderToolTipText", text("file.newFolder.tooltip"));
        UIManager.put("FileChooser.newFolderTitleText", text("file.newFolder.title"));
        UIManager.put("FileChooser.newFolderActionLabelText", text("file.newFolder.button"));
        UIManager.put("FileChooser.other.newFolder", text("file.newFolder.name"));
        UIManager.put("FileChooser.other.newFolder.subsequent", text("file.newFolder.name") + ".{0}");
        UIManager.put("FileChooser.mac.newFolder", text("file.newFolder.name"));
        UIManager.put("FileChooser.mac.newFolder.subsequent", text("file.newFolder.name") + ".{0}");
        UIManager.put("FileChooser.win32.newFolder", text("file.newFolder.name"));
        UIManager.put("FileChooser.win32.newFolder.subsequent", text("file.newFolder.name") + " ({0})");
        UIManager.put("FileChooser.listViewButtonToolTipText", text("file.listView.tooltip"));
        UIManager.put("FileChooser.detailsViewButtonToolTipText", text("file.detailsView.tooltip"));
        UIManager.put("FileChooser.fileNameHeaderText", text("file.header.name"));
        UIManager.put("FileChooser.fileSizeHeaderText", text("file.header.size"));
        UIManager.put("FileChooser.fileTypeHeaderText", text("file.header.type"));
        UIManager.put("FileChooser.fileDateHeaderText", text("file.header.date"));
        UIManager.put("FileChooser.fileAttrHeaderText", text("file.header.attributes"));
    }
}
