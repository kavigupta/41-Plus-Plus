package fortytwo.ide.gui;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import fortytwo.ide.environment.GUILinkedEnvironment;

public class Console42 extends JDialog {
	private static final long serialVersionUID = 1L;
	private final JPanel contentPane;
	private final RSyntaxTextArea line;
	private final LineHistory history;
	private final GUILinkedEnvironment environ;
	private int pointer = 0;
	public Console42(JFrame owner, GUILinkedEnvironment environ) {
		super(owner, "41++ Console");
		this.environ = environ;
		history = environ.history;
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		line = ComponentFactory.getLine42(true, this::exec);
		line.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				// no-op deliberately
			}
			@Override
			public void keyPressed(KeyEvent e) {
				// no-op deliberately
			}
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_UP
						|| e.getKeyCode() == KeyEvent.VK_KP_UP)
					movePointer(+1);
				else if (e.getKeyCode() == KeyEvent.VK_DOWN) movePointer(-1);
			}
		});
		contentPane.add(line, BorderLayout.SOUTH);
		contentPane.add(history, BorderLayout.CENTER);
		setContentPane(contentPane);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				owner.dispatchEvent(
						new WindowEvent(owner, WindowEvent.WINDOW_CLOSING));
			}
		});
	}
	public void movePointer(int direction) {
		final int size = history.nCommands();
		if (size == 0) return;
		if (pointer != history.nCommands())
			history.set(pointer, line.getText());
		// System.out.printf("Cached %s at location %s.", line.getText(),
		// pointer);
		pointer += -direction;
		if (pointer == size || pointer == -1) {
			pointer = size;
			line.setText("");
			return;
		}
		if (pointer < 0) pointer += size - 1;
		if (pointer > size) pointer -= size + 1;
		line.setText(history.get(pointer).replaceAll("[\r\n]", ""));
	}
	public void exec() {
		final String cmd = line.getText();
		history.displayCommand(cmd);
		line.setText("");
		try {
			environ.refresh();
			environ.execute(cmd);
		} catch (final Exception e) {
			if (!e.getMessage().startsWith("~")) e.printStackTrace();
		}
		pointer = history.nCommands();
	}
}
