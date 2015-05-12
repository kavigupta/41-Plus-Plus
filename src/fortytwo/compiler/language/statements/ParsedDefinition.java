package fortytwo.compiler.language.statements;

import java.util.ArrayList;

import lib.standard.collections.Pair;
import fortytwo.compiler.language.expressions.ParsedExpression;

public class ParsedDefinition implements ParsedStatement {
	public final String type, name;
	public final ArrayList<Pair<String, ParsedExpression>> fields;
	public ParsedDefinition(String type, String name,
			ArrayList<Pair<String, ParsedExpression>> fields) {
		this.type = type;
		this.name = name;
		this.fields = fields;
	}
}