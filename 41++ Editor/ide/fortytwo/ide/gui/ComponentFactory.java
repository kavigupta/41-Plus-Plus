package fortytwo.ide.gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import org.fife.ui.rsyntaxtextarea.AbstractTokenMakerFactory;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.TokenMakerFactory;

public class ComponentFactory {
	public static RSyntaxTextArea get42() {
		final RSyntaxTextArea textArea = new RSyntaxTextArea();
		final AbstractTokenMakerFactory atmf = (AbstractTokenMakerFactory) TokenMakerFactory
				.getDefaultInstance();
		atmf.putMapping("text/41++", "fortytwo.ide.highlighting.Highlighter42");
		textArea.setSyntaxEditingStyle("text/41++");
		return textArea;
	}
	public static RSyntaxTextArea getEditorBox42() {
		final RSyntaxTextArea textArea = get42();
		textArea.setEditable(true);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		return textArea;
	}
	public static RSyntaxTextArea getLine42(boolean editable,
			Runnable onEnter) {
		final RSyntaxTextArea textArea = get42();
		textArea.setEditable(editable);
		textArea.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_ENTER) onEnter.run();
			}
			@Override
			public void keyReleased(KeyEvent e) {
				// deliberate no-op
			}
			@Override
			public void keyPressed(KeyEvent e) {
				// deliberate no-op
			}
		});
		return textArea;
	}
}
