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
import fortytwo.compiler.parsed.statements.ParsedDefinition;
import fortytwo.compiler.parsed.statements.ParsedStatement;
import fortytwo.language.classification.SentenceType;
import fortytwo.language.identifier.FunctionSignature;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.vm.constructions.Function42;
import fortytwo.vm.constructions.FunctionImplemented;
import fortytwo.vm.errors.ParserErrors;
import fortytwo.vm.expressions.Expression;
import fortytwo.vm.expressions.LiteralExpression;
import fortytwo.vm.statements.Statement;

public class GlobalEnvironment {
	public final StaticEnvironment staticEnv;
	public final FunctionRoster funcs;
	public GlobalEnvironment(StaticEnvironment staticEnv, FunctionRoster funcs) {
		this.staticEnv = staticEnv;
		this.funcs = funcs;
	}
	public static GlobalEnvironment getDefaultEnvironment(
			StaticEnvironment staticEnv) {
		return new GlobalEnvironment(staticEnv,
				FunctionRoster.getDefault(staticEnv));
	}
	public static GlobalEnvironment interpret(List<Sentence> sentences) {
		StaticEnvironment environment = StaticEnvironment.getDefault();
		GlobalEnvironment global = GlobalEnvironment
				.getDefaultEnvironment(environment);
		LocalEnvironment local = global.minimalLocalEnvironment();
		ArrayList<ParsedFunction> functions = new ArrayList<>();
		for (int i = 0; i < sentences.size(); i++) {
			Sentence s = sentences.get(i);
			switch (s.type()) {
				case DECLARATION_FUNCT:
					FunctionDefinition f = (FunctionDefinition) s;
					environment.putReference(f);
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
							ParserErrors.expectedStatement(sC);
						body.add((ParsedStatement) sC);
					}
					if (r == null) ParserErrors.noExit(f);
					functions.add(new ParsedFunction(f, body, r));
					break;
				case DECLARATION_STRUCT:
					environment.structs
							.addStructure(((StructureDeclaration) s).structure);
					break;
				case DEFINITION:
					ParsedDefinition def = (ParsedDefinition) s;
					VariableRoster<Expression> fieldValues = new VariableRoster<Expression>();
					for (Entry<VariableIdentifier, ParsedExpression> pair : def.fields
							.entryIterator()) {
						LiteralExpression applied = pair
								.getValue()
								.contextualize(environment)
								.literalValue(
										new LocalEnvironment(global));
						fieldValues.assign(pair.getKey(), applied);
					}
					environment.addGlobalVariable(def.name.name,
							environment.structs.instance(def.name,
									fieldValues.literalValue(local),
									def.name.name.context()));
					break;
				default:
					ParserErrors.expectedDeclarationOrDefinition(s);
			}
		}
		HashMap<FunctionSignature, Function42> implFunctions = new HashMap<>();
		for (ParsedFunction func : functions) {
			FunctionImplemented impl = func.contextualize(environment);
			impl.body.forEach(Statement::typeCheck);
			FunctionDefinition f = func.definition();
			implFunctions.put(FunctionSignature.getInstance(f.name,
					f.parameterTypes, f.outputType), impl);
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
		result = prime * result + ((funcs == null) ? 0 : funcs.hashCode());
		result = prime * result
				+ ((staticEnv == null) ? 0 : staticEnv.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		GlobalEnvironment other = (GlobalEnvironment) obj;
		if (funcs == null) {
			if (other.funcs != null) return false;
		} else if (!funcs.equals(other.funcs)) return false;
		if (staticEnv == null) {
			if (other.staticEnv != null) return false;
		} else if (!staticEnv.equals(other.staticEnv)) return false;
		return true;
	}
}
