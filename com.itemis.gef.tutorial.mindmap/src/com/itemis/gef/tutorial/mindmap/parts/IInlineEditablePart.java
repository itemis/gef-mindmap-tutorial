package com.itemis.gef.tutorial.mindmap.parts;

import java.util.List;

import com.itemis.gef.tutorial.mindmap.models.IInlineEditableField;

/**
 * Parts, who are supposed to provide an inline editing functionality must
 * implement this interface.
 */
public interface IInlineEditablePart {
	/**
	 * Cancels the editing and switches back to the read only node.
	 *
	 * @param field
	 */
	void endEditing(IInlineEditableField field);

	/**
	 * Returns a list of fields in the part, which are inline editable.
	 * 
	 * @return a list of InlineEditableField
	 */
	List<IInlineEditableField> getEditableFields();

	/**
	 * Starts the editing of the entry, by exchanging the read only node with a
	 * suitable editor. The editor will be added to the field
	 *
	 * @param field the original visual in the part
	 */
	void startEditing(IInlineEditableField field);

	/**
	 * Submits the new values from the editor and switches back to the read only
	 * editor
	 *
	 * @param entry the original visual in the part
	 * @param value the value to store in the field
	 */
	void submitEditingValue(IInlineEditableField field, Object value);
}