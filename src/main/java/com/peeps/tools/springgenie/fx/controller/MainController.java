package com.peeps.tools.springgenie.fx.controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainController {

	@FXML
	private void showAddModelScreen() throws IOException {
		// Load the FXML
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addModel2.fxml"));
		Parent root = loader.load();

//		comnUtils.showAlert("test");

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
