package fortytwo.vm.environment;

import java.util.List;

import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.vm.environment.type.NonTopTypeEnvironment;

public class FrameTypeEnvironment {
	private final NonTopTypeEnvironment parent;
	private final List<VariableIdentifier> hasAccessTo;
	private UnorderedEnvironment child;
	public FrameTypeEnvironment(NonTopTypeEnvironment parent,
			List<VariableIdentifier> hasAccessTo, UnorderedEnvironment child) {
		this.parent = parent;
		this.hasAccessTo = hasAccessTo;
		this.child = child;
	}
	public FrameTypeEnvironment(NonTopTypeEnvironment parent,
			List<VariableIdentifier> hasAccessTo) {
		this.parent = parent;
		this.hasAccessTo = hasAccessTo;
		this.child = null;
	}
}
