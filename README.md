# Decorator Pattern With Java 8

This project provides the code samples for the [CodeFX post about the decorator pattern in Java 8](http://blog.codefx.org/design/patterns/decorator-pattern-java-8). It uses static and default interface methods to create a fluent API for decorations:

```java
HyperlinkListener listener = this::changeHtmlViewBackgroundColor;
listener = DecoratingHyperlinkListener.from(listener)
	.onHoverMakeVisible(urlLabel)
	.onHoverSetUrlOn(urlLabel)
	.logEvents()
	.decorate(l -> new OnActivateHighlightComponent(l, urlLabel))
	.decorate(OnEnterLogUrl::new);
```

## Swing?

The code uses Swing's `HyperlinkListener` for its examples. The main reason is to continue on a [previous post](http://blog.codefx.org/design/patterns/decorator-pattern-saved-my-day/) but it turns out to be a good interface for the demonstrations as it is not generic and has only one method with one argument. All of this reduces the necessary clutter to a bare minimum.

The demo is executable and creates a small Swing frame which contains two links and reacts to events on them.

## Code

The code is split into the package which defines the decoration API ([org.codefx.lab.decorator.def](https://github.com/CodeFX-org/decorator-java-8/tree/master/src/org/codefx/lab/decorator/def)) and the using code in [Demo.java](https://github.com/CodeFX-org/decorator-java-8/blob/master/src/org/codefx/lab/decorator/Demo.java).

The latter stands for all the places in a project where the decorations are used but new decorators are so specific that they should not be added to the decoration API. This is represented by the private listener classes in `Demo`. It contains the above code sample in `createHyperlinkListener()`.
