package fortytwo.compiler.language.declaration;

import java.util.List;

import fortytwo.compiler.language.expressions.ParsedExpression;
import fortytwo.compiler.language.identifier.FunctionName;
import fortytwo.compiler.language.identifier.TypeIdentifier;
import fortytwo.compiler.language.identifier.VariableIdentifier;

public class FunctionDefinition implements Declaration {
	public final FunctionName signature;
	public final List<VariableIdentifier> parameterVariables;
	public final List<TypeIdentifier> parameterTypes;
	public final ParsedExpression output;
	public FunctionDefinition(FunctionName signature,
			List<VariableIdentifier> parameterVariables,
			List<TypeIdentifier> parameterTypes, ParsedExpression output) {
		this.signature = signature;
		this.parameterVariables = parameterVariables;
		this.parameterTypes = parameterTypes;
		this.output = output;
	}
	@Override
	public SentenceType type() {
		return SentenceType.DECLARATION_FUNCT;
	}
}
