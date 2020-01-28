package com.itemis.gef.tutorial.mindmap.layout;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.gef.common.attributes.IAttributeCopier;
import org.eclipse.gef.common.attributes.IAttributeStore;
import org.eclipse.gef.dot.internal.DotAttributes;
import org.eclipse.gef.dot.internal.DotExecutableUtils;
import org.eclipse.gef.dot.internal.DotExport;
import org.eclipse.gef.dot.internal.DotFileUtils;
import org.eclipse.gef.dot.internal.DotImport;
import org.eclipse.gef.dot.internal.language.dot.GraphType;
import org.eclipse.gef.dot.internal.language.layout.Layout;
import org.eclipse.gef.geometry.planar.Dimension;
import org.eclipse.gef.geometry.planar.Point;
import org.eclipse.gef.graph.Graph;
import org.eclipse.gef.graph.GraphCopier;
import org.eclipse.gef.graph.Node;
import org.eclipse.gef.layout.ILayoutAlgorithm;
import org.eclipse.gef.layout.LayoutContext;
import org.eclipse.gef.layout.LayoutProperties;

import com.itemis.gef.tutorial.mindmap.operations.DotLayoutOperation;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

/**
 * An {@link ILayoutAlgorithm} that uses the DOT native executable, the
 * {@link DotImport}, {@link DotExport}, {@link DotExecutableUtils} and
 * {@link DotAttributes} to layout a GEF {@link Graph}.
 *
 * It requires that dotExecutablePath points to a valid native dot executable.
 */
public class DotLayoutAlgorithm implements ILayoutAlgorithm {

//	private String dotExecutablePath = null; // TODO: replace with your dot executable path
	private String dotExecutablePath = "/usr/local/bin/dot";
//	private String dotExecutablePath = "C:\\Program Files (x86)\\Graphviz2.38\\bin\\dot.exe";

	private Map<String, Node> nameToLayoutNodeMap = new HashMap<>();

	/**
	 * Transfer the DOT provided position information back to the input Graph.
	 */
	private void applyDotLayout(Graph dotGraph, Graph layoutGraph) {
		// transfer locations back to the layout graph
		for (Node source : dotGraph.getNodes()) {
			String name = DotAttributes._getName(source);
			Node target = nameToLayoutNodeMap.get(name);
			org.eclipse.gef.dot.internal.language.point.Point posParsed = DotAttributes.getPosParsed(source);
			Double width = 72 * DotAttributes.getWidthParsed(source);
			Double height = 72 * DotAttributes.getHeightParsed(source);
			LayoutProperties.setLocation(target,
					new Point(posParsed.getX() - width / 2, posParsed.getY() - height / 2));
		}
	}

	@Override
	public void applyLayout(LayoutContext layoutContext, boolean clean) {
		if (dotExecutablePath == null) {
			new Alert(AlertType.ERROR,
					"Sorry! Cannot apply layout. Please set graphviz dot executable path in DotNativeLayoutAlgorithm.",
					ButtonType.OK).showAndWait();
			return;
		}

		Graph layoutGraph = layoutContext.getGraph();

		Graph dotGraph = createDotGraph(layoutGraph);
		Graph layoutedDotGraph = computeDotLayout(dotGraph);
		applyDotLayout(layoutedDotGraph, layoutGraph);

		nameToLayoutNodeMap.clear();
	}

	/**
	 * Export the Graph with DotAttributs to a DOT string and call the dot
	 * executable to add layout info to it.
	 */
	private Graph computeDotLayout(Graph dotGraph) {
		String exportDot = new DotExport().exportDot(dotGraph);
		// You can print out the exported Dot in order to
		// System.out.println(exportDot);
		File tmpFile = DotFileUtils.write(exportDot);
		String[] dotResult = DotExecutableUtils.executeDot(new File(dotExecutablePath), true, tmpFile, null, null);
		if (!dotResult[1].isEmpty()) {
			System.err.println(dotResult[1]);
		}
		tmpFile.delete();
		Graph layoutedDotGraph = new DotImport().importDot(dotResult[0]).get(0);
		return layoutedDotGraph;
	}

	/**
	 * Convert a Graph with LayoutAttributes (input model to ILayoutAlgorithm) to a
	 * Graph with DotAttributes, which can be exported to a DOT string; transfer
	 * node names to be able to retrieve the results.
	 */
	private Graph createDotGraph(Graph layoutGraph) {
		Graph dotGraph = new GraphCopier(new IAttributeCopier() {
			@Override
			public void copy(IAttributeStore source, IAttributeStore target) {
				if (source instanceof Node && target instanceof Node) {
					// transfer name for identification purpose
					String name = (String) ((Node) source).getAttributes().get(DotLayoutOperation.NODE_ID_ATTR);
					DotAttributes._setName((Node) target, name);
					nameToLayoutNodeMap.put(name, (Node) source);

					// dot default shape is "ellipse", "box" is rectangular
					DotAttributes.setShape((Node) target, "box");

					// dot width and height are expected with dpi of 72
					Dimension size = LayoutProperties.getSize((Node) source);
					DotAttributes.setWidthParsed((Node) target, size.getWidth() / 72);
					DotAttributes.setHeightParsed((Node) target, size.getHeight() / 72);
				}
			}
		}).copy(layoutGraph);

		setGraphAttributes(dotGraph);

		return dotGraph;
	}

	/**
	 * Set graph type and DOT layout algorithm
	 */
	private void setGraphAttributes(Graph dotGraph) {
		DotAttributes._setType(dotGraph, GraphType.DIGRAPH);
		DotAttributes.setLayoutParsed(dotGraph, Layout.CIRCO);

		// attributes not supported by GEF Dot API can still be set as follows:
		dotGraph.getAttributes().put("overlap", "false");
	}
}