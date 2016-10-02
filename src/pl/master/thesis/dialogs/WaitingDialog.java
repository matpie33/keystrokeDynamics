package pl.master.thesis.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import pl.master.thesis.buttons.MyLabel;
import pl.master.thesis.panels.BasicPanel;
import pl.master.thesis.panels.PanelWelcome;

public class WaitingDialog extends JDialog{
	
	private static final long serialVersionUID = 8106079110799645572L;
	private String gifDirectory = "images/load.gif";
	private JLabel text;
	private JPanel p;
	private JLabel gif;
	private int gapx=20;
	private int gapy=20;
	
	public WaitingDialog (BasicPanel parent){
		setUpDialog(parent);
		setTitle("title");
	}

	private void setUpDialog(BasicPanel parent){
		URL url;
		try {			
			url = new URL("file:"+gifDirectory);
			ImageIcon imageIcon = new ImageIcon(url);
			gif = new JLabel(imageIcon);
			text = new MyLabel("Connecting to the database");
						
			p = new JPanel(new BorderLayout(gapx,gapy));
			p.setBackground(Color.BLACK);
			p.add(text,BorderLayout.CENTER);
			p.add(gif,BorderLayout.SOUTH);
			add(p);
			pack();
			setLocationRelativeTo(parent);
			setModal(true);
			
			
			
		} 
		catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
	}
	
	public void setLabelText (String text){
		this.text.setText(text);
		update();
	}
	
	public void removeGif(){
		p.remove(gif);
		update();	
	}
	
	public void addButton (JButton button){
		p.add(button, BorderLayout.SOUTH);
		update();
	}
	
	private void update(){
		revalidate();
		repaint();
		pack();
	}

}
