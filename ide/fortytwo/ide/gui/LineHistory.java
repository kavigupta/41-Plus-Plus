package fortytwo.ide.gui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import fortytwo.vm.errors.Error42;
import fortytwo.vm.expressions.LiteralExpression;

public class LineHistory extends JScrollPane {
	private JPanel entry;
	private int index = 0;
	public LineHistory() {
		super();
		entry = new JPanel();
		entry.setLayout(new GridBagLayout());
		this.setViewportView(entry);
	}
	public void put(Component comp) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.weightx = 1.0;
		gbc.gridwidth = 10;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.gridy = index;
		index++;
		entry.add(comp, gbc);
	}
	public void displayCommand(String cmd) {
		cmd = cmd.replaceAll("[\\r\\n]", "");
		System.out.println("Command " + cmd + ";");
		RSyntaxTextArea command = ComponentFactory.getLine42(false, () -> {});
		command.setText(cmd);
		put(command);
	}
	public void displayOutput(LiteralExpression literalValue) {
		// TODO Auto-generated method stub
	}
	public void displayln(String line) {
		// TODO auto-generated method stub
	}
	public void displayerr(Error42 err) {
		put(new JLabel("<html><red>" + err.msg + "</red></html>"));
		// TODO auto-generated method stub
	}
}
