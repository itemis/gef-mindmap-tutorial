/**
 * GEF 5.0.0 Mindmap Tutorial
 *
 *  Copyright 2017 by itemis AG
 *
 * This file is part of some open source application.
 *
 * Some open source application is free software: you can redistribute
 * it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 *
 * Some open source application is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 *
 * @license GPL-3.0+ <http://spdx.org/licenses/GPL-3.0+>
 */
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