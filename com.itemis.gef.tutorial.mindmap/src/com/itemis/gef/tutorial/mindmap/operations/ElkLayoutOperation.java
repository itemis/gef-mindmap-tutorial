package com.itemis.gef.tutorial.mindmap.operations;

import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.gef.geometry.planar.Point;
import org.eclipse.gef.graph.Edge;
import org.eclipse.gef.graph.Node;
import org.eclipse.gef.layout.ILayoutAlgorithm;

import com.google.common.collect.Maps;
import com.itemis.gef.tutorial.mindmap.layout.ElkLayoutAlgorithm;
import com.itemis.gef.tutorial.mindmap.parts.MindMapConnectionPart;
import com.itemis.gef.tutorial.mindmap.parts.MindMapNodePart;
import com.itemis.gef.tutorial.mindmap.parts.SimpleMindMapPart;

/**
 * Operation to layout the mind map automatically using
 * {@link ElkLayoutAlgorithm}.
 */
public class ElkLayoutOperation extends GefLayoutOperation {

	public static final String EDGE_START_ATTR = "START_POINT";
	public static final String EDGE_BEND_POINTS_ATTR = "BEND_POINTS";
	public static final String EDGE_END_ATTR = "END_POINT";

	Map<MindMapConnectionPart, Edge> layoutConnections = Maps.newHashMap();

//	private Map<MindMapConnectionPart, Point> initialStartPoints; // TODO
//	private Map<MindMapConnectionPart, List<Point>> initialBendPoints;
//	private Map<MindMapConnectionPart, Point> initialEndPoints;
	private Map<MindMapConnectionPart, Point> finalStartPoints;
	private Map<MindMapConnectionPart, List<Point>> finalBendPoints;
	private Map<MindMapConnectionPart, Point> finalEndPoints;

	public ElkLayoutOperation(SimpleMindMapPart part) {
		super("ELK Layout", part);
	}

	@Override
	protected Edge createLayoutEdge(MindMapConnectionPart connection, Node srcNode, Node trgNode) {
		Edge edge = super.createLayoutEdge(connection, srcNode, trgNode);
		layoutConnections.put(connection, edge);
		return edge;
	}

	@Override
	public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		finalBendPoints = Maps.newHashMap();
		finalStartPoints = Maps.newHashMap();
		finalEndPoints = Maps.newHashMap();
		return super.execute(monitor, info);
	}

	@Override
	protected ILayoutAlgorithm getLayoutAlgorithm() {
		return new ElkLayoutAlgorithm();
	}

	@Override
	public IStatus redo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		super.redo(monitor, info);

//		for (Map.Entry<MindMapConnectionPart, List<Point>> e : finalBendPoints.entrySet()) {
//			List<Point> controlPoints = e.getValue();
//			List<Point> points = Lists.newArrayList();
//			points.add(finalStartPoints.get(e.getKey()));
//			points.addAll(controlPoints);
//			points.add(finalEndPoints.get(e.getKey()));
//			e.getKey().getVisual().setControlPoints(points);
//			e.getKey().refreshVisual();
//		}

//		for (Map.Entry<MindMapConnectionPart, Point> e : finalStartPoints.entrySet()) {
//			Point startPoint = e.getValue();
//			e.getKey().getVisual().setStartPoint(startPoint);
//			e.getKey().refreshVisual();
//		}
//
//		for (Map.Entry<MindMapConnectionPart, Point> e : finalEndPoints.entrySet()) {
//			Point endPoint = e.getValue();
//			e.getKey().getVisual().setEndPoint(endPoint);
//			e.getKey().refreshVisual();
//		}

		return Status.OK_STATUS;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void saveInitialAndFinalData(Map<MindMapNodePart, Node> layoutNodes) {
		layoutConnections.forEach((part, edge) -> {
			// TODO initial
			finalStartPoints.put(part, (Point) edge.getAttributes().get(EDGE_START_ATTR));
			finalBendPoints.put(part, (List<Point>) edge.getAttributes().get(EDGE_BEND_POINTS_ATTR));
			finalEndPoints.put(part, (Point) edge.getAttributes().get(EDGE_END_ATTR));
		});
		layoutConnections.clear();
		super.saveInitialAndFinalData(layoutNodes);
	}

	@Override
	public IStatus undo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		super.undo(monitor, info);
		// TODO
		return Status.OK_STATUS;
	}
}