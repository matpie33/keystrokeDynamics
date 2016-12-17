package pl.master.thesis.listeners;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;

public class FocusListeners {

	public static FocusListener createListenerDefaultValueIfEmpty (final JTextField field){
		final String oldValue = field.getText();
		return new FocusListener (){
			@Override
			public void focusGained (FocusEvent e){
				if (field.getText().equals(oldValue))
				field.setText("");
			}
			
			@Override
			public void focusLost (FocusEvent e){
				if (field.getText().isEmpty())
				field.setText(oldValue);
				
			}
		};	
	}
}
