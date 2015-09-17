package fortytwo.vm.environment;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import fortytwo.compiler.parsed.Sentence;
import fortytwo.compiler.parsed.declaration.FunctionConstruct;
import fortytwo.compiler.parsed.declaration.StructureDefinition;
import fortytwo.compiler.parsed.expressions.Expression;
import fortytwo.compiler.parsed.statements.ParsedDefinition;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.vm.errors.ParserErrors;
import fortytwo.vm.expressions.LiteralExpression;
import fortytwo.vm.expressions.LiteralFunction;

public class UnorderedEnvironment {
	public final TypeEnvironment staticEnv;
	public final FunctionRoster funcs;
	public UnorderedEnvironment(TypeEnvironment staticEnv,
			FunctionRoster funcs) {
		this.staticEnv = staticEnv;
		this.funcs = funcs;
	}
	public static UnorderedEnvironment getDefaultEnvironment(
			TypeEnvironment staticEnv) {
		return new UnorderedEnvironment(staticEnv, FunctionRoster.getDefault());
	}
	public static UnorderedEnvironment interpret(List<Sentence> sentences) {
		final TypeEnvironment environment = TypeEnvironment.getDefault();
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
				case DEFINITION:
					final ParsedDefinition def = (ParsedDefinition) s;
					final VariableRoster<? extends Expression> fieldValues = new VariableRoster<>();
					for (final Entry<VariableIdentifier, ? extends Expression> pair : def.fields.pairs
							.entrySet()) {
						final LiteralExpression applied = pair.getValue()
								.literalValue(new OrderedEnvironment(global));
						fieldValues.assign(pair.getKey(), applied);
					}
					environment.addGlobalVariable(def.toCreate.name,
							environment.structs.instance(def.toCreate,
									fieldValues.literalValue(
											global.minimalLocalEnvironment()),
									def.toCreate.name.context()));
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
	public OrderedEnvironment minimalLocalEnvironment() {
		return new OrderedEnvironment(this);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (funcs == null ? 0 : funcs.hashCode());
		result = prime * result
				+ (staticEnv == null ? 0 : staticEnv.hashCode());
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
		if (staticEnv == null) {
			if (other.staticEnv != null) return false;
		} else if (!staticEnv.equals(other.staticEnv)) return false;
		return true;
	}
}
