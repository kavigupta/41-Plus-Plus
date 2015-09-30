package fortytwo.compiler.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fortytwo.compiler.Context;
import fortytwo.compiler.LiteralToken;
import fortytwo.compiler.parsed.declaration.FunctionDefinition;
import fortytwo.compiler.parsed.declaration.StructureDefinition;
import fortytwo.compiler.parsed.expressions.Expression;
import fortytwo.compiler.parsed.statements.FunctionCall;
import fortytwo.language.Language;
import fortytwo.language.Resources;
import fortytwo.language.classification.ExpressionType;
import fortytwo.language.classification.SentenceType;
import fortytwo.language.field.GenericField;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.identifier.functioncomponent.FunctionArgument;
import fortytwo.language.identifier.functioncomponent.FunctionComponent;
import fortytwo.language.identifier.functioncomponent.FunctionToken;
import fortytwo.language.type.*;
import fortytwo.library.standard.StdLib42;
import fortytwo.vm.constructions.GenericStructureSignature;
import fortytwo.vm.errors.ParserErrors;
import fortytwo.vm.errors.SyntaxErrors;

/**
 * Contains utility functions that parse functions and structures.
 */
public class ConstructionParser {
	/**
	 * Represents a function signature parse situation.
	 */
	public static enum FunctionParseSituation {
		/**
		 * In a definition. A single operator counts as a function
		 */
		DEFINITION(true), //
		/**
		 * In a call. Operators do not count as function tokens.
		 */
		CALL(false);
		/**
		 * Whether or not to allow a single operator as a function token
		 */
		public boolean allowOperators;
		private FunctionParseSituation(boolean allowOperators) {
			this.allowOperators = allowOperators;
		}
	}
	/**
	 * Parses the given list of tokens as a type definition.
	 */
	public static StructureDefinition parseStructDefinition(
			List<LiteralToken> line) {
		final Context context = Context.sum(line);
		/*
		 * Define a type called <structure> of <typevar1>, <typevar2>,
		 * and <typevar3> that contains a[n] <type> called <field> , a[n]
		 * <type> called <field>, ...
		 */
		if (!line.get(1).doesEqual(Resources.A)
				|| !line.get(3).doesEqual(Resources.CALLED))
			SyntaxErrors.invalidSentence(SentenceType.DECLARATION_STRUCT, line);
		final ArrayList<LiteralToken> structExpression = new ArrayList<>();
		int i = 4;
		for (; i < line.size() && !line.get(i).doesEqual(Resources.THAT)
				&& !line.get(i).doesEqual(Resources.OF); i++)
			structExpression.add(line.get(i));
		final List<GenericType> typeVariables = new ArrayList<>();
		if (i < line.size() && line.get(i).doesEqual(Resources.OF)) {
			i++;
			for (; i < line.size()
					&& !line.get(i).doesEqual(Resources.THAT); i++)
				if (line.get(i).isValidVariableIdentifier(false))
					typeVariables.add(new TypeVariable(VariableIdentifier
							.getInstance(line.get(i), false)));
		}
		final ArrayList<GenericField> fields = new ArrayList<>();
		for (; i < line.size(); i++) {
			if (!line.get(i).doesEqual(Resources.CALLED)) continue;
			if (i + 1 >= line.size()) SyntaxErrors
					.invalidSentence(SentenceType.DECLARATION_STRUCT, line);
			fields.add(new GenericField(
					VariableIdentifier.getInstance(line.get(i + 1), false),
					ExpressionParser.parseType(line.get(i - 1))));
		}
		return new StructureDefinition(
				new GenericStructureSignature(new GenericStructureType(
						structExpression, typeVariables, context), fields),
				Context.sum(line));
	}
	public static FunctionDefinition parseFunctionDefinition(
			List<LiteralToken> line) {
		/*
		 * Define a function called <function expression> that takes a[n]
		 * <type1> called <field1>, a[n] <type2> called <field2>, and a[n]
		 * <type3> called <field3>( and outputs a <return type>)?.
		 */
		if (!line.get(1).doesEqual(Resources.A)
				|| !line.get(2).doesEqual(Resources.DECL_FUNCTION)
				|| !line.get(3).doesEqual(Resources.CALLED))
			SyntaxErrors.invalidSentence(SentenceType.DECLARATION_FUNCT, line);
		int i = 4;
		final ArrayList<LiteralToken> funcExpress = new ArrayList<>();
		for (; i < line.size() && !line.get(i).doesEqual(Resources.THAT); i++)
			funcExpress.add(line.get(i));
		i++;
		final Map<VariableIdentifier, GenericType> vars = new HashMap<>();
		if (i < line.size()) {
			if (line.get(i).doesEqual(Resources.TAKES))
				for (; i < line.size(); i++) {
					if (!line.get(i).doesEqual(Resources.CALLED)) continue;
					final GenericType type = ExpressionParser
							.parseType(line.get(i - 1));
					// LOWPRI allow generic typing in functions...
					// later
					if (type.kind() != Kind.CONCRETE) ParserErrors
							.expectedCTInFunctionDecl(type, line, vars.size());
					vars.put(VariableIdentifier.getInstance(line.get(i + 1),
							false), type);
				}
			else if (!line.get(i).doesEqual(Resources.OUTPUTS)) SyntaxErrors
					.invalidSentence(SentenceType.DECLARATION_FUNCT, line);
		}
		int outputloc = Language.indexOf(line, Resources.OUTPUTS);
		final FunctionCall call = parseFunctionCall(funcExpress,
				FunctionParseSituation.DEFINITION);
		final GenericType outputType;
		if (outputloc < 0)
			outputType = new PrimitiveType(PrimitiveTypeWOC.VOID,
					line.get(line.size() - 1).context);
		else {
			if (Language.isArticle(line.get(outputloc + 1).token)) outputloc++;
			line.subList(0, outputloc + 1).clear();
			outputType = ExpressionParser
					.parseType(LiteralToken.parenthesize(line));
			// Check will be unecessary later LOWPRI
			if (outputType.kind() != Kind.CONCRETE)
				ParserErrors.expectedCTInFunctionDecl(outputType, line, -1);
		}
		return new FunctionDefinition(
				call.definitionSignature(vars, outputType, line),
				call.getArgumentsAsVariables(), Context.sum(line));
	}
	public static FunctionCall parseFunctionCall(List<LiteralToken> list,
			FunctionParseSituation situations) {
		boolean allowOperators = situations.allowOperators;
		boolean hasFunctionToken = false, hasOperator = false;
		final List<FunctionComponent> function = new ArrayList<>();
		final List<LiteralToken> currentExpression = new ArrayList<>();
		final List<Expression> arguments = new ArrayList<>();
		for (final LiteralToken tok : list) {
			boolean functionNameToken = tok.isFunctionToken();
			if (Language.isOperator(tok.token)) {
				if (allowOperators) {
					functionNameToken = true;
					allowOperators = false;
					hasOperator = true;
				}
			} else if (functionNameToken) {
				hasFunctionToken = true;
			}
			if (functionNameToken) {
				if (currentExpression.size() != 0) {
					final Expression argument = ExpressionParser
							.parsePureExpression(currentExpression);
					arguments.add(argument);
					function.add(FunctionArgument.INSTANCE);
					currentExpression.clear();
				}
				function.add(new FunctionToken(tok));
			} else if (tok.isExpression())
				currentExpression.add(tok);
			else break;
		}
		if (currentExpression.size() != 0) {
			final Expression argument = ExpressionParser
					.parsePureExpression(currentExpression);
			arguments.add(argument);
			function.add(FunctionArgument.INSTANCE);
			currentExpression.clear();
		}
		if (hasFunctionToken && hasOperator) SyntaxErrors
				.invalidExpression(ExpressionType.FUNCTION_SIGNATURE, list);
		return StdLib42.parseFunction(function, arguments);
	}
}
