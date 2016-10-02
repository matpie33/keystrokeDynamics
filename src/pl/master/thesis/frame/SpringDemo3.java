package pl.master.thesis.frame;

import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import java.awt.Container;

public class SpringDemo3 {
	/**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("SpringDemo3");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        //Set up the content pane.
        JPanel p =new JPanel();
        
        SpringLayout layout = new SpringLayout();
        p.setLayout(layout);
        p.setLayout(layout);
 
        //Create and add the components.
        JLabel label = new JLabel("Label: ");
        JTextField textField = new JTextField("Text field", 15);
        p.add(label);
        p.add(textField);
 
        //Adjust constraints for the label so it's at (5,5).
        layout.putConstraint(SpringLayout.WEST, label,
                             5,
                             SpringLayout.WEST, p);
        layout.putConstraint(SpringLayout.NORTH, label,
                             5,
                             SpringLayout.NORTH, p);
 
        //Adjust constraints for the text field so it's at
        //(<label's right edge> + 5, 5).
        layout.putConstraint(SpringLayout.WEST, textField,
                             5,
                             SpringLayout.EAST, label);
        layout.putConstraint(SpringLayout.NORTH, textField,
                             5,
                             SpringLayout.NORTH, p);
 
        //Adjust constraints for the content pane: Its right
        //edge should be 5 pixels beyond the text field's right
        //edge, and its bottom edge should be 5 pixels beyond
        //the bottom edge of the tallest component (which we'll
        //assume is textField).
        layout.putConstraint(SpringLayout.EAST, p,
                             5,
                             SpringLayout.EAST, textField);
        layout.putConstraint(SpringLayout.SOUTH, p,
                             5,
                             SpringLayout.SOUTH, textField);
 
        //Display the window.
        frame.add(p);
        frame.pack();
        frame.setVisible(true);
    }
 
    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

}
