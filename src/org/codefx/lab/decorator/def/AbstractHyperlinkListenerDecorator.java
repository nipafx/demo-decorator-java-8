package org.codefx.lab.decorator.def;

import java.util.Objects;

import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

/**
 * Abstract superclass to all decorators of {@link HyperlinkListener}.
 */
public abstract class AbstractHyperlinkListenerDecorator implements DecoratingHyperlinkListener {

	private final HyperlinkListener decoratedListener;

	protected AbstractHyperlinkListenerDecorator(HyperlinkListener decoratedListener) {
		Objects.requireNonNull(decoratedListener, "The argument 'decoratedListener' must not be null.");
		this.decoratedListener = decoratedListener;
	}

	@Override
	public void hyperlinkUpdate(HyperlinkEvent event) {
		decoratedListener.hyperlinkUpdate(event);
	}

}
