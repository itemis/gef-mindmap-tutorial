package com.itemis.gef.tutorial.mindmap.models;

import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * Describes a field of an {@link IInlineEditablePart}, which is actually editable.
 */
public abstract class AbstractInlineEditableField implements IInlineEditableField {
	
	/**
	 * The name of the field
	 */
	private final String propertyName;
	
	/**
	 * The original read only node in the parts visual
	 */
	private final Node readOnlyNode;
	
	/** 
	 * The editor node, e.g. a {@link TextField} or {@link TextArea}
	 */
	private Node editorNode;

	public AbstractInlineEditableField(String propertyName, Node readOnlyNode) {
		super();
		this.propertyName = propertyName;
		this.readOnlyNode = readOnlyNode;
	}

	@Override
	public String getPropertyName() {
		return propertyName;
	}

	@Override
	public Node getReadOnlyNode() {
		return readOnlyNode;
	}

	@Override
	public void setEditorNode(Node editorNode) {
		this.editorNode = editorNode;
	}

	@Override
	public Node getEditorNode() {
		return editorNode;
	}

	@Override
	public abstract Object getNewValue();

	@Override
	public abstract Object getOldValue();
}