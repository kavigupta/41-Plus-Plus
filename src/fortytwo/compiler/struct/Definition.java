package fortytwo.compiler.struct;

import java.util.List;

import lib.standard.collections.Pair;
import fortytwo.compiler.language.Language;

public class Definition implements Statement42 {
	public final String name, type;
	public final List<Pair<String, String>> fields;
	public static Statement42 getInstance(String variableName, String type,
			List<Pair<String, String>> fields) {
		return new Definition(variableName, type, fields);
	}
	private Definition(String variableName, String type,
			List<Pair<String, String>> fields) {
		this.name = variableName;
		this.type = type;
		this.fields = fields;
	}
	@Override
	public String toString() {
		return "Define "
				+ Language.articleized(type)
				+ " called "
				+ name
				+ (fields.size() == 0 ? "" : " with ")
				+ Language.sayList(fields, f -> Language.articleized(f.key)
						+ " of " + f.value);
	}
}
