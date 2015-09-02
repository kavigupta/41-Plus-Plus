package fortytwo.compiler.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;

import fortytwo.compiler.Context;
import fortytwo.compiler.LiteralToken;
import fortytwo.compiler.parsed.Sentence;
import fortytwo.compiler.parsed.expressions.Expression;
import fortytwo.compiler.parsed.statements.ParsedIfElse;
import fortytwo.compiler.parsed.statements.ParsedStatement;
import fortytwo.compiler.parsed.statements.ParsedStatementSeries;
import fortytwo.compiler.parsed.statements.ParsedWhileLoop;
import fortytwo.language.Resources;
import fortytwo.vm.errors.ParserErrors;

public class Parser {
	private Parser() {}
	public static List<Sentence> parse(String text) {
		List<Pair<Integer, List<LiteralToken>>> suite = getTabbedSuite(text);
		return parseSuite(suite);
	}
	private static List<Pair<Integer, List<LiteralToken>>> getTabbedSuite(
			String text) {
		List<Pair<Integer, List<LiteralToken>>> tabbedLines = new ArrayList<>();
		for (String line : text.split("\r|\n")) {
			int initTabs = 0;
			for (int i = 0; i < line.length(); i++) {
				if (line.charAt(i) != '\t') break;
				initTabs++;
			}
			List<List<LiteralToken>> unparsedSentences = findSentences(line);
			System.out.println("SINGLE LINE:" + unparsedSentences);
			int extraTabs = 0;
			for (int i = 0; i < unparsedSentences.size(); i++) {
				if (unparsedSentences.get(i).isEmpty()) continue;
				tabbedLines.add(Pair.of(initTabs + extraTabs,
						unparsedSentences.get(i)));
				switch (unparsedSentences.get(i).get(0).token) {
					case Resources.IF:
					case Resources.WHILE:
					case Resources.OTHERWISE:
						extraTabs++;
						break;
					default:
						if (extraTabs != 0) extraTabs--;
				}
			}
		}
		return tabbedLines;
	}
	private static List<List<LiteralToken>> findSentences(String line) {
		List<LiteralToken> tokens = Tokenizer
				.tokenize(LiteralToken.entire(line));
		List<List<LiteralToken>> phrases = new ArrayList<>();
		List<LiteralToken> current = new ArrayList<>();
		for (int i = 0; i < tokens.size(); i++) {
			LiteralToken token = tokens.get(i);
			if (!token.token.equals(Resources.COLON)) current.add(token);
			if (token.token.equals(Resources.PERIOD)
					|| token.token.equals(Resources.COLON)) {
				phrases.add(current);
				current = new ArrayList<>();
			}
		}
		return phrases;
	}
	private static List<Sentence> parseSuite(
			List<Pair<Integer, List<LiteralToken>>> suite) {
		List<Sentence> sentences = new ArrayList<>();
		while (suite.size() != 0) {
			sentences.add(pop(suite));
		}
		return sentences;
	}
	public static Sentence pop(
			List<Pair<Integer, List<LiteralToken>>> phrases) {
		if (phrases.size() == 0)
			return new ParsedStatementSeries(Arrays.asList(),
					Context.SYNTHETIC);
		switch (phrases.get(0).getValue().get(0).token) {
			case Resources.IF:
				return popIf(phrases);
			case Resources.WHILE:
				return popWhile(phrases);
			default:
				return popSentence(phrases);
		}
	}
	public static Sentence popSentence(
			List<Pair<Integer, List<LiteralToken>>> phrases) {
		return StatementParser.parseStatement(phrases.remove(0).getValue());
	}
	public static Sentence popIf(
			List<Pair<Integer, List<LiteralToken>>> phrases) {
		System.out.println(phrases);
		List<LiteralToken> IF = phrases.remove(0).getValue();
		IF.remove(0);
		Expression condition = ExpressionParser.parseExpression(IF);
		ParsedStatementSeries ifso = popSeries(phrases);
		ParsedStatementSeries ifelse = new ParsedStatementSeries(
				Arrays.asList(), Context.SYNTHETIC);
		if (phrases.size() > 0 && phrases.get(0).getValue().get(0).token
				.equals(Resources.OTHERWISE)) {
			phrases.remove(0); // This should just be "Otherwise:"
			ifelse = popSeries(phrases);
		}
		return ParsedIfElse.getInstance(condition, ifso, ifelse);
	}
	private static Sentence popWhile(
			List<Pair<Integer, List<LiteralToken>>> phrases) {
		List<LiteralToken> WHILE = phrases.remove(0).getRight();
		WHILE.remove(0);
		Expression condition = ExpressionParser.parseExpression(WHILE);
		ParsedStatementSeries whileTrue = popSeries(phrases);
		return new ParsedWhileLoop(condition, whileTrue, Context
				.sum(Arrays.asList(condition.context(), whileTrue.context())));
	}
	private static ParsedStatementSeries popSeries(
			List<Pair<Integer, List<LiteralToken>>> phrases) {
		if (phrases.size() == 0)
			return new ParsedStatementSeries(Arrays.asList(),
					Context.SYNTHETIC);
		int tabs = phrases.get(0).getKey();
		// TODO REMOVE semantic limitation of no declarations within suites.
		List<ParsedStatement> sentences = new ArrayList<>();
		while (phrases.size() > 0) {
			if (phrases.get(0).getKey() < tabs) break;
			Sentence s = pop(phrases);
			// TODO here
			if (!(s instanceof ParsedStatement))
				ParserErrors.expectedStatement(s);
			sentences.add((ParsedStatement) s);
		}
		return new ParsedStatementSeries(sentences, Context.sum(sentences
				.stream().map(x -> x.context()).collect(Collectors.toList())));
	}
}