package fortytwo.ide.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import javax.swing.*;

public class InternalFrameDemo extends JFrame {
	JDesktopPane desktop;
	public InternalFrameDemo() {
		super("InternalFrameDemo");
		final int inset = 50;
		final Dimension screenSize = Toolkit.getDefaultToolkit()
				.getScreenSize();
		setBounds(inset, inset, screenSize.width - inset * 2,
				screenSize.height - inset * 2);
		// Set up the GUI.
		desktop = new JDesktopPane(); // a specialized layered pane
		createFrame(); // create first "window"
		setContentPane(desktop);
	}
	protected void createFrame() {
		final JInternalFrame frame = new JInternalFrame("Document");
		frame.setLocation(0, 0);
		frame.setSize(200, 100);
		frame.setVisible(true);
		frame.getContentPane().add(createButton());
		desktop.add(frame);
		try {
			frame.setSelected(true);
		} catch (final java.beans.PropertyVetoException e) {}
	}
	private JButton createButton() {
		return new JButton(new AbstractAction("New") {
			@Override
			public void actionPerformed(final ActionEvent e) {
				createFrame();
			}
		});
	}
	private static void createAndShowGUI() {
		JFrame.setDefaultLookAndFeelDecorated(true);
		final InternalFrameDemo frame = new InternalFrameDemo();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	public static void main(final String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				createAndShowGUI();
			}
		});
	}
}