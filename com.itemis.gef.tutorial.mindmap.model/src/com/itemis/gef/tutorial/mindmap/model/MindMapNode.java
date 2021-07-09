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

import java.io.Serializable;
import java.util.List;

import org.eclipse.gef.geometry.planar.Rectangle;

import com.google.common.collect.Lists;

import javafx.scene.paint.Color;

public class MindMapNode extends AbstractMindMapItem implements Serializable {

	/**
	 * Generated UUID
	 */
	private static final long serialVersionUID = 8875579454539897410L;

	public static final String PROP_TITLE = "title";
	public static final String PROP_DESCRIPTION = "description";
	public static final String PROP_COLOR = "color";
	public static final String PROP_BOUNDS = "bounds";
	public static final String PROP_INCOMING_CONNECTIONS = "incomingConnections";
	public static final String PROP_OUTGOGING_CONNECTIONS = "outgoingConnections";

	/**
	 * The title of the node
	 */
	private String title;

	/**
	 * he description of the node, which is optional
	 */
	private String description;

	/**
	 * The background color of the node
	 */
	transient private Color color;

	/**
	 * The size and position of the visual representation
	 */
	private Rectangle bounds;

	private List<MindMapConnection> incomingConnections = Lists.newArrayList();
	private List<MindMapConnection> outgoingConnections = Lists.newArrayList();

	public void addIncomingConnection(MindMapConnection conn) {
		incomingConnections.add(conn);
		pcs.firePropertyChange(PROP_INCOMING_CONNECTIONS, null, conn);
	}

	public void addOutgoingConnection(MindMapConnection conn) {
		outgoingConnections.add(conn);
		pcs.firePropertyChange(PROP_OUTGOGING_CONNECTIONS, null, conn);
	}

	public Rectangle getBounds() {
		return bounds;
	}

	public Color getColor() {
		return color;
	}

	public String getDescription() {
		return description;
	}

	public List<MindMapConnection> getIncomingConnections() {
		return incomingConnections;
	}

	public List<MindMapConnection> getOutgoingConnections() {
		return outgoingConnections;
	}

	public String getTitle() {
		return title;
	}

	public void removeIncomingConnection(MindMapConnection conn) {
		incomingConnections.remove(conn);
		pcs.firePropertyChange(PROP_INCOMING_CONNECTIONS, conn, null);
	}

	public void removeOutgoingConnection(MindMapConnection conn) {
		outgoingConnections.remove(conn);
		pcs.firePropertyChange(PROP_OUTGOGING_CONNECTIONS, conn, null);
	}

	public void setBounds(Rectangle bounds) {
		pcs.firePropertyChange(PROP_BOUNDS, this.bounds, (this.bounds = bounds.getCopy()));
	}

	public void setColor(Color color) {
		pcs.firePropertyChange(PROP_COLOR, this.color, (this.color = color));
	}

	public void setDescription(String description) {
		pcs.firePropertyChange(PROP_DESCRIPTION, this.description, (this.description = description));
	}

	public void setTitle(String title) {
		pcs.firePropertyChange(PROP_TITLE, this.title, (this.title = title));
	}
}