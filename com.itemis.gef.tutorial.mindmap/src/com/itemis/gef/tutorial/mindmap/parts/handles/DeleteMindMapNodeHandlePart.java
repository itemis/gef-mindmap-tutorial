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
package com.itemis.gef.tutorial.mindmap.parts.handles;

import java.util.Iterator;

import org.eclipse.gef.mvc.fx.parts.AbstractHandlePart;
import org.eclipse.gef.mvc.fx.parts.IVisualPart;

import javafx.animation.FadeTransition;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.StrokeLineCap;
import javafx.util.Duration;

public class DeleteMindMapNodeHandlePart extends AbstractHandlePart<Group> {

	@Override
	protected Group doCreateVisual() {
		Group group = new Group();

		// create diagonal lines
		double SIZE = 5;
		Polyline topLeftBottomRight = new Polyline(-SIZE, -SIZE, SIZE, SIZE);
		Polyline topRightBottomLeft = new Polyline(SIZE, -SIZE, -SIZE, SIZE);

		// style them: red, wide, round caps
		Color stroke = Color.RED;
		double strokeWidth = 5d;
		StrokeLineCap lineCap = StrokeLineCap.ROUND;
		for (Polyline pl : new Polyline[] { topLeftBottomRight, topRightBottomLeft }) {
			pl.setStroke(stroke);
			pl.setStrokeWidth(strokeWidth);
			pl.setStrokeLineCap(lineCap);
		}

		// combine diagonal lines to X shape
		group.getChildren().addAll(topLeftBottomRight, topRightBottomLeft);

		// fade-in
		group.setOpacity(0d);
		FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), group);
		fadeIn.setFromValue(0d);
		fadeIn.setToValue(1d);
		fadeIn.play();

		// make it easier clickable
		group.setPickOnBounds(true);

		return group;
	}

	@Override
	protected void doRefreshVisual(Group visual) {
		Iterator<IVisualPart<? extends Node>> iterator = getAnchoragesUnmodifiable().keySet().iterator();
		if (!iterator.hasNext()) {
			return;
		}
		// position X in top right corner
		double PAD = 1;
		IVisualPart<? extends Node> host = iterator.next();
		Bounds hostBounds = host.getVisual().getBoundsInParent();
		visual.relocate(hostBounds.getMaxX() + PAD,
				hostBounds.getMinY() - visual.getBoundsInParent().getHeight() - PAD);
	}

}