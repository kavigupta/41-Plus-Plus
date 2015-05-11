package fortytwo.compiler.language;


public class Noop implements Statement {
	public static final Statement INSTANCE = new Noop();
	private Noop() {}
}
