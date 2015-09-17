package fortytwo.compiler.parsed;

/**
 * An interface representing something that has been parsed from source code
 */
public interface ParsedConstruct extends GenericToken {
	/**
	 * @return whether this element needs to be indented to be part of a block.
	 */
	public boolean isSimple();
}
