package fortytwo.language.identifier;

import java.util.Arrays;

import fortytwo.compiler.Context;
import fortytwo.compiler.LiteralToken;
import fortytwo.compiler.parsed.expressions.Expression;
import fortytwo.language.Language;
import fortytwo.language.Resources;
import fortytwo.language.classification.ExpressionType;
import fortytwo.language.classification.SentenceType;
import fortytwo.language.type.ConcreteType;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.environment.StaticEnvironment;
import fortytwo.vm.errors.SyntaxErrors;
import fortytwo.vm.expressions.LiteralExpression;

public class VariableIdentifier extends Expression {
	public static final VariableIdentifier VALUE = new VariableIdentifier(
			LiteralToken.synthetic(Resources.VALUE));
	public final LiteralToken name;
	public static VariableIdentifier getInstance(LiteralToken token) {
		if (token.token.equals(Resources.VALUE)) return VALUE;
		if (!Language.isValidVariableIdentifier(token))
			SyntaxErrors.invalidExpression(ExpressionType.VARIABLE,
					Arrays.asList(token));
		return new VariableIdentifier(token);
	}
	private VariableIdentifier(LiteralToken name) {
		this.name = name;
	}
	@Override
	public ConcreteType findType(StaticEnvironment env) {
		return env.typeOf(this);
	}
	@Override
	public LiteralExpression literalValue(LocalEnvironment env) {
		return env.referenceTo(this);
	}
	@Override
	public SentenceType kind() {
		return SentenceType.PURE_EXPRESSION;
	}
	@Override
	public Context context() {
		return name.context;
	}
	@Override
	public int hashCode() {
		return name.token.hashCode();
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		VariableIdentifier other = (VariableIdentifier) obj;
		return this.name.token.equals(other.name.token);
	}
	@Override
	public String toString() {
		return name.token;
	}
	@Override
	public String toSourceCode() {
		return name.token;
	}
}
