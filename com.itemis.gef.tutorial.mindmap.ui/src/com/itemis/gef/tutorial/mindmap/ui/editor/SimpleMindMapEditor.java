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

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * A Simple MindMap Editor usable in the Eclipse workbench.
 */
public class SimpleMindMapEditor extends AbstractFXEditor {

	public SimpleMindMapEditor() {
		super(Guice.createInjector(Modules.override(new SimpleMindMapModule()).with(new MvcFxUiModule())));
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.init(site, input);
		SimpleMindMap mindmap = null;
		IFile file = ((IFileEditorInput) input).getFile();
		
		// read the given input file
		try {
			ObjectInputStream is = new ObjectInputStream(file.getContents());
			mindmap = (SimpleMindMap) is.readObject();
			is.close();
		} catch (EOFException e) {
			// in case of an empty file
			mindmap = new SimpleMindMap();
		} catch (Exception e) {
			throw new PartInitException("Could not load input", e);
		}
			
		setPartName(file.getName());
		
		// reset default color, because we didn't save the color
		for (AbstractMindMapItem item : mindmap.getChildElements()) {
			if (item instanceof MindMapNode) {
				MindMapNode mindMapNode = (MindMapNode) item;
				mindMapNode.setColor(Color.GREENYELLOW);
			}
		}
		
		// retrieve the viewer's content
		IViewer viewer = getDomain().getAdapter(IViewer.class);
		ObservableList<Object> contents = viewer.getContents();
		contents.setAll(Collections.singletonList(mindmap));
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// retrieve the viewer's content
		IViewer viewer = getDomain().getAdapter(IViewer.class);
		ObservableList<Object> contents = viewer.getContents();
		SimpleMindMap mindmap = (SimpleMindMap) contents.iterator().next();
		try {
			// serialize mindmap
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(out);
			oos.writeObject(mindmap);
			oos.close();
			// write to file
			IFile file = ((IFileEditorInput) getEditorInput()).getFile();
			file.setContents(new ByteArrayInputStream(out.toByteArray()), true, false, monitor);
			markNonDirty();
			firePropertyChange(PROP_DIRTY);
		} catch (CoreException | IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void doSaveAs() {
	}

	/**
	 * Creating JavaFX widgets and set them to the stage.
	 */
	@Override
	protected void hookViewers() {
		final IViewer contentViewer = getContentViewer();
		
		// creating parent pane for canvas and button pane
		BorderPane pane = new BorderPane();
		pane.setCenter(contentViewer.getCanvas());
		pane.setRight(createToolPalette());
		pane.setMinWidth(800);
		pane.setMinHeight(600);
		
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
		
		// now listen to changes in the model, and deactivate buttons, if necessary
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