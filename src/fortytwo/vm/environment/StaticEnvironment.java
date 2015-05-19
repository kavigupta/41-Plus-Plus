package fortytwo.vm.environment;

import fortytwo.language.field.Field;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.vm.expressions.LiteralExpression;

public class StaticEnvironment {
	public final StructureRoster structs;
	public final FunctionSignatureRoster funcs;
	public final LiteralVariableRoster globalVariables;
	public final VariableTypeRoster types;
	public void addGlobalVariable(VariableIdentifier name,
			LiteralExpression express) {
		this.globalVariables.assign(name, express);
		this.types.add(new Field(name, express.resolveType()));
	}
	public StaticEnvironment(StructureRoster structureRoster,
			FunctionSignatureRoster sigRost, LiteralVariableRoster global,
			VariableTypeRoster types) {
		super();
		this.structs = structureRoster;
		this.funcs = sigRost;
		this.globalVariables = global;
		this.types = types;
	}
}
