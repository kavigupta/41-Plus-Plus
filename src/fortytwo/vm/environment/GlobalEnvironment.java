package fortytwo.vm.environment;

import java.util.ArrayList;
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
import fortytwo.language.identifier.VariableIdentifier;
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
		// TODO Default environment with default structures and whatnot
		return null;
	}
	public void initializeEnvironment(ArrayList<Sentence> sentences) {
		LocalEnvironment minimal = minimalLocalEnvironment();
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
					VariableRoster fieldValues = new VariableRoster();
					for (Entry<VariableIdentifier, ParsedExpression> pair : def.fields
							.entryIterator()) {
						LiteralExpression applied = pair.getValue()
								.contextualize(minimal)
								.literalValue(minimal);
						if (applied == null)
							throw new RuntimeException(/* LOWPRI-E */);
						fieldValues.assign(pair.getKey(), applied);
					}
					vars.assign(def.name.name,
							structs.instance(def.name.type, fieldValues));
					break;
				default:
					throw new RuntimeException(/* LOWPRI-E */);
			}
		}
		// TODO finsih this
	}
	public LocalEnvironment minimalLocalEnvironment() {
		return new LocalEnvironment(this);
	}
}
