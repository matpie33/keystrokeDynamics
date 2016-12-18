package pl.master.thesis.listeners;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

public class KeyListeners {

	public static KeyAdapter createKeyAdapterMaximumLength(final JTextField field, final int limit){
		return new KeyAdapter(){
			@Override
			public void keyTyped (KeyEvent e){
				if (field.getText().length()>=limit && field.getSelectedText()==null){
					e.consume();
				}
			}
		};	
	}
	
	public static void addKeyBindings (KeyStroke key, AbstractAction action, JComponent... c){
		for (JComponent c1: c){
			c1.getInputMap().put(key, "name");
			c1.getActionMap().put("name", action);
		}
		
	}
	
}
