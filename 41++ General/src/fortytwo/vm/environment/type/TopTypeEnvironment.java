package fortytwo.vm.environment.type;

import java.util.List;

import fortytwo.language.identifier.FunctionName;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.ConcreteType;
import fortytwo.language.type.FunctionType;
import fortytwo.library.standard.StdLib42;
import fortytwo.vm.errors.DNEErrors;

public class TopTypeEnvironment extends AbstractTypeEnvironment {
	public TopTypeEnvironment() {
		super(TypeRoster.getDefault(StdLib42.DEFAULT_STRUCT),
				EnvironmentType.UNORDERED);
	}
	@Override
	protected ConcreteType checkParentForTypeOf(VariableIdentifier name) {
		DNEErrors.variableDNE(name);
		// should not reach here.
		throw new IllegalStateException();
	}
	@Override
	protected FunctionType checkParentForTypeOf(FunctionName name,
			List<ConcreteType> types) {
		DNEErrors.functionSignatureDNE(name, types);
		// should not reach here.
		throw new IllegalStateException();
	}
	@Override
	protected boolean allowRequest(RequestType type) {
		return true;
	}
	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}
	@Override
	public int hashCode() {
		return super.hashCode();
	}
}
