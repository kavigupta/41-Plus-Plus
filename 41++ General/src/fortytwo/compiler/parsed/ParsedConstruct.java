package fortytwo.compiler.parsed;

/**
 * An interface representing something that has been parsed from source code
 */
public interface ParsedConstruct extends GenericToken {
	/**
	 * @return whether this element needs to be wrapped in verbal braces.
	 */
	public boolean isSimple();
}
