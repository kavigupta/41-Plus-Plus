package fortytwo.compiler.parsed.statements;

import fortytwo.language.classification.SentenceType;

public abstract class ParsedAssignment implements ParsedStatement {
	@Override
	public final SentenceType type() {
		return SentenceType.ASSIGNMENT;
	}
}
