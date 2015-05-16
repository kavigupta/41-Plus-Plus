package fortytwo.vm.expressions;

import fortytwo.compiler.language.identifier.TypeIdentifier;
import fortytwo.vm.constructions.Structure;
import fortytwo.vm.environment.VariableRoster;

public class LiteralObject extends LiteralExpression {
	public final Structure struct;
	public final VariableRoster fields;
	public LiteralObject(Structure struct, VariableRoster fields) {
		this.struct = struct;
		this.fields = fields;
	}
	@Override
	public TypeIdentifier resolveType() {
		return struct.getType();
	}
}
