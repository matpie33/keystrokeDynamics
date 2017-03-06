package pl.master.thesis.listeners;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import pl.master.thesis.keyEventHandler.KeyEventHandler;

public class MouseListeners {
	
	public static MouseListener notStartedWithTabKeyIfClicked (final KeyEventHandler handler){
		return new MouseAdapter (){
			@Override
			public void mouseClicked (MouseEvent e){
				System.out.println("mouse click");
				if (e.getComponent()==null){
					return;
				}
				handler.textFieldClicked(e.getComponent());
			}
		};	
	}

}
