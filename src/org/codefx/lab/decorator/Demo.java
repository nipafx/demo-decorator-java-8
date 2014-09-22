package org.codefx.lab.decorator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.net.URL;
import java.util.Objects;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkEvent.EventType;
import javax.swing.event.HyperlinkListener;

import org.codefx.lab.decorator.def.AbstractHyperlinkListenerDecorator;
import org.codefx.lab.decorator.def.DecoratingHyperlinkListener;

/**
 * Demonstrates how easily the decorator pattern can be used with Java 8. See {@link #createHyperlinkListener()} for the
 * decorations.
 */
public class Demo {

	// ATTRIBUTES

	private static final String HTML =
			"Here's a text with two links. One <a href=\"http://libfx.codefx.org\">here</a> "
					+ "and one <a href=\"http://blog.codefx.org\">here</a>.";

	private final JFrame window;

	private final JEditorPane htmlView;

	private final JLabel urlLabel;

	// CONSTRUCTION

	private Demo() {
		window = createFrame();
		htmlView = createHtmlView();
		urlLabel = createUrlLabel();
		putWindowTogether();

		HyperlinkListener listener = createHyperlinkListener();
		htmlView.addHyperlinkListener(listener);
	}

	private static JFrame createFrame() {
		JFrame window = new JFrame("Decorating With Java 8");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setMinimumSize(new Dimension(200, 0));
		window.setPreferredSize(new Dimension(200, 300));
		window.setMaximumSize(new Dimension(200, Integer.MAX_VALUE));
		return window;
	}

	private static JEditorPane createHtmlView() {
		JEditorPane htmlView = new JEditorPane();
		htmlView.setEditable(false);
		htmlView.setContentType("text/html");
		htmlView.setText(HTML);

		return htmlView;
	}

	private static JLabel createUrlLabel() {
		JLabel urlLabel = new JLabel();
		urlLabel.setOpaque(true);
		return urlLabel;
	}

	private void putWindowTogether() {
		window.getContentPane().add(htmlView, BorderLayout.CENTER);
		window.getContentPane().add(urlLabel, BorderLayout.SOUTH);
	}

	/**
	 * HERE BE DRAGONS!
	 */
	private HyperlinkListener createHyperlinkListener() {
		// this is the hyperlink listener which will be decorated;
		// it must not even implement 'DecoratingHyperlinkListener'
		HyperlinkListener listener = this::changeHtmlViewBackgroundColor;

		// now we can decorate it...
		// first, we use the static 'from' to create a 'DecoratingHyperlinkListener' from the 'HyperlinkListener'
		listener = DecoratingHyperlinkListener.from(listener)
				// next, we can call the default methods provided by the interface to decorate
				.onHoverMakeVisible(urlLabel)
				.onHoverSetUrlOn(urlLabel)
				.logEvents()
				// for a decorator which can not be referenced by the 'DecoratingHyperlinkListener' interface,
				// use the more generic 'decorate' method; either with a lambda expression ...
				.decorate(l -> new OnActivateHighlightComponent(l, urlLabel))
				// ... or, where possible, with a constructor reference
				.decorate(OnEnterLogUrl::new);
		return listener;
	}

	private void changeHtmlViewBackgroundColor(HyperlinkEvent event) {
		if (event.getEventType() == EventType.ENTERED)
			htmlView.setBackground(Color.ORANGE);
		else if (event.getEventType() == EventType.EXITED)
			htmlView.setBackground(Color.WHITE);
	}

	// RUN

	private void run() {
		window.pack();
		window.setVisible(true);
	}

	public static void main(String[] args) {
		new Demo().run();
	}

	// private classes

	private static class OnEnterLogUrl extends AbstractHyperlinkListenerDecorator {

		private static final String URL_NULL = "n.a.";

		protected OnEnterLogUrl(HyperlinkListener decoratedListener) {
			super(decoratedListener);
		}

		@Override
		public void hyperlinkUpdate(HyperlinkEvent event) {
			if (event.getEventType() == EventType.ENTERED)
				logUrl(event.getURL());

			super.hyperlinkUpdate(event);
		}

		private void logUrl(URL url) {
			String urlString = url != null ? url.toExternalForm() : URL_NULL;
			System.out.println("Link entered with URL " + urlString);
		}

	}

	private static class OnActivateHighlightComponent extends AbstractHyperlinkListenerDecorator {

		// ATTRIBUTES

		private static final long RESET_DELAY_IN_MS = 100;

		private final JComponent component;

		private final ScheduledThreadPoolExecutor executor;

		// CONSTRUCTION

		public OnActivateHighlightComponent(HyperlinkListener decoratedListener, JComponent component) {
			super(decoratedListener);

			Objects.requireNonNull(component, "The argument 'component' must not be null.");
			this.component = component;
			this.executor = new ScheduledThreadPoolExecutor(1);
		}

		// HANDLE HYPERLINK EVENT

		@Override
		public void hyperlinkUpdate(HyperlinkEvent event) {
			if (event.getEventType() == EventType.ACTIVATED)
				highlightBackground();

			super.hyperlinkUpdate(event);
		}

		private void highlightBackground() {
			Color colorBefore = component.getBackground();
			component.setBackground(Color.MAGENTA);
			resetBackgroundColorAfterDelay(colorBefore);
		}

		private void resetBackgroundColorAfterDelay(Color colorBefore) {
			Runnable resetBackgroundColor = () -> component.setBackground(colorBefore);
			Runnable resetBackgroundColorInDispatchThread = () -> EventQueue.invokeLater(resetBackgroundColor);
			executor.schedule(resetBackgroundColorInDispatchThread, RESET_DELAY_IN_MS, TimeUnit.MILLISECONDS);
		}

	}

}
