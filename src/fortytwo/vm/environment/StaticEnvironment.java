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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((funcs == null) ? 0 : funcs.hashCode());
		result = prime
				* result
				+ ((globalVariables == null) ? 0 : globalVariables
						.hashCode());
		result = prime * result
				+ ((structs == null) ? 0 : structs.hashCode());
		result = prime * result + ((types == null) ? 0 : types.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		StaticEnvironment other = (StaticEnvironment) obj;
		if (funcs == null) {
			if (other.funcs != null) return false;
		} else if (!funcs.equals(other.funcs)) return false;
		if (globalVariables == null) {
			if (other.globalVariables != null) return false;
		} else if (!globalVariables.equals(other.globalVariables))
			return false;
		if (structs == null) {
			if (other.structs != null) return false;
		} else if (!structs.equals(other.structs)) return false;
		if (types == null) {
			if (other.types != null) return false;
		} else if (!types.equals(other.types)) return false;
		return true;
	}
}
