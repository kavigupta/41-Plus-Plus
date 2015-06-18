package fortytwo.compiler.parsed;

import fortytwo.compiler.Context;

/**
 * An interface representing something that has a context associated with it.
 */
public interface Contextualized {
	/**
	 * @return the context of this element
	 */
	public Context context();
}
