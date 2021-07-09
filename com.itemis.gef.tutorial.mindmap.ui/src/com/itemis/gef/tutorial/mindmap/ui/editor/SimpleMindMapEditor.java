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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.mvc.fx.ui.MvcFxUiModule;
import org.eclipse.gef.mvc.fx.ui.parts.AbstractFXEditor;
import org.eclipse.gef.mvc.fx.viewer.IViewer;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;

import com.google.inject.Guice;
import com.google.inject.util.Modules;
import com.itemis.gef.tutorial.mindmap.SimpleMindMapModule;
import com.itemis.gef.tutorial.mindmap.model.AbstractMindMapItem;
import com.itemis.gef.tutorial.mindmap.model.MindMapNode;
import com.itemis.gef.tutorial.mindmap.model.SimpleMindMap;
import com.itemis.gef.tutorial.mindmap.models.ItemCreationModel;
import com.itemis.gef.tutorial.mindmap.models.ItemCreationModel.Type;
import com.itemis.gef.tutorial.mindmap.visuals.MindMapNodeVisual;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * An Eclipse editor usable as extension.
 * 
 */
public class SimpleMindMapEditor extends AbstractFXEditor {

	public SimpleMindMapEditor() {
		super(Guice.createInjector(Modules.override(new SimpleMindMapModule()).with(new MvcFxUiModule())));
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// retrieve the viewer's content
		SimpleMindMap mindmap = (SimpleMindMap) getContentViewer().getContents().get(0);
		try { // serialize mindmap
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(out);
			oos.writeObject(mindmap);
			oos.close();
			// write to file
			IFile file = ((IFileEditorInput) getEditorInput()).getFile();
			file.setContents(new ByteArrayInputStream(out.toByteArray()), true, false, monitor);
			markNonDirty();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (CoreException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void doSaveAs() {
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.init(site, input);

		SimpleMindMap mindmap = null; // read the given input file
		try {
			IFile file = ((IFileEditorInput) input).getFile();
			ObjectInputStream is = new ObjectInputStream(file.getContents());
			mindmap = (SimpleMindMap) is.readObject();
			is.close();
			setPartName(file.getName());

			// reset default color, because we didn't save the color
			for (AbstractMindMapItem item : mindmap.getChildElements()) {
				if (item instanceof MindMapNode) {
					((MindMapNode) item).setColor(Color.GREENYELLOW);
				}
			}
		} catch (EOFException e) {
			// create new SimpleMindMap...
			mindmap = new SimpleMindMap();
		} catch (Exception e) {
			throw new PartInitException("Could not load input", e);
		}
		getContentViewer().getContents().setAll(Collections.singletonList(mindmap));
	}

	/**
	 * Creating JavaFX widgets and set them to the stage.
	 */
	@Override
	protected void hookViewers() {
		final IViewer contentViewer = getContentViewer();

		// creating parent pane for viewer canvas and palette
		HBox pane = new HBox();

		pane.getChildren().add(contentViewer.getCanvas());
		pane.getChildren().add(createToolPalette());
		HBox.setHgrow(contentViewer.getCanvas(), Priority.ALWAYS);

		Scene scene = new Scene(pane);
		getCanvas().setScene(scene);
	}

	private Node createToolPalette() {
		ItemCreationModel creationModel = getContentViewer().getAdapter(ItemCreationModel.class);

		MindMapNodeVisual graphic = new MindMapNodeVisual();
		graphic.setTitle("New Node");

		// the toggleGroup makes sure, we only select one
		ToggleGroup toggleGroup = new ToggleGroup();

		ToggleButton createNode = new ToggleButton("", graphic);
		createNode.setToggleGroup(toggleGroup);
		createNode.setMaxWidth(Double.MAX_VALUE);
		createNode.selectedProperty().addListener((e, oldVal, newVal) -> {
			Type type = Type.None;
			if (newVal) {
				type = Type.Node;
			}
			creationModel.setType(type);
		});

		ToggleButton createConn = new ToggleButton("New Connection");
		createConn.setToggleGroup(toggleGroup);
		createConn.setMaxWidth(Double.MAX_VALUE);
		createConn.setMinHeight(50);
		createConn.selectedProperty().addListener((e, oldVal, newVal) -> {
			Type type = Type.None;
			if (newVal) {
				type = Type.Connection;
			}
			creationModel.setType(type);
		});

		// now listen to changes in the model, and deactivate buttons, if
		// necessary
		creationModel.getTypeProperty().addListener((e, oldVal, newVal) -> {
			if (oldVal == newVal) {
				return;
			}
			switch (newVal) {
			case Node:
			case Connection:
				break;
			case None:
			default:
				// unselect the button
				toggleGroup.getSelectedToggle().setSelected(false);
				break;

			}
		});

		return new VBox(20, createNode, createConn);
	}
}