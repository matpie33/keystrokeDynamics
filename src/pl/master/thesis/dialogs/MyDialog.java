package pl.master.thesis.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import pl.master.thesis.buttons.MyButton;
import pl.master.thesis.buttons.MyLabel;
import pl.master.thesis.frame.MainWindow;
import pl.master.thesis.panels.BasicPanel;
import pl.master.thesis.panels.PanelWelcome;
import pl.master.thesis.strings.Prompts;

public class MyDialog extends JDialog{
	
	private static final long serialVersionUID = 8106079110799645572L;
	private String gifDirectory = "images/load.gif";
	private JLabel text;
	private JPanel p;
	private JLabel gif;
	private int gapx=20;
	private int gapy=20;
	private BasicPanel parent;
	
	public MyDialog (BasicPanel parent){
		setUpDialog(parent);
		
	}

	private void setUpDialog(BasicPanel parent){
		this.parent = parent;				
	}
	
	public void createWaitingPanel(){
		createBorderPanel();
		try {
			createGifLabel();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		text = new MyLabel(Prompts.CONNECTING_PROMPT);
		p.add(text,BorderLayout.CENTER);
		p.add(gif,BorderLayout.SOUTH);
		setProperties();
	}
	
	public void createMsgDialog(String text){
		
		createGridBagPanel();
		GridBagConstraints c = new GridBagConstraints();
		
		
		JTextArea prompt = new JTextArea(text,10,30);
		prompt.setWrapStyleWord(true);
		prompt.setLineWrap(true);
		prompt.setOpaque(false);
		prompt.setForeground(Color.WHITE);
		
		JScrollPane j = new JScrollPane(prompt);
		j.getViewport().setOpaque(false);
		j.setBackground(Color.BLACK);
		
		MainWindow frame = ((BasicPanel)parent).getParentFrame();
		MyButton confirm = new MyButton (frame, Prompts.BTN_APPROVE);
		confirm.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				dispose();
			}
		});
		
		c.gridx = 0;
		c.gridy = 0;	
		c.weightx = 1;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		int a =5;
		c.insets = new Insets(a,a,a,a);
		p.add(j, c);
		
		c.weightx = 0;
		c.weighty = 0;
		c.fill = GridBagConstraints.NONE;
		c.gridy++;
		c.anchor = GridBagConstraints.CENTER;
		p.add(confirm, c);
		setProperties();
	}
	
	private void createGridBagPanel(){
		p = new JPanel(new GridBagLayout());
		
	}
	
	private void createGifLabel() throws MalformedURLException{
		URL url = new URL("file:"+gifDirectory);
		ImageIcon imageIcon = new ImageIcon(url);
		gif = new JLabel(imageIcon);
	}
	
	private void createBorderPanel(){
		p = new JPanel(new BorderLayout(gapx,gapy));
		
	}
	
	private void setProperties(){
		p.setBackground(Color.BLACK);
		add(p);	
		pack();
		setLocationRelativeTo(parent);
		setModal(true);
		setTitle("Title");
		
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
