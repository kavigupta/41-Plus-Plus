package fortytwo.compiler.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import fortytwo.compiler.Context;
import fortytwo.compiler.LiteralToken;
import fortytwo.compiler.parsed.Sentence;
import fortytwo.compiler.parsed.declaration.FunctionOutput;
import fortytwo.compiler.parsed.expressions.Expression;
import fortytwo.compiler.parsed.statements.Definition;
import fortytwo.compiler.parsed.statements.FunctionCall;
import fortytwo.compiler.parsed.statements.Redefinition;
import fortytwo.compiler.parsed.statements.Statement;
import fortytwo.language.Language;
import fortytwo.language.Resources;
import fortytwo.language.classification.SentenceType;
import fortytwo.language.field.TypedVariable;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.ConcreteType;
import fortytwo.language.type.GenericType;
import fortytwo.vm.environment.VariableRoster;
import fortytwo.vm.errors.ParserErrors;
import fortytwo.vm.errors.SyntaxErrors;
import fortytwo.vm.expressions.LiteralVoid;

public class StatementParser {
	public static Sentence parseStatement(List<LiteralToken> line) {
		line = new ArrayList<>(line);
		line.remove(line.size() - 1);
		switch (line.get(0).token) {
			case Resources.RUN:
				line.remove(0);
				final Expression e = ExpressionParser.parseExpression(line);
				if (e.kind() == SentenceType.PURE_EXPRESSION)
					ParserErrors.expectedFunctionCall(e);
				return e;
			case Resources.DEFINE:
				return parseDefinition(line);
			case Resources.SET:
				if (line.get(1).token.equals(Resources.THE)
						&& line.get(2).token.equals(Resources.VALUE)
						&& line.get(3).token.equals(Resources.OF)
						&& line.get(5).token.equals(Resources.TO))
					return parseAssignment(line);
				break;
			case Resources.EXIT:
				return parseReturn(line);
		}
		return parseVoidFunctionCall(line);
	}
	private static FunctionOutput parseReturn(List<LiteralToken> line) {
		final Context fullContext = Context.sum(line);
		/* Exit the function( and output <output>)?. */
		if (!line.get(1).token.equals(Resources.THE)
				|| !line.get(2).token.equals(Resources.DECL_FUNCTION))
			SyntaxErrors.invalidSentence(SentenceType.FUNCTION_OUTPUT, line);
		if (line.size() == 3)
			return new FunctionOutput(LiteralVoid.INSTANCE, Context.sum(line));
		if (!line.get(3).token.equals(Resources.AND)
				|| !line.get(4).token.equals(Resources.OUTPUT))
			SyntaxErrors.invalidSentence(SentenceType.FUNCTION_OUTPUT, line);
		line.subList(0, 5).clear();
		return new FunctionOutput(ExpressionParser.parseExpression(line),
				fullContext);
	}
	private static Statement parseVoidFunctionCall(List<LiteralToken> list) {
		final FunctionCall function = ConstructionParser.composeFunction(list);
		if (!function.getSingleExpression().isPresent()) return function;
		ParserErrors.expectedFunctionCall(function);
		// should never get here
		throw new IllegalStateException();
	}
	private static Redefinition parseAssignment(List<LiteralToken> line) {
		final Context fullContext = Context.sum(line);
		/*
		 * Set the <field> of <name> to <value>.
		 */
		final Expression toModify = ExpressionParser
				.parseExpression(Arrays.asList(line.get(4)));
		line.subList(0, 6).clear();
		final Expression expr = ExpressionParser.parseExpression(line);
		Optional<VariableIdentifier> name = toModify.identifier();
		if (!name.isPresent()) {
			// TODO handle redefinition of a non-variable
		}
		return new Redefinition(name.get(), expr, fullContext);
	}
	private static Sentence parseDefinition(List<LiteralToken> line) {
		final Context fullContext = Context.sum(line);
		/*
		 * Define a[n] <type> called <name>( with a <field1> of <value1>, a
		 * <field2> of <value2>, ...)?.
		 */
		if (line.size() < 5 || !Language.isArticle(line.get(1).token)
				|| !line.get(3).token.equals(Resources.CALLED))
			SyntaxErrors.invalidSentence(SentenceType.DEFINITION, line);
		final LiteralToken type = Language.deparenthesize(line.get(2));
		if (type.token.equals(Resources.DECL_FUNCTION))
			return ConstructionParser.parseFunctionDefinition(line);
		if (type.token.equals(Resources.TYPE))
			return ConstructionParser.parseStructDefinition(line);
		final LiteralToken name = line.get(4);
		final VariableRoster<Expression> fields = new VariableRoster<>();
		for (int i = 5; i < line.size(); i++) {
			if (!line.get(i).token.equals(Resources.OF)) continue;
			final LiteralToken fieldT = line.get(i - 1);
			final ArrayList<LiteralToken> tokens2 = new ArrayList<>();
			i++;
			while (i < line.size()
					&& !Language.isListElement(line.get(i).token)) {
				tokens2.add(line.get(i));
				i++;
			}
			fields.assign(VariableIdentifier.getInstance(fieldT, false),
					ExpressionParser.parseExpression(tokens2));
		}
		final GenericType genericType = ExpressionParser.parseType(type);
		if (!genericType.isConcrete())
			ParserErrors.expectedCTInDefinition(genericType);
		return new Definition(
				new TypedVariable(VariableIdentifier.getInstance(name, false),
						(ConcreteType) genericType),
				fields, fullContext);
	}
}
