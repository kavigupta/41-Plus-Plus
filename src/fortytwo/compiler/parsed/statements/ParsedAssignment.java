package fortytwo.compiler.parsed.statements;

public abstract class ParsedAssignment implements ParsedStatement {
	@Override
	public final SentenceType type() {
		return SentenceType.ASSIGNMENT;
	}
}
