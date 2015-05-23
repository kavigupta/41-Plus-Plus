package fortytwo.language.identifier;

import fortytwo.compiler.parsed.expressions.ParsedExpression;
import fortytwo.language.Language;
import fortytwo.language.Resources;
import fortytwo.language.type.ConcreteType;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.environment.StaticEnvironment;
import fortytwo.vm.expressions.Expression;
import fortytwo.vm.expressions.LiteralExpression;

public class VariableIdentifier implements ParsedExpression, Expression {
	public static final VariableIdentifier VALUE = new VariableIdentifier(
			Resources.VALUE);
	// LOWPRI quick and dirty solution. Fix later
	private StaticEnvironment env;
	public final String name;
	public static VariableIdentifier getInstance(String name) {
		if (name.equals(Resources.VALUE)) return VALUE;
		if (!Language.isValidVariableIdentifier(name))
			throw new RuntimeException(/* LOWPRI-E */);
		return new VariableIdentifier(name);
	}
	private VariableIdentifier(String name) {
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
		if (env == null) throw new RuntimeException(/* LOWPRI-E */);
		return env.typeOf(this);
	}
	@Override
	public boolean typeCheck() {
		// a variable's type automatically works
		return true;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		VariableIdentifier other = (VariableIdentifier) obj;
		if (name == null) {
			if (other.name != null) return false;
		} else if (!name.equals(other.name)) return false;
		return true;
	}
	@Override
	public String toString() {
		return name;
	}
	@Override
	public String toSourceCode() {
		return name;
	}
}
