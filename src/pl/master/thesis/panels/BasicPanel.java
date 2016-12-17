package pl.master.thesis.panels;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;

import pl.master.thesis.buttons.ErrorLabel;
import pl.master.thesis.buttons.MyButton;
import pl.master.thesis.frame.MainWindow;

public class BasicPanel extends JPanel{
	
	private static final long serialVersionUID = 1L;
	protected MyButton btnContinue;
	protected MyButton btnBack;
	protected MainWindow frame;
	protected BasicPanel previous;
	protected BasicPanel next;
	protected Insets fieldInsets=new Insets(0,10,5,10);
	protected Insets verticalInsets=new Insets(0,20,5,20);	
	protected Insets labelInsets = new Insets (0,20,5,5);
	protected final Color color;
	protected GridBagConstraints c;
	protected boolean isErrorShowing;
	protected JLabel errorLabel;
	
	
	public BasicPanel (final MainWindow frame){
		
		errorLabel = new ErrorLabel();
		isErrorShowing=false;
		this.frame=frame;	
		setLayout(new GridBagLayout());
		color=new Color(102,0,102);
		setBackground(color);

		c=new GridBagConstraints();
		c.insets=verticalInsets;
		c.gridx=0;
		c.gridy=0;
		
		btnContinue = new MyButton ("Dalej");
		btnBack = new MyButton ("Wstecz");
			
		setNextPanel();
		setPreviousPanel();
		
	}
	
	private void setNextPanel (){
		btnContinue.addActionListener(new ActionListener (){
			@Override
			public void actionPerformed (ActionEvent e){				
				frame.nextPanel();				
			}
		});
	}
	
	private void setPreviousPanel (){
		btnBack.addActionListener(new ActionListener (){
			@Override
			public void actionPerformed (ActionEvent e){
				frame.previousPanel();				
			}
		});
	}
	/**
	 * @param amount shows how much rows each row should be shifted; it can be negative value as well - in that case
	 * shift is done up direction, however if any component ends up with gridy negative we dont shift anything
	 */
	public void moveElementsDown (int amount, int gridy){
		GridBagLayout layout = (GridBagLayout)getLayout();
		Map <Component, GridBagConstraints> map = new HashMap <Component, GridBagConstraints>();
		
		for (int i=0; i<this.getComponentCount(); i++){
			
			Component component = this.getComponent(i);	
			GridBagConstraints c = layout.getConstraints(component);
			if (c.gridy<gridy) continue;
								
			c.gridy=c.gridy+amount;
			if (c.gridy<0) return;
			map.put(component, c);
		}
		
		for (Map.Entry<Component, GridBagConstraints> entries: map.entrySet()){
			add(entries.getKey(), entries.getValue());
		}
		repaint();
		revalidate();
	}
	
	public MainWindow getParentFrame(){
		return frame;
	}
	

	

}
