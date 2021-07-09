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

import org.eclipse.gef.common.adapt.IAdaptable;
import org.eclipse.gef.fx.anchors.DynamicAnchor;
import org.eclipse.gef.fx.anchors.DynamicAnchor.AnchorageReferenceGeometry;
import org.eclipse.gef.fx.anchors.IAnchor;
import org.eclipse.gef.geometry.planar.IGeometry;
import org.eclipse.gef.mvc.fx.parts.IVisualPart;

import com.google.common.reflect.TypeToken;
import com.google.inject.Provider;
import com.itemis.gef.tutorial.mindmap.visuals.MindMapConnectionVisual;

import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.scene.Node;

/**
 * The {@link SimpleMindMapAnchorProvider} create an anchor for a
 * {@link MindMapConnectionVisual}.
 *
 * It is bound to an {@link MindMapNodePart} and will be called in the
 * {@link MindMapConnectionPart}.
 *
 */
public class SimpleMindMapAnchorProvider extends IAdaptable.Bound.Impl<IVisualPart<? extends Node>>
		implements Provider<IAnchor> {

	// the anchor in case we already created one
	private DynamicAnchor anchor;

	@Override
	public ReadOnlyObjectProperty<IVisualPart<? extends Node>> adaptableProperty() {
		return null;
	}

	@Override
	public IAnchor get() {
		if (anchor == null) {
			// get the visual from the host (MindMapNodePart)
			Node anchorage = getAdaptable().getVisual();
			// create a new anchor instance
			anchor = new DynamicAnchor(anchorage);

			// binding the anchor reference to an object binding, which
			// recalculates the geometry when the layout bounds of
			// the anchorage are changing
			anchor.getComputationParameter(AnchorageReferenceGeometry.class).bind(new ObjectBinding<IGeometry>() {
				{
					bind(anchorage.layoutBoundsProperty());
				}

				@Override
				protected IGeometry computeValue() {
					@SuppressWarnings("serial")
					// get the registered geometry provider from the host
					Provider<IGeometry> geomProvider = getAdaptable().getAdapter(new TypeToken<Provider<IGeometry>>() {
					});
					return geomProvider.get();
				}
			});
		}
		return anchor;
	}
}