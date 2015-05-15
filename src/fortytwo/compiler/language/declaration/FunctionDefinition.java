package fortytwo.compiler.language.declaration;

import java.util.ArrayList;

import lib.standard.collections.Pair;
import fortytwo.compiler.language.expressions.ParsedExpression;
import fortytwo.compiler.language.identifier.TypeIdentifier;
import fortytwo.compiler.language.identifier.VariableIdentifier;

public class FunctionDefinition implements Declaration {
	public final FunctionSignature signature;
	public final ArrayList<Pair<TypeIdentifier, VariableIdentifier>> argTypes;
	public final ParsedExpression parsedExpression;
	public FunctionDefinition(FunctionSignature signature,
			ArrayList<Pair<TypeIdentifier, VariableIdentifier>> argTypes,
			ParsedExpression parsedExpression) {
		this.signature = signature;
		this.argTypes = argTypes;
		this.parsedExpression = parsedExpression;
	}
	@Override
	public SentenceType type() {
		return SentenceType.DECLARATION_FUNCT;
	}
}
