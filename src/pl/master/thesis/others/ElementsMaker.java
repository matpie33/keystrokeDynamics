package pl.master.thesis.others;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import pl.master.thesis.buttons.MyButton;
import pl.master.thesis.buttons.MyLabel;
import pl.master.thesis.listeners.KeyListeners;

public class ElementsMaker {
	
	private static Map <JTextField, MyLabel> hmap;
	
	public static void setTextFieldToLabelMap(Map <JTextField, MyLabel> map){
		hmap = map;
	}
	
	public static JTextArea createTextArea (String text, int rows, int columns){
		JTextArea textArea = new JTextArea(text, rows, columns);
		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(true);
		textArea.setOpaque(false);
		textArea.setForeground(Color.WHITE);
		return textArea;
	}
	
	public static JTextArea createWelcomeMessage (String text){
		JTextArea hello = new JTextArea ();	
		hello.setText(text);
		hello.setLineWrap(true);
		hello.setWrapStyleWord(true);
		hello.setEditable(false);
		hello.setOpaque(false);
		hello.setHighlighter(null);		
		return hello;
	}
	
	public static JScrollPane wrapComponent(JComponent c){
		JScrollPane j = new JScrollPane(c);
		j.getViewport().setOpaque(false);
		j.setBackground(Color.BLACK);
		return j;
	}
	
	public static MyButton createButton (String title, ActionListener action){
		MyButton confirm = new MyButton (title);
		confirm.addActionListener(action);
		return confirm;
	}
	
	public static MyLabel createLabel (String title){
		return new MyLabel(title);
	}

	public static JTextField createTextField (String textFieldType, int limit){
		JTextField textField = FieldsVerifier.findTextField(textFieldType, hmap);	
		textField.addKeyListener(KeyListeners.createKeyAdapterMaximumLength(textField, limit));
		return textField;
	}
}
