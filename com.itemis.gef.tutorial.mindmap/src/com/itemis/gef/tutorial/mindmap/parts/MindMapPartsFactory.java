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
package com.itemis.gef.tutorial.mindmap.parts;

import java.util.Map;

import org.eclipse.gef.mvc.fx.parts.IContentPart;
import org.eclipse.gef.mvc.fx.parts.IContentPartFactory;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.itemis.gef.tutorial.mindmap.model.MindMapConnection;
import com.itemis.gef.tutorial.mindmap.model.MindMapNode;
import com.itemis.gef.tutorial.mindmap.model.SimpleMindMap;

import javafx.scene.Node;

/**
 * The {@link MindMapPartsFactory} creates a Part for the mind map models, based
 * on the type of the model instance.
 *
 */
public class MindMapPartsFactory implements IContentPartFactory {

	@Inject
	private Injector injector;

	@Override
	public IContentPart<? extends Node> createContentPart(Object content, Map<Object, Object> contextMap) {
		if (content == null) {
			throw new IllegalArgumentException("Content must not be null!");
		}

		if (content instanceof SimpleMindMap) {
			return injector.getInstance(SimpleMindMapPart.class);
		} else if (content instanceof MindMapNode) {
			return injector.getInstance(MindMapNodePart.class);
		} else if (content instanceof MindMapConnection) {
			return injector.getInstance(MindMapConnectionPart.class);
		} else {
			throw new IllegalArgumentException("Unknown content type <" + content.getClass().getName() + ">");
		}
	}
}