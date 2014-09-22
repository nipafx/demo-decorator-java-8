package org.codefx.lab.decorator.def;

import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

/**
 * Logs the event to the console.
 */
public final class LogEventsToConsole extends AbstractHyperlinkListenerDecorator {

	// CONSTRUCTION

	private LogEventsToConsole(HyperlinkListener decoratedListener) {
		super(decoratedListener);
	}

	public static DecoratingHyperlinkListener decorate(HyperlinkListener decoratedListener) {
		return new LogEventsToConsole(decoratedListener);
	}

	// HANDLE HYPERLINK EVENT

	@Override
	public void hyperlinkUpdate(HyperlinkEvent event) {
		System.out.println("Link " + event.getEventType() + ".");
		super.hyperlinkUpdate(event);
	}

}
