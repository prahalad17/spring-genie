package com.peeps.tools.springgenie.fx.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.peeps.tools.springgenie.fx.utils.TemplateConstants;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;

public class AddModelController {

	@FXML
	private TextField classNameField;

	@FXML
	private TextField packageNameField;

	@FXML
	private CheckBox entityAnot;

	@FXML
	private CheckBox dataAnot;

	@FXML
	private CheckBox tableAnot;

	@FXML
	private CheckBox noArgsAnot;

	@FXML
	private CheckBox allArgsAnot;

	@FXML
	private TextField newFieldName;

	@FXML
	private ComboBox<String> newFieldType;

	@FXML
	private CheckBox pkDbProp;

	@FXML
	private TableView<ModelField> fieldsTable;
	@FXML
	private TableColumn<ModelField, String> fieldNameColumn;
	@FXML
	private TableColumn<ModelField, String> fieldTypeColumn;
	@FXML
	private TableColumn<ModelField, Boolean> fieldPropsColumn;

	private final ObservableList<ModelField> fields = FXCollections.observableArrayList();

	@FXML
	public void initialize() {

		newFieldType.setItems(
				FXCollections.observableArrayList("String", "int", "long", "boolean", "LocalDate", "LocalDateTime"));

		fieldNameColumn.setCellValueFactory(data -> data.getValue().fieldNameProperty());
		fieldTypeColumn.setCellValueFactory(data -> data.getValue().fieldTypeProperty());

		// make name editable (TextField cell)
		fieldNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		fieldNameColumn.setOnEditCommit(evt -> evt.getRowValue().setFieldName(evt.getNewValue()));
		fieldNameColumn.setEditable(true);

		// make type editable with ComboBox
		ObservableList<String> types = FXCollections.observableArrayList("String", "int", "long", "boolean",
				"LocalDate", "LocalDateTime");
		fieldTypeColumn.setCellFactory(ComboBoxTableCell.forTableColumn(types));
		fieldTypeColumn.setOnEditCommit(evt -> evt.getRowValue().setFieldType(evt.getNewValue()));
		fieldTypeColumn.setEditable(true);

		// add an ID checkbox column
		fieldPropsColumn.setCellValueFactory(cellData -> cellData.getValue().pkProperty());
		fieldPropsColumn.setCellFactory(CheckBoxTableCell.forTableColumn(fieldPropsColumn));
		fieldPropsColumn.setEditable(true);

		fieldsTable.setEditable(true);
		fieldsTable.setItems(fields);
	}

	@FXML
	private void handleAddField() {

		String name = newFieldName.getText().trim();
		String type = newFieldType.getValue().trim();

		boolean isPk = pkDbProp.isSelected();

		if (!name.isEmpty() && !type.isEmpty()) {

			// create ModelField with id/pk flags
			ModelField mf = new ModelField(name, type, isPk);
			fields.add(mf);
			// reset input controls
			newFieldName.clear();
			newFieldType.getSelectionModel().clearSelection();
			pkDbProp.setSelected(false);

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

	private String generateClassAnnotations(String className, List<ModelField> fields, Set<String> imports) {
		// For now we always add @Entity
		imports.add("import javax.persistence.Entity;");
		imports.add("import javax.persistence.Table;");
		StringBuilder sb = new StringBuilder();

		if (entityAnot.isSelected())
			sb.append("@Entity\n");

		if (dataAnot.isSelected())
			sb.append("@Data\n");
		if (allArgsAnot.isSelected())
			sb.append("@AllArgsConstructor\n");
		if (noArgsAnot.isSelected())
			sb.append("@NoArgsConstructor\n");

		String tableName = toSnakeCase(className);
		sb.append(String.format("@Table(name = \"%s\")", tableName));
		return sb.toString();
	}

	// small utility
	private String toSnakeCase(String input) {
		// Simple camelCase to snake_case, e.g. FormDetails -> form_details
		String s = input.replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase();
		return s;
	}

	private void generateModelClass(String className, List<ModelField> fields) {
		try {
			String template = loadTemplate("model_class_template.txt");
			String fieldsBlock = generateFieldBlock(fields);

			String packageName = "com.peeps.generated.model"; // or get from config/UI

			Set<String> imports = new TreeSet<>();

			String classAnnotations = generateClassAnnotations(className, fields, imports);

			String content = template.replace("${ClassName}", className).replace("${packageName}", packageName)
					.replace("${fields}", fieldsBlock).replace("${author}", TemplateConstants.authorName)
					.replace("${classAnnotations}", classAnnotations)
					.replace("${commonFields}", TemplateConstants.dbCommonFields).replace("${imports}","");

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
			sb.append("    @Column(name = \"").append(toSnakeCase(field.getFieldName())).append("\")").append("\n")
					.append("    private ").append(field.getFieldType()).append(" ").append(field.getFieldName())
					.append(";\n");
		}
		return sb.toString();
	}

	private void showAlert(String message) {
		Alert alert = new Alert(Alert.AlertType.WARNING);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

}
