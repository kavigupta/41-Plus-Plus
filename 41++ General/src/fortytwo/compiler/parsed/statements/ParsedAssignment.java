package fortytwo.compiler.parsed.statements;

import fortytwo.language.classification.SentenceType;

/**
 * Represents the root node for both field assignments and value reassignments.
 */
public abstract class ParsedAssignment extends ParsedStatement {
	@Override
	public final SentenceType kind() {
		return SentenceType.ASSIGNMENT;
	}
}
