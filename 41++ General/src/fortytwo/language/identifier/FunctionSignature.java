package fortytwo.language.identifier;

import java.util.List;

import fortytwo.compiler.Context;
import fortytwo.compiler.parsed.GenericToken;
import fortytwo.language.type.ConcreteType;
import fortytwo.language.type.FunctionType;

public class FunctionSignature implements GenericToken {
	public final FunctionName name;
	public final FunctionType type;
	public FunctionSignature(FunctionName name, FunctionType typeSignature) {
		this.name = name;
		this.type = typeSignature;
	}
	public boolean matches(FunctionName name, List<ConcreteType> inputs) {
		if (!this.name.equals(name)) return false;
		return type.accepts(inputs);
	}
	public VariableIdentifier identifier() {
		return VariableIdentifier.getInstance(this.toToken(), true);
	}
	@Override
	public String toSourceCode() {
		String name = this.name.identifier().toSourceCode();
		return '"' + name.substring(1, name.length() - 1) + ":"
				+ type.toSourceCode() + '"';
	}
	@Override
	public Context context() {
		return name.context();
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
		FunctionSignature other = (FunctionSignature) obj;
		if (name == null) {
			if (other.name != null) return false;
		} else if (!name.equals(other.name)) return false;
		if (type == null) {
			if (other.type != null) return false;
		} else if (!type.equals(other.type)) return false;
		return true;
	}
	@Override
	public String toString() {
		return "FunctionSignature [name=" + name + ", typeSignature=" + type
				+ "]";
	}
}
