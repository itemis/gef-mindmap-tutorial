package com.itemis.gef.tutorial.mindmap.policies;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.gef.mvc.fx.handlers.AbstractHandler;
import org.eclipse.gef.mvc.fx.handlers.IOnClickHandler;
import org.eclipse.gef.mvc.fx.operations.ITransactionalOperation;

import com.itemis.gef.tutorial.mindmap.models.IInlineEditableField;
import com.itemis.gef.tutorial.mindmap.models.InlineEditModel;
import com.itemis.gef.tutorial.mindmap.operations.SubmitInlineEditOperation;
import com.itemis.gef.tutorial.mindmap.parts.IInlineEditablePart;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class InlineEditOnClickHandler extends AbstractHandler implements IOnClickHandler {
	
	@Override
	public void click(MouseEvent e) {
		if (e.getClickCount() != 2) {
			return;
		}
		
		IInlineEditablePart host = (IInlineEditablePart) getHost();
		Node target = (Node) e.getTarget();
		IInlineEditableField field = getEditableField(host, target);
		
		if (field!=null) {
			InlineEditModel editModel = getHost().getRoot().getViewer().getAdapter(InlineEditModel.class);
			
			// store some information before editing
			editModel.setHost(getHost());
			editModel.setCurrentEditableField(field);
			
			// start editing
			host.startEditing(field);
			Node editorNode = field.getEditorNode();
			
			// add some listeners
			// they need to be somewhere else - where?
			editorNode.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
				@Override
				public void handle(KeyEvent event) {
					if (event.getCode() == KeyCode.ESCAPE) {
						host.endEditing(field);
					}
					if (field.isSubmitEvent(event)) {
						ITransactionalOperation op = new SubmitInlineEditOperation(host, field);
						try {
							getHost().getRoot().getViewer().getDomain().execute(op, new NullProgressMonitor());
						} catch (ExecutionException e) {
							e.printStackTrace();
						}
						host.endEditing(field);
					}
				}
			});
			
			editorNode.focusedProperty().addListener(
				new ChangeListener<Boolean>() {
					@Override
					public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
						if (!newValue) {
							Task<Boolean> task = new Task<Boolean>() {
								@Override
								protected Boolean call() throws Exception {
									// we wait if we get the focus right back, like in
									// clicking inside the textfield
									Thread.sleep(200);
									if (!editorNode.focusedProperty().getValue()) {
										// end editing in UI thread
										Platform.runLater(new Runnable() {
											@Override public void run() {
												host.endEditing(field);
											}
										});
									}
									return true;
								}
							};
							
							new Thread(task).start();
						}
					}
				});
			editorNode.requestFocus();
			}
		}
	
	private IInlineEditableField getEditableField(IInlineEditablePart host, EventTarget target) {
		for (IInlineEditableField field : host.getEditableFields()) {
			if (field.getReadOnlyNode().equals(target))
				return field;
			}
		return null;
	}
}
