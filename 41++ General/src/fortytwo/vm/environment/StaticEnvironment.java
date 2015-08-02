package fortytwo.vm.environment;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import fortytwo.compiler.parsed.declaration.FunctionDefinition;
import fortytwo.language.identifier.FunctionName;
import fortytwo.language.identifier.FunctionSignature;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.ConcreteType;
import fortytwo.library.standard.StdLib42;
import fortytwo.vm.constructions.Function42;
import fortytwo.vm.constructions.VariableRoster;
import fortytwo.vm.errors.DNEErrors;
import fortytwo.vm.expressions.LiteralExpression;

public class StaticEnvironment {
	private final StaticEnvironment container;
	public final StructureRoster structs;
	private final FunctionSignatureRoster funcs;
	private final VariableRoster<LiteralExpression> globalVariables;
	private final VariableTypeRoster types;
	public static StaticEnvironment getDefault() {
		StructureRoster structs = StdLib42.DEF_STRUCT;
		FunctionSignatureRoster funcs = new FunctionSignatureRoster();
		StdLib42.defaultSignatures(funcs);
		VariableRoster<LiteralExpression> globalVariables = new VariableRoster<>();
		VariableTypeRoster types = new VariableTypeRoster();
		return new StaticEnvironment(structs, funcs, globalVariables, types);
	}
	public static StaticEnvironment getChild(StaticEnvironment environment) {
		return new StaticEnvironment(environment);
	}
	private StaticEnvironment(StaticEnvironment env) {
		this.structs = env.structs;
		this.funcs = new FunctionSignatureRoster();
		this.globalVariables = new VariableRoster<>();
		this.types = new VariableTypeRoster();
		this.container = env;
	}
	private StaticEnvironment(StructureRoster structureRoster,
			FunctionSignatureRoster sigRost,
			VariableRoster<LiteralExpression> global,
			VariableTypeRoster types) {
		this.structs = structureRoster;
		this.funcs = sigRost;
		this.globalVariables = global;
		this.types = types;
		this.container = null;
	}
	public void addGlobalVariable(VariableIdentifier name,
			LiteralExpression express) {
		this.globalVariables.assign(name, express);
		this.addType(name, express.resolveType());
	}
	public void addType(VariableIdentifier variableIdentifier,
			ConcreteType concreteType) {
		types.add(variableIdentifier, concreteType);
	}
	public ConcreteType typeOf(VariableIdentifier name) {
		ConcreteType type = types.typeOf(name);
		if (type != null) return type;
		if (container == null) DNEErrors.variableDNE(name);
		return container.typeOf(name);
	}
	public FunctionSignature referenceTo(FunctionName name,
			List<ConcreteType> types) {
		Pair<Function42, ConcreteType> func = StdLib42
				.matchCompiledFieldAccess(this, name, types);
		if (func != null) return func.getKey().signature();
		FunctionSignature sig = funcs.referenceTo(name, types);
		if (sig != null) return sig;
		if (container == null) DNEErrors.functionSignatureDNE(name, types);
		return container.referenceTo(name, types);
	}
	public void putReference(FunctionDefinition f) {
		funcs.putReference(f);
	}
	public LiteralExpression referenceTo(VariableIdentifier name) {
		LiteralExpression expr = globalVariables.referenceTo(name);
		if (expr != null) return expr;
		if (container == null) DNEErrors.variableDNE(name);
		return container.referenceTo(name);
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
