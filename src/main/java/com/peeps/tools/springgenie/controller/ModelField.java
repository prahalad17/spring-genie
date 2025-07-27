package com.peeps.tools.springgenie.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ModelField {
	private final StringProperty fieldName;
	private final StringProperty fieldType;

	public ModelField(String fieldName, String fieldType) {
		this.fieldName = new SimpleStringProperty(fieldName);
		this.fieldType = new SimpleStringProperty(fieldType);
	}

	public String getFieldName() {
		return fieldName.get();
	}

	public void setFieldName(String value) {
		fieldName.set(value);
	}

	public StringProperty fieldNameProperty() {
		return fieldName;
	}

	public String getFieldType() {
		return fieldType.get();
	}

	public void setFieldType(String value) {
		fieldType.set(value);
	}

	public StringProperty fieldTypeProperty() {
		return fieldType;
	}
}
