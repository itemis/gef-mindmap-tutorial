package com.itemis.gef.tutorial.mindmap.parts.handles;

import java.util.List;
import java.util.Map;

import org.eclipse.gef.mvc.fx.parts.DefaultHoverIntentHandlePartFactory;
import org.eclipse.gef.mvc.fx.parts.IHandlePart;
import org.eclipse.gef.mvc.fx.parts.IVisualPart;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.itemis.gef.tutorial.mindmap.parts.MindMapNodePart;

import javafx.scene.Node;

/**
 * The {@link MindMapNodeHoverHandlesFactory} creates the handles to modify the
 * {@link MindMapNodePart}s.
 *
 */
public class MindMapHoverIntentHandlePartFactory extends DefaultHoverIntentHandlePartFactory {

	@Inject
	private Injector injector;

	@Override
	public List<IHandlePart<? extends Node>> createHandleParts(List<? extends IVisualPart<? extends Node>> targets,
			Map<Object, Object> contextMap) {
		List<IHandlePart<? extends Node>> handleParts = Lists.newArrayList();

		// add super handles
		handleParts.addAll(super.createHandleParts(targets, contextMap));

		if (targets.size() > 0) {
			if (targets.get(0) instanceof MindMapNodePart) {
				// add deletion handle
				DeleteMindMapNodeHandlePart delHp = injector.getInstance(DeleteMindMapNodeHandlePart.class);
				handleParts.add(delHp);
			}
		}

		return handleParts;
	}

}