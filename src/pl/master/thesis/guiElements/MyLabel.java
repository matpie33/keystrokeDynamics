package pl.master.thesis.guiElements;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;

public class MyLabel extends JLabel {

	private Font myFont;
	private Color myColor;

	public MyLabel() {
		super();
		myFont = new Font("Segoe ui light", Font.BOLD, 15);
		myColor = Color.WHITE;
		setFont(myFont);
		setForeground(myColor);

	}

	public MyLabel(String text) {
		this();
		setText(text);
	}

}
