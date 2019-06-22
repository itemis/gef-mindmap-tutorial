package com.itemis.gef.tutorial.mindmap.models;

import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

/** 
 * Implementation for inline text fields.
 */
public class InlineEditableTextField extends AbstractInlineEditableField {
	
	private boolean multiLine;

	public InlineEditableTextField(String propertyName, Text readOnlyNode, boolean multiLine) {
		super(propertyName, readOnlyNode);
		this.multiLine = multiLine;
	}

	@Override
	public void setEditorNode(Node editorNode) {
		if (editorNode == null || editorNode instanceof TextInputControl) {
			super.setEditorNode(editorNode);
		} else {
			throw new IllegalArgumentException("Only TextInputControls are allowed");
		}
	}

	@Override
	public TextInputControl getEditorNode() {
		return (TextInputControl) super.getEditorNode();
	}

	@Override
	public Text getReadOnlyNode() {
		return (Text) super.getReadOnlyNode();
	}

	@Override
	public Object getNewValue() {
		if (getEditorNode() != null) {
			return getEditorNode().getText();
		}
		return null;
	}

	@Override
	public Object getOldValue() {
		return getReadOnlyNode().getText();
	}

	@Override
	public boolean isSubmitEvent(Event e) {
		if (e instanceof KeyEvent) {
			if (((KeyEvent) e).getCode() == KeyCode.ENTER) {
				if (multiLine) {
					return ((KeyEvent) e).isAltDown();
				}
				return true;
			}
		}
		return false;
	}
}