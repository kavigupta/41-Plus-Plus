package fortytwo.compiler.language.statements.constructions;

import java.util.ArrayList;

import lib.standard.collections.Pair;
import fortytwo.compiler.language.expressions.ParsedExpression;
import fortytwo.compiler.language.expressions.ParsedVariable;
import fortytwo.compiler.language.statements.ParsedStatement;

public class FunctionDefinition implements ParsedStatement {
	public FunctionDefinition(FunctionSignature signature,
			ArrayList<Pair<ParsedVariable, String>> argTypes,
			ParsedExpression parsedExpression) {
		// TODO Auto-generated constructor stub
	}
}
