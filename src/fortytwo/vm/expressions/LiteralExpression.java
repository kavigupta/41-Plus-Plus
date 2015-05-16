package fortytwo.vm.expressions;

import fortytwo.compiler.language.expressions.ParsedExpression;
import fortytwo.compiler.language.identifier.TypeIdentifier;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.environment.VariableTypeRoster;

public abstract class LiteralExpression implements ParsedExpression, Expression {
	@Override
	public final Expression contextualize(LocalEnvironment env) {
		return this;
	}
	@Override
	public void execute(LocalEnvironment environment) {
		// No - op by default
	}
	@Override
	public final LiteralExpression literalValue(LocalEnvironment env) {
		return this;
	}
	@Override
	public SentenceType type() {
		return SentenceType.PURE_EXPRESSION;
	}
	@Override
	public final boolean typeCheck(VariableTypeRoster typeRoster) {
		return true;
	}
	@Override
	public final TypeIdentifier resolveType(VariableTypeRoster typeRoster) {
		return resolveType();
	}
	public abstract TypeIdentifier resolveType();
}
