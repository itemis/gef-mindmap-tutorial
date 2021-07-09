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
package com.itemis.gef.tutorial.mindmap.operations;

import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.gef.common.collections.ObservableSetMultimap;
import org.eclipse.gef.geometry.convert.fx.Geometry2FX;
import org.eclipse.gef.geometry.planar.AffineTransform;
import org.eclipse.gef.geometry.planar.Dimension;
import org.eclipse.gef.geometry.planar.Point;
import org.eclipse.gef.geometry.planar.Rectangle;
import org.eclipse.gef.graph.Edge;
import org.eclipse.gef.graph.Graph;
import org.eclipse.gef.graph.Node;
import org.eclipse.gef.layout.LayoutContext;
import org.eclipse.gef.layout.LayoutProperties;
import org.eclipse.gef.layout.algorithms.TreeLayoutAlgorithm;
import org.eclipse.gef.mvc.fx.operations.ITransactionalOperation;
import org.eclipse.gef.mvc.fx.parts.IVisualPart;

import com.google.common.collect.Maps;
import com.itemis.gef.tutorial.mindmap.parts.MindMapConnectionPart;
import com.itemis.gef.tutorial.mindmap.parts.MindMapNodePart;
import com.itemis.gef.tutorial.mindmap.parts.SimpleMindMapPart;
import com.itemis.gef.tutorial.mindmap.visuals.MindMapNodeVisual;

import javafx.geometry.Bounds;

/**
 * Operation to layout the mind map automatically using
 * {@link TreeLayoutAlgorithm}.
 */
public class LayoutNodesOperation extends AbstractOperation implements ITransactionalOperation {

	private SimpleMindMapPart mindMapPart;
	private Map<MindMapNodePart, Point> finalLocations;
	private Map<MindMapNodePart, Point> initialLocations;

	public LayoutNodesOperation(SimpleMindMapPart part) {
		super("Layout Mindmap");
		this.mindMapPart = part;
	}

	@Override
	public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		prepare();
		return redo(monitor, info);
	}

	private Node getGraphNode(Map<MindMapNodePart, Node> nodeMap, MindMapNodePart mmNode) {
		if (nodeMap.containsKey(mmNode)) {
			return nodeMap.get(mmNode);
		}

		Point currLocation = mmNode.getContent().getBounds().getLocation();

		MindMapNodeVisual visual = mmNode.getVisual();
		Bounds lb = visual.getLayoutBounds();

		// creating the node and setting the attributes for the layout algorithm
		Node node = new Node();
		node.getAttributes().put(LayoutProperties.LOCATION_PROPERTY, currLocation.clone());
		node.getAttributes().put(LayoutProperties.SIZE_PROPERTY, new Dimension(lb.getWidth(), lb.getHeight()));
		node.getAttributes().put(LayoutProperties.RESIZABLE_PROPERTY, false);

		nodeMap.put(mmNode, node);

		return node;
	}

	@Override
	public boolean isContentRelevant() {
		return true;
	}

	@Override
	public boolean isNoOp() {
		return mindMapPart.getChildrenUnmodifiable().isEmpty();
	}

	private void prepare() {
		finalLocations = Maps.newHashMap();
		initialLocations = Maps.newHashMap();

		Graph graph = new Graph();

		Map<MindMapNodePart, Node> layoutedNodes = Maps.newHashMap();

		for (IVisualPart<? extends javafx.scene.Node> item : mindMapPart.getChildrenUnmodifiable()) {
			if (item instanceof MindMapNodePart) {
				graph.getNodes().add(getGraphNode(layoutedNodes, ((MindMapNodePart) item)));
			} else {
				Node srcNode = null;
				Node trgNode = null;

				ObservableSetMultimap<IVisualPart<? extends javafx.scene.Node>, String> anchorages = item
						.getAnchoragesUnmodifiable();

				for (Entry<IVisualPart<? extends javafx.scene.Node>, String> e : anchorages.entries()) {
					if (MindMapConnectionPart.START_ROLE.equals(e.getValue())) {
						srcNode = getGraphNode(layoutedNodes, (MindMapNodePart) e.getKey());
					} else {
						trgNode = getGraphNode(layoutedNodes, (MindMapNodePart) e.getKey());
					}
				}

				Edge edge = new Edge(srcNode, trgNode);
				graph.getEdges().add(edge);
			}
		}

		LayoutProperties.setBounds(graph, new Rectangle(0, 0, 800, 500));

		LayoutContext ctx = new LayoutContext();
		ctx.setLayoutAlgorithm(new TreeLayoutAlgorithm());
		ctx.setGraph(graph);
		ctx.applyLayout(true);

		// filling the maps for storing initial and final locations
		layoutedNodes.forEach((part, node) -> {
			initialLocations.put(part, part.getContent().getBounds().getLocation());
			finalLocations.put(part, LayoutProperties.getLocation(node));
		});

		layoutedNodes.clear();
	}

	@Override
	public IStatus redo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		for (Map.Entry<MindMapNodePart, Point> e : finalLocations.entrySet()) {
			AffineTransform transform = new AffineTransform();
			transform.translate(e.getValue().x(), e.getValue().y());
			e.getKey().setContentTransform(Geometry2FX.toFXAffine(transform));
		}
		return Status.OK_STATUS;
	}

	@Override
	public IStatus undo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		for (Map.Entry<MindMapNodePart, Point> e : initialLocations.entrySet()) {
			AffineTransform transform = new AffineTransform();
			transform.translate(e.getValue().x(), e.getValue().y());
			e.getKey().setContentTransform(Geometry2FX.toFXAffine(transform));
		}
		return Status.OK_STATUS;
	}
}