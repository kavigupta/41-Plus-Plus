package fortytwo.compiler.parser;

import java.util.ArrayList;
import java.util.List;

import fortytwo.compiler.parsed.constructions.ParsedVariableRoster;
import fortytwo.compiler.parsed.declaration.FunctionReturn;
import fortytwo.compiler.parsed.expressions.ParsedExpression;
import fortytwo.compiler.parsed.sentences.Sentence;
import fortytwo.compiler.parsed.sentences.Sentence.SentenceType;
import fortytwo.compiler.parsed.statements.*;
import fortytwo.language.Language;
import fortytwo.language.field.Field;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.identifier.functioncomponent.FunctionArgument;
import fortytwo.language.type.ConcreteType;
import fortytwo.language.type.GenericType;

public class StatementParser {
	public static ParsedStatement parseCompleteStatement(
			List<List<String>> currentPhrases) {
		ParsedExpression condition;
		switch (currentPhrases.get(0).get(0)) {
			case "While":
				currentPhrases.get(0).remove(0);
				condition = ExpressionParser.parseExpression(currentPhrases
						.get(0));
				List<ParsedStatement> statements = new ArrayList<>();
				for (int i = 1; i < currentPhrases.size(); i++) {
					Sentence s = parseStatement(currentPhrases.get(i));
					if (!(s instanceof ParsedStatement))
						throw new RuntimeException(/*
											 * LOWPRI-E Declarations
											 * are not allowed in
											 * while statements.
											 */);
					statements.add((ParsedStatement) s);
				}
				return new ParsedWhileLoop(condition,
						new ParsedStatementSeries(statements));
			case "If":
				currentPhrases.get(0).remove(0);
				condition = ExpressionParser.parseExpression(currentPhrases
						.get(0));
				List<ParsedStatement> ifso = new ArrayList<>();
				int i = 1;
				for (; i < currentPhrases.size()
						&& !currentPhrases.get(i).get(0)
								.equals("otherwise"); i++) {
					Sentence s = parseStatement(currentPhrases.get(i));
					if (!(s instanceof ParsedStatement))
						throw new RuntimeException(/*
											 * LOWPRI-E Declarations
											 * are not allowed in if
											 * statements.
											 */);
					ifso.add((ParsedStatement) s);
				}
				List<ParsedStatement> ifelse = new ArrayList<>();
				for (i++; i < currentPhrases.size(); i++) {
					Sentence s = parseStatement(currentPhrases.get(i));
					if (!(s instanceof ParsedStatement))
						throw new RuntimeException(/*
											 * LOWPRI-E Declarations
											 * are not allowed in if
											 * statements.
											 */);
					ifelse.add((ParsedStatement) s);
				}
				return ParsedIfElse.getInstance(condition,
						new ParsedStatementSeries(ifso),
						new ParsedStatementSeries(ifelse));
		}
		List<ParsedStatement> statements = new ArrayList<>();
		for (int i = 0; i < currentPhrases.size(); i++) {
			Sentence s = parseStatement(currentPhrases.get(i));
			if (!(s instanceof ParsedStatement))
				throw new RuntimeException(/*
									 * LOWPRI-E Declarations
									 * are not allowed in if
									 * statements.
									 */);
			statements.add((ParsedStatement) s);
		}
		return new ParsedStatementSeries(statements);
	}
	private static Sentence parseStatement(List<String> line) {
		switch (line.get(0)) {
			case "Run":
				line.remove(0);
				ParsedExpression e = ExpressionParser.parseExpression(line);
				if (e.type() == SentenceType.PURE_EXPRESSION)
					throw new RuntimeException(/* LOWPRI-E */);
				return e;
			case "Define":
				return parseDefinition(line);
			case "Set":
				return parseAssignment(line);
			case "Exit":
				return parseReturn(line);
			default:
				return parseVoidFunctionCall(line);
		}
	}
	private static FunctionReturn parseReturn(List<String> line) {
		/* Exit the function( and output <output>)?. */
		if (!line.get(1).equals("the") || !line.get(2).equals("function"))
			throw new RuntimeException(/* LOWPRI-E */);
		if (line.size() == 3) return new FunctionReturn(null);
		if (!line.get(3).equals("and") || !line.get(4).equals("output"))
			throw new RuntimeException(/* LOWPRI-E */);
		line.subList(0, 5).clear();
		return new FunctionReturn(ExpressionParser.parseExpression(line));
	}
	private static ParsedStatement parseVoidFunctionCall(List<String> list) {
		ParsedFunctionCall function = ConstructionParser
				.composeFunction(list);
		if (function.name.function.size() == 1
				&& function.name.function.get(0) instanceof FunctionArgument)
			throw new RuntimeException(/* LOWPRI-E non-void function call */);
		return function;
	}
	private static ParsedAssignment parseAssignment(List<String> line) {
		/*
		 * Set the <field> of <name> to <value>.
		 */
		if (!line.get(1).equals("the") || !line.get(3).equals("of")
				|| !line.get(5).equals("to"))
			throw new RuntimeException(/* LOWPRI-E */);
		String field = line.get(2);
		ParsedExpression expr = ExpressionParser.parseExpression(line);
		VariableIdentifier name = VariableIdentifier.getInstance(line.get(4));
		line.subList(0, 6).clear();
		return field.equals("value") ? new ParsedRedefinition(name, expr)
				: new ParsedFieldAssignment(name,
						VariableIdentifier.getInstance(field), expr);
	}
	private static Sentence parseDefinition(List<String> line) {
		/*
		 * Define a[n] <type> called <name>( with a <field1> of <value1>, a
		 * <field2> of <value2>, ...)?.
		 */
		if (!Language.isArticle(line.get(1)) || line.get(3).equals("called"))
			throw new RuntimeException(/* LOWPRI-E */);
		String type = Language.deparenthesize(line.get(2));
		if (type.equals("function"))
			return ConstructionParser.parseFunctionDefinition(line);
		if (type.equals("type"))
			return ConstructionParser.parseStructDefinition(line);
		String name = line.get(4);
		ParsedVariableRoster fields = new ParsedVariableRoster();
		for (int i = 5; i < line.size(); i++) {
			if (!line.get(i).equals("of")) continue;
			String field = line.get(i - 1);
			ArrayList<String> tokens = new ArrayList<>();
			i++;
			while (!line.get(i).equals(",")) {
				tokens.add(line.get(i));
				i++;
			}
			fields.add(VariableIdentifier.getInstance(field),
					ExpressionParser.parseExpression(tokens));
		}
		GenericType genericType = ExpressionParser.parseType(type);
		if (!(genericType instanceof ConcreteType))
			throw new RuntimeException(/* LOWPRI-E */);
		return new ParsedDefinition(new Field(
				VariableIdentifier.getInstance(name),
				(ConcreteType) genericType), fields);
	}
}
