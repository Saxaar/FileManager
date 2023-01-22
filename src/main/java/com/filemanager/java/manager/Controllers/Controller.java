package com.filemanager.java.manager.Controllers;

import com.filemanager.java.manager.model.FileInfo;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;


import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class Controller implements Initializable {
    @FXML
    TableView <FileInfo> filesTable;

    public void buttonExitAction(ActionEvent actionEvent)  {
        Platform.exit();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //File Type Column
        TableColumn<FileInfo,String> fileTypeColumn = new TableColumn<>();
        fileTypeColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getType().getName()));
        fileTypeColumn.setPrefWidth(24);

        //File Name Column
        TableColumn<FileInfo,String> fileNameColumn = new TableColumn<>("Name");
        fileNameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getName()));
        fileNameColumn.setPrefWidth(240);

        //File size
        TableColumn<FileInfo,Long> fileSizeColumn = new TableColumn<>("Size");
        fileSizeColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getSize()));
        fileSizeColumn.setPrefWidth(140);
        fileSizeColumn.setCellFactory(column -> {
            return new TableCell<FileInfo,Long>() {
                @Override
                protected void updateItem(Long item, boolean empty) {
                    super.updateItem(item, empty);
                    if(item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        String text = String.format("%,d bytes" , item);
                        if(item == -1L ) {
                           text = "[DIR]";
                        }
                        setText(text);

                    }
                }
            };
        });


        filesTable.getColumns().addAll(fileTypeColumn,fileNameColumn,fileSizeColumn);
        filesTable.getSortOrder().add(fileTypeColumn);
        updateList(Paths.get("."));

    }

    public void updateList(Path path) {
        try {
            filesTable.getItems().clear();
            filesTable.getItems().addAll(Files.list(path).map(FileInfo::new).collect(Collectors.toList()));
            filesTable.sort();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING,"Unable to read", ButtonType.OK);
            alert.showAndWait();
        }
    }
}
