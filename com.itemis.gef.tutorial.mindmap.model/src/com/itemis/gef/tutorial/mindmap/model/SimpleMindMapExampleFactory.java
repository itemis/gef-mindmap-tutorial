package com.itemis.gef.tutorial.mindmap.model;

import java.util.Map;
import java.util.TreeMap;

import org.eclipse.gef.geometry.planar.Rectangle;

import javafx.scene.paint.Color;

public class SimpleMindMapExampleFactory {

	private static final double WIDTH = 150;

	/**
	 * Adding the step mindmap node to the mindMap.
	 */
	private void addSteps(SimpleMindMap mindMap, MindMapNode partNode, String[] steps) {
		for (int i = 0; i < steps.length; i++) {
			//
			String[] titleAndDescription = steps[i].split(" – ");
			Rectangle partBounds = partNode.getBounds();
			double x = partBounds.getX();
			double y = partBounds.getY();
			Rectangle stepBounds = new Rectangle(x, y + (i + 1) * 150, WIDTH, 100);
			MindMapNode stepNode = createMindMapNode(mindMap, titleAndDescription, Color.GREENYELLOW, stepBounds);
			createMindMapConnection(mindMap, partNode, stepNode);
		}
	}

	public SimpleMindMap createComplexExample() {
		SimpleMindMap mindMap = new SimpleMindMap();

		MindMapNode center = new MindMapNode();
		center.setTitle("The Core Idea");
		center.setDescription("This is my Core idea");
		center.setColor(Color.GREENYELLOW);
		center.setBounds(new Rectangle(250, 50, WIDTH, 100));

		mindMap.addChildElement(center);

		MindMapNode child = null;
		for (int i = 0; i < 5; i++) {
			child = new MindMapNode();
			child.setTitle("Association #" + i);
			child.setDescription("I just realized, this is related to the core idea!");
			child.setColor(Color.ALICEBLUE);

			child.setBounds(new Rectangle(50 + (i * 200), 250, WIDTH, 100));
			mindMap.addChildElement(child);

			MindMapConnection conn = new MindMapConnection();
			conn.connect(center, child);
			mindMap.addChildElement(conn);
		}

		MindMapNode child2 = new MindMapNode();
		child2.setTitle("Association #4-2");
		child2.setDescription("I just realized, this is related to the last idea!");
		child2.setColor(Color.LIGHTGRAY);
		child2.setBounds(new Rectangle(250, 550, WIDTH, 100));
		mindMap.addChildElement(child2);

		MindMapConnection conn = new MindMapConnection();
		conn.connect(child, child2);
		mindMap.addChildElement(conn);

		return mindMap;
	}

	/**
	 * Adding a mindmap connection to the mindMap.
	 */
	private void createMindMapConnection(SimpleMindMap mindMap, MindMapNode source, MindMapNode target) {
		MindMapConnection connection = new MindMapConnection();
		connection.connect(source, target);
		mindMap.addChildElement(connection);
	}

	/**
	 * Adding a mindmap node to the mindMap.
	 */
	private MindMapNode createMindMapNode(SimpleMindMap mindMap, String[] titleAndDescription, Color color,
			Rectangle bounds) {
		MindMapNode partNode = new MindMapNode();

		partNode.setTitle(titleAndDescription[0]);
		partNode.setDescription(titleAndDescription[1]);
		partNode.setColor(color);
		partNode.setBounds(bounds);

		mindMap.addChildElement(partNode);

		return partNode;
	}

	public SimpleMindMap createSingleNodeExample() {
		SimpleMindMap mindMap = new SimpleMindMap();

		MindMapNode center = new MindMapNode();
		center.setTitle("The Core Idea");
		center.setDescription("This is my Core idea. I need a larger Explanation to it, so I can test the warpping.");
		center.setColor(Color.GREENYELLOW);
		center.setBounds(new Rectangle(20, 50, WIDTH, 100));

		mindMap.addChildElement(center);

		return mindMap;
	}

	public SimpleMindMap createTutorialOverview() {
		Map<String, String[]> partsAndSteps = new TreeMap<>();
		partsAndSteps.put("Part I: The foundations", new String[] { "Step 1 – Preparing the development environment",
				"Step 2 – Creating the model", "Step 3 – Defining the visual" });

		partsAndSteps.put("Part II: GEF MVC", new String[] { "Step 4 – Creating the GEF parts",
				"Step 5 – Models, policies and behaviors", "Step 6 – Moving and resizing a node" });

		partsAndSteps.put("Part III: Adding Nodes and Connections", new String[] { "Step 7 – Undo/Redo operations",
				"Step 8 – Create new Nodes", "Step 9 – Create Connections" });

		partsAndSteps.put("Part IV: Modifying and Removing Nodes", new String[] { "Step 10 – Deleting Nodes (1)",
				"Step 11 – Modifying Nodes", "Step 12 – Creating Feedback", "Step 13 – Deleting Nodes (2)" });

		partsAndSteps.put("Part V: Creating an Eclipse Editor", new String[] { "Step 14 – Create an Eclipse Editor" });

		partsAndSteps.put("Part VI: Automatic Layout", new String[] { "Step 15 – Automatic Layouting via GEF Layout" });

		partsAndSteps.put("Part VII: Inline Editing", new String[] { "Step 16 – Adding Inline Editing Support" });

		SimpleMindMap mindMap = new SimpleMindMap();

		// adding the main mindmap node
		MindMapNode mainNode = new MindMapNode();
		mainNode.setTitle("Getting started with Eclipse GEF");
		mainNode.setDescription("The Mindmap Tutorial");
		mainNode.setColor(Color.ALICEBLUE);
		mainNode.setBounds(new Rectangle(580, 50, WIDTH, 100));
		mindMap.addChildElement(mainNode);

		// adding the children mindmap nodes
		int i = 0;
		for (String part : partsAndSteps.keySet()) {
			// adding the part mindmap node to the mindMap
			String[] titleAndDescription = part.split(": ");
			Rectangle bounds = new Rectangle(10 + i * 200, 200, WIDTH, 100);
			MindMapNode partNode = createMindMapNode(mindMap, titleAndDescription, Color.YELLOW, bounds);
			createMindMapConnection(mindMap, mainNode, partNode);

			// adding the step mindmap nodes to the mindMap
			String[] steps = partsAndSteps.get(part);
			addSteps(mindMap, partNode, steps);

			i++;
		}

		return mindMap;
	}
}