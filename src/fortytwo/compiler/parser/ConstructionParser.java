package fortytwo.compiler.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lib.standard.collections.Pair;
import fortytwo.compiler.parsed.declaration.FunctionDefinition;
import fortytwo.compiler.parsed.declaration.StructureDeclaration;
import fortytwo.compiler.parsed.expressions.ParsedExpression;
import fortytwo.compiler.parsed.statements.ParsedFunctionCall;
import fortytwo.language.Language;
import fortytwo.language.Resources;
import fortytwo.language.field.GenericField;
import fortytwo.language.identifier.FunctionName;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.identifier.functioncomponent.FunctionArgument;
import fortytwo.language.identifier.functioncomponent.FunctionComponent;
import fortytwo.language.identifier.functioncomponent.FunctionToken;
import fortytwo.language.type.*;
import fortytwo.library.standard.StdLib42;
import fortytwo.vm.constructions.GenericStructure;

public class ConstructionParser {
	public static ParsedFunctionCall composeFunction(List<String> list) {
		Pair<FunctionName, List<ParsedExpression>> fsig = parseFunctionSignature(list);
		return ParsedFunctionCall.getInstance(fsig.key, fsig.value);
	}
	public static StructureDeclaration parseStructDefinition(List<String> line) {
		/*
		 * Define a structure called <structure> of <typevar1>, <typevar2>,
		 * and <typevar3> that contains a[n] <type> called <field> , a[n]
		 * <type> called <field>, ...
		 */
		if (!line.get(1).equals(Resources.A)
				|| !line.get(3).equals(Resources.CALLED))
			throw new RuntimeException(/* LOWPRI-E */);
		line.subList(0, 4).clear();
		ArrayList<String> structExpression = new ArrayList<>();
		int i = 0;
		for (; i < line.size() && !line.get(i).equals(Resources.THAT)
				&& !line.get(i).equals(Resources.OF); i++) {
			structExpression.add(line.get(i));
		}
		List<TypeVariable> typeVariables = new ArrayList<>();
		if (i < line.size() && line.get(i).equals(Resources.OF)) {
			i++;
			for (; i < line.size() && !line.get(i).equals(Resources.THAT); i++) {
				if (Language.isValidVariableIdentifier(line.get(i))) {
					typeVariables.add(new TypeVariable(VariableIdentifier
							.getInstance(line.get(i))));
				}
			}
		}
		ArrayList<GenericField> fields = new ArrayList<>();
		for (; i < line.size(); i++) {
			if (!line.get(i).equals(Resources.CALLED)) continue;
			fields.add(new GenericField(VariableIdentifier.getInstance(line
					.get(i + 1)), ExpressionParser.parseType(line
					.get(i - 1))));
		}
		return new StructureDeclaration(new GenericStructure(
				new GenericStructureType(structExpression, typeVariables),
				fields));
	}
	public static FunctionDefinition parseFunctionDefinition(List<String> line) {
		/*
		 * Define a function called <function expression> that takes a[n]
		 * <type1> called <field1>, a[n] <type2> called <field2>, and a[n]
		 * <type3> called <field3>( and outputs a <return type>)?.
		 */
		if (!line.get(1).equals(Resources.A)
				|| !line.get(2).equals(Resources.DECL_FUNCTION)
				|| !line.get(3).equals(Resources.CALLED))
			throw new RuntimeException(/* LOWPRI-E */);
		int i = 4;
		ArrayList<String> funcExpress = new ArrayList<>();
		for (; i < line.size() && !line.get(i).equals(Resources.THAT); i++) {
			funcExpress.add(line.get(i));
		}
		i++;
		Map<VariableIdentifier, GenericType> vars = new HashMap<>();
		if (i < line.size()) {
			switch (line.get(i)) {
				case Resources.TAKES:
					for (; i < line.size(); i++) {
						if (!line.get(i).equals(Resources.CALLED))
							continue;
						GenericType type = ExpressionParser
								.parseType(line.get(i - 1));
						// LOWPRI allow generic typing in functions...
						// later
						if (!(type instanceof ConcreteType))
							throw new RuntimeException(/* LOWPRI-E */);
						vars.put(VariableIdentifier.getInstance(line
								.get(i + 1)), type);
					}
					break;
				case Resources.OUTPUTS:
					break;
				default:
					throw new RuntimeException(/*
										 * LOWPRI-E
										 */);
			}
		}
		int outputloc = line.indexOf(Resources.OUTPUTS);
		Pair<FunctionName, List<ParsedExpression>> sig = parseFunctionSignature(funcExpress);
		List<VariableIdentifier> variables = sig.value
				.stream()
				.map(x -> {
					if (!(x instanceof VariableIdentifier))
						throw new RuntimeException(/* LOWPRI-E */);
					return (VariableIdentifier) x;
				}).collect(Collectors.toList());
		List<GenericType> types = new ArrayList<>();
		for (VariableIdentifier vid : variables) {
			GenericType gt = vars.get(vid);
			if (gt == null) throw new RuntimeException(/* LOWPRI-E */);
			types.add(gt);
		}
		if (outputloc < 0)
			return new FunctionDefinition(sig.key, variables, types,
					PrimitiveType.VOID);
		if (!Language.isArticle(line.get(outputloc + 1)))
			throw new RuntimeException(/* LOWPRI-E */);
		line.subList(0, outputloc + 2).clear();
		GenericType outputType = ExpressionParser.parseType(Language
				.parenthesize(line));
		if (!(outputType instanceof ConcreteType))
			throw new RuntimeException(/* LOWPRI-E */);
		return new FunctionDefinition(sig.key, variables, types,
				(ConcreteType) outputType);
	}
	private static Pair<FunctionName, List<ParsedExpression>> parseFunctionSignature(
			List<String> list) {
		List<FunctionComponent> function = new ArrayList<>();
		List<String> currentExpression = new ArrayList<>();
		List<ParsedExpression> arguments = new ArrayList<>();
		for (String token : list) {
			if (Language.isExpression(token)) {
				currentExpression.add(token);
			} else if (Language.isFunctionToken(token)) {
				if (currentExpression.size() != 0) {
					ParsedExpression argument = ExpressionParser
							.parsePureExpression(currentExpression);
					arguments.add(argument);
					function.add(FunctionArgument.INSTANCE);
					currentExpression.clear();
				}
				function.add(new FunctionToken(token));
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
