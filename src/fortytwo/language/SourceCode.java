package fortytwo.language;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import fortytwo.compiler.parsed.constructions.ParsedVariableRoster;
import fortytwo.compiler.parsed.declaration.FunctionDefinition;
import fortytwo.compiler.parsed.declaration.FunctionReturn;
import fortytwo.compiler.parsed.declaration.StructureDeclaration;
import fortytwo.compiler.parsed.expressions.ParsedExpression;
import fortytwo.compiler.parsed.statements.*;
import fortytwo.language.identifier.FunctionName;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.identifier.functioncomponent.FunctionComponent;
import fortytwo.language.identifier.functioncomponent.FunctionToken;
import fortytwo.language.type.ConcreteType;
import fortytwo.language.type.GenericType;
import fortytwo.language.type.PrimitiveType;
import fortytwo.vm.expressions.*;

public class SourceCode {
	public static String display(FunctionDefinition def) {
		return "Define a function called "
				+ displayFunctionSignature(def.name, def.parameterVariables)
				+ " that takes "
				+ displayFieldList(def.parameterTypes,
						def.parameterVariables)
				+ displayOutputType(def.outputType) + ".";
	}
	public static String display(FunctionReturn functionReturn) {
		return "Exit the function" + displayReturn(functionReturn.output)
				+ ".";
	}
	public static String display(StructureDeclaration structureDeclaration) {
		// TODO Auto-generated method stub
		return null;
	}
	public static String display(ParsedDefinition parsedDefinition) {
		return "Define "
				+ Language.articleized(parsedDefinition.name.type
						.toSourceCode()) + " called "
				+ Language.articleized(parsedDefinition.name.name.name)
				+ " with " + displayFieldList(parsedDefinition.fields)
				+ ".";
	}
	public static String display(ParsedFieldAssignment parsedFieldAssignment) {
		return "Set the " + parsedFieldAssignment.field.name + " of "
				+ parsedFieldAssignment.name.name + " to "
				+ parsedFieldAssignment.value.toSourceCode() + ".";
	}
	public static String display(ParsedFunctionCall parsedFunctionCall) {
		// TODO Auto-generated method stub
		return null;
	}
	public static String display(ParsedIfElse parsedIfElse) {
		// TODO Auto-generated method stub
		return null;
	}
	public static String display(ParsedRedefinition parsedRedefinition) {
		// TODO Auto-generated method stub
		return null;
	}
	public static String display(ParsedStatementSeries parsedStatementSeries) {
		// TODO Auto-generated method stub
		return null;
	}
	public static String display(ParsedWhileLoop parsedWhileLoop) {
		// TODO Auto-generated method stub
		return null;
	}
	public static String display(VariableIdentifier variableIdentifier) {
		// TODO Auto-generated method stub
		return null;
	}
	public static String display(LiteralArray literalArray) {
		// TODO Auto-generated method stub
		return null;
	}
	public static String display(LiteralBool literalBool) {
		// TODO Auto-generated method stub
		return null;
	}
	public static String display(LiteralNumber literalNumber) {
		// TODO Auto-generated method stub
		return null;
	}
	public static String display(LiteralObject literalObject) {
		// TODO Auto-generated method stub
		return null;
	}
	public static String display(LiteralString literalString) {
		// TODO Auto-generated method stub
		return null;
	}
	private static String displayFieldList(ParsedVariableRoster fields) {
		List<String> items = new ArrayList<>();
		for (Entry<VariableIdentifier, ParsedExpression> e : fields.pairs
				.entrySet()) {
			items.add(Language.articleized(e.getKey() + " of "
					+ e.getValue()));
		}
		return displayList(items);
	}
	private static String displayReturn(ParsedExpression output) {
		if (output == null) return "";
		return " and output " + output.toSourceCode();
	}
	private static String displayOutputType(ConcreteType outputType) {
		if (outputType == PrimitiveType.VOID) return "";
		return " and outputs " + Language.articleized(outputType.toString());
	}
	private static String displayFieldList(List<GenericType> parameterTypes,
			List<VariableIdentifier> parameterVariables) {
		List<String> items = new ArrayList<>();
		for (int i = 0; i < parameterTypes.size(); i++) {
			items.add(Language.articleized(parameterTypes.get(i)
					+ " called " + parameterVariables.get(i)));
		}
		return displayList(items);
	}
	private static String displayList(List<String> items) {
		switch (items.size()) {
			case 0:
				return "";
			case 1:
				return items.get(0);
			case 2:
				return items.get(0) + " and " + items.get(1);
		}
		String s = "";
		for (int i = 0; i < items.size() - 1; i++) {
			s += items.get(i) + ", ";
		}
		return s + "and " + items.get(items.size() - 1);
	}
	private static String displayFunctionSignature(FunctionName name,
			List<VariableIdentifier> parameterVariables) {
		String s = "";
		int i = 0;
		for (FunctionComponent tok : name.function) {
			if (tok instanceof FunctionToken) {
				s += ((FunctionToken) tok).token + " ";
			} else {
				s += parameterVariables.get(i);
				i++;
			}
		}
		return s.substring(0, s.length() - 1);
	}
}
