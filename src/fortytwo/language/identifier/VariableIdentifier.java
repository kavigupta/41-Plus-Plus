package fortytwo.language.identifier;

import fortytwo.compiler.Context;
import fortytwo.compiler.Token;
import fortytwo.compiler.parsed.expressions.ParsedExpression;
import fortytwo.language.Language;
import fortytwo.language.Resources;
import fortytwo.language.type.ConcreteType;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.environment.StaticEnvironment;
import fortytwo.vm.errors.CriticalErrors;
import fortytwo.vm.errors.ParserErrors;
import fortytwo.vm.expressions.Expression;
import fortytwo.vm.expressions.LiteralExpression;

public class VariableIdentifier implements ParsedExpression, Expression {
	public static final VariableIdentifier VALUE = new VariableIdentifier(
			new Token(Resources.VALUE, Context.synthetic()));
	// LOWPRI quick and dirty solution. Fix later
	private StaticEnvironment env;
	public final Token name;
	public static VariableIdentifier getInstance(Token token) {
		if (token.token.equals(Resources.VALUE)) return VALUE;
		if (!Language.isValidVariableIdentifier(token.token))
			ParserErrors.invalidVariableIdenitifer(token);
		return new VariableIdentifier(token);
	}
	private VariableIdentifier(Token name) {
		this.name = name;
	}
	@Override
	public VariableIdentifier contextualize(StaticEnvironment env) {
		this.env = env;
		return this;
	}
	@Override
	public SentenceType type() {
		return SentenceType.PURE_EXPRESSION;
	}
	@Override
	public void execute(LocalEnvironment environment) {
		// no-op
	}
	@Override
	public LiteralExpression literalValue(LocalEnvironment environment) {
		return environment.referenceTo(this);
	}
	@Override
	public ConcreteType resolveType() {
		if (env == null)
			CriticalErrors.uncompiledVariableBeingResolved(this);
		return env.typeOf(this);
	}
	@Override
	public boolean typeCheck() {
		// a variable's type automatically works
		return true;
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
