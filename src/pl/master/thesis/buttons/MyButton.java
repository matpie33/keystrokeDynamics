package pl.master.thesis.buttons;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import pl.master.thesis.frame.MainWindow;
import pl.master.thesis.panels.BasicPanel;

public class MyButton extends JButton{
	
	private static final long serialVersionUID = -2392177191789587997L;
	private final Color defaultColor;
	private final Color hoverColor;
	private final Color clickedColor;
	private MainWindow frame;
	
	public MyButton (MainWindow frame){
		
		this.frame=frame;
		addListeners();
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));				
		defaultColor=Color.YELLOW;
		hoverColor=Color.GREEN;		
		clickedColor = new Color (51,102,0);
		setContentAreaFilled(false);
		setFocusPainted(false);
		setBorderPainted(false);
		setBackground(defaultColor);
	}
	
	public MyButton(MainWindow frame,String name){
		this(frame);
		setText(name);	
	}
	
	@Override
    protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		RenderingHints rh = new RenderingHints(
	             RenderingHints.KEY_TEXT_ANTIALIASING,
	             RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	    g2d.setRenderingHints(rh);
        g2d.setColor(getBackground());    
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 50, 50);
        super.paintComponent(g2d);
    }
	
	public void setDestinationPanel (final BasicPanel panel){
		addActionListener(new ActionListener (){
			@Override
			public void actionPerformed (ActionEvent e){
				frame.nextPanel();				
			}
		});
	}
	
	private void addListeners (){
		addMouseListener(new MouseAdapter (){
			@Override
			public void mouseEntered(MouseEvent e){
				setBackground(hoverColor);
			}
			public void mouseExited (MouseEvent e){
				setBackground(defaultColor);
			}
			public void mousePressed (MouseEvent e){
				setBackground(clickedColor);
			}
		});
	}

}
