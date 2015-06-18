package fortytwo.compiler.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lib.standard.collections.Pair;
import fortytwo.compiler.Context;
import fortytwo.compiler.LiteralToken;
import fortytwo.compiler.parsed.declaration.FunctionDefinition;
import fortytwo.compiler.parsed.declaration.StructureDefinition;
import fortytwo.compiler.parsed.expressions.ParsedExpression;
import fortytwo.compiler.parsed.statements.ParsedFunctionCall;
import fortytwo.language.Language;
import fortytwo.language.Resources;
import fortytwo.language.classification.SentenceType;
import fortytwo.language.field.GenericField;
import fortytwo.language.identifier.FunctionName;
import fortytwo.language.identifier.FunctionSignature;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.identifier.functioncomponent.FunctionArgument;
import fortytwo.language.identifier.functioncomponent.FunctionComponent;
import fortytwo.language.identifier.functioncomponent.FunctionToken;
import fortytwo.language.type.*;
import fortytwo.library.standard.StdLib42;
import fortytwo.vm.constructions.GenericStructureSignature;
import fortytwo.vm.errors.ParserErrors;
import fortytwo.vm.errors.SyntaxErrors;
import fortytwo.vm.errors.TypingErrors;

public class ConstructionParser {
	public static ParsedFunctionCall composeFunction(List<LiteralToken> list) {
		Pair<FunctionName, List<ParsedExpression>> fsig = parseFunctionSignature(list);
		return ParsedFunctionCall.getInstance(fsig.key, fsig.value);
	}
	public static StructureDefinition parseStructDefinition(
			List<LiteralToken> line) {
		Context context = Context.sum(line);
		/*
		 * Define a type called <structure> of <typevar1>, <typevar2>,
		 * and <typevar3> that contains a[n] <type> called <field> , a[n]
		 * <type> called <field>, ...
		 */
		if (!line.get(1).doesEqual(Resources.A)
				|| !line.get(3).doesEqual(Resources.CALLED))
			SyntaxErrors.invalidSentence(SentenceType.DECLARATION_STRUCT,
					line);
		line.subList(0, 4).clear();
		ArrayList<LiteralToken> structExpression = new ArrayList<>();
		int i = 0;
		for (; i < line.size() && !line.get(i).doesEqual(Resources.THAT)
				&& !line.get(i).doesEqual(Resources.OF); i++) {
			structExpression.add(line.get(i));
		}
		List<GenericType> typeVariables = new ArrayList<>();
		if (i < line.size() && line.get(i).doesEqual(Resources.OF)) {
			i++;
			for (; i < line.size() && !line.get(i).doesEqual(Resources.THAT); i++) {
				if (Language.isValidVariableIdentifier(line.get(i))) {
					typeVariables.add(new TypeVariable(VariableIdentifier
							.getInstance(line.get(i))));
				}
			}
		}
		ArrayList<GenericField> fields = new ArrayList<>();
		for (; i < line.size(); i++) {
			if (!line.get(i).doesEqual(Resources.CALLED)) continue;
			fields.add(new GenericField(VariableIdentifier.getInstance(line
					.get(i + 1)), ExpressionParser.parseType(line
					.get(i - 1))));
		}
		return new StructureDefinition(new GenericStructureSignature(
				new GenericStructureType(structExpression, typeVariables,
						context), fields), Context.sum(line));
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
			SyntaxErrors.invalidSentence(SentenceType.DECLARATION_FUNCT,
					line);
		int i = 4;
		ArrayList<LiteralToken> funcExpress = new ArrayList<>();
		for (; i < line.size() && !line.get(i).doesEqual(Resources.THAT); i++) {
			funcExpress.add(line.get(i));
		}
		i++;
		Map<VariableIdentifier, GenericType> vars = new HashMap<>();
		if (i < line.size()) {
			if (line.get(i).doesEqual(Resources.TAKES)) {
				for (; i < line.size(); i++) {
					if (!line.get(i).doesEqual(Resources.CALLED))
						continue;
					GenericType type = ExpressionParser.parseType(line
							.get(i - 1));
					// LOWPRI allow generic typing in functions...
					// later
					if (type.kind() != Kind.CONCRETE)
						ParserErrors.expectedCTInFunctionDecl(type, line,
								vars.size());
					vars.put(VariableIdentifier.getInstance(line
							.get(i + 1)), type);
				}
			} else if (line.get(i).doesEqual(Resources.OUTPUTS)) {} else SyntaxErrors
					.invalidSentence(SentenceType.DECLARATION_FUNCT, line);
		}
		int outputloc = Language.indexOf(line, Resources.OUTPUTS);
		Pair<FunctionName, List<ParsedExpression>> sig = parseFunctionSignature(funcExpress);
		List<VariableIdentifier> variables = sig.value
				.stream()
				.map(x -> {
					if (!(x instanceof VariableIdentifier))
						ParserErrors.expectedVariableInDecl(true,
								x.toToken(), line);
					return (VariableIdentifier) x;
				}).collect(Collectors.toList());
		List<GenericType> types = new ArrayList<>();
		for (VariableIdentifier vid : variables) {
			GenericType gt = vars.get(vid);
			if (gt == null)
				TypingErrors.incompleteFieldTypingInFunctionDecl(vid, line);
			types.add(gt);
		}
		if (outputloc < 0)
			return new FunctionDefinition(FunctionSignature.getInstance(
					sig.key, types,
					new PrimitiveType(PrimitiveTypeWithoutContext.VOID,
							line.get(line.size() - 1).context)),
					variables, Context.sum(line));
		if (Language.isArticle(line.get(outputloc + 1).token)) outputloc++;
		line.subList(0, outputloc + 1).clear();
		GenericType outputType = ExpressionParser.parseType(LiteralToken
				.parenthesize(line));
		// Check will be unecessary later LOWPRI
		if (outputType.kind() != Kind.CONCRETE)
			ParserErrors.expectedCTInFunctionDecl(outputType, line, -1);
		return new FunctionDefinition(FunctionSignature.getInstance(sig.key,
				types, outputType), variables, Context.sum(line));
	}
	private static Pair<FunctionName, List<ParsedExpression>> parseFunctionSignature(
			List<LiteralToken> list) {
		List<FunctionComponent> function = new ArrayList<>();
		List<LiteralToken> currentExpression = new ArrayList<>();
		List<ParsedExpression> arguments = new ArrayList<>();
		for (LiteralToken tok : list) {
			if (Language.isExpression(tok.token)) {
				currentExpression.add(tok);
			} else if (Language.isFunctionToken(tok.token)) {
				if (currentExpression.size() != 0) {
					ParsedExpression argument = ExpressionParser
							.parsePureExpression(currentExpression);
					arguments.add(argument);
					function.add(FunctionArgument.INSTANCE);
					currentExpression.clear();
				}
				function.add(new FunctionToken(tok));
			} else break;
		}
		if (currentExpression.size() != 0) {
			ParsedExpression argument = ExpressionParser
					.parsePureExpression(currentExpression);
			arguments.add(argument);
			function.add(FunctionArgument.INSTANCE);
			currentExpression.clear();
		}
		return StdLib42.parseFunction(function, arguments);
	}
}
