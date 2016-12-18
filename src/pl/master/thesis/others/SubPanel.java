package pl.master.thesis.others;

import java.awt.GridLayout;

import javax.swing.JPanel;

public class SubPanel {
	
	private PanelCreator leftPanel;
	private PanelCreator rightPanel;
	private JPanel panel;
	
	public SubPanel (){
		super();
		panel = new JPanel();
		panel.setLayout(new GridLayout(1,2));
		leftPanel = new PanelCreator(MyColors.LIGHT_VIOLET);
		rightPanel = new PanelCreator(MyColors.LIGHT_BLUE);
		panel.add (leftPanel.getPanel());
		panel.add (rightPanel.getPanel());
	}
	
	public PanelCreator getLeft (){
		return leftPanel;
	}
	
	public PanelCreator getRight(){
		return rightPanel;
	}
	
	public JPanel getPanel(){
		return panel;
	}

}
