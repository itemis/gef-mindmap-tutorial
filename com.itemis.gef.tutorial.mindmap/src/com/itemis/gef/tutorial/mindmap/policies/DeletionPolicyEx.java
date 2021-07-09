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
package com.itemis.gef.tutorial.mindmap.policies;

import java.util.ArrayList;

import org.eclipse.gef.mvc.fx.parts.IContentPart;
import org.eclipse.gef.mvc.fx.parts.IVisualPart;
import org.eclipse.gef.mvc.fx.policies.DeletionPolicy;

import com.itemis.gef.tutorial.mindmap.parts.MindMapConnectionPart;
import com.itemis.gef.tutorial.mindmap.parts.MindMapNodePart;

import javafx.scene.Node;

/**
 * Deletes connections before mind map nodes.
 */
public class DeletionPolicyEx extends DeletionPolicy {

	@Override
	public void delete(IContentPart<? extends Node> contentPartToDelete) {
		if (contentPartToDelete instanceof MindMapNodePart) {
			MindMapNodePart node = (MindMapNodePart) contentPartToDelete;
			// delete connections from/to the part first
			for (IVisualPart<? extends Node> a : new ArrayList<>(node.getAnchoredsUnmodifiable())) {
				if (a instanceof MindMapConnectionPart) {
					super.delete((MindMapConnectionPart) a);
				}
			}
			super.delete(contentPartToDelete);
		}
	}
}
