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
import fortytwo.vm.errors.ParserErrors;

public class Parser {
	private Parser() {}
	public static List<Sentence> parse(String text) {
		List<Token> tokens = Tokenizer.tokenize(Context.minimal(), text);
		List<List<Token>> phrases = new ArrayList<>();
		List<Token> current = new ArrayList<>();
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			current.add(token);
			if (token.token.equals(Resources.PERIOD)
					|| token.token.equals(Resources.COLON)) {
				phrases.add(current);
				current = new ArrayList<>();
			}
		}
		return parse(phrases);
	}
	public static List<Sentence> parse(List<List<Token>> phrases) {
		List<Sentence> sentences = new ArrayList<>();
		while (phrases.size() != 0) {
			sentences.add(pop(phrases));
		}
		return sentences;
	}
	public static Sentence pop(List<List<Token>> phrases) {
		if (phrases.size() == 0)
			return new ParsedStatementSeries(Arrays.asList());
		switch (phrases.get(0).get(0).token) {
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
	public static Sentence popSentence(List<List<Token>> phrases) {
		return StatementParser.parseStatement(phrases.remove(0));
	}
	public static Sentence popIf(List<List<Token>> phrases) {
		List<Token> IF = phrases.remove(0);
		IF.remove(0);
		ParsedExpression condition = ExpressionParser.parseExpression(IF);
		ParsedStatementSeries ifso = popSeries(phrases);
		ParsedStatementSeries ifelse = new ParsedStatementSeries(
				Arrays.asList());
		if (phrases.size() > 0
				&& phrases.get(0).get(0).token.equals(Resources.OTHERWISE)) {
			phrases.remove(0); // This should just be "Otherwise:"
			ifelse = popSeries(phrases);
		}
		return ParsedIfElse.getInstance(condition, ifso, ifelse);
	}
	private static Sentence popWhile(List<List<Token>> phrases) {
		List<Token> WHILE = phrases.remove(0);
		WHILE.remove(0);
		ParsedStatementSeries whileTrue = popSeries(phrases);
		return new ParsedWhileLoop(ExpressionParser.parseExpression(WHILE),
				whileTrue);
	}
	private static ParsedStatementSeries popSeries(List<List<Token>> phrases) {
		if (phrases.size() == 0)
			return new ParsedStatementSeries(Arrays.asList());
		if (!Language.isOpeningBrace(phrases.get(0).stream()
				.map(x -> x.token).collect(Collectors.toList()))) {
			Sentence sent = pop(phrases);
			if (!(sent instanceof ParsedStatement))
				ParserErrors.expectedStatement(sent);
			return ParsedStatementSeries.getInstance((ParsedStatement) sent);
		}
		List<Token> openingBrace = phrases.remove(0); // remove brace
		List<List<Token>> inBraces = new ArrayList<>();
		int braces = 1;
		while (phrases.size() > 0) {
			List<String> phr = phrases.get(0).stream().map(x -> x.token)
					.collect(Collectors.toList());
			if (Language.isOpeningBrace(phr)) braces++;
			if (Language.isClosingBrace(phr)) {
				phrases.remove(0);
				braces--;
				if (braces == 0) {
					List<Sentence> sentences = parse(inBraces);
					List<ParsedStatement> statements = sentences
							.stream()
							.map(x -> {
								if (!(x instanceof ParsedStatement))
									ParserErrors.expectedStatement(x);
								return (ParsedStatement) x;
							}).collect(Collectors.toList());
					return new ParsedStatementSeries(statements);
				}
			}
			inBraces.add(phrases.remove(0));
		}
		ParserErrors.closeVerbalBraceNotFound(openingBrace);
		// should never be reached.
		return null;
	}
}