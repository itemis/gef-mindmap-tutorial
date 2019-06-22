package com.itemis.gef.tutorial.mindmap.operations;

import java.util.LinkedList;
import java.util.List;
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
import org.eclipse.gef.layout.algorithms.RadialLayoutAlgorithm;
import org.eclipse.gef.mvc.fx.operations.ITransactionalOperation;
import org.eclipse.gef.mvc.fx.parts.IVisualPart;

import com.google.common.collect.Maps;
import com.itemis.gef.tutorial.mindmap.parts.MindMapConnectionPart;
import com.itemis.gef.tutorial.mindmap.parts.MindMapNodePart;
import com.itemis.gef.tutorial.mindmap.parts.SimpleMindMapPart;
import com.itemis.gef.tutorial.mindmap.visuals.MindMapNodeVisual;

import javafx.geometry.Bounds;

/**
 * Operation to layout the mind map automatically, using the
 * {@link RadialLayoutAlgorithm}.
 */
public class LayoutNodesOperation extends AbstractOperation implements ITransactionalOperation {

	private SimpleMindMapPart mindMapPart;
	private Map<MindMapNodePart, Point> deltaMap;

	public LayoutNodesOperation(SimpleMindMapPart part) {
		super("Layout Mindmap");
		this.mindMapPart = part;
	}

	@Override
	public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		prepare();
		return redo(monitor, info);
	}

	@Override
	public boolean isContentRelevant() {
		return true;
	}

	@Override
	public boolean isNoOp() {
		return mindMapPart.getChildrenUnmodifiable().isEmpty();
	}

	@Override
	public IStatus redo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		for (Map.Entry<MindMapNodePart, Point> e : deltaMap.entrySet()) {
			AffineTransform transform = new AffineTransform();
			transform.translate(e.getValue().x(), e.getValue().y());
			e.getKey().setContentTransform(Geometry2FX.toFXAffine(transform));
			e.getKey().refreshVisual();
		}

		return Status.OK_STATUS;
	}

	@Override
	public IStatus undo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		for (Map.Entry<MindMapNodePart, Point> e : deltaMap.entrySet()) {
			AffineTransform transform = new AffineTransform();
			transform.translate(-e.getValue().x(), -e.getValue().y());
			e.getKey().setContentTransform(Geometry2FX.toFXAffine(transform));
			e.getKey().refreshVisual();
		}
		return Status.OK_STATUS;
	}

	private void prepare() {
			deltaMap = Maps.newHashMap();
	//			Graph graph = new Graph();
	
			List<Node> nodes = new LinkedList<>();
			List<Edge> edges = new LinkedList<>();
	
			Map<MindMapNodePart, Node> layoutedNodes = Maps.newHashMap();
	
			for (IVisualPart<? extends javafx.scene.Node> item : mindMapPart.getChildrenUnmodifiable()) {
				if (item instanceof MindMapNodePart) {
					Node node = getGraphNode(layoutedNodes, ((MindMapNodePart) item));
	//					node.setGraph(graph);
					nodes.add(node);
				} else {
					Node srcNode = null;
					Node trgNode = null;
					ObservableSetMultimap<IVisualPart<? extends javafx.scene.Node>, String> anchorages = item
							.getAnchoragesUnmodifiable();
					for (Entry<IVisualPart<? extends javafx.scene.Node>, String> e : anchorages.entries()) {
						if (MindMapConnectionPart.START_ROLE.equals(e.getValue())) {
							srcNode = getGraphNode(layoutedNodes, (MindMapNodePart) e.getKey());
							// srcNode.setGraph(graph);
							nodes.add(srcNode);
						} else {
							trgNode = getGraphNode(layoutedNodes, (MindMapNodePart) e.getKey());
							// trgNode.setGraph(graph);
							nodes.add(trgNode);
						}
					}
					Edge edge = new Edge.Builder(srcNode, trgNode).buildEdge();
					edges.add(edge);
					// edge.setGraph(graph);
					// graph.getEdges().add(edge);
				}
			}
	
			Graph graph = new Graph(nodes, edges);
	
			LayoutProperties.setBounds(graph, new Rectangle(0, 0, 500, 500));
			LayoutContext ctx = new LayoutContext();
			graph.getNodes().addAll(layoutedNodes.values());
			ctx.setLayoutAlgorithm(new RadialLayoutAlgorithm());
			ctx.setGraph(graph);
			ctx.applyLayout(true);
			// constructing the delta map
			layoutedNodes.forEach((part, node) -> {
				Point currLocation = part.getContent().getBounds().getLocation();
				Point nodeLoc = LayoutProperties.getLocation(node);
				Point delta = currLocation.getDifference(nodeLoc);
				deltaMap.put(part, delta);
			});
	
			layoutedNodes.clear();
		}

	private Node getGraphNode(Map<MindMapNodePart, Node> nodeMap, MindMapNodePart mmNode) {
		if (nodeMap.containsKey(mmNode)) {
			return nodeMap.get(mmNode);
		}
		Point currLocation = mmNode.getContent().getBounds().getLocation();
		MindMapNodeVisual visual = mmNode.getVisual();
		Bounds lb = visual.getLayoutBounds();
		// creating the node and setting the attributes for the layout algorithm
		Node node = new Node.Builder().buildNode();
		node.getAttributes().put(LayoutProperties.LOCATION_PROPERTY, currLocation.clone());
		node.getAttributes().put(LayoutProperties.SIZE_PROPERTY, new Dimension(lb.getWidth(), lb.getHeight()));
		node.getAttributes().put(LayoutProperties.RESIZABLE_PROPERTY, false);
		nodeMap.put(mmNode, node);
		return node;
	}
}
