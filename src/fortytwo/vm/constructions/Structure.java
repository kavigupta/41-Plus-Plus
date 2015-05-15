package fortytwo.vm.constructions;

import java.util.ArrayList;
import java.util.List;

import lib.standard.collections.Pair;
import fortytwo.compiler.language.declaration.FunctionSignature;
import fortytwo.compiler.language.expressions.ParsedExpression;
import fortytwo.compiler.language.identifier.VariableIdentifier;

public class Structure {
	public final FunctionSignature sig;
	public final List<Pair<ParsedExpression, VariableIdentifier>> fields;
	public static Structure getInstance(FunctionSignature signature,
			ArrayList<Pair<ParsedExpression, VariableIdentifier>> fields) {
		return new Structure(signature, fields);
	}
	public Structure(FunctionSignature signature,
			ArrayList<Pair<ParsedExpression, VariableIdentifier>> fields) {
		this.sig = signature;
		this.fields = fields;
	}
}
