package pl.master.thesis.dialogs;

import java.awt.GridBagConstraints;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import pl.master.thesis.guiElements.MyButton;
import pl.master.thesis.guiElements.MyLabel;
import pl.master.thesis.listeners.ActionListeners;
import pl.master.thesis.others.ElementsMaker;
import pl.master.thesis.others.MyColors;
import pl.master.thesis.others.PanelCreator;
import pl.master.thesis.panels.BasicPanel;
import pl.master.thesis.strings.Prompts;
import pl.master.thesis.strings.WindowLabels;

public class MyDialog extends JDialog{
	
	private static final long serialVersionUID = 8106079110799645572L;
	private String gifDirectory = "images/load.gif";
	private JLabel text;
	private JPanel p;
	private JLabel gif;
	private int gapx=20;
	private int gapy=20;
	private BasicPanel parent;
	private PanelCreator mainPanel;
	
	public MyDialog (BasicPanel parent){
		setUpDialog(parent);
		
	}

	private void setUpDialog(BasicPanel parent){
		mainPanel = new PanelCreator(MyColors.LIGHT_BLUE);
		this.parent = parent;				
	}
	
	public void createWaitingPanel(){
		try {
			createGifLabel();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		text = new MyLabel(Prompts.CONNECTING_PROMPT);
		mainPanel.createRow(GridBagConstraints.CENTER,1, text);
		mainPanel.createRow(GridBagConstraints.CENTER,1, gif);
		setProperties();
	}
	
	public void createMsgDialog(String text){
			
		JTextArea prompt = ElementsMaker.createTextArea(text, 10, 30);		
		JScrollPane j = ElementsMaker.wrapComponent(prompt);
		
		MyButton confirm = ElementsMaker.createButton(Prompts.BTN_APPROVE, 
				ActionListeners.createDisposeListener(this));
		
		mainPanel.createRow(1,j);
		mainPanel.createRow(GridBagConstraints.CENTER,1, confirm);
		setProperties();
	}
	
	
	private void createGifLabel() throws MalformedURLException{
		URL url = new URL("file:"+gifDirectory);
		ImageIcon imageIcon = new ImageIcon(url);
		gif = new JLabel(imageIcon);
	}
	
	
	private void setProperties(){
		setContentPane(mainPanel.getPanel());
		pack();
		setLocationRelativeTo(parent.getPanel());
		setModal(true);
		setTitle(WindowLabels.CONNECTING_TO_DB_TITLE);		
	}
	
	public void setLabelText (String text){
		MyButton confirm = ElementsMaker.createButton(Prompts.BTN_APPROVE, 
				ActionListeners.createDisposeListener(this));
		mainPanel.setAsRow(0, new MyLabel(text));
		mainPanel.setAsRow(1, confirm);	
		pack();
		setLocationRelativeTo(parent.getPanel());
	}
	
	public void removeGif(){
		update();	
	}
	
	public void addButton (JButton button){
		update();
	}
	
	private void update(){
		revalidate();
		repaint();
		pack();
	}

}
