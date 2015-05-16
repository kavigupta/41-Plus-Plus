package fortytwo.compiler.parsed.declaration;

import java.util.List;

import fortytwo.compiler.parsed.expressions.ParsedExpression;
import fortytwo.language.identifier.FunctionName;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.ConcreteType;

public class FunctionDefinition implements Declaration {
	public final FunctionName signature;
	public final List<VariableIdentifier> parameterVariables;
	public final List<ConcreteType> parameterTypes;
	public final ParsedExpression output;
	public FunctionDefinition(FunctionName signature,
			List<VariableIdentifier> parameterVariables,
			List<ConcreteType> parameterTypes, ParsedExpression output) {
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
