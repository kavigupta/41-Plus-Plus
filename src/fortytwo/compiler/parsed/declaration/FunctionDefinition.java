package fortytwo.compiler.parsed.declaration;

import java.util.List;

import fortytwo.compiler.Context;
import fortytwo.language.SourceCode;
import fortytwo.language.classification.SentenceType;
import fortytwo.language.identifier.FunctionName;
import fortytwo.language.identifier.VariableID;
import fortytwo.language.type.ConcreteType;
import fortytwo.language.type.GenericType;
import fortytwo.vm.environment.StaticEnvironment;

public class FunctionDefinition implements Declaration {
	public final FunctionName name;
	public final List<VariableID> parameterVariables;
	public final List<GenericType> parameterTypes;
	public final ConcreteType outputType;
	private final Context context;
	public FunctionDefinition(FunctionName signature,
			List<VariableID> parameterVariables,
			List<GenericType> parameterTypes, ConcreteType output,
			Context context) {
		this.name = signature;
		this.parameterVariables = parameterVariables;
		this.parameterTypes = parameterTypes;
		this.outputType = output;
		this.context = context;
	}
	@Override
	public SentenceType type() {
		return SentenceType.DECLARATION_FUNCT;
	}
	@Override
	public String toSourceCode() {
		return SourceCode.display(this);
	}
	public void registerInputs(StaticEnvironment environment) {
		for (int i = 0; i < parameterTypes.size(); i++) {
			// LOWPRI allow generic typing in functions...
			environment.addType(parameterVariables.get(i), (ConcreteType)
			// LOWPRI remove cast
					parameterTypes.get(i));
		}
	}
	@Override
	public boolean isSimple() {
		return true;
	}
	@Override
	public Context context() {
		return context;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((outputType == null) ? 0 : outputType.hashCode());
		result = prime
				* result
				+ ((parameterTypes == null) ? 0 : parameterTypes.hashCode());
		result = prime
				* result
				+ ((parameterVariables == null) ? 0 : parameterVariables
						.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		FunctionDefinition other = (FunctionDefinition) obj;
		if (name == null) {
			if (other.name != null) return false;
		} else if (!name.equals(other.name)) return false;
		if (outputType == null) {
			if (other.outputType != null) return false;
		} else if (!outputType.equals(other.outputType)) return false;
		if (parameterTypes == null) {
			if (other.parameterTypes != null) return false;
		} else if (!parameterTypes.equals(other.parameterTypes))
			return false;
		if (parameterVariables == null) {
			if (other.parameterVariables != null) return false;
		} else if (!parameterVariables.equals(other.parameterVariables))
			return false;
		return true;
	}
}
