package fortytwo.compiler.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lib.standard.collections.Pair;
import fortytwo.compiler.language.Language;
import fortytwo.compiler.language.declaration.*;
import fortytwo.compiler.language.expressions.ParsedExpression;
import fortytwo.compiler.language.identifier.TypeIdentifier;
import fortytwo.compiler.language.identifier.VariableIdentifier;
import fortytwo.compiler.language.statements.ParsedFunctionCall;
import fortytwo.vm.constructions.Structure;

public class ConstructionParser {
	public static ParsedFunctionCall composeFunction(List<String> list) {
		List<FunctionComponent> function = new ArrayList<>();
		List<String> currentExpression = new ArrayList<>();
		ArrayList<Pair<VariableIdentifier, ParsedExpression>> arguments = new ArrayList<>();
		for (String token : list) {
			if (Language.isExpression(token)) {
				currentExpression.add(token);
			} else {
				if (currentExpression.size() != 0) {
					ParsedExpression argument = ExpressionParser
							.parsePureExpression(currentExpression);
					VariableIdentifier ParsedVariable = VariableIdentifier
							.getInstance("_$" + arguments.size());
					arguments.add(Pair.getInstance(ParsedVariable,
							argument));
					function.add(new FunctionVariable(ParsedVariable));
					currentExpression.clear();
				}
				function.add(new FunctionToken(token));
			}
		}
		if (currentExpression.size() != 0) {
			ParsedExpression argument = ExpressionParser
					.parsePureExpression(currentExpression);
			VariableIdentifier var = VariableIdentifier.getInstance("_$"
					+ arguments.size());
			arguments.add(Pair.getInstance(var, argument));
			function.add(new FunctionVariable(var));
			currentExpression.clear();
		}
		return ParsedFunctionCall.getInstance(
				FunctionSignature.getInstance(function), arguments);
	}
	public static Declaration parseStructDefinition(List<String> line) {
		/*
		 * Define a structure called <structure expression> ; which contains
		 * a[n] <type> called <field> , a[n] <type> called <field>, ...
		 */
		if (!line.get(1).equals("a") || !line.get(3).equals("called"))
			throw new RuntimeException(/* LOWPRI-E */);
		int i = 4;
		ArrayList<String> structExpression = new ArrayList<>();
		for (; i < line.size() && !line.get(i).equals(";"); i++) {
			structExpression.add(line.get(i));
		}
		ArrayList<Pair<ParsedExpression, VariableIdentifier>> fields = new ArrayList<>();
		for (; i < line.size(); i++) {
			if (!line.get(i).equals("called")) continue;
			fields.add(Pair.getInstance(ExpressionParser
					.parseExpression(Arrays.asList(line.get(i - 1))),
					VariableIdentifier.getInstance(line.get(i + 1))));
		}
		return new StructureDeclaration(Structure.getInstance(
				parseFunctionSignature(structExpression), fields));
	}
	public static FunctionDefinition parseFunctionDefinition(List<String> line) {
		/*
		 * Define a function called <function expression> that takes a[n]
		 * <type1> called <field1>, a[n] <type2> called <field2>, and a[n]
		 * <type3> called <field3>( and outputs a <return type>)?.
		 */
		if (!line.get(1).equals("a") || !line.get(2).equals("function")
				|| !line.get(3).equals("called"))
			throw new RuntimeException(/* LOWPRI-E */);
		int i = 4;
		ArrayList<String> funcExpress = new ArrayList<>();
		for (; i < line.size() && !line.get(i).equals("that"); i++) {
			funcExpress.add(line.get(i));
		}
		i++;
		if (!line.get(i).equals("takes")) throw new RuntimeException(/*
														 * LOWPRI-E
														 */);
		ArrayList<Pair<TypeIdentifier, VariableIdentifier>> types = new ArrayList<>();
		for (; i < line.size(); i++) {
			if (!line.get(i).equals("called")) continue;
			types.add(Pair.getInstance(
					TypeIdentifier.getInstance(line.get(i - 1)),
					VariableIdentifier.getInstance(line.get(i + 1))));
		}
		int outputloc = line.indexOf("outputs");
		if (outputloc < 0)
			return new FunctionDefinition(
					parseFunctionSignature(funcExpress), types, null);
		if (!Language.isArticle(line.get(outputloc + 1)))
			throw new RuntimeException(/* LOWPRI-E */);
		line.subList(0, outputloc + 2);
		return new FunctionDefinition(parseFunctionSignature(funcExpress),
				types, ExpressionParser.parseExpression(line));
	}
	private static FunctionSignature parseFunctionSignature(
			ArrayList<String> list) {
		List<FunctionComponent> components = new ArrayList<>();
		for (String token : list) {
			if (token.charAt(0) == '_')
				components.add(new FunctionVariable(VariableIdentifier
						.getInstance(token)));
			else components.add(new FunctionToken(token));
		}
		return FunctionSignature.getInstance(components);
	}
}
