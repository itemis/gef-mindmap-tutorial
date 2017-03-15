package com.itemis.gef.tutorial.mindmap.operations;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.gef.mvc.fx.operations.ITransactionalOperation;

import com.itemis.gef.tutorial.mindmap.parts.MindMapNodePart;

/**
 * operation to change the title property of a MindMapNode
 *
 * @author hniederhausen
 *
 */
public class SetMindMapNodeDescriptionOperation extends AbstractOperation implements ITransactionalOperation {

	private final MindMapNodePart nodePart;
	private final String oldDescription;
	private final String newDescription;

	public SetMindMapNodeDescriptionOperation(MindMapNodePart nodePart, String newDescription) {
		super("Change color");
		this.nodePart = nodePart;
		this.newDescription = newDescription;
		this.oldDescription = nodePart.getContent().getDescription();
	}

	@Override
	public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		nodePart.getContent().setDescription(newDescription);
		return Status.OK_STATUS;
	}

	@Override
	public boolean isContentRelevant() {
		// yes we change the model
		return true;
	}

	@Override
	public boolean isNoOp() {
		return newDescription.equals(oldDescription);
	}

	@Override
	public IStatus redo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		return execute(monitor, info);
	}

	@Override
	public IStatus undo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		nodePart.getContent().setDescription(oldDescription);
		return Status.OK_STATUS;
	}

}