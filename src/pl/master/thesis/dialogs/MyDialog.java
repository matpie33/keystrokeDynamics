package pl.master.thesis.dialogs;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.guimaker.panels.MainPanel;
import com.guimaker.row.RowMaker;

import pl.master.thesis.guiElements.MyButton;
import pl.master.thesis.guiElements.MyLabel;
import pl.master.thesis.listeners.ActionListeners;
import pl.master.thesis.others.ElementsMaker;
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
	private MainPanel mainPanel;
	
	public MyDialog (BasicPanel parent){
		setUpDialog(parent);
	}

	private void setUpDialog(BasicPanel parent){
		mainPanel = new MainPanel(Color.BLACK);
		this.parent = parent;				
	}
	
	public void createWaitingDialog(){
		try {
			createGifLabel();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		text = new MyLabel(Prompts.CONNECTING_PROMPT);
		mainPanel.addRow(RowMaker.createUnfilledRow(GridBagConstraints.CENTER, text));
		mainPanel.addRow(RowMaker.createUnfilledRow(GridBagConstraints.CENTER, gif));
		setProperties();
	}
	
	public void createLongMessageDialog(String text){
		JTextArea prompt = ElementsMaker.createTextArea(text, 10, 30);		
		prompt.setEditable(false);
		JScrollPane j = ElementsMaker.wrapComponent(prompt);
		createMsgDialog(j);
	}
	
	private void createMsgDialog(JComponent textComponent){
		MyButton confirm = ElementsMaker.createButton(Prompts.BTN_APPROVE, 
				ActionListeners.createDisposeListener(this));
		
		mainPanel.addRow(RowMaker.createUnfilledRow(GridBagConstraints.CENTER, textComponent));
		mainPanel.addRow(RowMaker.createUnfilledRow(GridBagConstraints.CENTER, confirm));
		setProperties();
	}
	
	public void createShortMessageDialog(String text){
		JLabel label = ElementsMaker.createLabel(text);
		createMsgDialog(label);
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
		mainPanel.addRow(RowMaker.createHorizontallyFilledRow(new MyLabel(text)));
		mainPanel.addRow(RowMaker.createUnfilledRow(GridBagConstraints.CENTER, confirm));
		pack();
		setLocationRelativeTo(parent.getPanel());
	}
	
	public void removeGif(){
		mainPanel.removeRowWithElements(gif);
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
