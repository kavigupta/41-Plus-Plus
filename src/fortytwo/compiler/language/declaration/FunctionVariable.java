package fortytwo.compiler.language.declaration;

import fortytwo.compiler.language.identifier.VariableIdentifier;

public class FunctionVariable implements FunctionComponent {
	public final VariableIdentifier value;
	public FunctionVariable(VariableIdentifier value) {
		this.value = value;
	}
}
