package pl.master.thesis.listeners;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;

import pl.master.thesis.keyEventHandler.KeyEventHandler;

public class FocusListeners {

	public static FocusListener defaultValueIfEmpty (final JTextField field, final KeyEventHandler handler){
		final String oldValue = field.getText();
		return new FocusListener (){
			@Override
			public void focusGained (FocusEvent e){
				System.out.println("focus gained");
				if (field.getText().equals(oldValue))
				field.setText("");
			}
			
			@Override
			public void focusLost (FocusEvent e){
				handler.focusLost();
				if (field.getText().isEmpty())
				field.setText(oldValue);
				
			}
		};	
	}
	
	public static FocusListener switchSavingMode (final KeyEventHandler handler){
		return new FocusAdapter (){
			@Override
			public void focusGained (FocusEvent e){
				handler.textFieldClicked(e.getComponent());
			}
			
		};	
	}
	
	
}
