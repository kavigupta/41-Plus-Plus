package fortytwo.vm;

public interface VirtualMachine {
	static final VirtualMachine DEBUGGER = new VirtualMachine() {
		@Override
		public void println(String s) {
			System.out.println(s);
		}
	};
	public static final VirtualMachine defaultVM = DEBUGGER;
	public void println(String s);
}
