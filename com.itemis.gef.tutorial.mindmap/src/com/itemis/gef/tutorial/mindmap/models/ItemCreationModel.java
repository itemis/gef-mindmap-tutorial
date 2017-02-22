package com.itemis.gef.tutorial.mindmap.models;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * The {@link ItemCreationModel} is sued to store the creation state in the
 * application.
 *
 */
public class ItemCreationModel {

	public enum Type {
		None, Node
	}

	private ObjectProperty<Type> typeProperty = new SimpleObjectProperty<ItemCreationModel.Type>(Type.None);

	public Type getType() {
		return typeProperty.getValue();
	}

	public ObjectProperty<Type> getTypeProperty() {
		return typeProperty;
	}

	public void setType(Type type) {
		this.typeProperty.setValue(type);
	}
}