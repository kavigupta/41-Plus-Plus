package fortytwo.vm.environment;

import java.util.List;

import fortytwo.language.identifier.VariableIdentifier;

public class FrameTypeEnvironment {
	private final TypeEnvironment parent;
	private final List<VariableIdentifier> hasAccessTo;
	private UnorderedEnvironment child;
	public FrameTypeEnvironment(TypeEnvironment parent,
			List<VariableIdentifier> hasAccessTo, UnorderedEnvironment child) {
		this.parent = parent;
		this.hasAccessTo = hasAccessTo;
		this.child = child;
	}
	public FrameTypeEnvironment(TypeEnvironment parent,
			List<VariableIdentifier> hasAccessTo) {
		this.parent = parent;
		this.hasAccessTo = hasAccessTo;
		this.child = null;
	}
}
