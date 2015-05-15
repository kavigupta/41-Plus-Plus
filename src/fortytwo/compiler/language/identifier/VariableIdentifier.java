package fortytwo.compiler.language.identifier;

import fortytwo.compiler.language.Language;
import fortytwo.compiler.language.expressions.ParsedExpression;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.expressions.Expression;

public class VariableIdentifier implements ParsedExpression {
	public final String name;
	public static VariableIdentifier getInstance(String name) {
		if (!Language.isValidVariableIdentifier(name))
			throw new RuntimeException(/* LOWPRI-E */);
		return new VariableIdentifier(name);
	}
	private VariableIdentifier(String name) {
		this.name = name;
	}
	@Override
	public Expression contextualize(LocalEnvironment env) {
		return env.referenceTo(this);
	}
	@Override
	public SentenceType type() {
		return SentenceType.PURE_EXPRESSION;
	}
}
