package com.peeps.tools.springgenie.fx.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class ModelController {
	
	@FXML
	private TextField classNameField;
	@FXML
	private TextField newFieldName;
	@FXML
	private TextField newFieldType;
	@FXML
	private TableView<ModelField> fieldsTable;
	@FXML
	private TableColumn<ModelField, String> fieldNameColumn;
	@FXML
	private TableColumn<ModelField, String> fieldTypeColumn;

	private final ObservableList<ModelField> fields = FXCollections.observableArrayList();

	@FXML
	public void initialize() {
		// Bind the table columns
		fieldNameColumn.setCellValueFactory(data -> data.getValue().fieldNameProperty());
		fieldTypeColumn.setCellValueFactory(data -> data.getValue().fieldTypeProperty());

		// Set items to table
		fieldsTable.setItems(fields);
	}

	@FXML
	private void handleAddField() {
		String name = newFieldName.getText().trim();
		String type = newFieldType.getText().trim();

		if (!name.isEmpty() && !type.isEmpty()) {
			fields.add(new ModelField(name, type));
			newFieldName.clear();
			newFieldType.clear();
		} else {
			showAlert("Please enter both field name and type.");
		}
	}

	@FXML
	private void handleGenerateModel() {
		String className = classNameField.getText().trim();
		if (className.isEmpty() || fields.isEmpty()) {
			showAlert("Class name or fields are missing.");
			return;
		}
		generateModelClass(className, fields);
	}

	private void showAlert(String message) {
		Alert alert = new Alert(Alert.AlertType.WARNING);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	private String loadTemplate(String templateName) throws IOException {
		InputStream is = getClass().getResourceAsStream("/templates/" + templateName);
		if (is == null) {
			throw new FileNotFoundException("Template not found: " + templateName);
		}
		return new String(is.readAllBytes(), StandardCharsets.UTF_8);
	}

	private String generateFieldBlock(List<ModelField> fields) {
		StringBuilder sb = new StringBuilder();
		for (ModelField field : fields) {
			sb.append("    private ").append(field.getFieldType()).append(" ").append(field.getFieldName())
					.append(";\n");
		}
		return sb.toString();
	}

	private void generateModelClass(String className, List<ModelField> fields) {
		try {
			String template = loadTemplate("model_class_template.txt");
			String fieldsBlock = generateFieldBlock(fields);

			String packageName = "com.peeps.generated.model"; // or get from config/UI

			String content = template.replace("${ClassName}", className).replace("${packageName}", packageName)
					.replace("${fields}", fieldsBlock);

			System.out.println(content);
			// Output path (update as needed)
//			File outputDir = new File("generated-src/com/peeps/generated/model");
//			if (!outputDir.exists())
//				outputDir.mkdirs();
//
//			File outputFile = new File(outputDir, className + ".java");
//			Files.write(outputFile.toPath(), content.getBytes(StandardCharsets.UTF_8));

//			showAlert("Class generated at: " + outputFile.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
			showAlert("Error generating model: " + e.getMessage());
		}
	}
}
