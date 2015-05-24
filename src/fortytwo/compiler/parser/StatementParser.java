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
import fortytwo.language.Resources;
import fortytwo.language.field.Field;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.identifier.functioncomponent.FunctionArgument;
import fortytwo.language.type.ConcreteType;
import fortytwo.language.type.GenericType;
import fortytwo.vm.errors.CompilerError;

public class StatementParser {
	public static Sentence parseStatement(List<String> line) {
		line = new ArrayList<>(line);
		line.remove(line.size() - 1);
		switch (line.get(0)) {
			case Resources.RUN:
				line.remove(0);
				ParsedExpression e = ExpressionParser.parseExpression(line);
				if (e.type() == SentenceType.PURE_EXPRESSION)
					throw new RuntimeException(/* LOWPRI-E */);
				return e;
			case Resources.DEFINE:
				return parseDefinition(line);
			case Resources.SET:
				if (line.get(1).equals(Resources.THE)
						&& line.get(3).equals(Resources.OF)
						&& line.get(5).equals(Resources.TO))
					return parseAssignment(line);
				else break;
			case Resources.EXIT:
				return parseReturn(line);
		}
		return parseVoidFunctionCall(line);
	}
	private static FunctionReturn parseReturn(List<String> line) {
		/* Exit the function( and output <output>)?. */
		if (!line.get(1).equals(Resources.THE)
				|| !line.get(2).equals(Resources.DECL_FUNCTION))
			throw new RuntimeException(/* LOWPRI-E */);
		if (line.size() == 3) return new FunctionReturn(null);
		if (!line.get(3).equals(Resources.AND)
				|| !line.get(4).equals(Resources.OUTPUT))
			throw new RuntimeException(/* LOWPRI-E */);
		line.subList(0, 5).clear();
		return new FunctionReturn(ExpressionParser.parseExpression(line));
	}
	private static ParsedStatement parseVoidFunctionCall(List<String> list) {
		ParsedFunctionCall function = ConstructionParser
				.composeFunction(list);
		if (function.name.function.size() == 1
				&& function.name.function.get(0) instanceof FunctionArgument)
			CompilerError.expectedStatementButReceivedExpression(list, null);// TODO
																// FIX
		return function;
	}
	private static ParsedAssignment parseAssignment(List<String> line) {
		/*
		 * Set the <field> of <name> to <value>.
		 */
		if (!line.get(1).equals(Resources.THE)
				|| !line.get(3).equals(Resources.OF)
				|| !line.get(5).equals(Resources.TO))
			throw new RuntimeException(/* LOWPRI-E */);
		String field = line.get(2);
		VariableIdentifier name = VariableIdentifier.getInstance(line.get(4));
		line.subList(0, 6).clear();
		ParsedExpression expr = ExpressionParser.parseExpression(line);
		return field.equals(Resources.VALUE) ? new ParsedRedefinition(name,
				expr) : new ParsedFieldAssignment(name,
				VariableIdentifier.getInstance(field), expr);
	}
	private static Sentence parseDefinition(List<String> line) {
		/*
		 * Define a[n] <type> called <name>( with a <field1> of <value1>, a
		 * <field2> of <value2>, ...)?.
		 */
		if (!Language.isArticle(line.get(1))
				|| !line.get(3).equals(Resources.CALLED))
			throw new RuntimeException(/* LOWPRI-E */);
		String type = Language.deparenthesize(line.get(2));
		if (type.equals(Resources.DECL_FUNCTION))
			return ConstructionParser.parseFunctionDefinition(line);
		if (type.equals(Resources.TYPE))
			return ConstructionParser.parseStructDefinition(line);
		String name = line.get(4);
		ParsedVariableRoster fields = new ParsedVariableRoster();
		for (int i = 5; i < line.size(); i++) {
			if (!line.get(i).equals(Resources.OF)) continue;
			String field = line.get(i - 1);
			ArrayList<String> tokens = new ArrayList<>();
			i++;
			while (i < line.size() && !Language.isListElement(line.get(i))) {
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
