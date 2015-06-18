package fortytwo.compiler;

import static fortytwo.language.Resources.CLOSE_PAREN;
import static fortytwo.language.Resources.OPEN_PAREN;
import static fortytwo.language.Resources.SPACE;

import java.util.Arrays;
import java.util.List;

import fortytwo.compiler.parsed.GenericToken;
import fortytwo.compiler.parser.Tokenizer;

/**
 * A class representing a literal token, which contains a string along with an
 * attached context. For the purposes of comparison, all that matters is the
 * token, the context merely tags along for help in syntax highlighting and
 * error reporting, it serves no internal use.
 */
public class LiteralToken implements GenericToken {
	/**
	 * A synthetic context representing an empty string {@code ""}
	 */
	public static final LiteralToken SYNTHETIC_EMPTY_STRING = synthetic(new String());
	/**
	 * The literal text this token represents.
	 */
	public final String token;
	/**
	 * The context this token represents, ignored for everything but error
	 * handling
	 */
	public final Context context;
	/**
	 * @param line the line to be converted to a token
	 * @return a token representing an entire line
	 */
	private LiteralToken(String token, Context context) {
		this.token = token;
		this.context = context;
	}
	/**
	 * @param line the line to convert to a token
	 * @return a token representing the entire line, with an offset of 0.
	 */
	public static LiteralToken entire(String line) {
		return new LiteralToken(line, Context.entire(line));
	}
	/**
	 * @param token a token containing an error
	 * @return a token representing an error token
	 */
	public static LiteralToken errorToken(String token) {
		return new LiteralToken(token, null);
	}
	/**
	 * @param token a generic token
	 * @return the literal string and context version of the token
	 */
	public static LiteralToken literalVersionOf(GenericToken token) {
		return new LiteralToken(token.toSourceCode(), token.context());
	}
	/**
	 * @param token a string that was generated synthetically
	 * @return a token representing the synthetic case of that string.
	 */
	public static LiteralToken synthetic(String token) {
		return new LiteralToken(token, Context.SYNTHETIC);
	}
	/**
	 * @param a the first token
	 * @param b the second token
	 * @return a token with strings appended and contexts summed.
	 */
	public static LiteralToken append(LiteralToken a, LiteralToken b) {
		return new LiteralToken(a.token + b.token, Context.sum(Arrays.asList(
				a, b)));
	}
	/**
	 * @param line a list of tokens to be put in parenthesis
	 * @return the tokens in parenthesis separated by spaces
	 */
	public static LiteralToken parenthesize(List<LiteralToken> line) {
		if (line.size() == 0) return SYNTHETIC_EMPTY_STRING;
		if (line.size() == 1) return line.get(0);
		StringBuilder s = new StringBuilder(OPEN_PAREN);
		for (LiteralToken l : line) {
			s.append(l.token).append(SPACE);
		}
		return new LiteralToken(s.append(CLOSE_PAREN).toString(), Context
				.sum(line).inParen());
	}
	/**
	 * @param i start
	 * @param j end
	 * @return The literal token representing substring(i, j)
	 */
	public LiteralToken subToken(int i, int j) {
		return new LiteralToken(token.substring(i, j), context.subContext(i,
				j));
	}
	/**
	 * @return {@code true} if the token is meaningful syntactically
	 */
	public boolean isMeaningful() {
		if (token.length() == 0) return false;
		if (token.charAt(0) == '[') return false;
		if (Character.isWhitespace(token.charAt(0))) return false;
		return true;
	}
	/**
	 * @return this token, but with an extra space at the end (for tokenization
	 *         simplicity purposes) TODO replace with encapsulation
	 */
	public LiteralToken padWithSpace() {
		return new LiteralToken(token + " ", context);
	}
	/**
	 * @return this token, but unescaped (if it is a literal string)
	 */
	public LiteralToken unescape() {
		if (token.charAt(0) != '\'') return this;
		String str = token.substring(1, token.length() - 1);
		return new LiteralToken('\'' + Tokenizer.unescape(str) + '\'',
				context);
	}
	public boolean doesEqual(String token) {
		return this.token.equals(token);
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
		LiteralToken other = (LiteralToken) obj;
		if (token == null) {
			if (other.token != null) return false;
		} else if (!token.equals(other.token)) return false;
		return true;
	}
	@Override
	public String toString() {
		return token;
	}
	@Override
	public String toSourceCode() {
		return token;
	}
	@Override
	public Context context() {
		return context;
	}
}
