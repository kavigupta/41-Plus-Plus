package fortytwo.compiler.parsed;

import fortytwo.compiler.LiteralToken;

/**
 * An interface representing objects that have been tokenized but not parsed
 */
public interface GenericToken extends Contextualized {
	/**
	 * @return the source code used to create this object
	 */
	public String toSourceCode();
	/**
	 * @return a token representing this element's context and source code
	 */
	public default LiteralToken toToken() {
		return LiteralToken.literalVersionOf(this);
	}
}
