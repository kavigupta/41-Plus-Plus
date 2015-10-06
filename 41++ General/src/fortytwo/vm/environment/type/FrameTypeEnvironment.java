package fortytwo.vm.environment.type;

import java.util.List;
import java.util.Optional;

import fortytwo.language.identifier.FunctionName;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.ConcreteType;
import fortytwo.language.type.FunctionType;
import fortytwo.vm.environment.FunctionSignatureRoster;
import fortytwo.vm.environment.StructureRoster;
import fortytwo.vm.environment.VariableTypeRoster;

public class FrameTypeEnvironment extends AbstractTypeEnvironment {
	private final AbstractTypeEnvironment parent;
	private final List<VariableIdentifier> allowed;
	private AbstractTypeEnvironment child;
	public FrameTypeEnvironment(AbstractTypeEnvironment parent,
			List<VariableIdentifier> allowed) {
		super(new TypeRoster(new StructureRoster(),
				new FunctionSignatureRoster(), new VariableTypeRoster()),
				EnvironmentType.UNORDERED);
		this.parent = parent;
		this.allowed = allowed;
	}
	public void initializeChild(AbstractTypeEnvironment child) {
		this.child = child;
	}
	@Override
	protected boolean allowRequest(RequestType type) {
		return true;
	}
	@Override
	protected Optional<ConcreteType> checkParentForTypeOf(
			VariableIdentifier name) {
		return parent.typeOf(name, allowed.contains(name) ? RequestType.ANY
				: RequestType.REQUIRES_UNORDERED);
	}
	@Override
	protected Optional<FunctionType> checkParentForTypeOf(FunctionName name,
			List<ConcreteType> types) {
		return parent.referenceTo(name, types, RequestType.REQUIRES_UNORDERED);
	}
	public Optional<FunctionType> getTypeOfMemberFunction(FunctionName name,
			List<ConcreteType> types) {
		return child.referenceTo(name, types, RequestType.ONLY_THIS_FRAME);
	}
}
