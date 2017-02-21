package com.itemis.gef.tutorial.mindmap.parts;

import java.util.Collections;
import java.util.List;

import org.eclipse.gef.geometry.planar.Rectangle;
import org.eclipse.gef.mvc.fx.parts.AbstractContentPart;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import com.itemis.gef.tutorial.mindmap.model.MindMapNode;
import com.itemis.gef.tutorial.mindmap.visuals.MindMapNodeVisual;

/**
 * the {@link MindMapNodePart} is responsible to create and update the
 * {@link MindMapNodeVisual} for a instance of the {@link MindMapNode}.
 *
 */
public class MindMapNodePart extends AbstractContentPart<MindMapNodeVisual> {

	@Override
	protected MindMapNodeVisual doCreateVisual() {
		return new MindMapNodeVisual();
	}

	@Override
	protected SetMultimap<? extends Object, String> doGetContentAnchorages() {
		// Nothing to anchor to
		return HashMultimap.create();
	}

	@Override
	protected List<? extends Object> doGetContentChildren() {
		// we don't have any children.
		return Collections.emptyList();
	}

	@Override
	protected void doRefreshVisual(MindMapNodeVisual visual) {
		// updating the visuals texts and position

		MindMapNode node = getContent();
		Rectangle rec = node.getBounds();

		visual.setTitle(node.getTitle());
		visual.setDescription(node.getDescription());
		visual.setColor(node.getColor());

		visual.setPrefSize(rec.getWidth(), rec.getHeight());
		// perform layout pass so that visual is resized to its preferred size
		visual.getParent().layout();

		visual.setTranslateX(rec.getX());
		visual.setTranslateY(rec.getY());
	}

	@Override
	public MindMapNode getContent() {
		return (MindMapNode) super.getContent();
	}
}