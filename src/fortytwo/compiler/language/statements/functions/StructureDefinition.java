package fortytwo.compiler.language.statements.functions;

import java.util.ArrayList;

import lib.standard.collections.Pair;
import fortytwo.compiler.language.expressions.ParsedExpression;
import fortytwo.compiler.language.statements.ParsedStatement;
import fortytwo.vm.environment.Environment;
import fortytwo.vm.expressions.Expression;

public class StructureDefinition implements ParsedExpression {
	@Override
	public Expression contextualize(Environment env) {
		// TODO Auto-generated method stub
		return null;
	}
	public static ParsedStatement getInstance(FunctionSignature signature,
			ArrayList<Pair<ParsedExpression, String>> fields) {
		// TODO Auto-generated method stub
		return null;
	}
}
