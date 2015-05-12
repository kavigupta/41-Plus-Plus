package fortytwo.compiler.language.statements.constructions;

import fortytwo.compiler.language.expressions.ParsedVariable;

public class FunctionVariable implements FunctionComponent {
	public final ParsedVariable value;
	public FunctionVariable(ParsedVariable value) {
		this.value = value;
	}
}
