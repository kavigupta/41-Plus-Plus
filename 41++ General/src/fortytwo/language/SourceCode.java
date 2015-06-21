package fortytwo.language;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lib.standard.collections.Pair;
import fortytwo.compiler.Context;
import fortytwo.compiler.LiteralToken;
import fortytwo.compiler.parsed.ParsedConstruct;
import fortytwo.compiler.parsed.Sentence;
import fortytwo.compiler.parsed.declaration.FunctionOutput;
import fortytwo.compiler.parsed.declaration.StructureDefinition;
import fortytwo.compiler.parsed.expressions.Expression;
import fortytwo.compiler.parsed.statements.ParsedDefinition;
import fortytwo.compiler.parsed.statements.ParsedIfElse;
import fortytwo.compiler.parsed.statements.ParsedStatementSeries;
import fortytwo.compiler.parsed.statements.ParsedWhileLoop;
import fortytwo.language.field.GenericField;
import fortytwo.language.identifier.FunctionName;
import fortytwo.language.identifier.FunctionSignature;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.identifier.functioncomponent.FunctionComponent;
import fortytwo.language.identifier.functioncomponent.FunctionToken;
import fortytwo.language.type.*;
import fortytwo.vm.constructions.VariableRoster;
import fortytwo.vm.expressions.*;

