package com.itemis.gef.tutorial.mindmap;

import org.eclipse.gef.common.adapt.AdapterKey;
import org.eclipse.gef.common.adapt.inject.AdapterMaps;
import org.eclipse.gef.mvc.fx.MvcFxModule;
import org.eclipse.gef.mvc.fx.behaviors.HoverIntentBehavior;
import org.eclipse.gef.mvc.fx.behaviors.SelectionBehavior;
import org.eclipse.gef.mvc.fx.handlers.FocusAndSelectOnClickHandler;
import org.eclipse.gef.mvc.fx.handlers.HoverOnHoverHandler;
import org.eclipse.gef.mvc.fx.handlers.ResizeTranslateFirstAnchorageOnHandleDragHandler;
import org.eclipse.gef.mvc.fx.handlers.TranslateSelectedOnDragHandler;
import org.eclipse.gef.mvc.fx.parts.DefaultHoverFeedbackPartFactory;
import org.eclipse.gef.mvc.fx.parts.DefaultSelectionFeedbackPartFactory;
import org.eclipse.gef.mvc.fx.parts.DefaultSelectionHandlePartFactory;
import org.eclipse.gef.mvc.fx.parts.SquareSegmentHandlePart;
import org.eclipse.gef.mvc.fx.policies.ResizePolicy;
import org.eclipse.gef.mvc.fx.policies.TransformPolicy;
import org.eclipse.gef.mvc.fx.providers.ShapeBoundsProvider;
import org.eclipse.gef.mvc.fx.providers.ShapeOutlineProvider;

import com.google.inject.multibindings.MapBinder;
import com.itemis.gef.tutorial.mindmap.behaviors.CreateFeedbackBehavior;
import com.itemis.gef.tutorial.mindmap.models.ItemCreationModel;
import com.itemis.gef.tutorial.mindmap.parts.MindMapNodePart;
import com.itemis.gef.tutorial.mindmap.parts.MindMapPartsFactory;
import com.itemis.gef.tutorial.mindmap.parts.SimpleMindMapAnchorProvider;
import com.itemis.gef.tutorial.mindmap.parts.feedback.CreateFeedbackPartFactory;
import com.itemis.gef.tutorial.mindmap.parts.handles.DeleteMindMapNodeHandlePart;
import com.itemis.gef.tutorial.mindmap.parts.handles.MindMapHoverIntentHandlePartFactory;
import com.itemis.gef.tutorial.mindmap.parts.handles.MindMapSelectionHandlePartFactory;
import com.itemis.gef.tutorial.mindmap.policies.CreateNewConnectionOnClickHandler;
import com.itemis.gef.tutorial.mindmap.policies.CreateNewNodeOnClickHandler;
import com.itemis.gef.tutorial.mindmap.policies.DeleteNodeOnHandleClickHandler;
import com.itemis.gef.tutorial.mindmap.policies.ShowMindMapNodeContextMenuOnClickHandler;

/**
 * The Guice Module to configure our parts and behaviors.
 *
 */
public class SimpleMindMapModule extends MvcFxModule {

	@Override
	protected void bindAbstractContentPartAdapters(MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		super.bindAbstractContentPartAdapters(adapterMapBinder);

		// binding the HoverOnHoverPolicy to every part
		// if a mouse is moving above a part it is set i the HoverModel
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(HoverOnHoverHandler.class);

		// add the focus and select policy to every part, listening to clicks
		// and changing the focus and selection model
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(FocusAndSelectOnClickHandler.class);
	}

	protected void bindDeleteMindMapNodeHandlePartAdapters(MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(DeleteNodeOnHandleClickHandler.class);
	}

	@Override
	protected void bindHoverHandlePartFactoryAsContentViewerAdapter(MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		adapterMapBinder.addBinding(AdapterKey.role(HoverIntentBehavior.HOVER_INTENT_HANDLE_PART_FACTORY))
				.to(MindMapHoverIntentHandlePartFactory.class);
	}

