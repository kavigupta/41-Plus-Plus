package fortytwo.vm.expressions;

import java.util.function.Consumer;

import lib.standard.collections.Pair;
import fortytwo.compiler.Context;
import fortytwo.compiler.parsed.constructions.ParsedVariableRoster;
import fortytwo.language.SourceCode;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.ConcreteType;
import fortytwo.vm.constructions.Structure;

public class LiteralObject extends LiteralExpression {
	public final Structure struct;
	private final ParsedVariableRoster<LiteralExpression> fields;
	public LiteralObject(Structure struct,
			ParsedVariableRoster<LiteralExpression> fields, Context context) {
		super(context);
		this.struct = struct;
		this.fields = fields;
	}
	public void redefine(VariableIdentifier name, LiteralExpression express) {
		fields.redefine(name, express);
	}
	public int nFields() {
		return fields.numberOfVariables();
	}
	public void forEachField(
			Consumer<Pair<VariableIdentifier, LiteralExpression>> le) {
		fields.pairs.forEach(le);
	}
	public LiteralExpression valueOf(VariableIdentifier field) {
		return fields.referenceTo(field);
	}
	@Override
	public ConcreteType resolveType() {
		return struct.type;
	}
	@Override
	public String toSourceCode() {
		return SourceCode.display(this);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fields == null) ? 0 : fields.hashCode());
		result = prime * result + ((struct == null) ? 0 : struct.hashCode());
		return result;
	}
	@Override
	public boolean typedEquals(LiteralExpression obj) {
		LiteralObject other = (LiteralObject) obj;
		if (fields == null) {
			if (other.fields != null) return false;
		} else if (!fields.equals(other.fields)) return false;
		if (struct == null) {
			if (other.struct != null) return false;
		} else if (!struct.equals(other.struct)) return false;
		return true;
	}
}
