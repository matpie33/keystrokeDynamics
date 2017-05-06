package pl.master.thesis.guiElements;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;

import pl.master.thesis.others.MyColors;

public class MyButton extends JButton {

	private static final long serialVersionUID = -2392177191789587997L;
	private final Color defaultColor;
	private final Color hoverColor;
	private final Color clickedColor;

	public MyButton() {

		addListeners();
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		defaultColor = MyColors.ORANGE;
		hoverColor = MyColors.YELLOW;
		clickedColor = MyColors.DARK_ORANGE;
		setContentAreaFilled(false);
		setFocusPainted(false);
		setBorderPainted(false);
		setBackground(defaultColor);
	}

	public MyButton(String name) {
		this();
		setText(name);
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		RenderingHints rh = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2d.setRenderingHints(rh);
		g2d.setColor(getBackground());
		g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 20);
		super.paintComponent(g2d);
	}

	private void addListeners() {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				setBackground(hoverColor);
			}

			public void mouseExited(MouseEvent e) {
				setBackground(defaultColor);
			}

			public void mousePressed(MouseEvent e) {
				setBackground(clickedColor);
			}
		});
	}

}
