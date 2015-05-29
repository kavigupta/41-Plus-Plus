package fortytwo.compiler.parser;

import java.util.ArrayList;
import java.util.List;

import fortytwo.compiler.Token;
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
import fortytwo.vm.errors.CompilerErrors;
import fortytwo.vm.errors.ParserErrors;
import fortytwo.vm.errors.TypingErrors;

public class StatementParser {
	public static Sentence parseStatement(List<Token> line) {
		line = new ArrayList<>(line);
		line.remove(line.size() - 1);
		switch (line.get(0).token) {
			case Resources.RUN:
				line.remove(0);
				ParsedExpression e = ExpressionParser.parseExpression(line);
				if (e.type() == SentenceType.PURE_EXPRESSION)
					ParserErrors.runPureExpression(line);
				return e;
			case Resources.DEFINE:
				return parseDefinition(line);
			case Resources.SET:
				if (line.get(1).token.equals(Resources.THE)
						&& line.get(3).token.equals(Resources.OF)
						&& line.get(5).token.equals(Resources.TO))
					return parseAssignment(line);
				else break;
			case Resources.EXIT:
				return parseReturn(line);
		}
		return parseVoidFunctionCall(line);
	}
	private static FunctionReturn parseReturn(List<Token> line) {
		/* Exit the function( and output <output>)?. */
		if (!line.get(1).token.equals(Resources.THE)
				|| !line.get(2).token.equals(Resources.DECL_FUNCTION))
			ParserErrors.invalidFunctionExit(line);
		if (line.size() == 3) return new FunctionReturn(null);
		if (!line.get(3).token.equals(Resources.AND)
				|| !line.get(4).token.equals(Resources.OUTPUT))
			ParserErrors.invalidFunctionExit(line);
		line.subList(0, 5).clear();
		return new FunctionReturn(ExpressionParser.parseExpression(line));
	}
	private static ParsedStatement parseVoidFunctionCall(List<Token> list) {
		ParsedFunctionCall function = ConstructionParser
				.composeFunction(list);
		if (function.name.function.size() != 1
				|| !(function.name.function.get(0) instanceof FunctionArgument))
			return function;
		ParsedExpression exp = function.arguments.get(0);
		if (exp instanceof ParsedFunctionCall) return exp;
		CompilerErrors.expectedStatementButReceivedExpression(list);
		// should never get here
		return null;
	}
	private static ParsedAssignment parseAssignment(List<Token> line) {
		/*
		 * Set the <field> of <name> to <value>.
		 */
		if (!line.get(1).token.equals(Resources.THE)
				|| !line.get(3).token.equals(Resources.OF)
				|| !line.get(5).token.equals(Resources.TO))
			ParserErrors.invalidAssignment(line);
		Token fieldT = line.get(2);
		VariableIdentifier name = VariableIdentifier.getInstance(line.get(4));
		line.subList(0, 6).clear();
		ParsedExpression expr = ExpressionParser.parseExpression(line);
		return fieldT.token.equals(Resources.VALUE) ? new ParsedRedefinition(
				name, expr) : new ParsedFieldAssignment(name,
				VariableIdentifier.getInstance(fieldT), expr);
	}
	private static Sentence parseDefinition(List<Token> line) {
		/*
		 * Define a[n] <type> called <name>( with a <field1> of <value1>, a
		 * <field2> of <value2>, ...)?.
		 */
		if (!Language.isArticle(line.get(1).token)
				|| !line.get(3).token.equals(Resources.CALLED))
			ParserErrors.invalidDefinition(line);
		Token type = Language.deparenthesize(line.get(2));
		if (type.token.equals(Resources.DECL_FUNCTION))
			return ConstructionParser.parseFunctionDefinition(line);
		if (type.token.equals(Resources.TYPE))
			return ConstructionParser.parseStructDefinition(line);
		Token name = line.get(4);
		ParsedVariableRoster fields = new ParsedVariableRoster();
		for (int i = 5; i < line.size(); i++) {
			if (!line.get(i).token.equals(Resources.OF)) continue;
			Token fieldT = line.get(i - 1);
			ArrayList<Token> tokens2 = new ArrayList<>();
			i++;
			while (i < line.size()
					&& !Language.isListElement(line.get(i).token)) {
				tokens2.add(line.get(i));
				i++;
			}
			fields.add(VariableIdentifier.getInstance(fieldT),
					ExpressionParser.parseExpression(tokens2));
		}
		GenericType genericType = ExpressionParser.parseType(type);
		if (!(genericType instanceof ConcreteType))
			TypingErrors.nonConcreteTypeInVariableDeclaration(line,
					genericType);
		return new ParsedDefinition(new Field(
				VariableIdentifier.getInstance(name),
				(ConcreteType) genericType), fields);
	}
}
