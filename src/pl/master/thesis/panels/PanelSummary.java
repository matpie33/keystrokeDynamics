package pl.master.thesis.panels;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SplashScreen;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import pl.masater.thesis.listeners.ConnectionListener;
import pl.master.thesis.buttons.MyButton;
import pl.master.thesis.buttons.MyLabel;
import pl.master.thesis.database.ConnectionSwingWorker;
import pl.master.thesis.database.Database;
import pl.master.thesis.database.SqlStatements;
import pl.master.thesis.frame.MainWindow;
import pl.master.thesis.others.FieldsVerifier;
import pl.master.thesis.others.Strings;

public class PanelSummary extends BasicPanel {
	
	private static final long serialVersionUID = 3706383803572619289L;
	private final String propertiesFile = "properties/db properties.xml";
	private Map <JTextField,MyLabel> hmap;
	private ConnectionListener clistener;
	
	class con extends ConnectionSwingWorker{
		@Override
		protected void doSqlStatements (Connection con) throws SQLException{
			try{
				String user =FieldsVerifier.findTextField(Strings.NAZWA_UZYTKOWNIKA, hmap).getText();
				String password =FieldsVerifier.findTextField(Strings.HASLO, hmap).getText();
				SqlStatements.addUser(con, user, password);
				frame.nextPanel();
				clistener.close();
			}
			catch (SQLException e1) {
				e1.printStackTrace();
				if (e1.getSQLState()=="23505"){
					clistener.setText("User you specified already exists.");
					frame.gotoPanel(MainWindow.DATA_PANEL);
				}
				else if (e1.getSQLState()=="XJ040"){
					clistener.setText ("Database is already used by something.");
				}
				else{
					clistener.setText("Unexpected sql error.");
				}
			}
			
		}
		
		@Override
		public void done(){
//			clistener.setText("Done");
			clistener.removeGif();
			JButton b = new MyButton(frame,"O.k.");
			b.addActionListener(new ActionListener (){
				@Override
				public void actionPerformed(ActionEvent e){
					clistener.close();
				}
			});
			clistener.addButton(b);
		}
	}
	

	public PanelSummary(MainWindow fra){		
		super(fra);
		hmap=new LinkedHashMap <JTextField,MyLabel> ();
		clistener= new ConnectionListener (this, new con());
		
		
	}
		
	public void showFieldsValues(Map <JTextField,MyLabel> hmap){
		
		removeAll();
		
		this.hmap=hmap;
		
		c.insets=verticalInsets;
		MyLabel title = new MyLabel ("Podsumowanie");	
		c.anchor=GridBagConstraints.CENTER;
		c.weighty=1;
		c.weightx=1;
//		c.insets=new Insets(0,20,0,20);
		add(title,c);
		c.gridwidth=1;
		c.anchor=GridBagConstraints.WEST;
		
		for (Map.Entry<JTextField, MyLabel> entry: hmap.entrySet()){

			MyLabel l = entry.getValue();
			JTextField t = entry.getKey();
			
			if (l.getText().matches(Strings.DZIEN+"|"+
					Strings.MIESIAC+"|"+Strings.ROK+"|"+Strings.HASLO+"|"+Strings.POTWIERDZ_HASLO)) continue;
			System.out.println(l.getText());		
			
			String text="";
			if (l.getText().equals(Strings.DATA_URODZENIA)){
				JTextField day= FieldsVerifier.findTextField(Strings.DZIEN, hmap);
				JTextField month= FieldsVerifier.findTextField(Strings.MIESIAC, hmap);
				JTextField year= FieldsVerifier.findTextField(Strings.ROK, hmap);
				text=day.getText()+"-"+month.getText()+"-"+year.getText();
				
			}
			else text=t.getText();
			
			MyLabel newL = new MyLabel (l.getText()+": "+text);
			c.gridy++;
			add(newL,c);	
			
		}
		
//		c.insets=verticalInsets;
		c.gridy++;
		c.gridwidth=1;
		add(btnBack,c);

		c.anchor=GridBagConstraints.EAST;	
		JButton btnConnect = new MyButton (frame, "Continue");
		btnConnect.addActionListener(clistener);
		btnConnect.addActionListener(new ActionListener (){ //TODO do it other way
			@Override
			public void actionPerformed (ActionEvent e){
				clistener.setSwingWorker(new con());
			}
		});
		add(btnConnect,c);
	}
		
}


