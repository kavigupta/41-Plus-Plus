package fortytwo.compiler.parsed.declaration;

import java.util.List;

import fortytwo.language.identifier.FunctionName;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.ConcreteType;

public class FunctionDefinition implements Declaration {
	public final FunctionName name;
	public final List<VariableIdentifier> parameterVariables;
	public final List<ConcreteType> parameterTypes;
	public final ConcreteType outputTypes;
	public FunctionDefinition(FunctionName signature,
			List<VariableIdentifier> parameterVariables,
			List<ConcreteType> parameterTypes, ConcreteType output) {
		this.name = signature;
		this.parameterVariables = parameterVariables;
		this.parameterTypes = parameterTypes;
		this.outputTypes = output;
	}
	@Override
	public SentenceType type() {
		return SentenceType.DECLARATION_FUNCT;
	}
}