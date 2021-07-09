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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.gef.mvc.fx.parts.DefaultSelectionHandlePartFactory;
import org.eclipse.gef.mvc.fx.parts.IHandlePart;
import org.eclipse.gef.mvc.fx.parts.IVisualPart;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.itemis.gef.tutorial.mindmap.parts.MindMapNodePart;

import javafx.scene.Node;

public class MindMapSelectionHandlePartFactory extends DefaultSelectionHandlePartFactory {

	@Inject
	private Injector injector;

	@Override
	protected List<IHandlePart<? extends Node>> createSingleSelectionHandleParts(IVisualPart<? extends Node> target,
			Map<Object, Object> contextMap) {
		List<IHandlePart<? extends Node>> handleParts = new ArrayList<>();

		// add super handles
		handleParts.addAll(super.createSingleSelectionHandleParts(target, contextMap));

		if (target instanceof MindMapNodePart) {
			// add deletion handle part
			DeleteMindMapNodeHandlePart delHp = injector.getInstance(DeleteMindMapNodeHandlePart.class);
			handleParts.add(delHp);
		}

		return handleParts;
	}

}
