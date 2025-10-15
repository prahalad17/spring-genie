package com.peeps.tools.springgenie.fx.controller;

import java.io.File;
import java.io.IOException;

import com.peeps.tools.springgenie.fx.utils.ProjectUtils;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainController {

	@FXML
	private Label folderPathLabel; // Make sure fx:id is set in Scene Builder

	@FXML
	private void handleChooseFolder() {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle("Select Folder");

		// Get the current window (stage)
		Stage stage = (Stage) folderPathLabel.getScene().getWindow();

		File selectedDirectory = directoryChooser.showDialog(stage);

		if (selectedDirectory != null) {
			folderPathLabel.setText(selectedDirectory.getAbsolutePath());

			if (ProjectUtils.isSpringBootProject(selectedDirectory)) {
				folderPathLabel.setText("✅ Spring Boot project: " + selectedDirectory.getAbsolutePath());

				// Example: Print files to console
				ProjectUtils.listFilesRecursively(selectedDirectory);
			} else {
				folderPathLabel.setText("❌ Not a Spring Boot project");
			}
		} else {
			folderPathLabel.setText("No folder selected");
		}
	}

	@FXML
	private void showAddModelScreen() throws IOException {
		// Load the FXML
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addModel.fxml"));
		Parent root = loader.load();

		// Create a new stage (popup)
		Stage popupStage = new Stage();
		popupStage.setTitle("Add Model");

		// Optional: make it modal (block interaction with parent until closed)
		popupStage.initModality(Modality.APPLICATION_MODAL);

		// Optional: set the parent window as owner
//		 popupStage.initOwner(((Node) event.getSource()).getScene().getWindow());

		// Set scene
		Scene scene = new Scene(root);
		popupStage.setScene(scene);

		popupStage.showAndWait(); // waits until closed
	}

}
