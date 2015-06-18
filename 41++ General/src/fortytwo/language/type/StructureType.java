package fortytwo.language.type;

import java.util.List;

import fortytwo.compiler.Context;
import fortytwo.compiler.LiteralToken;
import fortytwo.language.SourceCode;
import fortytwo.vm.expressions.LiteralExpression;

public class StructureType implements ConcreteType {
	public final List<LiteralToken> name;
	public final List<ConcreteType> types;
	private final Context context;
	public StructureType(List<LiteralToken> name, List<ConcreteType> types,
			Context context) {
		this.name = name;
		this.types = types;
		this.context = context;
	}
	@Override
	public Context context() {
		return context;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((types == null) ? 0 : types.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		StructureType other = (StructureType) obj;
		if (name == null) {
			if (other.name != null) return false;
		} else if (!name.equals(other.name)) return false;
		if (types == null) {
			if (other.types != null) return false;
		} else if (!types.equals(other.types)) return false;
		return true;
	}
	@Override
	public String toSourceCode() {
		return SourceCode.display(this);
	}
	@Override
	public LiteralExpression defaultValue() {
		return null;
	}
	@Override
	public String toString() {
		return toSourceCode();
	}
}
