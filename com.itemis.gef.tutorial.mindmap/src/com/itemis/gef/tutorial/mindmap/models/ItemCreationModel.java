/**
 * GEF 5.0.0 Mindmap Tutorial
 *
 *  Copyright 2017 by itemis AG
 *
 * This file is part of some open source application.
 *
 * Some open source application is free software: you can redistribute
 * it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 *
 * Some open source application is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 *
 * @license GPL-3.0+ <http://spdx.org/licenses/GPL-3.0+>
 */
package com.itemis.gef.tutorial.mindmap.models;

import com.itemis.gef.tutorial.mindmap.parts.MindMapNodePart;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * The {@link ItemCreationModel} is sued to store the creation state in the
 * application.
 *
 */
public class ItemCreationModel {

	public enum Type {
		None, Node, Connection
	};

	private ObjectProperty<Type> typeProperty = new SimpleObjectProperty<>(Type.None);
	private ObjectProperty<MindMapNodePart> sourceProperty = new SimpleObjectProperty<>();

	public MindMapNodePart getSource() {
		return sourceProperty.getValue();
	}

	public ObjectProperty<MindMapNodePart> getSourceProperty() {
		return sourceProperty;
	}

	public Type getType() {
		return typeProperty.getValue();
	}

	public ObjectProperty<Type> getTypeProperty() {
		return typeProperty;
	}

	public void setSource(MindMapNodePart source) {
		this.sourceProperty.setValue(source);
	}

	public void setType(Type type) {
		this.typeProperty.setValue(type);
	}
}