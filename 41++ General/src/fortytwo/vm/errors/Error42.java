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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((context == null) ? 0 : context.hashCode());
		result = prime * result + ((msg == null) ? 0 : msg.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Error42 other = (Error42) obj;
		if (context == null) {
			if (other.context != null) return false;
		} else if (!context.equals(other.context)) return false;
		if (msg == null) {
			if (other.msg != null) return false;
		} else if (!msg.equals(other.msg)) return false;
		if (type != other.type) return false;
		return true;
	}
	@Override
	public String toString() {
		return "Error42 [type=" + type + ", msg=" + msg + ", context=" + context
				+ "]";
	}
}
