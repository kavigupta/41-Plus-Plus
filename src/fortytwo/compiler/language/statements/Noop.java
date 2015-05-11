package fortytwo.compiler.language.statements;


public class Noop implements Statement {
	public static final Statement INSTANCE = new Noop();
	private Noop() {}
}
