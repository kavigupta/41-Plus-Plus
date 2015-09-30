package fortytwo.vm.environment;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import fortytwo.compiler.parsed.Sentence;
import fortytwo.compiler.parsed.declaration.FunctionConstruct;
import fortytwo.compiler.parsed.declaration.StructureDefinition;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.vm.environment.type.AbstractTypeEnvironment;
import fortytwo.vm.environment.type.TopLevelTypeEnvironment;
import fortytwo.vm.errors.DNEErrors;
import fortytwo.vm.errors.ParserErrors;
import fortytwo.vm.expressions.LiteralExpression;
import fortytwo.vm.expressions.LiteralFunction;

public class UnorderedEnvironment {
	public final AbstractTypeEnvironment typeEnv;
	private final VariableRoster<LiteralExpression> globalVariables;
	public final FunctionRoster funcs;
	public UnorderedEnvironment(AbstractTypeEnvironment staticEnv,
			VariableRoster<LiteralExpression> globalVariables,
			FunctionRoster funcs) {
		this.typeEnv = staticEnv;
		this.globalVariables = globalVariables;
		this.funcs = funcs;
	}
	public static UnorderedEnvironment getDefaultEnvironment(
			AbstractTypeEnvironment staticEnv) {
		return new UnorderedEnvironment(staticEnv, new VariableRoster<>(),
				FunctionRoster.getDefault());
	}
	public static UnorderedEnvironment interpret(List<Sentence> sentences) {
		final AbstractTypeEnvironment environment = new TopLevelTypeEnvironment();
		final UnorderedEnvironment global = UnorderedEnvironment
				.getDefaultEnvironment(environment);
		final HashMap<VariableIdentifier, LiteralFunction> functions = new HashMap<>();
		for (int i = 0; i < sentences.size(); i++) {
			final Sentence s = sentences.get(i);
			switch (s.kind()) {
				case FUNCTION:
					((FunctionConstruct) s).register(environment, functions);
					break;
				case DECLARATION_STRUCT:
					((StructureDefinition) s).register(environment, functions);
					break;
				default:
					ParserErrors.expectedDeclarationOrDefinition(s);
			}
		}
		final HashMap<VariableIdentifier, LiteralFunction> implFunctions = new HashMap<>();
		for (final Entry<VariableIdentifier, LiteralFunction> func : functions
				.entrySet()) {
			func.getValue().isTypeChecked(environment);
			final LiteralFunction impl = func.getValue()
					.contextualize(environment);
			implFunctions.put(func.getKey(), impl);
			environment.putReference(func.getKey().identifier().get(),
					func.getValue().type);
		}
		global.funcs.functions.putAll(implFunctions);
		return global;
	}
	public LiteralExpression referenceTo(VariableIdentifier name) {
		final LiteralExpression expr = globalVariables.referenceTo(name);
		if (expr != null) return expr;
		// TODO add container logic
		// if (!container.isPresent())
		DNEErrors.variableDNE(name);
		return null;
		// return container.get().referenceTo(name);
	}
	public void addGlobalVariable(VariableIdentifier name,
			LiteralExpression express) {
		this.globalVariables.assign(name, express);
		typeEnv.addType(name, express.resolveType());
	}
	public OrderedEnvironment minimalLocalEnvironment() {
		return new OrderedEnvironment(this);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (funcs == null ? 0 : funcs.hashCode());
		result = prime * result + (typeEnv == null ? 0 : typeEnv.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		final UnorderedEnvironment other = (UnorderedEnvironment) obj;
		if (funcs == null) {
			if (other.funcs != null) return false;
		} else if (!funcs.equals(other.funcs)) return false;
		if (typeEnv == null) {
			if (other.typeEnv != null) return false;
		} else if (!typeEnv.equals(other.typeEnv)) return false;
		return true;
	}
}
