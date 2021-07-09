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

import org.eclipse.gef.geometry.planar.Rectangle;
import org.eclipse.gef.mvc.fx.handlers.AbstractHandler;
import org.eclipse.gef.mvc.fx.handlers.IOnClickHandler;
import org.eclipse.gef.mvc.fx.parts.IContentPart;
import org.eclipse.gef.mvc.fx.parts.IVisualPart;
import org.eclipse.gef.mvc.fx.policies.CreationPolicy;
import org.eclipse.gef.mvc.fx.viewer.IViewer;

import com.google.common.collect.HashMultimap;
import com.itemis.gef.tutorial.mindmap.model.MindMapNode;
import com.itemis.gef.tutorial.mindmap.models.ItemCreationModel;
import com.itemis.gef.tutorial.mindmap.models.ItemCreationModel.Type;
import com.itemis.gef.tutorial.mindmap.parts.SimpleMindMapPart;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 * Policy, which listens to primary clicks and creates a new node if the
 * {@link ItemCreationModel} is in the right state.
 *
 */
public class CreateNewNodeOnClickHandler extends AbstractHandler implements IOnClickHandler {

	@Override
	public void click(MouseEvent e) {
		if (!e.isPrimaryButtonDown()) {
			return; // wrong mouse button
		}

		IViewer viewer = getHost().getRoot().getViewer();
		ItemCreationModel creationModel = viewer.getAdapter(ItemCreationModel.class);
		if (creationModel == null) {
			throw new IllegalStateException("No ItemCreationModel bound to viewer!");
		}

		if (creationModel.getType() != Type.Node) {
			// don't want to create a node
			return;
		}

		IVisualPart<? extends Node> part = viewer.getRootPart().getChildrenUnmodifiable().get(0);
		if (part instanceof SimpleMindMapPart) {
			// calculate the mouse coordinates
			// determine coordinates of new nodes origin in model coordinates
			Point2D mouseInLocal = part.getVisual().sceneToLocal(e.getSceneX(), e.getSceneY());

			MindMapNode newNode = new MindMapNode();
			newNode.setTitle("New node");
			newNode.setDescription("no description");
			newNode.setColor(Color.GREENYELLOW);
			newNode.setBounds(new Rectangle(mouseInLocal.getX(), mouseInLocal.getY(), 120, 80));

			// GEF provides the CreatePolicy to add a new element to the model
			CreationPolicy creationPolicy = getHost().getRoot().getAdapter(CreationPolicy.class);
			init(creationPolicy);
			// create a IContentPart for our new model. The newly created
			// IContentPart is returned, but we do not need it.
			creationPolicy.create(newNode, part, HashMultimap.<IContentPart<? extends Node>, String>create());
			// commit the creation
			commit(creationPolicy);
		}

		// reset creation state
		creationModel.setType(Type.None);
	}
}