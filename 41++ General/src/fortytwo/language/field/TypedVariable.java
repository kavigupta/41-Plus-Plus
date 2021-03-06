package fortytwo.language.field;

import java.util.Optional;

import fortytwo.compiler.parsed.expressions.Expression;
import fortytwo.language.classification.SentenceType;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.ConcreteType;
import fortytwo.vm.environment.OrderedEnvironment;
import fortytwo.vm.environment.type.AbstractTypeEnvironment;
import fortytwo.vm.expressions.LiteralExpression;

public class TypedVariable extends Expression {
	public final VariableIdentifier name;
	public final ConcreteType type;
	public TypedVariable(VariableIdentifier name, ConcreteType type) {
		super(name.context());
		this.name = name;
		this.type = type;
	}
	@Override
	public LiteralExpression literalValue(OrderedEnvironment env) {
		return env.referenceTo(this.name);
	}
	@Override
	public ConcreteType findType(AbstractTypeEnvironment env) {
		return type;
	}
	@Override
	public SentenceType kind() {
		return SentenceType.PURE_EXPRESSION;
	}
	@Override
	public String toSourceCode() {
		return name.toSourceCode();
	}
	@Override
	public Optional<VariableIdentifier> identifier() {
		return Optional.of(name);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (name == null ? 0 : name.hashCode());
		result = prime * result + (type == null ? 0 : type.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		final TypedVariable other = (TypedVariable) obj;
		if (name == null) {
			if (other.name != null) return false;
		} else if (!name.equals(other.name)) return false;
		if (type == null) {
			if (other.type != null) return false;
		} else if (!type.equals(other.type)) return false;
		return true;
	}
}
