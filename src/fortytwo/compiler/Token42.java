package fortytwo.compiler;

import java.util.List;

public class Token42 {
	public final String token;
	public final Context context;
	public static Token42 minimal(String line) {
		return new Token42(line, Context.entire(line));
	}
	public Token42(String line, Context context) {
		this.token = line;
		this.context = context;
	}
	public Token42 subToken(int i, int j) {
		return new Token42(token.substring(i, j), context.subContext(i, j));
	}
	public boolean isMeaningful() {
		if (token.length() == 0) return false;
		if (token.charAt(0) == '[') return false;
		if (Character.isWhitespace(token.charAt(0))) return false;
		return true;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((token == null) ? 0 : token.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (obj instanceof String) return ((String) obj).equals(token);
		if (getClass() != obj.getClass()) return false;
		Token42 other = (Token42) obj;
		if (token == null) {
			if (other.token != null) return false;
		} else if (!token.equals(other.token)) return false;
		return true;
	}
	@Override
	public String toString() {
		return token;
	}
	public static int indexOf(List<Token42> tokens, String s) {
		for (int i = 0; i < tokens.size(); i++) {
			if (tokens.get(i).token.equals(s)) return i;
		}
		return -1;
	}
}
