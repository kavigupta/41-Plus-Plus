package fortytwo.compiler.language.statements;

import java.util.ArrayList;

import lib.standard.collections.Pair;
import fortytwo.compiler.language.expressions.Expression;

public class Definition implements Statement {
	public final String type, name;
	public final ArrayList<Pair<String, Expression>> fields;
	public Definition(String type, String name,
			ArrayList<Pair<String, Expression>> fields) {
		this.type = type;
		this.name = name;
		this.fields = fields;
	}
}
