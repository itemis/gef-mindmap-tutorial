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
import org.eclipse.gef.layout.ILayoutAlgorithm;
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
 * Operation to layout the mind map automatically using GEF
 * {@link TreeLayoutAlgorithm}.
 */
public class GefLayoutOperation extends AbstractOperation implements ITransactionalOperation {

	private SimpleMindMapPart mindMapPart;
	private Map<MindMapNodePart, Point> finalLocations;
	private Map<MindMapNodePart, Point> initialLocations;

	public GefLayoutOperation(SimpleMindMapPart part) {
		this("Layout Mindmap (Tree)", part);
	}

	protected GefLayoutOperation(String label, SimpleMindMapPart part) {
		super(label);
		this.mindMapPart = part;
	}

	protected Edge createLayoutEdge(MindMapConnectionPart connection, Node srcNode, Node trgNode) {
		return new Edge(srcNode, trgNode);
	}

	/**
	 * Creating the node and setting the attributes for the layout algorithm.
	 */
	protected Node createLayoutNode(MindMapNodePart mmNode) {
		Node node = new Node();
		Point currLocation = mmNode.getContent().getBounds().getLocation();
		node.getAttributes().put(LayoutProperties.LOCATION_PROPERTY, currLocation.clone());

		MindMapNodeVisual visual = mmNode.getVisual();
		Bounds lb = visual.getLayoutBounds();
		node.getAttributes().put(LayoutProperties.SIZE_PROPERTY, new Dimension(lb.getWidth(), lb.getHeight()));
		node.getAttributes().put(LayoutProperties.RESIZABLE_PROPERTY, false);
		return node;
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

		Node node = createLayoutNode(mmNode);
		nodeMap.put(mmNode, node);

		return node;
	}

	protected ILayoutAlgorithm getLayoutAlgorithm() {
		return new TreeLayoutAlgorithm();
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

		Map<MindMapNodePart, Node> layoutNodes = Maps.newHashMap();

		for (IVisualPart<? extends javafx.scene.Node> item : mindMapPart.getChildrenUnmodifiable()) {
			if (item instanceof MindMapNodePart) {
				graph.getNodes().add(getGraphNode(layoutNodes, ((MindMapNodePart) item)));
			} else {
				Node srcNode = null;
				Node trgNode = null;

				ObservableSetMultimap<IVisualPart<? extends javafx.scene.Node>, String> anchorages = item
						.getAnchoragesUnmodifiable();

				for (Entry<IVisualPart<? extends javafx.scene.Node>, String> e : anchorages.entries()) {
					if (MindMapConnectionPart.START_ROLE.equals(e.getValue())) {
						srcNode = getGraphNode(layoutNodes, (MindMapNodePart) e.getKey());
					} else {
						trgNode = getGraphNode(layoutNodes, (MindMapNodePart) e.getKey());
					}
				}

				Edge edge = createLayoutEdge((MindMapConnectionPart) item, srcNode, trgNode);
				graph.getEdges().add(edge);
			}
		}

		LayoutProperties.setBounds(graph, new Rectangle(0, 0, 800, 500));

		LayoutContext ctx = new LayoutContext();
		ctx.setLayoutAlgorithm(getLayoutAlgorithm());
		ctx.setGraph(graph);
		ctx.applyLayout(true);

		// filling the maps for storing initial and final locations
		saveInitialAndFinalData(layoutNodes);
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

	protected void saveInitialAndFinalData(Map<MindMapNodePart, Node> layoutNodes) {
		layoutNodes.forEach((part, node) -> {
			initialLocations.put(part, part.getContent().getBounds().getLocation());
			finalLocations.put(part, LayoutProperties.getLocation(node));
		});
		layoutNodes.clear();
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