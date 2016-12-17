package pl.master.thesis.others;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class MainPanel {
	
	private List<JPanel> rows;
	private JPanel panel;

	public MainPanel(Color color) {
//		super();
		panel = new JPanel();
		panel.setBackground(color);
		panel.setLayout(new GridBagLayout());
		rows = new LinkedList<JPanel>();
	}

	
	public void createRow(JComponent ... components){
		JPanel p = addComponentsToSinglePanel(components);
		createConstraintsAndAdd(p, 0);
		updateView();
	}

	public void createRow(int anchor, int weighty, JComponent... components ) {
		JPanel p = addComponentsToSinglePanel(components);
		createConstraintsAndAdd(p, anchor, weighty);
		updateView();

	}

	private JPanel addComponentsToSinglePanel(JComponent[] components) {
		JPanel p = new JPanel();
		p.setOpaque(false);
		p.setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.WEST;

		int a = 15;
		gbc.insets = new Insets(0, 0, 0, a);
		int i=0;
		int size = components.length;
		for (JComponent c : components) {
			if (c instanceof JScrollPane) {
				gbc.fill = GridBagConstraints.BOTH;
				gbc.weightx = 1;
				gbc.weighty = 1;
			}
			else if (c instanceof JButton || c instanceof JLabel){
				gbc.weightx = 0;
				gbc.weighty = 0;
			}
			if (i==size-1)
			    gbc.weightx=0.5;

			p.add(c, gbc);
			i++;
		}
		return p;
	}

	private void createConstraintsAndAdd(JPanel p, int anchor, int weighty) {
		GridBagConstraints c = createConstraints(rows.size());		
		c.anchor = anchor;
		c.weightx = 1;
		panel.add(p, c);
		rows.add(p);
	}
	
	private void createConstraintsAndAdd(JPanel p,int weighty) {
		GridBagConstraints c = createConstraints(rows.size());		
		c.weightx = 1;
		c.fill= GridBagConstraints.BOTH;
		panel.add(p, c);
		rows.add(p);
	}
	
	private GridBagConstraints createConstraints (int rowNumber){
	    	GridBagConstraints c = new GridBagConstraints();
		c.gridy = rowNumber;		
		int a = 5;
		c.insets = new Insets(a, a, a, a);
		return c;
	}


	public MainPanel setAsRow(int number, JComponent... components) {
    	if (rows.size()<number+1){
		    createRow(GridBagConstraints.WEST, 1, components);
		    return this;
		}
	    
		GridBagLayout g = (GridBagLayout) panel.getLayout();
		GridBagConstraints c = new GridBagConstraints();
		
		JPanel row = rows.get(number);
		c = g.getConstraints(row);
		
		panel.remove(row);
		rows.remove(row);
		
		JPanel asRow = addComponentsToSinglePanel(components);
		panel.add(asRow, c);
		rows.add(number, asRow);
		updateView();
		return this;
	}
	
	public void removeRow (int number){
	    	JPanel row = rows.get(number);
	    	movePanels(Direction.BACKWARD, number);
	    	panel.remove(row);
		rows.remove(row);
		updateView();
	}
	
	private enum Direction {
	    FORWARD,BACKWARD;
	}
	
	private void movePanels (Direction direction, int startIndex){
	    for (int i=rows.size()-1; i>=startIndex; i--){
	    	GridBagLayout g = (GridBagLayout) panel.getLayout();
		JPanel row = rows.get(i);		
		GridBagConstraints c = g.getConstraints(row);
		if (direction.equals(Direction.FORWARD)){
		    c.gridy++;
		}
		else if (direction.equals(Direction.BACKWARD)){
		    c.gridy--;
		}
		
		panel.remove(row);
		panel.add(row, c);
    	    }
	}
	
	public SubPanel divideRow (int number){
		SubPanel p = new SubPanel ();
		rows.add(p.getPanel());
		GridBagConstraints c = createConstraints(number);
		c.weightx=1;
		c.weighty=1;
		panel.add(p.getPanel(), c);
		return p;
	}
	
	
	public void insertRow (int number, JComponent ... components){
	    movePanels(Direction.FORWARD,number);	    	
		JPanel newRow = addComponentsToSinglePanel(components);
		GridBagConstraints c = createConstraints(number);
		c.weightx=1;
		c.weighty=1;
		panel.add(newRow, c);
		rows.add(number, newRow);	
		updateView();
	}
	
	public int getNumberOfRows(){
	    return rows.size();
	}
	
	public List <JPanel> getRows(){
	    return rows;
	}
	
	private void updateView(){
		panel.repaint();
		panel.revalidate();
	}
	
	public JPanel getPanel(){
		return panel;
	}
	
}
