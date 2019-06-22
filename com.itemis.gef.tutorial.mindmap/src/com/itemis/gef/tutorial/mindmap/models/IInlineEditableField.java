package com.itemis.gef.tutorial.mindmap.models;

import javafx.event.Event;
import javafx.scene.Node;

/**
 * Interface to describe a field in a part, which is inline editable 
 */
public interface IInlineEditableField {
	
	/**
	 *  @return the name of the property to edit
	 */
	String getPropertyName();

	/**
	 * @return the JavaFX visual showing the value in the parts visual
	 */
	Node getReadOnlyNode();

	/**
	 * Set's the JavaFX editor
	 * @param editorNode a JavaFX control
	 */
	void setEditorNode(Node editorNode);

	/**
	 * @return the set editor or <code>null</code>
	 */
	Node getEditorNode();

	/**
	 * @return the value entered in the editor
	 */
	Object getNewValue();

	/**
	 * @return
	 */
	Object getOldValue();

	/**
	 * Checks if the given JavaFX event should trigger a submit action
	 * 
	 * @param e a JavaFX event (e.g. KeyEvent) 
	 * 
	 * @return <code>true</code> if the event triggers a submit, <code>false</code> else
	 */
	boolean isSubmitEvent(Event e);
}