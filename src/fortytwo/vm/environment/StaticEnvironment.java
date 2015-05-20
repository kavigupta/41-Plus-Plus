package fortytwo.vm.environment;

import fortytwo.language.field.Field;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.library.standard.StdLib42;
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
	public static StaticEnvironment getDefault() {
		StructureRoster structs = StdLib42.DEF_STRUCT;
		FunctionSignatureRoster funcs = new FunctionSignatureRoster();
		StdLib42.defaultSignatures(funcs);
		LiteralVariableRoster globalVariables = new LiteralVariableRoster();
		VariableTypeRoster types = new VariableTypeRoster();
		return new StaticEnvironment(structs, funcs, globalVariables, types);
	}
	private StaticEnvironment(StructureRoster structureRoster,
			FunctionSignatureRoster sigRost, LiteralVariableRoster global,
			VariableTypeRoster types) {
		this.structs = structureRoster;
		this.funcs = sigRost;
		this.globalVariables = global;
		this.types = types;
	}
	@Override
	public StaticEnvironment clone() {
		return new StaticEnvironment(structs.clone(), funcs.clone(),
				globalVariables.clone(), types.clone());
	}
}
