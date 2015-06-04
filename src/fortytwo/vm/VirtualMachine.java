package fortytwo.vm;

public interface VirtualMachine {
	static final VirtualMachine DEBUGGER = new VirtualMachine() {
		@Override
		public void displayLine(String s) {
			System.out.print(s);
			System.out.print(System.lineSeparator());
			// I'm writing it like this so that it doesn't show up in my log
			// search/deletes
		}
	};
	public static final VirtualMachine defaultVM = DEBUGGER;
	public void displayLine(String s);
}
