package fortytwo.vm.errors;

import fortytwo.compiler.Context;

public class Error42 {
	public final ErrorType type;
	public final String msg;
	public final Context context;
	public Error42(ErrorType type, String msg, Context context) {
		super();
		this.type = type;
		this.msg = msg;
		this.context = context;
	}
}
