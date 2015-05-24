package fortytwo.compiler;


public class Token {
	public final String token;
	public final Context context;
	public Token(String line, Context context) {
		this.token = line;
		this.context = context;
	}
}
