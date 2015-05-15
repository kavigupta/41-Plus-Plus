package fortytwo.vm.constructions;

import java.util.ArrayList;
import java.util.List;

import fortytwo.compiler.language.expressions.ParsedExpression;
import fortytwo.compiler.language.identifier.FunctionName;

public class GenericStructure {
	public final FunctionName sig;
	public final List<ParsedExpression> typeVariables;
	public final List<Field> fields;
	public static GenericStructure getInstance(FunctionName signature,
			List<ParsedExpression> typeVariables, ArrayList<Field> fields) {
		return new GenericStructure(signature, fields);
	}
	public GenericStructure(FunctionName signature, ArrayList<Field> fields) {
		this.sig = signature;
		this.fields = fields;
	}
}
