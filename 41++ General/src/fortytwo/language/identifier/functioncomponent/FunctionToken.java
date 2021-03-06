package fortytwo.language.identifier.functioncomponent;

import fortytwo.compiler.Context;
import fortytwo.compiler.LiteralToken;

public class FunctionToken implements FunctionComponent {
	public final LiteralToken token;
	public FunctionToken(LiteralToken token) {
		this.token = token;
	}
	@Override
	public Context context() {
		return token.context;
	}
	@Override
	public String toString() {
		return token.token;
	}
	@Override
	public boolean isArgument() {
		return false;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (token == null ? 0 : token.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		final FunctionToken other = (FunctionToken) obj;
		if (token == null) {
			if (other.token != null) return false;
		} else if (!token.equals(other.token)) return false;
		return true;
	}
}
