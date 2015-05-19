package fortytwo.vm.environment;

import java.util.ArrayList;
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
import fortytwo.language.field.Field;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.vm.expressions.LiteralExpression;

public class StaticEnvironment {
	public final StructureRoster structs;
	public final FunctionSignatureRoster funcs;
	public final LiteralVariableRoster globalVariables;
	public final VariableTypeRoster types;
	public StaticEnvironment getInstance(List<Sentence> sentences) {
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
						LiteralExpression applied = pair
								.getValue()
								.contextualize(environment)
								.literalValue(
										new LocalEnvironment(global));
						if (applied == null)
							throw new RuntimeException(/* LOWPRI-E */);
						fieldValues.assign(pair.getKey(), applied);
					}
					environment.addGlobalVariable(
							def.name.name,
							structs.instance(def.name.type,
									fieldValues.literalValue(local)));
					break;
				default:
					throw new RuntimeException(/* LOWPRI-E */);
			}
		}
		// TODO finsih this
		return environment;
	}
	public void addGlobalVariable(VariableIdentifier name,
			LiteralExpression express) {
		this.globalVariables.assign(name, express);
		this.types.add(new Field(name, express.resolveType()));
		// TODO Auto-generated method stub
	}
	public StaticEnvironment(StructureRoster structureRoster,
			FunctionSignatureRoster sigRost, LiteralVariableRoster global,
			VariableTypeRoster types) {
		super();
		this.structs = structureRoster;
		this.funcs = sigRost;
		this.globalVariables = global;
		this.types = types;
	}
}
