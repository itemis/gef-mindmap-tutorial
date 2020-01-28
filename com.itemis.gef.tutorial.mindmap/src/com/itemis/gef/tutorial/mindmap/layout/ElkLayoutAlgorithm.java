package com.itemis.gef.tutorial.mindmap.layout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.eclipse.elk.alg.layered.LayeredLayoutProvider;
import org.eclipse.elk.alg.layered.options.LayeredMetaDataProvider;
import org.eclipse.elk.alg.layered.options.LayeredOptions;
import org.eclipse.elk.alg.layered.options.NodePlacementStrategy;
import org.eclipse.elk.core.AbstractLayoutProvider;
import org.eclipse.elk.core.data.LayoutMetaDataService;
import org.eclipse.elk.core.options.Direction;
import org.eclipse.elk.core.util.BasicProgressMonitor;
import org.eclipse.elk.graph.ElkEdge;
import org.eclipse.elk.graph.ElkEdgeSection;
import org.eclipse.elk.graph.ElkNode;
import org.eclipse.elk.graph.ElkPort;
import org.eclipse.elk.graph.util.ElkGraphUtil;
import org.eclipse.gef.geometry.planar.Dimension;
import org.eclipse.gef.geometry.planar.Point;
import org.eclipse.gef.graph.Edge;
import org.eclipse.gef.graph.Graph;
import org.eclipse.gef.graph.Node;
import org.eclipse.gef.layout.ILayoutAlgorithm;
import org.eclipse.gef.layout.LayoutContext;
import org.eclipse.gef.layout.LayoutProperties;

import com.itemis.gef.tutorial.mindmap.operations.ElkLayoutOperation;

/**
 * An {@link ILayoutAlgorithm} that uses the ELK Layered algorithm of the
 * Eclipse Layout Kernel to layout a GEF {@link Graph}.
 */
public class ElkLayoutAlgorithm implements ILayoutAlgorithm {

	private Map<Node, ElkNode> nodeMappings = new HashMap<>();
	private Map<Edge, ElkEdge> edgeMappings = new HashMap<>();

	private void applyElkLayout(ElkNode elkGraph, Graph gefGraph) {
		// process the nodes
		for (Entry<Node, ElkNode> entry : nodeMappings.entrySet()) {
			Node gefNode = entry.getKey();
			ElkNode elkNode = entry.getValue();
			LayoutProperties.setLocation(gefNode, new Point(elkNode.getX(), elkNode.getY()));
		}

		// process the edges
		for (Entry<Edge, ElkEdge> entry : edgeMappings.entrySet()) {
			Edge gefEdge = entry.getKey();
			ElkEdge elkEdge = entry.getValue();

			ElkEdgeSection section = elkEdge.getSections().get(0);
			Point edgeSource = new Point(section.getStartX(), section.getStartY());
			Point edgeTarget = new Point(section.getEndX(), section.getEndY());
			List<Point> controlPoints = section.getBendPoints().stream()
					.map(bendPoint -> new Point(bendPoint.getX(), bendPoint.getY()))//
					.collect(Collectors.toList());

			gefEdge.getAttributes().put(ElkLayoutOperation.EDGE_START_ATTR, edgeSource);
			gefEdge.getAttributes().put(ElkLayoutOperation.EDGE_BEND_POINTS_ATTR, controlPoints);
			gefEdge.getAttributes().put(ElkLayoutOperation.EDGE_END_ATTR, edgeTarget);
		}
	}

	@Override
	public void applyLayout(LayoutContext layoutContext, boolean clean) {
		Graph gefGraph = layoutContext.getGraph();

		ElkNode elkGraph = createElkGraph(gefGraph);
		computeElkLayout(elkGraph);
		applyElkLayout(elkGraph, gefGraph);
	}

	private void computeElkLayout(ElkNode elkGraph) {
		// see https://www.eclipse.org/forums/index.php/t/1097344/
		LayoutMetaDataService.getInstance().registerLayoutMetaDataProviders(new LayeredMetaDataProvider());
		AbstractLayoutProvider layoutProvider = new LayeredLayoutProvider();
		layoutProvider.layout(elkGraph, new BasicProgressMonitor());
	}

	private ElkNode createElkGraph(Graph gefGraph) {
		ElkNode elkGraph = ElkGraphUtil.createGraph();

		// setting the graph properties
		elkGraph.setProperty(LayeredOptions.DIRECTION, Direction.DOWN /* Direction.RIGHT */);
		elkGraph.setProperty(LayeredOptions.NODE_PLACEMENT_STRATEGY, NodePlacementStrategy.NETWORK_SIMPLEX);

		// process the nodes
		for (Node gefNode : gefGraph.getNodes()) {
			Dimension gefNodeSize = LayoutProperties.getSize(gefNode);

			ElkNode elkNode = ElkGraphUtil.createNode(elkGraph);
			elkNode.setWidth(gefNodeSize.width);
			elkNode.setHeight(gefNodeSize.height);

			nodeMappings.put(gefNode, elkNode);
		}

		// process the edges
		for (Edge gefEdge : gefGraph.getEdges()) {
			ElkNode sourceNode = nodeMappings.get(gefEdge.getSource());
			ElkNode targetNode = nodeMappings.get(gefEdge.getTarget());

			ElkEdge elkEdge = ElkGraphUtil.createEdge(elkGraph);

			ElkPort sourcePort = ElkGraphUtil.createPort(sourceNode);
			sourcePort.getOutgoingEdges().add(elkEdge);

			ElkPort targetPort = ElkGraphUtil.createPort(targetNode);
			targetPort.getIncomingEdges().add(elkEdge);

			edgeMappings.put(gefEdge, elkEdge);
		}

		return elkGraph;
	}
}