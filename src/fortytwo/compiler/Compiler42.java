package fortytwo.compiler;

import fortytwo.compiler.parser.Parser;
import fortytwo.vm.VirtualMachine;
import fortytwo.vm.environment.GlobalEnvironment;

public class Compiler42 {
	public static GlobalEnvironment compile(String text, VirtualMachine vm) {
		return GlobalEnvironment.interpret(Parser.parse(text), vm);
	}
}
