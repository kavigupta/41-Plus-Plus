package fortytwo.vm.environment.type;

import java.util.List;
import java.util.Optional;

import fortytwo.language.identifier.FunctionName;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.ConcreteType;
import fortytwo.language.type.FunctionType;
import fortytwo.library.standard.StdLib42;

public class TopTypeEnvironment extends AbstractTypeEnvironment {
	public TopTypeEnvironment() {
		super(TypeRoster.getDefault(StdLib42.DEFAULT_STRUCT),
				EnvironmentType.UNORDERED);
	}
	@Override
	protected Optional<ConcreteType> checkParentForTypeOf(
			VariableIdentifier name) {
		return Optional.empty();
	}
	@Override
	protected Optional<FunctionType> checkParentForTypeOf(FunctionName name,
			List<ConcreteType> types) {
		return Optional.empty();
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
