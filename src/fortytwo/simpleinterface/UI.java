package fortytwo.simpleinterface;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;

import lib.standard.io.IO;
import fortytwo.compiler.Compiler42;
import fortytwo.vm.VirtualMachine;

public class UI {
	public static void main(String[] args) {
		JFrame frame = new JFrame("41++ executor");
		frame.getContentPane().setLayout(
				new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		frame.add(new JLabel("Put a path to your 41++ source code file below"));
		JTextField path = new JTextField();
		frame.add(path);
		JButton execute = new JButton("Execute Code");
		execute.addActionListener(e -> execute(frame, path.getText()));
		frame.add(execute);
		frame.setVisible(true);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	private static void execute(JFrame parent, String path) {
		ArrayList<String> str;
		try {
			str = IO.readLines(new File(path));
		} catch (Throwable t) {
			JOptionPane
					.showMessageDialog(
							parent,
							"Error in reading from the file "
									+ path
									+ ". Are you sure you typed in the path correctly?",
							"File Read Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		StringBuffer text = new StringBuffer();
		str.forEach(s -> text.append(s).append(System.lineSeparator()));
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(path
					+ "-output.txt"));
			Compiler42.execute(text.toString(), new VirtualMachine() {
				@Override
				public void displayLine(String s) {
					try {
						System.out.println("Writing " + s);
						bw.write(s);
						bw.write(System.lineSeparator());
					} catch (IOException e) {
						errorInWritingTo(parent, path + "-output.txt");
						e.printStackTrace();
						return;
					}
				}
			});
			bw.close();
			bw.close();
		} catch (IOException e) {
			errorInWritingTo(parent, path + "-output.txt");
			return;
		} catch (RuntimeException e) {
			errorInCode(parent);
			e.printStackTrace();
			return;
		}
		JOptionPane.showMessageDialog(parent,
				"Code run successfully. See your output at " + path,
				"Success!", JOptionPane.INFORMATION_MESSAGE);
	}
	private static void errorInCode(JFrame parent) {
		JOptionPane
				.showMessageDialog(
						parent,
						"There was an error in your code. I'm sorry I can't be more descriptive but that part of the program hasn't been done yet. Sorry.",
						"Error in code.", JOptionPane.ERROR_MESSAGE);
	}
	private static void errorInWritingTo(JFrame parent, String path) {
		JOptionPane
				.showMessageDialog(
						parent,
						"Error in writing to the file"
								+ path
								+ ". Please make sure it is not being edited in another program.",
						"File Write Error", JOptionPane.ERROR_MESSAGE);
	}
}
