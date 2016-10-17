package pl.master.thesis.panels;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import pl.master.thesis.buttons.MyButton;
import pl.master.thesis.buttons.MyLabel;
import pl.master.thesis.dialogs.MyDialog;
import pl.master.thesis.frame.MainWindow;
import pl.master.thesis.others.FieldsVerifier;
import pl.master.thesis.strings.FormsLabels;
import pl.master.thesis.strings.Prompts;
import pl.master.thesis.swingWorkers.AddUserWorker;

public class PanelSummary extends BasicPanel {
	
	private static final long serialVersionUID = 3706383803572619289L;
	private Map <JTextField,MyLabel> hmap;
//	private ConnectionListener connectionListener;
	
	
	

	public PanelSummary(MainWindow fra){		
		super(fra);
		hmap=new LinkedHashMap <JTextField,MyLabel> ();
//		connectionListener= new ConnectionListener (this, new con());		
	}
		
	public void showFieldsValues(Map <JTextField,MyLabel> hmap){
		
		removeAll();
		
		this.hmap=hmap;
		
		c.insets=verticalInsets;
		MyLabel title = new MyLabel ("Podsumowanie");	
		c.anchor=GridBagConstraints.CENTER;
		c.weighty=1;
		c.weightx=1;
		add(title,c);
		
		c.gridwidth=1;
		c.anchor=GridBagConstraints.WEST;
		
		addValuesFromTextFieldsAndLabels(hmap);
		
		
		c.gridy++;
		c.gridwidth=1;
		add(btnBack,c);

		c.anchor=GridBagConstraints.EAST;	
		JButton btnConnect = createButtonConnect();
		add(btnConnect,c);
	}
	
	private JButton createButtonConnect (){
		JButton btnConnect = new MyButton (frame, Prompts.BTN_CONTINUE);
		final BasicPanel panel = this;
	
		btnConnect.addActionListener(new ActionListener (){ 
			@Override
			public void actionPerformed (ActionEvent e){
				MyDialog myDialog = new MyDialog(panel);	
				myDialog.createWaitingPanel();
				
				SwingWorker s = new AddUserWorker(panel, myDialog, hmap);
				s.execute();
				myDialog.setVisible(true);	
			}
		});
		return btnConnect;
	}

	private void addValuesFromTextFieldsAndLabels(Map<JTextField, MyLabel> hmap) {
		for (Map.Entry<JTextField, MyLabel> entry: hmap.entrySet()){

			MyLabel label = entry.getValue();
			JTextField textField = entry.getKey();
			
			if (label.getText().matches(FormsLabels.DZIEN+"|"+
					FormsLabels.MIESIAC+"|"+FormsLabels.ROK+"|"+FormsLabels.HASLO+"|"+FormsLabels.POTWIERDZ_HASLO)) continue;
						
			String text="";
			if (label.getText().equals(FormsLabels.DATA_URODZENIA)){
				JTextField day = FieldsVerifier.findTextField(FormsLabels.DZIEN, hmap);
				JTextField month = FieldsVerifier.findTextField(FormsLabels.MIESIAC, hmap);
				JTextField year = FieldsVerifier.findTextField(FormsLabels.ROK, hmap);
				text=day.getText()+"-"+month.getText()+"-"+year.getText();				
			}
			else text=textField.getText();
			
			MyLabel newL = new MyLabel (label.getText()+": "+text);
			c.gridy++;
			add(newL,c);	
			
		}
		
	}
		
	
}


