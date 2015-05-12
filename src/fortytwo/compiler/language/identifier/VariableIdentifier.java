package fortytwo.compiler.language.identifier;

public class VariableIdentifier {
	public final String name;
	public static VariableIdentifier getInstance(String name) {
		return new VariableIdentifier(name);
	}
	private VariableIdentifier(String name) {
		this.name = name;
	}
}
