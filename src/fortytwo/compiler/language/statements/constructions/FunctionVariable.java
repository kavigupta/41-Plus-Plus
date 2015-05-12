package fortytwo.compiler.language.statements.constructions;

import fortytwo.compiler.language.identifier.VariableIdentifier;

public class FunctionVariable implements FunctionComponent {
	public final VariableIdentifier value;
	public FunctionVariable(VariableIdentifier value) {
		this.value = value;
	}
}
