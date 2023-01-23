package com.filemanager.java.manager.Controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Controller {
        @FXML
        VBox leftPanel, rightPanel;

    public void buttonExitAction(ActionEvent actionEvent)  {
        Platform.exit();
    }


    public void copyButtonAction(ActionEvent actionEvent) {
        PanelController leftPC = (PanelController) leftPanel.getProperties().get("controller");
        PanelController rightPC = (PanelController) rightPanel.getProperties().get("controller");

        if(leftPC.getSelectedFileName() == null && rightPC.getSelectedFileName() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Select FILE before copy",ButtonType.OK);
            alert.showAndWait();
            return;
    }
        PanelController srcPC = null, dstPC = null;
        if(leftPC.getSelectedFileName()!=null) {
            srcPC = leftPC;
            dstPC = rightPC;
    }
        if(rightPC.getSelectedFileName()!=null) {
            srcPC = rightPC;
            dstPC = leftPC;
        }
        Path srcPath = Paths.get(srcPC.getCurrentPath(), srcPC.getSelectedFileName());
        Path dstPath = Paths.get(dstPC.getCurrentPath()).resolve(srcPath.getFileName().toString());
        try {
           Files.copy(srcPath,dstPath);
           dstPC.updateList(Paths.get(dstPC.getCurrentPath()));
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING,
                    "Error, can't copy file! or file already exists",ButtonType.OK);
            alert.showAndWait();
            return;
        }

}
}