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

import java.util.Collections;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.gef.mvc.fx.handlers.AbstractHandler;
import org.eclipse.gef.mvc.fx.handlers.IOnClickHandler;
import org.eclipse.gef.mvc.fx.operations.ChangeSelectionOperation;
import org.eclipse.gef.mvc.fx.parts.IContentPart;
import org.eclipse.gef.mvc.fx.parts.IVisualPart;
import org.eclipse.gef.mvc.fx.policies.CreationPolicy;
import org.eclipse.gef.mvc.fx.viewer.IViewer;

import com.google.common.collect.HashMultimap;
import com.itemis.gef.tutorial.mindmap.model.MindMapConnection;
import com.itemis.gef.tutorial.mindmap.models.ItemCreationModel;
import com.itemis.gef.tutorial.mindmap.models.ItemCreationModel.Type;
import com.itemis.gef.tutorial.mindmap.parts.MindMapNodePart;
import com.itemis.gef.tutorial.mindmap.parts.SimpleMindMapPart;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

/**
 * The policy to create a new node using the {@link ItemCreationModel}
 *
 * @author hniederhausen
 *
 */
public class CreateNewConnectionOnClickHandler extends AbstractHandler implements IOnClickHandler {

	@Override
	public void click(MouseEvent e) {
		if (!e.isPrimaryButtonDown()) {
			return;
		}

		IViewer viewer = getHost().getRoot().getViewer();
		ItemCreationModel creationModel = viewer.getAdapter(ItemCreationModel.class);
		if (creationModel.getType() != Type.Connection) {
			return; // don't want to create a connection
		}

		if (creationModel.getSource() == null) {
			// the host is the source
			creationModel.setSource((MindMapNodePart) getHost());
			return; // wait for the next click
		}

		// okay, we have a pair
		MindMapNodePart source = creationModel.getSource();
		MindMapNodePart target = (MindMapNodePart) getHost();

		// check if valid
		if (source == target) {
			return;
		}

		IVisualPart<? extends Node> part = getHost().getRoot().getChildrenUnmodifiable().get(0);
		if (part instanceof SimpleMindMapPart) {
			MindMapConnection newConn = new MindMapConnection();
			newConn.connect(source.getContent(), target.getContent());

			// use CreatePolicy to add a new connection to the model
			CreationPolicy creationPolicy = getHost().getRoot().getAdapter(CreationPolicy.class);
			init(creationPolicy);
			creationPolicy.create(newConn, part, HashMultimap.<IContentPart<? extends Node>, String>create());
			commit(creationPolicy);

			// select target node
			// FIXME
			try {
				viewer.getDomain().execute(new ChangeSelectionOperation(viewer, Collections.singletonList(target)),
						null);
			} catch (ExecutionException e1) {
			}
		}

		// reset creation state
		creationModel.setSource(null);
		creationModel.setType(Type.None);
	}
}