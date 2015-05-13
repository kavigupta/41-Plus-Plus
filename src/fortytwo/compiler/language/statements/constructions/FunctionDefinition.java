package fortytwo.compiler.language.statements.constructions;

import java.util.ArrayList;

import lib.standard.collections.Pair;
import fortytwo.compiler.language.expressions.ParsedExpression;
import fortytwo.compiler.language.identifier.TypeIdentifier;
import fortytwo.compiler.language.identifier.VariableIdentifier;
import fortytwo.compiler.language.statements.ParsedStatement;

public class FunctionDefinition implements ParsedStatement {
	public FunctionDefinition(FunctionSignature signature,
			ArrayList<Pair<TypeIdentifier, VariableIdentifier>> argTypes,
			ParsedExpression parsedExpression) {
		// TODO Auto-generated constructor stub
	}
}
