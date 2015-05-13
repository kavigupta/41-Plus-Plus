package fortytwo.compiler.language.statements.constructions;

import java.util.ArrayList;
import java.util.List;

import lib.standard.collections.Pair;
import fortytwo.compiler.language.expressions.ParsedExpression;
import fortytwo.compiler.language.statements.ParsedStatement;

public class StructureDefinition implements ParsedStatement {
	public final FunctionSignature sig;
	public final List<Pair<ParsedExpression, String>> fields;
	public static StructureDefinition getInstance(FunctionSignature signature,
			ArrayList<Pair<ParsedExpression, String>> fields) {
		return new StructureDefinition(signature, fields);
	}
	public StructureDefinition(FunctionSignature signature,
			ArrayList<Pair<ParsedExpression, String>> fields) {
		this.sig = signature;
		this.fields = fields;
	}
}
