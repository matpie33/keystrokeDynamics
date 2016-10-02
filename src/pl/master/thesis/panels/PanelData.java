package pl.master.thesis.panels;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import pl.master.thesis.buttons.MyLabel;
import pl.master.thesis.frame.MainWindow;
import pl.master.thesis.others.DateVerifier;
import pl.master.thesis.others.FieldsVerifier;
import pl.master.thesis.others.Strings;

public class PanelData extends BasicPanel{
	
	private static final long serialVersionUID = 1L;
		
	public PanelData(final MainWindow frame, final PanelSummary summaryPanel, final HashMap <JTextField,MyLabel> hmap, 
			final List <String> strings){
		
	super(frame);	
	final List <JTextField> fields = new ArrayList <JTextField>();
	fields.addAll(hmap.keySet());	
	
	final JPanel date = new JPanel();
	date.setOpaque(false);
	date.setLayout(new BorderLayout(10,0));
	
	final JTextField days= FieldsVerifier.findTextField(Strings.DZIEN, hmap);
	final JTextField months=FieldsVerifier.findTextField(Strings.MIESIAC, hmap);
	final JTextField years=FieldsVerifier.findTextField(Strings.ROK, hmap);
	
	addKTListener(days,2);
	addKTListener(months,2);
	addKTListener(years,4);
	
	addTFListener(days);
	addTFListener(months);
	addTFListener(years);
	
	date.add(years,BorderLayout.EAST);
	date.add(months,BorderLayout.CENTER);			
	date.add(days,BorderLayout.WEST);
	
	MyLabel title = new MyLabel ("Dane osobowe");
	final PanelData panel =this;
	
	JButton b = new JButton ();
	b.setMnemonic('f');
	b.addActionListener(new ActionListener (){
		@Override
		public void actionPerformed (ActionEvent e){
				
			for (Map.Entry<JTextField, MyLabel> entries: hmap.entrySet()){
				String s = entries.getValue().getText();
				if (s.equals(Strings.IMIE)){
					entries.getKey().setText("Kamil");
				}
				if (s.equals(Strings.ADRES_EMAIL)){
					entries.getKey().setText("Spaaw@poczta.fm");
				}
				if (s.equals(Strings.NAZWA_UZYTKOWNIKA)){
					entries.getKey().setText("Kamilo123");
				}
				if (s.equals(Strings.IMIE)){
					entries.getKey().setText("Kamil");
				}
				if (s.equals(Strings.NAZWISKO)){
					entries.getKey().setText("NOWAK");
				}
				if (s.equals(Strings.DZIEN)){
					entries.getKey().setText("14");
				}
				if (s.equals(Strings.MIESIAC)){
					entries.getKey().setText("06");
				}
				if (s.equals(Strings.ROK)){
					entries.getKey().setText("1984");
				}
				if (s.equals(Strings.HASLO)){
					entries.getKey().setText("degdras11");
				}
				if (s.equals(Strings.POTWIERDZ_HASLO)){
					entries.getKey().setText("degdras11");
				}
				if (s.equals(Strings.ODPOWIEDZ)){
					entries.getKey().setText("3");
				}
				if (s.equals(Strings.PYTANIE_POMOCNICZE)){
					entries.getKey().setText("Ile masz zwierz¹t w domu?");
				}
				
			}
			
		}
		
		
		
	});
	add(b);
	
//	btnContinue.setMnemonic(java.awt.event.KeyEvent.VK_G);
	btnContinue.addActionListener (new ActionListener(){
		@Override
		public void actionPerformed (ActionEvent event){
			
			summaryPanel.showFieldsValues(hmap);			
			
			String errorText=FieldsVerifier.verifyFields(hmap);
			if (!errorText.isEmpty()){
				JOptionPane.showMessageDialog(panel, errorText, "B³¹d", JOptionPane.ERROR_MESSAGE);
				frame.previousPanel(); //TODO we go to next panel, and here we go previous too, meaning we stay the same
			}					
							
		}		
		
	});
	
	for (int i=0; i<fields.size();i++){
		final JTextField field = fields.get(i);
		addTFListener(field);		
	}

	c.weighty=1;
	c.weightx=1;
	c.insets=verticalInsets;
//	c.insets=new Insets(0,20,0,20);
	c.gridwidth=2;
	add(title,c);
	c.gridwidth=1;
//	c.fill=GridBagConstraints.NONE;
	
	JPanel datee = new JPanel();
	datee.setLayout(new BorderLayout(10,0));
	
	for (Map.Entry<JTextField, MyLabel> set: hmap.entrySet()) {
			
		MyLabel label = (MyLabel)set.getValue();
		JTextField field = set.getKey();
		if (label.getText().equals(Strings.DZIEN) || label.getText().equals(Strings.MIESIAC) || 
				label.getText().equals(Strings.ROK))
			continue;
		
		c.gridy++;
		c.gridx=0;
		c.insets=labelInsets;	
		c.anchor=GridBagConstraints.WEST;
		
		add(label,c);		
		
		c.anchor=GridBagConstraints.WEST;		
		c.gridx=1;				
		c.insets=fieldInsets;
		
		if (label.getText().equals(Strings.DATA_URODZENIA))								
			add(date,c);		
		else	add(field,c);				
				
	}
		
	c.anchor=GridBagConstraints.WEST;
	c.gridy++;
	c.gridx=0;
	c.insets=labelInsets;
	add(btnBack,c);
	
	c.gridx=1;
	c.anchor=GridBagConstraints.EAST;
	c.insets=fieldInsets;
	add(btnContinue,c);
	
	}
		
	private void addKTListener (final JTextField field, final int limit){
		KeyAdapter k = new KeyAdapter(){
			@Override
			public void keyTyped (KeyEvent e){
				if (field.getText().length()>=limit && field.getSelectedText()==null){
					e.consume();
				}
			}
		};
		field.addKeyListener(k);
	}
	
	private void addTFListener (final JTextField field){
		final String oldValue = field.getText();
		FocusListener textFieldListener = new FocusListener (){
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
		
		field.addFocusListener(textFieldListener);
	}

	
	
}
