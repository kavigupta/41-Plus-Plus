package fortytwo.vm;

import java.util.function.Consumer;

import fortytwo.compiler.Context;
import fortytwo.vm.errors.Error42;
import fortytwo.vm.errors.ErrorType;

public final class VirtualMachine {
	public static Consumer<String> displayln = System.out::println;
	public static Consumer<Error42> displayerr = x -> {
		throw new RuntimeException(x.type + ":\t" + x.msg + ":" + x.context);
	};
	private static Error42 err = null;
	private static String msg = "";
	private VirtualMachine() {}
	public static void displayLine(String s) {
		msg += s + System.lineSeparator();
		displayln.accept(s);
	}
	public static void error(ErrorType type, String msg, Context context) {
		err = new Error42(type, msg, context);
		displayerr.accept(err);
	}
	public static boolean errorState() {
		return err != null;
	}
	public static String popMessage() {
		final String temp = msg;
		msg = "";
		return temp;
	}
	public static Error42 popError() {
		final Error42 temp = err;
		err = null;
		return temp;
	}
	public static void clean() {
		popMessage();
		popError();
	}
}
