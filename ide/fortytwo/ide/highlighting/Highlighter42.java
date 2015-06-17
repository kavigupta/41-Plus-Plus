package fortytwo.ide.highlighting;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.swing.text.Segment;

import org.fife.ui.rsyntaxtextarea.AbstractTokenMaker;
import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenMap;

import fortytwo.compiler.Context;
import fortytwo.compiler.Token42;
import fortytwo.compiler.parser.Tokenizer;
import fortytwo.language.Language;
import fortytwo.language.Resources;
import fortytwo.vm.VirtualMachine;
import fortytwo.vm.errors.Error42;

public class Highlighter42 extends AbstractTokenMaker {
	/**
	 * Returns a list of tokens representing the given text.
	 *
	 * @param text The text to break into tokens.
	 * @param startTokenType The token with which to start tokenizing.
	 * @param startOffset The offset at which the line of tokens begins.
	 * @return A linked list of tokens representing <code>text</code>.
	 */
	@Override
	public Token getTokenList(Segment text, int startTokenType, int startOffset) {
		resetTokenList();
		// Token starting offsets are always of the form:
		// 'startOffset + (currentTokenStart-offset)', but since startOffset
		// and
		// offset are constant, tokens' starting positions become:
		// 'newStartOffset+currentTokenStart'.
		// disable the error handler
		Consumer<Error42> errorHandler = VirtualMachine.displayerr;
		VirtualMachine.displayerr = x -> System.err.println(x);
		String code = new String(text.array, text.offset, text.count);
		addAllTokens(
				Tokenizer.tokenizeFully(Context.entire(code), code)
						.stream().filter(x -> x.token.length() != 0)
						.collect(Collectors.toList()), text, startOffset);
		addNullToken();
		// reset the error handler
		VirtualMachine.displayerr = errorHandler;
		// Return the first token in our linked list.
		return firstToken;
	}
	private void addAllTokens(List<Token42> tokens, Segment text,
			int startOffset) {
		if (tokens.size() == 0) return;
		Token42 error = null;
		int errorStart = 0;
		if (tokens.get(tokens.size() - 1).context == null) {
			error = tokens.remove(tokens.size() - 1);
			if (tokens.size() != 0)
				errorStart = tokens.get(tokens.size() - 1).context.end;
		}
		for (int i = 0; i < tokens.size(); i++) {
			Token42 tok = tokens.get(i);
			if (tok.token.startsWith("(")) {
				Token42 depar = Language.deparenthesize(tok);
				addParenToken(tok.context.start, text, startOffset);
				addAllTokens(Tokenizer.tokenizeFully(depar.context,
						depar.token), text, startOffset);
				addParenToken(tok.context.end - 1, text, startOffset);
			} else {
				addToken(tokens, i, text, startOffset);
			}
		}
		if (error != null)
			addErrorToken(error, text, startOffset, errorStart);
	}
	public void addToken(List<Token42> tokens, int index, Segment text,
			int startOffset) {
		int currentTokenStart = tokens.get(index).context.start + text.offset;
		int currentTokenEnd = tokens.get(index).context.end + text.offset;
		addToken(text.array, currentTokenStart, currentTokenEnd - 1,
				classify(tokens, index), startOffset
						+ (currentTokenStart - text.offset));
	}
	public void addParenToken(int location, Segment text, int startOffset) {
		int currentTokenStart = location + text.offset;
		int currentTokenEnd = location + text.offset;
		addToken(text.array, currentTokenStart, currentTokenEnd,
				Token.SEPARATOR, startOffset
						+ (currentTokenStart - text.offset));
	}
	public void addErrorToken(Token42 error, Segment text, int startOffset,
			int tokenStart) {
		int currentTokenStart = tokenStart + text.offset;
		int currentTokenEnd = currentTokenStart + error.token.length();
		addToken(text.array, currentTokenStart, currentTokenEnd - 2,
				Token.ERROR_IDENTIFIER, startOffset
						+ (currentTokenStart - text.offset));
	}
	private static int classify(List<Token42> tokens, int index) {
		if (tokens.get(index).token.length() == 0) return Token.IDENTIFIER;
		// Anything that can be determined just based on the first character
		// is handled first.
		switch (tokens.get(index).token.charAt(0)) {
			case ' ':
			case '\t':
			case '\r':
			case '\n':
			case '\f':
				return Token.WHITESPACE;
			case '\'':
				return Token.LITERAL_STRING_DOUBLE_QUOTE;
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
				return Token.LITERAL_NUMBER_DECIMAL_INT;
			case '[':
				return Token.COMMENT_MARKUP;
			case '"':
				return Token.VARIABLE;
			case '.':
			case ':':
			case ',':
				return Token.SEPARATOR;
			case '+':
			case '-':
			case '*':
			case '/':
			case '%':
				return Token.OPERATOR;
		}
		// anything that can be determined by itself is handled second
		switch (tokens.get(index).token) {
			case "Define":
			case "Set":
			case "Exit":
			case "Run":
			case "While":
			case "If":
			case "Otherwise":
			case "Do":
			case "That's":
				return Token.RESERVED_WORD;
			case "number":
			case "bool":
			case "string":
			case "type":
			case "function":
				return Token.DATA_TYPE;
			case "a":
			case "an":
			case "to":
			case "that":
			case "takes":
			case "output":
			case "following":
			case "with":
			case "outputs":
				return Token.RESERVED_WORD_2;
			case "value":
				return Token.VARIABLE;
		}
		String prev = previousWord(tokens, index);
		String next = nextWord(tokens, index);
		switch (tokens.get(index).token) {
			case "called":
			case "the":
				if (Language.isValidVariableIdentifier(next)
						|| Resources.DO.equals(prev)
						|| Resources.DECL_FUNCTION.equals(prev))
					return Token.RESERVED_WORD_2;
				else break;
			case "of":
				if (Language.isValidVariableIdentifier(prev))
					return Token.RESERVED_WORD_2;
				else break;
			case "all":
				if ("That's".equals(prev))
					return Token.RESERVED_WORD_2;
				else break;
		}
		return Token.IDENTIFIER;
	}
	private static String nextWord(List<Token42> tokens, int index) {
		for (int i = index + 1; i < tokens.size(); i++) {
			if (tokens.get(i).token.matches("\\s+")) continue;
			return tokens.get(i).token;
		}
		return "";
	}
	private static String previousWord(List<Token42> tokens, int index) {
		for (int i = index - 1; i >= 0; i--) {
			if (tokens.get(i).token.matches("\\s+")) continue;
			return tokens.get(i).token;
		}
		return "";
	}
	@Override
	public TokenMap getWordsToHighlight() {
		return new TokenMap();
	}
	@Override
	public void addToken(char[] segment, int start, int end, int tokenType,
			int startOffset) {
		// This assumes all keywords, etc. were parsed as "identifiers."
		if (tokenType == Token.IDENTIFIER) {
			int value = wordsToHighlight.get(segment, start, end);
			if (value != -1) {
				tokenType = value;
			}
		}
		super.addToken(segment, start, end, tokenType, startOffset);
	}
}
