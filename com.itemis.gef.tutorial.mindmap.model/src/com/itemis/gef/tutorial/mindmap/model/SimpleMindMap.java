package com.itemis.gef.tutorial.mindmap.model;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * The SimpleMindMap contains the list of children, be it nodes or connection.
 */
public class SimpleMindMap extends AbstractMindMapItem {

	/**
	 * Generated UUID
	 */
	private static final long serialVersionUID = 4667064215236604843L;

	public static final String PROP_CHILD_ELEMENTS = "childElements";

	private List<AbstractMindMapItem> childElements = Lists.newArrayList();

	public void addChildElement(AbstractMindMapItem node) {
		childElements.add(node);
		pcs.firePropertyChange(PROP_CHILD_ELEMENTS, null, node);
	}

	public void addChildElement(AbstractMindMapItem node, int idx) {
		childElements.add(idx, node);
		pcs.firePropertyChange(PROP_CHILD_ELEMENTS, null, node);
	}

	public List<AbstractMindMapItem> getChildElements() {
		return childElements;
	}

	public void removeChildElement(AbstractMindMapItem node) {
		childElements.remove(node);
		pcs.firePropertyChange(PROP_CHILD_ELEMENTS, node, null);
	}
}