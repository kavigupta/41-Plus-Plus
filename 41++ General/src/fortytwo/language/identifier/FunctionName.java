package fortytwo.language.identifier;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import fortytwo.compiler.Context;
import fortytwo.compiler.LiteralToken;
import fortytwo.language.identifier.functioncomponent.FunctionArgument;
import fortytwo.language.identifier.functioncomponent.FunctionComponent;
import fortytwo.language.identifier.functioncomponent.FunctionToken;
import fortytwo.language.type.ConcreteType;

public class FunctionName {
	public final List<FunctionComponent> function;
	public static FunctionName getInstance(String... name) {
		return getInstance(Arrays.asList(name).stream()
				.map(x -> x.length() == 0 ? FunctionArgument.INSTANCE
						: new FunctionToken(LiteralToken.entire(x)))
				.collect(Collectors.toList()));
	}
	public static FunctionName getInstance(LiteralToken... name) {
		return getInstance(Arrays.asList(name)
				.stream().map(x -> x.token.length() == 0
						? FunctionArgument.INSTANCE : new FunctionToken(x))
				.collect(Collectors.toList()));
	}
	public static FunctionName getInstance(List<FunctionComponent> function) {
		return new FunctionName(function);
	}
	private FunctionName(List<FunctionComponent> function) {
		this.function = function;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (function == null ? 0 : function.hashCode());
		return result;
	}
	public Context context() {
		return Context.sum(function.stream().map(FunctionComponent::context)
				.collect(Collectors.toList()));
	}
	public String display(List<ConcreteType> types) {
		int count = 0;
		final StringBuffer sbuff = new StringBuffer();
		for (final FunctionComponent comp : function)
			if (comp instanceof FunctionToken)
				sbuff.append(((FunctionToken) comp).token.token).append(" ");
			else {
				sbuff.append("{").append(types.get(count).toSourceCode())
						.append("} ");
				count++;
			}
		if (sbuff.length() == 0) return "";
		return sbuff.substring(0, sbuff.length() - 1);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		final FunctionName other = (FunctionName) obj;
		if (function == null) {
			if (other.function != null) return false;
		} else if (!function.equals(other.function)) return false;
		return true;
	}
	@Override
	public String toString() {
		return function.toString();
	}
}
