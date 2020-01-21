package com.itemis.gef.tutorial.mindmap.ui.editor;

import org.eclipse.gef.mvc.fx.ui.actions.FitToViewportActionGroup;
import org.eclipse.gef.mvc.fx.ui.actions.ScrollActionGroup;
import org.eclipse.gef.mvc.fx.ui.actions.ZoomActionGroup;
import org.eclipse.gef.mvc.fx.ui.parts.FXEditorActionBarContributor;
import org.eclipse.gef.mvc.fx.viewer.IViewer;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;

/**
 * Registers handlers for undo, redo, select-all, and delete.
 * 
 * Additionally contributes toolbar actions for zooming and scrolling.
 */
public class EditorActionBarContributor extends FXEditorActionBarContributor {

	private ZoomActionGroup zoom;
	private FitToViewportActionGroup fitToViewport;
	private ScrollActionGroup scroll;
	private IEditorPart previousEditor;

	public EditorActionBarContributor() {
		zoom = new ZoomActionGroup();
		fitToViewport = new FitToViewportActionGroup();
		scroll = new ScrollActionGroup();
	}
	
	@Override
	public void setActiveEditor(IEditorPart activeEditor) {
		super.setActiveEditor(activeEditor);
		
		if (previousEditor == activeEditor) {
			return;
		}
		
		if (previousEditor instanceof SimpleMindMapEditor) {
			SimpleMindMapEditor oldEditor = (SimpleMindMapEditor) previousEditor;
			IViewer viewer = oldEditor.getContentViewer();
			viewer.unsetAdapter(zoom);
			viewer.unsetAdapter(fitToViewport);
			viewer.unsetAdapter(scroll);
		}
		if (activeEditor instanceof SimpleMindMapEditor) {
			SimpleMindMapEditor newEditor = (SimpleMindMapEditor) activeEditor;
			IViewer viewer = newEditor.getContentViewer();
			viewer.setAdapter(zoom);
			viewer.setAdapter(fitToViewport);
			viewer.setAdapter(scroll);
		}
		previousEditor = activeEditor;
	}
	
	@Override
	public void contributeToToolBar(IToolBarManager toolBarManager) {
		IActionBars actionBars = getActionBars();
		zoom.fillActionBars(actionBars);
		toolBarManager.add(new Separator());
		fitToViewport.fillActionBars(actionBars);
		toolBarManager.add(new Separator());
		scroll.fillActionBars(actionBars);
	}
}