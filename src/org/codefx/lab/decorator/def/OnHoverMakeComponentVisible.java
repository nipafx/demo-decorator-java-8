package org.codefx.lab.decorator.def;

import java.util.Objects;

import javax.swing.JComponent;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

/**
 * Makes the component specified during construction visible while the mouse is hovering over the link.
 */
public final class OnHoverMakeComponentVisible extends AbstractOnHoverHyperlinkListenerDecorator {

	// ATTRIBUTES

	private final JComponent component;

	// CONSTRUCTION

	private OnHoverMakeComponentVisible(HyperlinkListener decoratedListener, JComponent component) {
		super(decoratedListener);

		Objects.requireNonNull(component, "The argument 'component' must not be null.");
		this.component = component;
	}

	public static DecoratingHyperlinkListener decorate(HyperlinkListener decoratedListener, JComponent component) {
		return new OnHoverMakeComponentVisible(decoratedListener, component);
	}

	// HANDLE HYPERLINK EVENT

	@Override
	protected void onEnter(HyperlinkEvent event) {
		component.setVisible(true);
	}

	@Override
	protected void onExit(HyperlinkEvent event) {
		component.setVisible(false);
	}

}