public class SourceCode {
	public static String displayFunctionDefinition(FunctionSignature sig,
			List<VariableIdentifier> inputVariables) {
		String fieldList = displayFieldList(sig.inputTypes, inputVariables);
		return "Define a function called "
				+ displayFunctionSignature(sig.name, inputVariables)
				+ (fieldList.length() == 0 ? ""
						: (" that takes " + fieldList))
				+ displayOutputType(sig.outputType, fieldList.length() != 0);
	}
	public static String display(FunctionOutput functionReturn) {
		return "Exit the function" + displayReturn(functionReturn.output);
	}
	public static String display(StructureDefinition structureDeclaration) {
		String fields = displayFieldList(structureDeclaration.structure.fields);
		if (fields.length() != 0) {
			fields = " that contains " + fields;
		}
		return "Define a type called "
				+ structureDeclaration.structure.type.toSourceCode()
				+ fields;
	}
	public static String display(ParsedDefinition parsedDefinition) {
		return "Define "
				+ Language.articleized(parsedDefinition.toCreate.type
						.toSourceCode())
				+ " called "
				+ parsedDefinition.toCreate.name.name
				+ (parsedDefinition.fields.numberOfVariables() == 0 ? ""
						: (" with " + displayFieldList(parsedDefinition.fields)));
	}
	public static String display(ParsedConstruct obj,
			VariableIdentifier field, ParsedConstruct value) {
		return "Set the " + field.name + " of " + obj.toSourceCode() + " to "
				+ value.toSourceCode();
	}
	public static String display(FunctionName name,
			List<? extends ParsedConstruct> arguments) {
		String func = displayFunctionSignature(name, arguments).trim();
		if (func.length() == 0) return "";
		if (Character.isUpperCase(func.charAt(0))
				&& Character.isAlphabetic(func.charAt(0))) return func;
		return "(" + func + ")";
	}
	public static String display(ParsedIfElse parsedIfElse) {
		return "If "
				+ parsedIfElse.condition.toSourceCode()
				+ ": "
				+ wrapInBraces(parsedIfElse.ifso)
				+ (parsedIfElse.ifelse.statements.size() == 0 ? ""
						: (". Otherwise: " + parsedIfElse.ifelse
								.toSourceCode()));
	}
	public static String displayRedefinition(ParsedConstruct name,
			ParsedConstruct expr) {
		return "Set the value of " + name.toSourceCode() + " to "
				+ expr.toSourceCode();
	}
	public static String display(ParsedStatementSeries parsedStatementSeries) {
		return displaySeries(parsedStatementSeries.statements);
	}
	public static String displaySeries(
			List<? extends ParsedConstruct> statements) {
		if (statements.size() == 0) return "";
		if (statements.size() == 1) return statements.get(0).toSourceCode();
		String total = statements.stream().map(x -> x.toSourceCode())
				.reduce("", (a, b) -> a + ". " + b);
		return total.substring(2);
	}
	public static String display(ParsedWhileLoop parsedWhileLoop) {
		return "While " + parsedWhileLoop.condition.toSourceCode() + ": "
				+ wrapInBraces(parsedWhileLoop.statement);
	}
	public static String display(ParsedConstruct first, Operation operation,
			ParsedConstruct second) {
		if (operation == Operation.ADD || operation == Operation.SUBTRACT)
			if (first.toSourceCode().equals("0"))
				return operation.toSourceCode() + second;
		return "(" + first.toSourceCode() + operation.toSourceCode()
				+ second.toSourceCode() + ")";
	}
	public static String display(LiteralArray literalArray) {
		StringBuffer sbuff = new StringBuffer("[");
		for (int i = 0; i < literalArray.length(); i++) {
			// no issue here.
			sbuff.append(
					literalArray.get(i + 1, Context.SYNTHETIC)
							.toSourceCode()).append(", ");
		}
		return sbuff.delete(sbuff.length() - 2, sbuff.length()).append("]")
				.toString();
	}
	public static String display(LiteralBool literalBool) {
		return Boolean.toString(literalBool.contents);
	}
	public static String display(LiteralNumber literalNumber) {
		return literalNumber.contents.toPlainString();
	}
	public static String display(LiteralObject literalObject) {
		StringBuffer sbuff = new StringBuffer("{")
				.append(literalObject.struct.type.toSourceCode());
		if (literalObject.nFields() != 0) sbuff.append(": ");
		literalObject.forEachField(e -> sbuff
				.append(e.getKey().toSourceCode()).append(" <= ")
				.append(e.getValue().toSourceCode()).append(", "));
		return literalObject.nFields() == 0 ? sbuff.append("}").toString()
				: sbuff.substring(0, sbuff.length() - 2) + ("}");
	}
	public static String display(LiteralString literalString) {
		return "'" + literalString.contents + "'";
	}
	public static String display(StructureType st) {
		if (st.types.size() == 0)
			return LiteralToken.parenthesize(st.name).token;
		StringBuffer buff = new StringBuffer("(");
		for (LiteralToken s : st.name) {
			buff.append(s).append(" ");
		}
		List<String> types = st.types.stream().map(x -> x.toSourceCode())
				.collect(Collectors.toList());
		return buff.append("of ").append(displayList(types)).append(")")
				.toString();
	}
	public static String display(GenericStructureType type) {
		StringBuffer name = new StringBuffer();
		for (LiteralToken s : type.name)
			name.append(s).append(' ');
		String list = displayList(type.inputs.stream()
				.map(x -> x.toSourceCode()).collect(Collectors.toList()));
		return name.toString().trim()
				+ (list.length() == 0 ? "" : (" of " + list));
	}
	private static String wrapInBraces(Sentence statement) {
		String s = statement.toSourceCode();
		if (statement.isSimple()) return s;
		return "Do the following: " + s + "." + " That's all";
	}
	private static String displayFieldList(VariableRoster<?> fields) {
		List<String> items = new ArrayList<>();
		for (Pair<VariableIdentifier, ? extends Expression> e : fields.pairs) {
			items.add(Language.articleized(e.getKey().toSourceCode()
					+ " of " + e.getValue().toSourceCode()));
		}
		return displayList(items);
	}
	private static String displayReturn(Expression output) {
		if (output == null) return "";
		return " and output " + output.toSourceCode();
	}
	private static String displayOutputType(GenericType outputType,
			boolean hasFields) {
		if (outputType instanceof PrimitiveType
				&& ((PrimitiveType) outputType).types == PrimitiveTypeWithoutContext.VOID)
			return "";
		return " " + (hasFields ? "and" : "that") + " outputs "
				+ Language.articleized(outputType.toSourceCode());
	}
	private static String displayFieldList(List<GenericType> parameterTypes,
			List<VariableIdentifier> parameterVariables) {
		List<String> items = new ArrayList<>();
		for (int i = 0; i < parameterTypes.size(); i++) {
			items.add(displayField(parameterTypes.get(i),
					parameterVariables.get(i)));
		}
		return displayList(items);
	}
	private static String displayField(GenericType genericType,
			VariableIdentifier variableIdentifier) {
		return Language.articleized(genericType.toSourceCode() + " called "
				+ variableIdentifier.toSourceCode());
	}
	private static String displayField(GenericField field) {
		return displayField(field.type, field.name);
	}
	public static String displayList(List<String> items) {
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
			List<? extends ParsedConstruct> parameterVariables) {
		String s = "";
		int i = 0;
		for (FunctionComponent tok : name.function) {
			if (tok instanceof FunctionToken) {
				s += ((FunctionToken) tok).token + " ";
			} else {
				s += parameterVariables.get(i).toSourceCode() + " ";
				i++;
			}
		}
		return s.trim();
	}
	private static String displayFieldList(List<GenericField> fields) {
		return displayList(fields.stream().map(f -> displayField(f))
				.collect(Collectors.toList()));
	}
}
