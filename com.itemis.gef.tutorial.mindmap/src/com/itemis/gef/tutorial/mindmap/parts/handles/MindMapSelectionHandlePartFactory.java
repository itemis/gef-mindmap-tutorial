package com.itemis.gef.tutorial.mindmap.parts.handles;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.gef.mvc.fx.parts.DefaultSelectionHandlePartFactory;
import org.eclipse.gef.mvc.fx.parts.IHandlePart;
import org.eclipse.gef.mvc.fx.parts.IVisualPart;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.itemis.gef.tutorial.mindmap.parts.MindMapNodePart;

import javafx.scene.Node;

public class MindMapSelectionHandlePartFactory extends DefaultSelectionHandlePartFactory {

	@Inject
	private Injector injector;

	@Override
	protected List<IHandlePart<? extends Node>> createSingleSelectionHandleParts(IVisualPart<? extends Node> target,
			Map<Object, Object> contextMap) {
		List<IHandlePart<? extends Node>> handleParts = new ArrayList<>();

		// add super handles
		handleParts.addAll(super.createSingleSelectionHandleParts(target, contextMap));

		if (target instanceof MindMapNodePart) {
			// add deletion handle part
			DeleteMindMapNodeHandlePart delHp = injector.getInstance(DeleteMindMapNodeHandlePart.class);
			handleParts.add(delHp);
		}

		return handleParts;
	}

}
