package fortytwo.compiler.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import fortytwo.compiler.Context;
import fortytwo.compiler.LiteralToken;
import fortytwo.compiler.parsed.Sentence;
import fortytwo.compiler.parsed.declaration.FunctionConstruct;
import fortytwo.compiler.parsed.declaration.FunctionDefinition;
import fortytwo.compiler.parsed.expressions.Expression;
import fortytwo.compiler.parsed.statements.IfElse;
import fortytwo.compiler.parsed.statements.Statement;
import fortytwo.compiler.parsed.statements.Suite;
import fortytwo.compiler.parsed.statements.WhileLoop;
import fortytwo.language.Resources;

public class Parser {
	private Parser() {}
	public static List<Sentence> parse(String text) {
		final List<Sentence> sentences = parseSuite(getTabbedSuite(text));
		return sentences;
	}
	private static List<Pair<Integer, List<LiteralToken>>> getTabbedSuite(
			String text) {
		final List<Pair<Integer, List<LiteralToken>>> tabbedLines = new ArrayList<>();
		for (final String line : text.split("\r|\n")) {
			int initTabs = 0;
			for (int i = 0; i < line.length(); i++) {
				if (line.charAt(i) != '\t') break;
				initTabs++;
			}
			final List<List<LiteralToken>> unparsedSentences = findSentences(
					line);
			int extraTabs = 0;
			for (int i = 0; i < unparsedSentences.size(); i++) {
				if (unparsedSentences.get(i).isEmpty()) continue;
				tabbedLines.add(Pair.of(Integer.valueOf(initTabs + extraTabs),
						unparsedSentences.get(i)));
				switch (unparsedSentences.get(i).get(0).token) {
					case Resources.IF:
					case Resources.WHILE:
					case Resources.OTHERWISE:
						extraTabs++;
						break;
					case Resources.DEFINE:
						if (unparsedSentences.get(0).get(2).token
								.equals(Resources.DECL_FUNCTION))
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
		final List<LiteralToken> tokens = Tokenizer
				.tokenize(LiteralToken.entire(line));
		final List<List<LiteralToken>> phrases = new ArrayList<>();
		List<LiteralToken> current = new ArrayList<>();
		for (int i = 0; i < tokens.size(); i++) {
			final LiteralToken token = tokens.get(i);
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
		final List<Sentence> sentences = new ArrayList<>();
		while (suite.size() != 0)
			sentences.add(pop(suite));
		return sentences;
	}
	public static Sentence pop(
			List<Pair<Integer, List<LiteralToken>>> phrases) {
		if (phrases.size() == 0)
			return new Suite(Arrays.asList(),
					Context.SYNTHETIC);
		switch (phrases.get(0).getValue().get(0).token) {
			case Resources.IF:
				return popIf(phrases);
			case Resources.WHILE:
				return popWhile(phrases);
			case Resources.DEFINE:
				if (phrases.get(0).getValue().get(2).token
						.equals(Resources.DECL_FUNCTION))
					return popFunctionDecl(phrases);
				//$FALL-THROUGH$
			default:
				return popSentence(phrases);
		}
	}
	public static Sentence popFunctionDecl(
			List<Pair<Integer, List<LiteralToken>>> phrases) {
		final List<LiteralToken> defn = phrases.remove(0).getValue();
		defn.remove(defn.size() - 1);
		final FunctionDefinition declaration = ConstructionParser
				.parseFunctionDefinition(defn);
		final List<Sentence> suite = popSeries(phrases);
		return new FunctionConstruct(declaration, suite);
	}
	public static Sentence popSentence(
			List<Pair<Integer, List<LiteralToken>>> phrases) {
		return StatementParser.parseStatement(phrases.remove(0).getValue());
	}
	public static Sentence popIf(
			List<Pair<Integer, List<LiteralToken>>> phrases) {
		final List<LiteralToken> IF = phrases.remove(0).getValue();
		IF.remove(0);
		final Expression condition = ExpressionParser.parseExpression(IF);
		final Suite ifso = temporaryHack(popSeries(phrases));
		Suite ifelse = new Suite(
				Arrays.asList(), Context.SYNTHETIC);
		if (phrases.size() > 0 && phrases.get(0).getValue().get(0).token
				.equals(Resources.OTHERWISE)) {
			phrases.remove(0); // This should just be "Otherwise:"
			ifelse = temporaryHack(popSeries(phrases));
		}
		return IfElse.getInstance(condition, ifso, ifelse);
	}
	private static Sentence popWhile(
			List<Pair<Integer, List<LiteralToken>>> phrases) {
		final List<LiteralToken> WHILE = phrases.remove(0).getRight();
		WHILE.remove(0);
		final Expression condition = ExpressionParser.parseExpression(WHILE);
		final Suite whileTrue = temporaryHack(
				popSeries(phrases));
		return new WhileLoop(condition, whileTrue, Context
				.sum(Arrays.asList(condition.context(), whileTrue.context())));
	}
	public static Suite temporaryHack(
			List<Sentence> popSeries) {
		// TODO temporary hack
		final List<Statement> statement = new ArrayList<>();
		for (final Sentence s : popSeries)
			statement.add((Statement) s);
		return new Suite(statement, Context.sum(popSeries));
	}
	@SuppressWarnings("boxing")
	private static List<Sentence> popSeries(
			List<Pair<Integer, List<LiteralToken>>> phrases) {
		if (phrases.size() == 0) return new ArrayList<>();
		final int tabs = phrases.get(0).getKey();
		final List<Sentence> sentences = new ArrayList<>();
		while (phrases.size() > 0) {
			if (phrases.get(0).getKey() < tabs) break;
			final Sentence s = pop(phrases);
			sentences.add(s);
		}
		return sentences;
	}
}