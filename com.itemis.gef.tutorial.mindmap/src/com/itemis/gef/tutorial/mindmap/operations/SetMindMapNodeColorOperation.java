package com.itemis.gef.tutorial.mindmap.operations;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.gef.mvc.fx.operations.ITransactionalOperation;

import com.itemis.gef.tutorial.mindmap.parts.MindMapNodePart;

import javafx.scene.paint.Color;

/**
 * operation to change the color property of a MindMapNode
 *
 */
public class SetMindMapNodeColorOperation extends AbstractOperation implements ITransactionalOperation {

	private final MindMapNodePart nodePart;
	private final Color newColor;
	private final Color oldColor;

	public SetMindMapNodeColorOperation(MindMapNodePart nodePart, Color newColor) {
		super("Change color");
		this.nodePart = nodePart;
		this.newColor = newColor;
		this.oldColor = nodePart.getContent().getColor();
	}

	@Override
	public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		nodePart.getContent().setColor(newColor);
		return Status.OK_STATUS;
	}

	@Override
	public boolean isContentRelevant() {
		// yes we change the model
		return true;
	}

	@Override
	public boolean isNoOp() {
		return newColor.equals(oldColor);
	}

	@Override
	public IStatus redo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		return execute(monitor, info);
	}

	@Override
	public IStatus undo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		nodePart.getContent().setColor(oldColor);
		return Status.OK_STATUS;
	}

}