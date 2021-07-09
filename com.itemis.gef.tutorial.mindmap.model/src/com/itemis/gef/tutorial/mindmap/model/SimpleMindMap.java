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