package com.itemis.gef.tutorial.mindmap.model;

public class MindMapConnection extends AbstractMindMapItem {

	/**
	 * Generated UUID
	 */
	private static final long serialVersionUID = 6065237357753406466L;

	private MindMapNode source;
	private MindMapNode target;
	private boolean connected;

	public void connect(MindMapNode source, MindMapNode target) {
		if (source == null || target == null || source == target) {
			throw new IllegalArgumentException();
		}
		disconnect();
		this.source = source;
		this.target = target;
		reconnect();
	}

	public void disconnect() {
		if (connected) {
			source.removeOutgoingConnection(this);
			target.removeIncomingConnection(this);
			connected = false;
		}
	}

	public MindMapNode getSource() {
		return source;
	}

	public MindMapNode getTarget() {
		return target;
	}

	public void reconnect() {
		if (!connected) {
			source.addOutgoingConnection(this);
			target.addIncomingConnection(this);
			connected = true;
		}
	}

	public void setSource(MindMapNode source) {
		this.source = source;
	}

	public void setTarget(MindMapNode target) {
		this.target = target;
	}
}