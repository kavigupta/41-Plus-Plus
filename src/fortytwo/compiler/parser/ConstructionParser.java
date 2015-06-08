package fortytwo.compiler.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lib.standard.collections.Pair;
import fortytwo.compiler.Context;
import fortytwo.compiler.Token;
import fortytwo.compiler.parsed.declaration.FunctionDefinition;
import fortytwo.compiler.parsed.declaration.StructureDeclaration;
import fortytwo.compiler.parsed.expressions.UntypedExpression;
import fortytwo.compiler.parsed.statements.ParsedFunctionCall;
import fortytwo.language.Language;
import fortytwo.language.Resources;
import fortytwo.language.classification.SentenceType;
import fortytwo.language.field.GenericField;
import fortytwo.language.identifier.FunctionName;
import fortytwo.language.identifier.VariableID;
import fortytwo.language.identifier.functioncomponent.FunctionArgument;
import fortytwo.language.identifier.functioncomponent.FunctionComponent;
import fortytwo.language.identifier.functioncomponent.FunctionToken;
import fortytwo.language.type.*;
import fortytwo.library.standard.StdLib42;
import fortytwo.vm.constructions.GenericStructure;
import fortytwo.vm.errors.ParserErrors;
import fortytwo.vm.errors.SyntaxErrors;
import fortytwo.vm.errors.TypingErrors;

public class ConstructionParser {
	public static ParsedFunctionCall composeFunction(List<Token> list) {
		Pair<FunctionName, List<UntypedExpression>> fsig = parseFunctionSignature(list);
		return ParsedFunctionCall.getInstance(fsig.key, fsig.value);
	}
	public static StructureDeclaration parseStructDefinition(List<Token> line) {
		Context context = Context.tokenSum(line);
		/*
		 * Define a type called <structure> of <typevar1>, <typevar2>,
		 * and <typevar3> that contains a[n] <type> called <field> , a[n]
		 * <type> called <field>, ...
		 */
		if (!line.get(1).token.equals(Resources.A)
				|| !line.get(3).token.equals(Resources.CALLED))
			SyntaxErrors.invalidSentence(SentenceType.DECLARATION_STRUCT,
					line);
		line.subList(0, 4).clear();
		ArrayList<Token> structExpression = new ArrayList<>();
		int i = 0;
		for (; i < line.size() && !line.get(i).token.equals(Resources.THAT)
				&& !line.get(i).token.equals(Resources.OF); i++) {
			structExpression.add(line.get(i));
		}
		List<GenericType> typeVariables = new ArrayList<>();
		if (i < line.size() && line.get(i).token.equals(Resources.OF)) {
			i++;
			for (; i < line.size()
					&& !line.get(i).token.equals(Resources.THAT); i++) {
				if (Language.isValidVariableIdentifier(line.get(i).token)) {
					typeVariables.add(new TypeVariable(VariableID
							.getInstance(line.get(i))));
				}
			}
		}
		ArrayList<GenericField> fields = new ArrayList<>();
		for (; i < line.size(); i++) {
			if (!line.get(i).token.equals(Resources.CALLED)) continue;
			fields.add(new GenericField(VariableID.getInstance(line
					.get(i + 1)), ExpressionParser.parseType(line
					.get(i - 1))));
		}
		return new StructureDeclaration(new GenericStructure(
				new GenericStructureType(structExpression, typeVariables,
						context), fields), Context.tokenSum(line));
	}
	public static FunctionDefinition parseFunctionDefinition(List<Token> line) {
		/*
		 * Define a function called <function expression> that takes a[n]
		 * <type1> called <field1>, a[n] <type2> called <field2>, and a[n]
		 * <type3> called <field3>( and outputs a <return type>)?.
		 */
		if (!line.get(1).token.equals(Resources.A)
				|| !line.get(2).token.equals(Resources.DECL_FUNCTION)
				|| !line.get(3).token.equals(Resources.CALLED))
			SyntaxErrors.invalidSentence(SentenceType.DECLARATION_FUNCT,
					line);
		int i = 4;
		ArrayList<Token> funcExpress = new ArrayList<>();
		for (; i < line.size() && !line.get(i).token.equals(Resources.THAT); i++) {
			funcExpress.add(line.get(i));
		}
		i++;
		Map<VariableID, GenericType> vars = new HashMap<>();
		if (i < line.size()) {
			switch (line.get(i).token) {
				case Resources.TAKES:
					for (; i < line.size(); i++) {
						if (!line.get(i).token.equals(Resources.CALLED))
							continue;
						GenericType type = ExpressionParser
								.parseType(line.get(i - 1));
						// LOWPRI allow generic typing in functions...
						// later
						if (!(type instanceof ConcreteType))
							ParserErrors.expectedCTInFunctionDecl(type,
									line, vars.size());
						vars.put(VariableID.getInstance(line
								.get(i + 1)), type);
					}
					break;
				case Resources.OUTPUTS:
					break;
				default:
					SyntaxErrors.invalidSentence(
							SentenceType.DECLARATION_FUNCT, line);
			}
		}
		int outputloc = Token.indexOf(line, Resources.OUTPUTS);
		Pair<FunctionName, List<UntypedExpression>> sig = parseFunctionSignature(funcExpress);
		List<VariableID> variables = sig.value
				.stream()
				.map(x -> {
					if (!(x instanceof VariableID))
						ParserErrors.expectedVariableInDecl(true, x.toToken(),
								line);
					return (VariableID) x;
				}).collect(Collectors.toList());
		List<GenericType> types = new ArrayList<>();
		for (VariableID vid : variables) {
			GenericType gt = vars.get(vid);
			if (gt == null) TypingErrors.incompleteFieldTypingInFunctionDecl(vid, line);
			types.add(gt);
		}
		if (outputloc < 0)
			return new FunctionDefinition(sig.key, variables, types,
					new PrimitiveType(PrimitiveTypeWithoutContext.VOID, line.get(line
							.size() - 1).context),
					Context.tokenSum(line));
		if (Language.isArticle(line.get(outputloc + 1).token)) outputloc++;
		line.subList(0, outputloc + 1).clear();
		GenericType outputType = ExpressionParser.parseType(Language
				.parenthesize(line));
		if (!(outputType instanceof ConcreteType))
			ParserErrors.expectedCTInFunctionDecl(outputType, line, -1);
		return new FunctionDefinition(sig.key, variables, types,
				(ConcreteType) outputType, Context.tokenSum(line));
	}
	private static Pair<FunctionName, List<UntypedExpression>> parseFunctionSignature(
			List<Token> list) {
		List<FunctionComponent> function = new ArrayList<>();
		List<Token> currentExpression = new ArrayList<>();
		List<UntypedExpression> arguments = new ArrayList<>();
		for (Token tok : list) {
			if (Language.isExpression(tok.token)) {
				currentExpression.add(tok);
			} else if (Language.isFunctionToken(tok.token)) {
				if (currentExpression.size() != 0) {
					UntypedExpression argument = ExpressionParser
							.parsePureExpression(currentExpression);
					arguments.add(argument);
					function.add(FunctionArgument.INSTANCE);
					currentExpression.clear();
				}
				function.add(new FunctionToken(tok));
			} else break;
		}
		if (currentExpression.size() != 0) {
			UntypedExpression argument = ExpressionParser
					.parsePureExpression(currentExpression);
			arguments.add(argument);
			function.add(FunctionArgument.INSTANCE);
			currentExpression.clear();
		}
		return StdLib42.parseFunction(function, arguments);
	}
}
