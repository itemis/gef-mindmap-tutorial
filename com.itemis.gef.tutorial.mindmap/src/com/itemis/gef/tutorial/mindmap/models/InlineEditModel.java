package com.itemis.gef.tutorial.mindmap.models;

import org.eclipse.gef.mvc.fx.parts.IVisualPart;

import javafx.scene.Node;

/**
 * The InlineEditModel is used to store temporary information while direct editing a part.
 */
public class InlineEditModel {
	
	private IVisualPart<? extends Node> host;
	private IInlineEditableField currentEditableField;

	public void setHost(IVisualPart<? extends Node> host) {
		this.host = host;
	}

	public IVisualPart<? extends Node> getHost() {
		return host;
	}

	public void setCurrentEditableField(IInlineEditableField currentEditableField) {
		this.currentEditableField = currentEditableField;
	}

	public IInlineEditableField getCurrentEditableField() {
		return currentEditableField;
	}
}