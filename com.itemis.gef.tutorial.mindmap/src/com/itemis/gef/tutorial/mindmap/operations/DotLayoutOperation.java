package com.itemis.gef.tutorial.mindmap.operations;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.gef.graph.Node;

import com.itemis.gef.tutorial.mindmap.layout.DotLayoutAlgorithm;
import com.itemis.gef.tutorial.mindmap.parts.MindMapNodePart;
import com.itemis.gef.tutorial.mindmap.parts.SimpleMindMapPart;

/**
 * Operation to layout the mind map automatically using
 * {@link DotLayoutAlgorithm}.
 */
public class DotLayoutOperation extends GefLayoutOperation {

	public static final String NODE_ID_ATTR = "ID";

	private int nodeID;

	public DotLayoutOperation(SimpleMindMapPart part) {
		super(part);
	}

	@Override
	protected Node createLayoutNode(MindMapNodePart mmNode) {
		Node node = super.createLayoutNode(mmNode);
		node.getAttributes().put(NODE_ID_ATTR, Integer.toString(nodeID++));
		return node;
	}

	@Override
	public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		nodeID = 0;
		return super.execute(monitor, info);
	}

	@Override
	protected DotLayoutAlgorithm getLayoutAlgorithm() {
		return new DotLayoutAlgorithm();
	}
}