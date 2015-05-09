package fortytwo.compiler.struct;

public class Assignment implements Statement42 {
	public final String name, field, value;
	public static Statement42 getInstance(String variableName, String field,
			String value) {
		return new Assignment(variableName, field, value);
	}
	private Assignment(String variableName, String field, String value) {
		this.name = variableName;
		this.field = field;
		this.value = value;
	}
	@Override
	public String toString() {
		return "Set the " + field + " of " + name + " to " + value;
	}
}
