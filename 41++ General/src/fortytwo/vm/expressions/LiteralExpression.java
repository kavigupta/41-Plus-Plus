package fortytwo.vm.expressions;

import fortytwo.compiler.Context;
import fortytwo.compiler.parsed.expressions.Expression;
import fortytwo.language.classification.SentenceType;
import fortytwo.language.type.ConcreteType;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.environment.StaticEnvironment;

public abstract class LiteralExpression extends Expression {
	protected Context context;
	public LiteralExpression(Context context) {
		this.context = context;
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
	public SentenceType kind() {
		return SentenceType.PURE_EXPRESSION;
	}
	@Override
	public final Context context() {
		return context;
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
