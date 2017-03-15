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
public class SetMindMapNodeTitleOperation extends AbstractOperation implements ITransactionalOperation {

	private final MindMapNodePart nodePart;
	private final String oldTitle;
	private final String newTitle;

	public SetMindMapNodeTitleOperation(MindMapNodePart nodePart, String newTitle) {
		super("Change color");
		this.nodePart = nodePart;
		this.newTitle = newTitle;
		this.oldTitle = nodePart.getContent().getTitle();
	}

	@Override
	public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		nodePart.getContent().setTitle(newTitle);
		return Status.OK_STATUS;
	}

	@Override
	public boolean isContentRelevant() {
		// yes we change the model
		return true;
	}

	@Override
	public boolean isNoOp() {
		return newTitle.equals(oldTitle);
	}

	@Override
	public IStatus redo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		return execute(monitor, info);
	}

	@Override
	public IStatus undo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		nodePart.getContent().setTitle(oldTitle);
		return Status.OK_STATUS;
	}
}