package fortytwo.compiler;

import java.util.List;

public class Context {
	public static Context construct(Context parent, int start, int end) {
		// TODO Auto-generated method stub
		return new Context();
	}
	public static Context minimal() {
		// TODO Auto-generated method stub
		return new Context();
	}
	public static Context synthetic() {
		// TODO Auto-generated method stub
		return new Context();
	}
	public static Context sum(List<Token> items) {
		// TODO Auto-generated method stub
		return new Context();
	}
	public Context inParen() {
		// TODO Auto-generated method stub
		return new Context();
	}
	public Context subContext(int i, int j) {
		// TODO Auto-generated method stub
		return new Context();
	}
}
