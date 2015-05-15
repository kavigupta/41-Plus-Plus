package fortytwo.vm.environment;

import java.util.ArrayList;

import lib.standard.collections.Pair;
import fortytwo.compiler.language.constructions.ParsedFunction;
import fortytwo.compiler.language.declaration.FunctionDefinition;
import fortytwo.compiler.language.declaration.FunctionReturn;
import fortytwo.compiler.language.declaration.StructureDeclaration;
import fortytwo.compiler.language.expressions.ParsedExpression;
import fortytwo.compiler.language.identifier.VariableIdentifier;
import fortytwo.compiler.language.sentences.Sentence;
import fortytwo.compiler.language.sentences.Sentence.SentenceType;
import fortytwo.compiler.language.statements.ParsedDefinition;
import fortytwo.compiler.language.statements.ParsedStatement;
import fortytwo.vm.expressions.LiteralExpression;

public class GlobalEnvironment {
	public final StructureRoster structs;
	public final FunctionRoster funcs;
	public final VariableRoster vars;
	private GlobalEnvironment() {
		structs = new StructureRoster();
		funcs = new FunctionRoster();
		vars = new VariableRoster();
	}
	public static GlobalEnvironment getDefaultEnvironment() {
		// TODO Default environment with default structures and whatnot.
		return null;
	}
	public void initializeEnvironment(ArrayList<Sentence> sentences) {
		LocalEnvironment minimal = LocalEnvironment.minimalEnvironment(this);
		ArrayList<ParsedFunction> functions = new ArrayList<>();
		for (int i = 0; i < sentences.size(); i++) {
			Sentence s = sentences.get(i);
			switch (s.type()) {
				case DECLARATION_FUNCT:
					FunctionDefinition f = (FunctionDefinition) s;
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
					structs.addStructure(((StructureDeclaration) s).structure);
					break;
				case DEFINITION:
					ParsedDefinition def = (ParsedDefinition) s;
					ArrayList<Pair<VariableIdentifier, LiteralExpression>> fieldValues = new ArrayList<>();
					for (Pair<VariableIdentifier, ParsedExpression> pair : def.fields) {
						LiteralExpression applied = pair.value
								.contextualize(minimal).literalValue(
										minimal);
						if (applied == null)
							throw new RuntimeException(/* LOWPRI-E */);
						fieldValues.add(Pair.getInstance(pair.key,
								applied));
					}
					vars.add(def.name,
							minimal.instance(def.type, fieldValues));
					break;
				default:
					throw new RuntimeException(/* LOWPRI-E */);
			}
		}
		// TODO
	}
}
