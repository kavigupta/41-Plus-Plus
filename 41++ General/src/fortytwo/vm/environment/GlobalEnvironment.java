package fortytwo.vm.environment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import fortytwo.compiler.parsed.Sentence;
import fortytwo.compiler.parsed.declaration.FunctionConstruct;
import fortytwo.compiler.parsed.declaration.FunctionDefinition;
import fortytwo.compiler.parsed.declaration.StructureDefinition;
import fortytwo.compiler.parsed.expressions.Expression;
import fortytwo.compiler.parsed.statements.ParsedDefinition;
import fortytwo.compiler.parser.Parser;
import fortytwo.language.identifier.FunctionSignature;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.vm.constructions.Function42;
import fortytwo.vm.constructions.FunctionImplemented;
import fortytwo.vm.constructions.VariableRoster;
import fortytwo.vm.errors.ParserErrors;
import fortytwo.vm.expressions.LiteralExpression;

public class GlobalEnvironment {
	public final StaticEnvironment staticEnv;
	public final FunctionRoster funcs;
	public GlobalEnvironment(StaticEnvironment staticEnv,
			FunctionRoster funcs) {
		this.staticEnv = staticEnv;
		this.funcs = funcs;
	}
	public static GlobalEnvironment getDefaultEnvironment(
			StaticEnvironment staticEnv) {
		return new GlobalEnvironment(staticEnv,
				FunctionRoster.getDefault(staticEnv));
	}
	public static GlobalEnvironment interpret(List<Sentence> sentences) {
		final StaticEnvironment environment = StaticEnvironment.getDefault();
		final GlobalEnvironment global = GlobalEnvironment
				.getDefaultEnvironment(environment);
		final ArrayList<FunctionImplemented> functions = new ArrayList<>();
		for (int i = 0; i < sentences.size(); i++) {
			final Sentence s = sentences.get(i);
			switch (s.kind()) {
				case FUNCTION:
					final FunctionConstruct f = (FunctionConstruct) s;
					environment.putReference(f.declaration);
					// TODO remove once multiple exit is defined
					functions.add(new FunctionImplemented(f.declaration,
							Parser.temporaryHack(f.suite).statements));
					break;
				case DECLARATION_STRUCT:
					environment.structs
							.addStructure(((StructureDefinition) s).structure);
					break;
				case DEFINITION:
					final ParsedDefinition def = (ParsedDefinition) s;
					final VariableRoster<? extends Expression> fieldValues = new VariableRoster<>();
					for (final Entry<VariableIdentifier, ? extends Expression> pair : def.fields.pairs) {
						final LiteralExpression applied = pair.getValue()
								.literalValue(new LocalEnvironment(global));
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
		final HashMap<FunctionSignature, Function42> implFunctions = new HashMap<>();
		for (final FunctionImplemented func : functions) {
			func.typeCheck(environment);
			final FunctionImplemented impl = func.contextualize(environment);
			final FunctionDefinition f = func.definition();
			implFunctions.put(f.sig, impl);
		}
		global.funcs.functions.putAll(implFunctions);
		return global;
	}
	public LocalEnvironment minimalLocalEnvironment() {
		return new LocalEnvironment(this);
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
		final GlobalEnvironment other = (GlobalEnvironment) obj;
		if (funcs == null) {
			if (other.funcs != null) return false;
		} else if (!funcs.equals(other.funcs)) return false;
		if (staticEnv == null) {
			if (other.staticEnv != null) return false;
		} else if (!staticEnv.equals(other.staticEnv)) return false;
		return true;
	}
}
