package fortytwo.language.field;

import fortytwo.compiler.Context;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.ConcreteType;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.expressions.Expression;
import fortytwo.vm.expressions.LiteralExpression;

public class Field implements Expression {
	public final VariableIdentifier name;
	public final ConcreteType type;
	public Field(VariableIdentifier name, ConcreteType type) {
		this.name = name;
		this.type = type;
	}
	@Override
	public void execute(LocalEnvironment environment) {
		// no-op
	}
	@Override
	public void clean(LocalEnvironment environment) {
		environment.vars.deregister(name);
	}
	@Override
	public boolean typeCheck() {
		// has been typechecked on creation
		return true;
	}
	@Override
	public LiteralExpression literalValue(LocalEnvironment env) {
		return env.referenceTo(this.name);
	}
	@Override
	public ConcreteType resolveType() {
		return type;
	}
	@Override
	public Context context() {
		return name.context();
	}
	@Override
	public String toSourceCode() {
		return name.toSourceCode();
	}
	@Override
	public boolean isSimple() {
		return true;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Field other = (Field) obj;
		if (name == null) {
			if (other.name != null) return false;
		} else if (!name.equals(other.name)) return false;
		if (type == null) {
			if (other.type != null) return false;
		} else if (!type.equals(other.type)) return false;
		return true;
	}
}
