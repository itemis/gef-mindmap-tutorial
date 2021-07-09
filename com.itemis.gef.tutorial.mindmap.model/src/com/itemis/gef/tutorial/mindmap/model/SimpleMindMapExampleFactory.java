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
package com.itemis.gef.tutorial.mindmap.model;

import org.eclipse.gef.geometry.planar.Rectangle;

import javafx.scene.paint.Color;

public class SimpleMindMapExampleFactory {

	private static final double WIDTH = 150;

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
}