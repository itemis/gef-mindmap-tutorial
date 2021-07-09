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
package com.itemis.gef.tutorial.mindmap.parts.feedback;

import org.eclipse.gef.common.adapt.AdapterKey;
import org.eclipse.gef.fx.anchors.IAnchor;
import org.eclipse.gef.fx.anchors.StaticAnchor;
import org.eclipse.gef.geometry.convert.fx.FX2Geometry;
import org.eclipse.gef.geometry.convert.fx.Geometry2FX;
import org.eclipse.gef.geometry.planar.Point;
import org.eclipse.gef.mvc.fx.parts.AbstractFeedbackPart;
import org.eclipse.gef.mvc.fx.parts.IVisualPart;

import com.google.common.reflect.TypeToken;
import com.google.inject.Provider;
import com.itemis.gef.tutorial.mindmap.parts.MindMapNodePart;
import com.itemis.gef.tutorial.mindmap.visuals.MindMapConnectionVisual;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

/**
 * Part representing a connection creation feedback, anchoring to an
 * {@link MindMapNodePart} and the mouseposition.
 *
 */
public class CreateConnectionFeedbackPart extends AbstractFeedbackPart<Node> {

	private class MousePositionAnchor extends StaticAnchor implements EventHandler<MouseEvent> {

		public MousePositionAnchor(Point referencePositionInScene) {
			super(referencePositionInScene);
		}

		public void dispose() {
			// listen to any mouse move and reposition the anchor
			getRoot().getVisual().getScene().removeEventHandler(MouseEvent.MOUSE_MOVED, this);
		}

		@Override
		public void handle(MouseEvent event) {
			Point v = new Point(event.getSceneX(), event.getSceneY());
			referencePositionProperty().setValue(v);
		}

		public void init() {
			// listen to any mouse move and reposition the anchor
			getRoot().getVisual().getScene().addEventHandler(MouseEvent.MOUSE_MOVED, this);
		}

	}

	@Override
	public void doAttachToAnchorageVisual(IVisualPart<? extends Node> anchorage, String role) {
		// find a anchor provider, which must be registered in the module
		// be aware to use the right interfaces (Provider is used a lot)
		@SuppressWarnings("serial")
		Provider<? extends IAnchor> adapter = anchorage
				.getAdapter(AdapterKey.get(new TypeToken<Provider<? extends IAnchor>>() {
				}));
		if (adapter == null) {
			throw new IllegalStateException("No adapter for <" + anchorage.getClass() + "> found.");
		}
		// set the start anchor
		IAnchor anchor = adapter.get();
		getVisual().setStartAnchor(anchor);

		MousePositionAnchor endAnchor = new MousePositionAnchor(
				FX2Geometry.toPoint(getVisual().localToScene(Geometry2FX.toFXPoint(getVisual().getStartPoint()))));
		endAnchor.init();
		getVisual().setEndAnchor(endAnchor);
	}

	@Override
	protected Node doCreateVisual() {
		return new MindMapConnectionVisual();
	}

	@Override
	protected void doDetachFromAnchorageVisual(IVisualPart<? extends Node> anchorage, String role) {
		getVisual().setStartPoint(getVisual().getStartPoint());
		((MousePositionAnchor) getVisual().getEndAnchor()).dispose();
		getVisual().setEndPoint(getVisual().getEndPoint());
	}

	@Override
	protected void doRefreshVisual(Node visual) {
	}

	@Override
	public MindMapConnectionVisual getVisual() {
		return (MindMapConnectionVisual) super.getVisual();
	}

}