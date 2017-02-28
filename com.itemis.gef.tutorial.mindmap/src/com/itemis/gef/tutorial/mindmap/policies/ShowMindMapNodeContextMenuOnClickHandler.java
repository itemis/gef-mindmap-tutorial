package com.itemis.gef.tutorial.mindmap.policies;

import java.util.ArrayList;

import org.eclipse.gef.mvc.fx.handlers.AbstractHandler;
import org.eclipse.gef.mvc.fx.handlers.IOnClickHandler;
import org.eclipse.gef.mvc.fx.models.HoverModel;
import org.eclipse.gef.mvc.fx.parts.IContentPart;
import org.eclipse.gef.mvc.fx.parts.IRootPart;
import org.eclipse.gef.mvc.fx.parts.IVisualPart;
import org.eclipse.gef.mvc.fx.policies.DeletionPolicy;

import com.itemis.gef.tutorial.mindmap.parts.MindMapConnectionPart;

import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;

/**
 * This policy shows a context menu for MindMapNodeParts, providing some editing
 * functionality.
 *
 * @author hniederhausen
 *
 */
public class ShowMindMapNodeContextMenuOnClickHandler extends AbstractHandler implements IOnClickHandler {

	@Override
	public void click(MouseEvent event) {
		if (!event.isSecondaryButtonDown()) {
			return; // only listen to secondary buttons
		}

		MenuItem deleteNodeItem = new MenuItem("Delete Node");
		deleteNodeItem.setOnAction((e) -> {
			// FIXME
			// remove part from hover model (transient model, i.e. changes
			// are not undoable)
			HoverModel hover = getHost().getViewer().getAdapter(HoverModel.class);
			if (getHost() == hover.getHover()) {
				hover.clearHover();
			}

			// query DeletionPolicy for the removal of the host part
			IRootPart<? extends Node> root = getHost().getRoot();
			DeletionPolicy delPolicy = root.getAdapter(DeletionPolicy.class);
			init(delPolicy);

			// delete all anchored connection parts
			for (IVisualPart<? extends Node> a : new ArrayList<>(getHost().getAnchoredsUnmodifiable())) {
				if (a instanceof MindMapConnectionPart) {
					delPolicy.delete((IContentPart<? extends Node>) a);
				}
			}

			// delete the node part
			delPolicy.delete((IContentPart<? extends Node>) getHost());
			commit(delPolicy);
		});

		ContextMenu ctxMenu = new ContextMenu(deleteNodeItem);
		// show the menu at the mouse position
		ctxMenu.show((Node) event.getTarget(), event.getScreenX(), event.getScreenY());
	}
}