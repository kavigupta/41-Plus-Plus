package fortytwo.compiler;

import java.util.List;

import fortytwo.compiler.parsed.Contextualized;

/**
 * An object representing the position in a string of a token.
 */
public class Context implements Contextualized {
	/**
	 * A context representing a "synthetic" context, such as a value created by
	 * a natively implemented function.
	 */
	public static final Context SYNTHETIC = new Context("", -1, -1);
	private final String in;
	/**
	 * The starting position within the string.
	 */
	private final int start;
	/**
	 * The ending position within the string
	 */
	private final int end;
	/**
	 * 
	 * @param offset
	 *        the offset of the internal string in the original text
	 * @return the starting position in the original text
	 */
	public int getStartPosition(int offset) {
		return this.start + offset;
	}
	/**
	 * 
	 * @param offset
	 *        the offset of the internal string in the original text
	 * @return the ending position in the original text
	 */
	public int getEndPosition(int offset) {
		return this.end + offset;
	}
	private boolean isSynthetic() {
		return this == SYNTHETIC;
	}
	/**
	 * @return a context representing this token in parentheses.
	 */
	public Context inParen() {
		if (isSynthetic()) return SYNTHETIC;
		assert start - 1 >= 0;
		return new Context(in, start - 1, end + 1);
	}
	/**
	 * @return a context representing this context with a unary + or - in
	 *         front.
	 */
	public Context withUnaryApplied() {
		if (isSynthetic()) return SYNTHETIC;
		assert start - 1 >= 0;
		return new Context(in, start - 1, end);
	}
	/**
	 * @param start
	 *        the beginning of the subContext with respect to this
	 *        context's start.
	 * @param end
	 *        the end of the subContext with respect to this context's
	 *        start.
	 * @return a context that spans [start, end) of this context's span.
	 */
	public Context subContext(int start, int end) {
		if (isSynthetic()) return SYNTHETIC;
		assert start >= 0;
		assert end >= 0;
		assert this.start + end < this.end;
		return new Context(this.in, this.start + start, this.start + end);
	}
	public Context removeLast() {
		return end == start ? this : subContext(0, end - start - 1);
	}
	/**
	 * @param text
	 *        the text to be turned into a token
	 * @return the Context of the entire text, treated as a single token.
	 */
	public static Context entire(String text) {
		return new Context(text, 0, text.length());
	}
	/**
	 * @param arguments
	 *        contexts, perhaps attached to other data, to be summed.
	 * @return the sum of the given contexts, currently implemented as a simple
	 *         hull of the union of their intervals.
	 */
	public static Context sum(List<? extends Contextualized> asList) {
		if (asList.size() == 0) return SYNTHETIC;
		return asList.stream().map(Contextualized::context).reduce(Context::sum)
				.get();
	}
	private static Context sum(Context a, Context b) {
		if (a.isSynthetic() || b.isSynthetic()) return SYNTHETIC;
		assert a.in.equals(b.in);
		return new Context(a.in, Math.min(a.start, b.start),
				Math.max(a.end, b.end));
	}
	private Context(String in, int start, int end) {
		this.in = in;
		this.start = start;
		this.end = end;
	}
	@Override
	public Context context() {
		return this;
	}
	@Override
	public String toString() {
		return isSynthetic() ? "Synthetic"
				: in.substring(Math.max(start - 10, 0), start) + "~"
						+ in.substring(start, end) + "~"
						+ in.substring(end, Math.min(end + 10, in.length()));
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + end;
		result = prime * result + start;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		final Context other = (Context) obj;
		if (end != other.end) return false;
		if (start != other.start) return false;
		return true;
	}
}
