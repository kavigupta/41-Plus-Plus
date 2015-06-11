package fortytwo.ide.gui;

import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import fortytwo.ide.environment.GUILinkedEnvironment;

public class Console42 extends JDialog {
	private JPanel contentPane;
	private RSyntaxTextArea line;
	private LineHistory history;
	private GUILinkedEnvironment environ;
	public Console42(JFrame owner, GUILinkedEnvironment environ) {
		super(owner, "41++ Console");
		this.environ = environ;
		history = environ.history;
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		line = ComponentFactory.getLine42(true, this::exec);
		contentPane.add(line, BorderLayout.SOUTH);
		contentPane.add(history, BorderLayout.CENTER);
		setContentPane(contentPane);
		addWindowListener(new WindowListener() {
			@Override
			public void windowOpened(WindowEvent e) {}
			@Override
			public void windowIconified(WindowEvent e) {}
			@Override
			public void windowDeiconified(WindowEvent e) {}
			@Override
			public void windowDeactivated(WindowEvent e) {}
			@Override
			public void windowClosing(WindowEvent e) {
				owner.dispatchEvent(new WindowEvent(owner,
						WindowEvent.WINDOW_CLOSING));
			}
			@Override
			public void windowClosed(WindowEvent e) {}
			@Override
			public void windowActivated(WindowEvent e) {}
		});
	}
	public void exec() {
		String cmd = line.getText();
		history.displayCommand(cmd);
		line.setText("");
		try {
			environ.refresh();
			environ.execute(cmd);
		} catch (Exception e) {
			if (!e.getMessage().startsWith("~")) e.printStackTrace();
		}
	}
}
