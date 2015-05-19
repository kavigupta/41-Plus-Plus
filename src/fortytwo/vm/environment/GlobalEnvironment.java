package fortytwo.vm.environment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import fortytwo.compiler.parsed.constructions.ParsedFunction;
import fortytwo.compiler.parsed.declaration.FunctionDefinition;
import fortytwo.compiler.parsed.declaration.FunctionReturn;
import fortytwo.compiler.parsed.declaration.StructureDeclaration;
import fortytwo.compiler.parsed.expressions.ParsedExpression;
import fortytwo.compiler.parsed.sentences.Sentence;
import fortytwo.compiler.parsed.sentences.Sentence.SentenceType;
import fortytwo.compiler.parsed.statements.ParsedDefinition;
import fortytwo.compiler.parsed.statements.ParsedStatement;
import fortytwo.language.identifier.FunctionSignature;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.vm.constructions.Function42;
import fortytwo.vm.constructions.FunctionImplemented;
import fortytwo.vm.expressions.LiteralExpression;
import fortytwo.vm.statements.Statement;

public class GlobalEnvironment {
	public final StaticEnvironment staticEnv;
	public final FunctionRoster funcs;
	private GlobalEnvironment(StaticEnvironment staticEnv, FunctionRoster funcs) {
		this.staticEnv = staticEnv;
		this.funcs = funcs;
	}
	public static GlobalEnvironment getDefaultEnvironment(
			StaticEnvironment environment) {
		// TODO add functions ???
		return null;
	}
	public static GlobalEnvironment initialized(List<Sentence> sentences) {
		StaticEnvironment environment = new StaticEnvironment(
				new StructureRoster(), new FunctionSignatureRoster(),
				new LiteralVariableRoster(), new VariableTypeRoster());
		GlobalEnvironment global = GlobalEnvironment
				.getDefaultEnvironment(environment);
		LocalEnvironment local = global.minimalLocalEnvironment();
		ArrayList<ParsedFunction> functions = new ArrayList<>();
		for (int i = 0; i < sentences.size(); i++) {
			Sentence s = sentences.get(i);
			switch (s.type()) {
				case DECLARATION_FUNCT:
					FunctionDefinition f = (FunctionDefinition) s;
					environment.funcs.putReference(f);
					FunctionReturn r = null;
					i++;
					ArrayList<ParsedStatement> body = new ArrayList<>();
					for (; i < sentences.size(); i++) {
						if (sentences.get(i).type() == SentenceType.FUNCTION_RETURN) {
							r = (FunctionReturn) sentences.get(i);
							break;
						}
						Sentence sC = sentences.get(i);
						if (!(sC instanceof ParsedStatement))
							throw new RuntimeException(/* LOWPRI-E */);
						body.add((ParsedStatement) sC);
					}
					if (r == null)
						throw new RuntimeException(/* LOWPRI-E */);
					functions.add(new ParsedFunction(f, body, r));
					break;
				case DECLARATION_STRUCT:
					environment.structs
							.addStructure(((StructureDeclaration) s).structure);
					break;
				case DEFINITION:
					ParsedDefinition def = (ParsedDefinition) s;
					VariableRoster fieldValues = new VariableRoster();
					for (Entry<VariableIdentifier, ParsedExpression> pair : def.fields
							.entryIterator()) {
						LiteralExpression applied = pair
								.getValue()
								.contextualize(environment)
								.literalValue(
										new LocalEnvironment(global));
						if (applied == null)
							throw new RuntimeException(/* LOWPRI-E */);
						fieldValues.assign(pair.getKey(), applied);
					}
					environment.addGlobalVariable(def.name.name,
							environment.structs.instance(def.name.type,
									fieldValues.literalValue(local)));
					break;
				default:
					throw new RuntimeException(/* LOWPRI-E */);
			}
		}
		HashMap<FunctionSignature, Function42> implFunctions = new HashMap<>();
		for (ParsedFunction func : functions) {
			FunctionImplemented impl = func.contextualize(environment);
			impl.body.forEach(Statement::typeCheck);
			implFunctions.put(environment.funcs.referenceTo(func.f.name,
					func.f.parameterTypes), impl);
		}
		global.funcs.functions.putAll(implFunctions);
		return global;
	}
	public LocalEnvironment minimalLocalEnvironment() {
		return new LocalEnvironment(this);
	}
}
