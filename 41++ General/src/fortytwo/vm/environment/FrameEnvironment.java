package fortytwo.vm.environment;

import java.util.List;

import fortytwo.language.identifier.VariableIdentifier;

public class FrameEnvironment {
	private final OrderedEnvironment parent;
	private final List<VariableIdentifier> hasAccessTo;
	private UnorderedEnvironment child;
	public FrameEnvironment(OrderedEnvironment parent,
			List<VariableIdentifier> hasAccessTo, UnorderedEnvironment child) {
		this.parent = parent;
		this.hasAccessTo = hasAccessTo;
		this.child = child;
	}
	public FrameEnvironment(OrderedEnvironment parent,
			List<VariableIdentifier> hasAccessTo) {
		this.parent = parent;
		this.hasAccessTo = hasAccessTo;
		this.child = null;
	}
}
