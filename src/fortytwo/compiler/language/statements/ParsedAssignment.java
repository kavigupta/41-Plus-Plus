package fortytwo.compiler.language.statements;

public abstract class ParsedAssignment implements ParsedStatement {
	@Override
	public final SentenceType type() {
		return SentenceType.ASSIGNMENT;
	}
}
