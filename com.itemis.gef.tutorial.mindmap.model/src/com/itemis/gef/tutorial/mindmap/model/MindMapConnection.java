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

public class MindMapConnection extends AbstractMindMapItem {

	/**
	 * Generated UUID
	 */
	private static final long serialVersionUID = 6065237357753406466L;

	private MindMapNode source;
	private MindMapNode target;
	private boolean connected;

	public void connect(MindMapNode source, MindMapNode target) {
		if (source == null || target == null || source == target) {
			throw new IllegalArgumentException();
		}
		disconnect();
		this.source = source;
		this.target = target;
		reconnect();
	}

	public void disconnect() {
		if (connected) {
			source.removeOutgoingConnection(this);
			target.removeIncomingConnection(this);
			connected = false;
		}
	}

	public MindMapNode getSource() {
		return source;
	}

	public MindMapNode getTarget() {
		return target;
	}

	public void reconnect() {
		if (!connected) {
			source.addOutgoingConnection(this);
			target.addIncomingConnection(this);
			connected = true;
		}
	}

	public void setSource(MindMapNode source) {
		this.source = source;
	}

	public void setTarget(MindMapNode target) {
		this.target = target;
	}
}