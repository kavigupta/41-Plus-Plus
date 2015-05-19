package fortytwo.vm.environment;

import java.util.ArrayList;

import fortytwo.compiler.parsed.sentences.Sentence;

public class GlobalEnvironment {
	public final StaticEnvironment staticEnv;
	public final FunctionRoster funcs;
	private GlobalEnvironment(StaticEnvironment staticEnv) {
		this.staticEnv = staticEnv;
		funcs = new FunctionRoster();
	}
	public static GlobalEnvironment getDefaultEnvironment(
			StaticEnvironment environment) {
		// TODO Default environment with default structures and whatnot
		return null;
	}
	public void initializeEnvironment(ArrayList<Sentence> sentences) {
		// TODO Auto-generated method stub
	}
	public LocalEnvironment minimalLocalEnvironment() {
		return new LocalEnvironment(this);
	}
}