	@Override
	protected void bindIContentPartFactoryAsContentViewerAdapter(MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		// bind MindMapPartsFactory adapter to the content viewer
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(MindMapPartsFactory.class);
	}

	@Override
	protected void bindIRootPartAdaptersForContentViewer(MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		super.bindIRootPartAdaptersForContentViewer(adapterMapBinder);

		// support creation of nodes via mouse click
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(CreateNewNodeOnClickHandler.class);

		// adding the creation feedback behavior
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(CreateFeedbackBehavior.class);
	}

	@Override
	protected void bindIViewerAdaptersForContentViewer(MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		super.bindIViewerAdaptersForContentViewer(adapterMapBinder);
		// add ItemCreationModel adapter binding
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(ItemCreationModel.class);

		// binding the creation feedback part factory using the role that is
		// specified in the behavior
		AdapterKey<?> role = AdapterKey.role(CreateFeedbackBehavior.CREATE_FEEDBACK_PART_FACTORY);
		adapterMapBinder.addBinding(role).to(CreateFeedbackPartFactory.class);
	}

	/**
	 *
	 * @param adapterMapBinder
	 */
	protected void bindMindMapNodePartAdapters(MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		// bind anchor provider used to create the connection anchors
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(SimpleMindMapAnchorProvider.class);

		// bind a geometry provider, which is used in our anchor provider
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(ShapeOutlineProvider.class);

		// provides a hover feedback to the shape, used by the HoverBehavior
		AdapterKey<?> role = AdapterKey.role(DefaultHoverFeedbackPartFactory.HOVER_FEEDBACK_GEOMETRY_PROVIDER);
		adapterMapBinder.addBinding(role).to(ShapeOutlineProvider.class);

		// provides a selection feedback to the shape
		role = AdapterKey.role(DefaultSelectionFeedbackPartFactory.SELECTION_FEEDBACK_GEOMETRY_PROVIDER);
		adapterMapBinder.addBinding(role).to(ShapeBoundsProvider.class);

		// support moving nodes via mouse drag
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(TransformPolicy.class);
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(TranslateSelectedOnDragHandler.class);

		// specify the factory to create the geometry object for the selection
		// handles
		role = AdapterKey.role(DefaultSelectionHandlePartFactory.SELECTION_HANDLES_GEOMETRY_PROVIDER);
		adapterMapBinder.addBinding(role).to(ShapeBoundsProvider.class);

		// support resizing nodes
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(ResizePolicy.class);

		// support creation of connections
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(CreateNewConnectionOnClickHandler.class);

		// bind the context menu policy to the part
		adapterMapBinder.addBinding(AdapterKey.defaultRole()).to(ShowMindMapNodeContextMenuOnClickHandler.class);
	}

	@Override
	protected void bindSelectionHandlePartFactoryAsContentViewerAdapter(
			MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		adapterMapBinder.addBinding(AdapterKey.role(SelectionBehavior.SELECTION_HANDLE_PART_FACTORY))
				.to(MindMapSelectionHandlePartFactory.class);
	}

	/**
	 * Binds the parts of the selection handles (the squares in the corner) to
	 * policies
	 *
	 * @param adapterMapBinder
	 */
	protected void bindSquareSegmentHandlePartPartAdapter(MapBinder<AdapterKey<?>, Object> adapterMapBinder) {
		adapterMapBinder.addBinding(AdapterKey.defaultRole())
				.to(ResizeTranslateFirstAnchorageOnHandleDragHandler.class);
	}

	@Override
	protected void configure() {
		// start the default configuration
		super.configure();

		bindMindMapNodePartAdapters(AdapterMaps.getAdapterMapBinder(binder(), MindMapNodePart.class));

		// with this binding we create the handles
		bindSquareSegmentHandlePartPartAdapter(
				AdapterMaps.getAdapterMapBinder(binder(), SquareSegmentHandlePart.class));
		bindDeleteMindMapNodeHandlePartAdapters(
				AdapterMaps.getAdapterMapBinder(binder(), DeleteMindMapNodeHandlePart.class));
	}
}