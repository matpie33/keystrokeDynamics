package pl.master.thesis.listeners;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;

import pl.master.thesis.keyEventHandler.KeyEventHandler;
import pl.master.thesis.keyEventHandler.SavingMode;

public class FocusListeners {

	public static FocusListener defaultValueIfEmpty (final JTextField field){
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
	
	public static FocusListener switchSavingMode (final JTextField field, final KeyEventHandler handler, final SavingMode mode){
		return new FocusListener (){
			@Override
			public void focusGained (FocusEvent e){
				handler.setMode(mode);
			}
			
			@Override
			public void focusLost (FocusEvent e){
				handler.setMode(SavingMode.USERDATA);				
			}
		};	
	}
	
	
}
