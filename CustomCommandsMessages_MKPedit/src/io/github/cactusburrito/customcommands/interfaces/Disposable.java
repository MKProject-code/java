package io.github.cactusburrito.customcommands.interfaces;

/**
 * Dispose interface
 * This plugin uses disposal to safely and fully remove all unwanted instances of objects that may somehow
 * accidentally or in unforeseen circumstances not be nulled/deleted. All nulled objects will then be garbage collected
 * naturally.
 */
public interface Disposable
{
	/**
	 * Dispose of the object and its components.
	 */
	public void Dispose();
}
