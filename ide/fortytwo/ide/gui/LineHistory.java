package fortytwo.ide.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

public class LineHistory extends JScrollPane {
	private JPanel entry;
	private int index = 0;
	public LineHistory() {
		super();
		entry = new JPanel();
		entry.setLayout(new GridBagLayout());
		this.setViewportView(entry);
	}
	public void addCommand(String cmd) {
		cmd = cmd.replaceAll("[\\r\\n]", "");
		System.out.println("Command " + cmd + ";");
		RSyntaxTextArea command = ComponentFactory.getLine42(false, () -> {});
		command.setText(cmd);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.weightx = 1.0;
		gbc.gridwidth = 10;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.gridy = index;
		index++;
		entry.add(command, gbc);
	}
}
