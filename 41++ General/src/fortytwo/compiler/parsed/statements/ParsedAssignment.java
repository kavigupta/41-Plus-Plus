package fortytwo.compiler.parsed.statements;

import fortytwo.compiler.Context;
import fortytwo.language.classification.SentenceType;

/**
 * Represents the root node for both field assignments and value reassignments.
 */
public abstract class ParsedAssignment extends ParsedStatement {
	public ParsedAssignment(Context context) {
		super(context);
	}
	@Override
	public final SentenceType kind() {
		return SentenceType.ASSIGNMENT;
	}
}
