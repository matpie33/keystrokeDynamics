package pl.master.thesis.buttons;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;

public class ErrorLabel extends JLabel{
		
	private static final long serialVersionUID = 754204639716407901L;
		private Font myFont;
		private Color myColor;
		
		public ErrorLabel (){
			super();
			myFont = new Font("Segoe ui light",Font.BOLD, 15);
			myColor = Color.RED;
			setFont (myFont);
			setForeground (myColor);
			
		}
		
		public ErrorLabel (String text){
			this();
			setText(text);
		}
		
}
	

