package fortytwo.vm.environment.type;

import java.util.List;

import fortytwo.language.identifier.FunctionName;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.ConcreteType;
import fortytwo.language.type.FunctionType;

public class FrameTypeEnvironment extends AbstractTypeEnvironment {
	private final AbstractTypeEnvironment container;
	private final List<VariableIdentifier> allowed;
	private FrameTypeEnvironment(TypeRoster typeRoster,
			EnvironmentType isOrdered, AbstractTypeEnvironment container,
			List<VariableIdentifier> allowed) {
		super(typeRoster, isOrdered);
		this.container = container;
		this.allowed = allowed;
	}
	@Override
	protected boolean allowRequest(RequestType type) {
		return true;
	}
	@Override
	protected ConcreteType checkParentForTypeOf(VariableIdentifier name) {
		return container.typeOf(name, allowed.contains(name) ? RequestType.ANY
				: RequestType.REQUIRES_UNORDERED);
	}
	@Override
	protected FunctionType checkParentForTypeOf(FunctionName name,
			List<ConcreteType> types) {
		return container.referenceTo(name, types,
				RequestType.REQUIRES_UNORDERED);
	}
}
