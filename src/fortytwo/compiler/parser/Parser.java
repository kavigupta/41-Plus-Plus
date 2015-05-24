package fortytwo.compiler.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import fortytwo.compiler.Context;
import fortytwo.compiler.Token;
import fortytwo.compiler.parsed.expressions.ParsedExpression;
import fortytwo.compiler.parsed.sentences.Sentence;
import fortytwo.compiler.parsed.statements.ParsedIfElse;
import fortytwo.compiler.parsed.statements.ParsedStatement;
import fortytwo.compiler.parsed.statements.ParsedStatementSeries;
import fortytwo.compiler.parsed.statements.ParsedWhileLoop;
import fortytwo.language.Language;
import fortytwo.language.Resources;

public class Parser {
	private Parser() {}
	public static List<Sentence> parse(String text) {
		List<String> tokens = tokenize42(text);
		List<List<String>> phrases = new ArrayList<>();
		List<String> current = new ArrayList<>();
		for (int i = 0; i < tokens.size(); i++) {
			String token = tokens.get(i);
			current.add(token);
			if (token.equals(Resources.PERIOD)
					|| token.equals(Resources.COLON)) {
				phrases.add(current);
				current = new ArrayList<>();
			}
		}
		return parse(phrases);
	}
	public static List<Sentence> parse(List<List<String>> phrases) {
		List<Sentence> sentences = new ArrayList<>();
		while (phrases.size() != 0) {
			sentences.add(pop(phrases));
		}
		return sentences;
	}
	public static Sentence pop(List<List<String>> phrases) {
		if (phrases.size() == 0)
			return new ParsedStatementSeries(Arrays.asList());
		switch (phrases.get(0).get(0)) {
			case Resources.IF:
				return popIf(phrases);
			case Resources.WHILE:
				return popWhile(phrases);
			case Resources.DO:
				return popSeries(phrases);
			default:
				return popSentence(phrases);
		}
	}
	public static Sentence popSentence(List<List<String>> phrases) {
		return StatementParser.parseStatement(phrases.remove(0));
	}
	public static Sentence popIf(List<List<String>> phrases) {
		List<String> IF = phrases.remove(0);
		IF.remove(0);
		ParsedExpression condition = ExpressionParser.parseExpression(IF);
		ParsedStatementSeries ifso = popSeries(phrases);
		ParsedStatementSeries ifelse = new ParsedStatementSeries(
				Arrays.asList());
		if (phrases.size() > 0
				&& phrases.get(0).get(0).equals(Resources.OTHERWISE)) {
			phrases.remove(0); // This should just be "Otherwise:"
			ifelse = popSeries(phrases);
		}
		return ParsedIfElse.getInstance(condition, ifso, ifelse);
	}
	private static Sentence popWhile(List<List<String>> phrases) {
		List<String> WHILE = phrases.remove(0);
		WHILE.remove(0);
		ParsedStatementSeries whileTrue = popSeries(phrases);
		return new ParsedWhileLoop(ExpressionParser.parseExpression(WHILE),
				whileTrue);
	}
	private static ParsedStatementSeries popSeries(List<List<String>> phrases) {
		if (phrases.size() == 0)
			return new ParsedStatementSeries(Arrays.asList());
		if (!Language.isOpeningBrace(phrases.get(0))) {
			Sentence sent = pop(phrases);
			if (!(sent instanceof ParsedStatement))
				throw new RuntimeException(/* LOWPRI-E */);
			return ParsedStatementSeries.getInstance((ParsedStatement) sent);
		}
		phrases.remove(0); // remove brace
		List<List<String>> inBraces = new ArrayList<>();
		int braces = 1;
		while (phrases.size() > 0) {
			if (Language.isOpeningBrace(phrases.get(0))) braces++;
			if (Language.isClosingBrace(phrases.get(0))) {
				phrases.remove(0);
				braces--;
				if (braces == 0) {
					List<Sentence> sentences = parse(inBraces);
					List<ParsedStatement> statements = sentences
							.stream()
							.map(x -> {
								if (x instanceof ParsedStatement)
									return (ParsedStatement) x;
								throw new RuntimeException(/* LOWPRI-E */);
							}).collect(Collectors.toList());
					return new ParsedStatementSeries(statements);
				}
			}
			inBraces.add(phrases.remove(0));
		}
		throw new RuntimeException(/* LOWPRI-E */);
	}
	public static List<Token> tokenize42(String text) {
		// text = text.replaceAll(Resources.PAD_FIND, Resources.PAD_REPLACE)
		// .trim() + Resources.SPACE;
		// return tokenizeRecursive(text).stream()
		// .filter(x -> x.trim().length() != 0)
		// .collect(Collectors.toList());
		return Tokenizer.tokenize(Context.minimal(), text).stream()
				.filter(t -> t.token.trim().length() != 0)
				.collect(Collectors.toList());
	}
}