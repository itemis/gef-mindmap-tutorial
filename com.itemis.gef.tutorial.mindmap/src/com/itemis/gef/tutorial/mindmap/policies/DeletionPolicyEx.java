package com.itemis.gef.tutorial.mindmap.policies;

import java.util.ArrayList;

import org.eclipse.gef.mvc.fx.parts.IContentPart;
import org.eclipse.gef.mvc.fx.parts.IVisualPart;
import org.eclipse.gef.mvc.fx.policies.DeletionPolicy;

import com.itemis.gef.tutorial.mindmap.parts.MindMapConnectionPart;
import com.itemis.gef.tutorial.mindmap.parts.MindMapNodePart;

import javafx.scene.Node;

/**
 * Deletes connections before mind map nodes.
 */
public class DeletionPolicyEx extends DeletionPolicy {

	@Override
	public void delete(IContentPart<? extends Node> contentPartToDelete) {
		if (contentPartToDelete instanceof MindMapNodePart) {
			MindMapNodePart node = (MindMapNodePart) contentPartToDelete;
			// delete connections from/to the part first
			for (IVisualPart<? extends Node> a : new ArrayList<>(node.getAnchoredsUnmodifiable())) {
				if (a instanceof MindMapConnectionPart) {
					super.delete((MindMapConnectionPart) a);
				}
			}
			super.delete(contentPartToDelete);
		}
	}
}
