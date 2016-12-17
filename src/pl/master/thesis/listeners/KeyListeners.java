package pl.master.thesis.listeners;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JTextField;

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
}
