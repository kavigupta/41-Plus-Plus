package fortytwo.vm.expressions;

import fortytwo.language.SourceCode;
import fortytwo.language.type.ConcreteType;
import fortytwo.vm.constructions.Structure;
import fortytwo.vm.environment.LiteralVariableRoster;

public class LiteralObject extends LiteralExpression {
	public final Structure struct;
	public final LiteralVariableRoster fields;
	public LiteralObject(Structure struct, LiteralVariableRoster fields) {
		this.struct = struct;
		this.fields = fields;
	}
	@Override
	public ConcreteType resolveType() {
		return struct.type;
	}
	@Override
	public String toSourceCode() {
		return SourceCode.display(this);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fields == null) ? 0 : fields.hashCode());
		result = prime * result + ((struct == null) ? 0 : struct.hashCode());
		return result;
	}
	@Override
	public boolean typedEquals(LiteralExpression obj) {
		LiteralObject other = (LiteralObject) obj;
		if (fields == null) {
			if (other.fields != null) return false;
		} else if (!fields.equals(other.fields)) return false;
		if (struct == null) {
			if (other.struct != null) return false;
		} else if (!struct.equals(other.struct)) return false;
		return true;
	}
}
