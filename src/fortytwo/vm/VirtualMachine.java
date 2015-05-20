package fortytwo.vm;

public interface VirtualMachine {
	public static final VirtualMachine DEBUGGER = new VirtualMachine() {
		@Override
		public void println(String s) {
			System.out.println(s);
		}
	};
	public void println(String s);
}
