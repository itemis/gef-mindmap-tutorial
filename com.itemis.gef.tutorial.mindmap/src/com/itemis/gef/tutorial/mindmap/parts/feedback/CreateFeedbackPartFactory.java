package com.itemis.gef.tutorial.mindmap.parts.feedback;

import java.util.List;
import java.util.Map;

import org.eclipse.gef.mvc.fx.parts.IFeedbackPart;
import org.eclipse.gef.mvc.fx.parts.IFeedbackPartFactory;
import org.eclipse.gef.mvc.fx.parts.IVisualPart;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.itemis.gef.tutorial.mindmap.behaviors.CreateFeedbackBehavior;
import com.itemis.gef.tutorial.mindmap.parts.MindMapNodePart;

import javafx.scene.Node;

/**
 * The factory is used in the {@link CreateFeedbackBehavior} to create parts for
 * the feedback.
 *
 */
public class CreateFeedbackPartFactory implements IFeedbackPartFactory {

	@Inject
	private Injector injector;

	@Override
	public List<IFeedbackPart<? extends Node>> createFeedbackParts(List<? extends IVisualPart<? extends Node>> targets,
			Map<Object, Object> contextMap) {
		List<IFeedbackPart<? extends Node>> parts = Lists.newArrayList();

		if (targets.isEmpty()) {
			return parts; // shouldn't happen, just to be sure
		}

		// we just expect one target
		IVisualPart<? extends Node> target = targets.get(0);

		if (target instanceof MindMapNodePart) {
			// a MindMapNode target is the source of a connection so we create
			// the connection feedback
			CreateConnectionFeedbackPart part = injector.getInstance(CreateConnectionFeedbackPart.class);
			parts.add(part);
		}

		return parts;
	}

}