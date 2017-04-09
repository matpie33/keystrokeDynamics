package pl.master.thesis.guiElements;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;

public class ErrorLabel extends JLabel {

	private static final long serialVersionUID = 754204639716407901L;

	public ErrorLabel() {
		super();
		setFont(new Font("Segoe ui light", Font.BOLD, 15));
		setForeground(Color.RED);

	}

	public ErrorLabel(String text) {
		this();
		setText(text);
	}

}
