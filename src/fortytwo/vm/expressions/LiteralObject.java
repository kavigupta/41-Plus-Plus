package fortytwo.vm.expressions;

import java.util.List;

import lib.standard.collections.Pair;
import fortytwo.compiler.language.identifier.VariableIdentifier;
import fortytwo.vm.constructions.GenericStructure;

public class LiteralObject extends LiteralExpression {
	public final GenericStructure struct;
	public final List<Pair<VariableIdentifier, LiteralExpression>> fields;
	public LiteralObject(GenericStructure struct,
			List<Pair<VariableIdentifier, LiteralExpression>> fields) {
		this.struct = struct;
		this.fields = fields;
	}
}
