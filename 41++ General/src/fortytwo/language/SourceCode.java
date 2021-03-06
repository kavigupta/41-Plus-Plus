package fortytwo.language;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import fortytwo.compiler.Context;
import fortytwo.compiler.LiteralToken;
import fortytwo.compiler.parsed.ParsedConstruct;
import fortytwo.compiler.parsed.Sentence;
import fortytwo.compiler.parsed.expressions.Expression;
import fortytwo.language.field.GenericField;
import fortytwo.language.identifier.FunctionName;
import fortytwo.language.identifier.FunctionSignature;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.identifier.functioncomponent.FunctionComponent;
import fortytwo.language.identifier.functioncomponent.FunctionToken;
import fortytwo.language.type.*;
import fortytwo.vm.environment.VariableRoster;
import fortytwo.vm.expressions.*;

public class SourceCode {
	public static String displayFunctionDefinition(FunctionSignature sig,
			List<VariableIdentifier> inputVariables) {
		final String fieldList = displayFieldList(sig.type.inputTypes,
				inputVariables);
		return "Define a function called "
				+ displayFunctionSignature(sig.name, inputVariables)
				+ (fieldList.length() == 0 ? "" : " that takes " + fieldList)
				+ displayOutputType(sig.type.outputType,
						fieldList.length() != 0);
	}
	public static String display(ParsedConstruct obj, VariableIdentifier field,
			ParsedConstruct value) {
		return "Set the " + field.name + " of " + obj.toSourceCode() + " to "
				+ value.toSourceCode();
	}
	public static String display(FunctionName name,
			List<? extends ParsedConstruct> arguments) {
		final String func = displayFunctionSignature(name, arguments).trim();
		if (func.length() == 0) return "";
		if (Character.isUpperCase(func.charAt(0))
				&& Character.isAlphabetic(func.charAt(0)))
			return func;
		return "(" + func + ")";
	}
	public static String displayRedefinition(ParsedConstruct name,
			ParsedConstruct expr) {
		return "Set the value of " + name.toSourceCode() + " to "
				+ expr.toSourceCode();
	}
	public static String displaySeries(
			List<? extends ParsedConstruct> statements) {
		if (statements.size() == 0) return "";
		if (statements.size() == 1) return statements.get(0).toSourceCode();
		final String total = statements.stream().map(x -> x.toSourceCode())
				.reduce("", (a, b) -> a + ".\n" + b);
		return total.substring(2);
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
		final StringBuffer sbuff = new StringBuffer("[");
		for (int i = 0; i < literalArray.length(); i++)
			// no issue here.
			sbuff.append(
					literalArray.get(i + 1, Context.SYNTHETIC).toSourceCode())
					.append(", ");
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
		final StringBuffer sbuff = new StringBuffer("{")
				.append(literalObject.struct.type.toSourceCode());
		if (literalObject.nFields() != 0) sbuff.append(": ");
		literalObject.forEachField(
				(name, expr) -> sbuff.append(name.toSourceCode()).append(" <= ")
						.append(expr.toSourceCode()).append(", "));
		return literalObject.nFields() == 0 ? sbuff.append("}").toString()
				: sbuff.substring(0, sbuff.length() - 2) + "}";
	}
	public static String display(LiteralString literalString) {
		return "'" + literalString.contents + "'";
	}
	public static String display(StructureType st) {
		if (st.types.size() == 0)
			return LiteralToken.parenthesize(st.name).token;
		final StringBuffer buff = new StringBuffer("(");
		for (final LiteralToken s : st.name)
			buff.append(s).append(" ");
		final List<String> types = st.types.stream().map(x -> x.toSourceCode())
				.collect(Collectors.toList());
		return buff.append("of ").append(displayList(types)).append(")")
				.toString();
	}
	public static String display(GenericStructureType type) {
		final StringBuffer name = new StringBuffer();
		for (final LiteralToken s : type.name)
			name.append(s).append(' ');
		final String list = displayList(type.inputs.stream()
				.map(x -> x.toSourceCode()).collect(Collectors.toList()));
		return name.toString().trim()
				+ (list.length() == 0 ? "" : " of " + list);
	}
	public static String wrapInBraces(Sentence statement) {
		final String s = statement.toSourceCode();
		if (statement.isSimple()) return "\n\t" + s;
		return Arrays.asList(s.split("\r|\n")).stream().map(x -> "\n\t" + x)
				.reduce("", (a, b) -> a + b);
	}
	public static String displayFieldList(VariableRoster<?> fields) {
		final List<String> items = new ArrayList<>();
		for (final Entry<VariableIdentifier, ? extends Expression> e : fields.pairs
				.entrySet())
			items.add(Language.articleized(e.getKey().toSourceCode() + " of "
					+ e.getValue().toSourceCode()));
		return displayList(items);
	}
	private static String displayOutputType(GenericType outputType,
			boolean hasFields) {
		if (outputType instanceof PrimitiveType
				&& ((PrimitiveType) outputType).type == PrimitiveTypeWOC.VOID)
			return "";
		return " " + (hasFields ? "and" : "that") + " outputs "
				+ Language.articleized(outputType.toSourceCode());
	}
	private static String displayFieldList(List<GenericType> parameterTypes,
			List<VariableIdentifier> parameterVariables) {
		final List<String> items = new ArrayList<>();
		for (int i = 0; i < parameterTypes.size(); i++)
			items.add(displayField(parameterTypes.get(i),
					parameterVariables.get(i)));
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
		for (int i = 0; i < items.size() - 1; i++)
			s += items.get(i) + ", ";
		return s + "and " + items.get(items.size() - 1);
	}
	private static String displayFunctionSignature(FunctionName name,
			List<? extends ParsedConstruct> parameterVariables) {
		String s = "";
		int i = 0;
		for (final FunctionComponent tok : name.function)
			if (tok instanceof FunctionToken) {
				String src = ((FunctionToken) tok).token.toSourceCode();
				// do not pad operators
				if (!Language.isOperator(src)) {
					// do not allow consecutive spaces
					if (!s.endsWith(" ")) src = " " + src;
					src += " ";
				}
				s += src;
			} else {
				s += parameterVariables.get(i).toSourceCode();
				i++;
			}
		return s.trim();
	}
	public static String displayFieldList(List<GenericField> fields) {
		return displayList(fields.stream().map(f -> displayField(f))
				.collect(Collectors.toList()));
	}
}
