package com.peeps.tools.springgenie.fx.controller;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ModelField {
	private final StringProperty fieldName;
	private final StringProperty fieldType;
	private final BooleanProperty pk; // primary / generated PK marker

	public ModelField(String fieldName, String fieldType) {
		this(fieldName, fieldType, false);
	}

	public ModelField(String fieldName, String fieldType, boolean isPk) {
		this.fieldName = new SimpleStringProperty(fieldName);
		this.fieldType = new SimpleStringProperty(fieldType);
		this.pk = new SimpleBooleanProperty(isPk);
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

	public boolean isPk() {
		return pk.get();
	}

	public void setPk(boolean value) {
		pk.set(value);
	}

	public BooleanProperty pkProperty() {
		return pk;
	}

	@Override
	public String toString() {
		return String.format("ModelField{name=%s,type=%s,id=%s,pk=%s}", getFieldName(), getFieldType(), isPk());
	}
}
