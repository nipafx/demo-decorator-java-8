package org.codefx.lab.decorator.def;

import java.util.function.Function;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.event.HyperlinkListener;

/**
 * This interface extends {@link HyperlinkListener} with decorating default methods.
 */
@FunctionalInterface
public interface DecoratingHyperlinkListener extends HyperlinkListener {

	// adapt to 'HyperlinkListener'

	/**
	 * Creates a new decorating listener from the specified listener by forwarding all calls to it.
	 * 
	 * @param listener
	 *            the {@link HyperlinkListener} which will actually handle the events
	 * @return a new {@link DecoratingHyperlinkListener}
	 */
	static DecoratingHyperlinkListener from(HyperlinkListener listener) {
		return event -> listener.hyperlinkUpdate(event);
	}

	// decorating methods

	/**
	 * Returns a new listener which is decorated by applying the specified decorator function to this instance.
	 * 
	 * @param decorator
	 *            the {@link Function} which decorates this listener
	 * @param T
	 *            the type of {@link DecoratingHyperlinkListener} returned by the function
	 * @return a listener of type {@code T extends DecoratingHyperlinkListener}; the return value of the specified
	 *         {@code decorator}
	 */
	default DecoratingHyperlinkListener decorate(
			Function<? super DecoratingHyperlinkListener, ? extends DecoratingHyperlinkListener> decorator) {
		return decorator.apply(this);
	}

	/**
	 * Returns a new listener which logs events to the console.
	 * 
	 * @return a new {@link DecoratingHyperlinkListener}
	 */
	default DecoratingHyperlinkListener logEvents() {
		return LogEventsToConsole.decorate(this);
	}

	/**
	 * Returns a new listener which makes the specified component visible while the mouse is hovering over the link.
	 * 
	 * @param component
	 *            the {@link JComponent} to be made visible
	 * @return a new {@link DecoratingHyperlinkListener}
	 */
	default DecoratingHyperlinkListener onHoverMakeVisible(JComponent component) {
		return OnHoverMakeComponentVisible.decorate(this, component);
	}

	/**
	 * Returns a new listener which sets the URL as test on the specified label while the mouse is hovering over the
	 * link.
	 * 
	 * @param label
	 *            the {@link JLabel} on which the URL is set
	 * @return a new {@link DecoratingHyperlinkListener}
	 */
	default DecoratingHyperlinkListener onHoverSetUrlOn(JLabel label) {
		return OnHoverSetUrlAsLabelText.decorate(this, label);
	}

}
