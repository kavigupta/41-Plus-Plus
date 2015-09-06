package fortytwo.vm.expressions;

import java.util.Optional;

import fortytwo.compiler.Context;
import fortytwo.compiler.parsed.expressions.Expression;
import fortytwo.language.classification.SentenceType;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.ConcreteType;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.environment.StaticEnvironment;

public abstract class LiteralExpression extends Expression {
	public LiteralExpression(Context context) {
		super(context);
	}
	@Override
	public ConcreteType findType(StaticEnvironment env) {
		return resolveType();
	}
	@Override
	public final LiteralExpression literalValue(LocalEnvironment env) {
		return this;
	}
	@Override
	public Optional<VariableIdentifier> identifier() {
		return Optional.empty();
	}
	@Override
	public SentenceType kind() {
		return SentenceType.PURE_EXPRESSION;
	}
	public abstract ConcreteType resolveType();
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj.getClass() != this.getClass()) return false;
		return typedEquals((LiteralExpression) obj);
	}
	public abstract boolean typedEquals(LiteralExpression other);
}
