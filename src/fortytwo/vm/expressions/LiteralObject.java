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
}
