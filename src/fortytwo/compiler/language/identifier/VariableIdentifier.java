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
}
