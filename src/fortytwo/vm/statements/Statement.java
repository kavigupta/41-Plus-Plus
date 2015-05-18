package fortytwo.vm.statements;

import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.environment.StructureRoster;
import fortytwo.vm.environment.VariableTypeRoster;

public interface Statement {
	public void execute(LocalEnvironment environment);
	/**
	 * This always returns true or throws an error. The return type is just to
	 * make checking explicit
	 */
	public boolean typeCheck(VariableTypeRoster typeRoster,
			StructureRoster structRoster);
}
