package fortytwo.compiler.language.statements;

public class Noop implements ParsedStatement {
	public static final ParsedStatement INSTANCE = new Noop();
	private Noop() {}
}
