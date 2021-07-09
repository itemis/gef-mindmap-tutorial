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
package com.itemis.gef.tutorial.mindmap.visuals;

import org.eclipse.gef.fx.nodes.GeometryNode;
import org.eclipse.gef.geometry.planar.RoundedRectangle;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class MindMapNodeVisual extends Region {

	private static final double HORIZONTAL_PADDING = 20d;
	private static final double VERTICAL_PADDING = 10d;
	private static final double VERTICAL_SPACING = 5d;

	private Text titleText;
	private TextFlow descriptionFlow;
	private Text descriptionText;
	private GeometryNode<RoundedRectangle> shape;
	private VBox labelVBox;

	public MindMapNodeVisual() {
		// create background shape
		shape = new GeometryNode<>(new RoundedRectangle(0, 0, 70, 30, 8, 8));
		shape.setFill(Color.LIGHTGREEN);
		shape.setStroke(Color.BLACK);

		// create vertical box for title and description
		labelVBox = new VBox(VERTICAL_SPACING);
		labelVBox.setPadding(new Insets(VERTICAL_PADDING, HORIZONTAL_PADDING, VERTICAL_PADDING, HORIZONTAL_PADDING));

		// ensure shape and labels are resized to fit this visual
		shape.prefWidthProperty().bind(widthProperty());
		shape.prefHeightProperty().bind(heightProperty());
		labelVBox.prefWidthProperty().bind(widthProperty());
		labelVBox.prefHeightProperty().bind(heightProperty());

		// create title text
		titleText = new Text();
		titleText.setTextOrigin(VPos.TOP);

		// create description text
		descriptionText = new Text();
		descriptionText.setTextOrigin(VPos.TOP);

		// use TextFlow to enable wrapping of the description text within the
		// label bounds
		descriptionFlow = new TextFlow(descriptionText);
		// only constrain the width, so that the height is computed in
		// dependence on the width
		descriptionFlow.maxWidthProperty().bind(shape.widthProperty().subtract(HORIZONTAL_PADDING * 2));

		// vertically lay out title and description
		labelVBox.getChildren().addAll(titleText, descriptionFlow);

		// ensure title is always visible (see also #computeMinWidth(double) and
		// #computeMinHeight(double) methods)
		setMinSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);

		// wrap shape and VBox in Groups so that their bounds-in-parent is
		// considered when determining the layout-bounds of this visual
		getChildren().addAll(new Group(shape), new Group(labelVBox));
	}

	@Override
	public double computeMinHeight(double width) {
		// ensure title is always visible
		// descriptionFlow.minHeight(width) +
		// titleText.getLayoutBounds().getHeight() + VERTICAL_PADDING * 2;
		return labelVBox.minHeight(width);
	}

	@Override
	public double computeMinWidth(double height) {
		// ensure title is always visible
		return titleText.getLayoutBounds().getWidth() + HORIZONTAL_PADDING * 2;
	}

	@Override
	protected double computePrefHeight(double width) {
		return minHeight(width);
	}

	@Override
	protected double computePrefWidth(double height) {
		return minWidth(height);
	}

	@Override
	public Orientation getContentBias() {
		return Orientation.HORIZONTAL;
	}

	public Text getDescriptionText() {
		return descriptionText;
	}

	public GeometryNode<?> getGeometryNode() {
		return shape;
	}

	public Text getTitleText() {
		return titleText;
	}

	public void setColor(Color color) {
		shape.setFill(color);
	}

	public void setDescription(String description) {
		this.descriptionText.setText(description);
	}

	public void setTitle(String title) {
		this.titleText.setText(title);
	}
}