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

import pl.master.thesis.buttons.MyButton;
import pl.master.thesis.buttons.MyLabel;
import pl.master.thesis.dialogs.MyDialog;
import pl.master.thesis.frame.MainWindow;
import pl.master.thesis.others.DateVerifier;
import pl.master.thesis.others.FieldsVerifier;
import pl.master.thesis.strings.FormsLabels;
import pl.master.thesis.strings.Prompts;

public class PanelData extends BasicPanel{

	private Map <JTextField, MyLabel> hmap;
	
	
	public PanelData(final MainWindow frame, final PanelSummary summaryPanel, final HashMap <JTextField,MyLabel> hmap, 
			final List <String> strings){
		
		super(frame);	
		this.hmap = hmap;
		List <JTextField> fields = new ArrayList <JTextField>();
		fields.addAll(hmap.keySet());	
		
		JPanel datePanel = createDatePanel();	
		MyLabel title = new MyLabel (Prompts.TITLE_DATA);
		JButton exampleInput = createButtonForExampleInput(hmap); //TODO REMOVE IT WHEN APPLICATION IS FINISHED	
			
		MyButton btnContinue = createButtonContinue(summaryPanel);	
		for (int i=0; i<fields.size();i++){
			JTextField field = fields.get(i);
			addListenerDefaultValueIfEmpty(field);		
		}
	
		add(exampleInput);
		
		c.weighty=1;
		c.weightx=1;
		c.insets=verticalInsets;
		c.gridwidth=2;
		add(title,c);
		
		c.gridwidth=1;
		
		addTextFieldsAndLabelsFromMap (hmap, datePanel);
		
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
	
	private JPanel createDatePanel (){
		
		JPanel datePanel = new JPanel();
		datePanel.setOpaque(false);
		datePanel.setLayout(new BorderLayout(10,0));
		
		JTextField days = createTextfieldWithMaxCharacters(FormsLabels.DZIEN, 2);
		JTextField months = createTextfieldWithMaxCharacters(FormsLabels.MIESIAC, 2);
		JTextField years = createTextfieldWithMaxCharacters(FormsLabels.ROK, 4);
			
		datePanel.add(years,BorderLayout.EAST);
		datePanel.add(months,BorderLayout.CENTER);			
		datePanel.add(days,BorderLayout.WEST);
		return datePanel;
		
	}
	
	private JTextField createTextfieldWithMaxCharacters(String textFieldType, int maxCharacters){
		
		JTextField textField = FieldsVerifier.findTextField(textFieldType, hmap);
		setMaximumCharacters(textField, maxCharacters);
		addListenerDefaultValueIfEmpty(textField);
		return textField;
		
	}
	
	private JButton createButtonForExampleInput (final Map <JTextField, MyLabel> hmap){
		
		JButton button = new JButton ();
		button.setText("");
		button.setMnemonic('f');
		button.addActionListener(new ActionListener (){
			@Override
			public void actionPerformed (ActionEvent e){
				setExampleValuesToTextFields(hmap);	
			}							
		});		
		return button;
		
	}
	
	private MyButton createButtonContinue(final PanelSummary summaryPanel){
		MyButton button = new MyButton (frame, Prompts.BTN_CONTINUE);
		final BasicPanel dataPanel = this;
		button.addActionListener (new ActionListener(){
			@Override
			public void actionPerformed (ActionEvent event){
				
				summaryPanel.showFieldsValues(hmap);			
				
				String errorText=FieldsVerifier.verifyFields(hmap);
				if (!errorText.isEmpty()){
					MyDialog dialog = new MyDialog(dataPanel);	
					dialog.createMsgDialog(errorText);
					dialog.setVisible(true);
				}	
				else frame.nextPanel();
								
			}		
			
		});
		return button;
	}
	
	private void setExampleValuesToTextFields(Map <JTextField, MyLabel> hmap){
		
		for (Map.Entry<JTextField, MyLabel> entries: hmap.entrySet()){
			String s = entries.getValue().getText();
			if (s.equals(FormsLabels.IMIE)){
				entries.getKey().setText("Kamil");
			}
			if (s.equals(FormsLabels.ADRES_EMAIL)){
				entries.getKey().setText("Spaaw@poczta.fm");
			}
			if (s.equals(FormsLabels.NAZWA_UZYTKOWNIKA)){
				entries.getKey().setText("Kamilo123");
			}
			if (s.equals(FormsLabels.IMIE)){
				entries.getKey().setText("Kamil");
			}
			if (s.equals(FormsLabels.NAZWISKO)){
				entries.getKey().setText("NOWAK");
			}
			if (s.equals(FormsLabels.DZIEN)){
				entries.getKey().setText("14");
			}
			if (s.equals(FormsLabels.MIESIAC)){
				entries.getKey().setText("06");
			}
			if (s.equals(FormsLabels.ROK)){
				entries.getKey().setText("1984");
			}
			if (s.equals(FormsLabels.HASLO)){
				entries.getKey().setText("degdras11");
			}
			if (s.equals(FormsLabels.POTWIERDZ_HASLO)){
				entries.getKey().setText("degdras11");
			}
			if (s.equals(FormsLabels.ODPOWIEDZ)){
				entries.getKey().setText("3");
			}
			if (s.equals(FormsLabels.PYTANIE_POMOCNICZE)){
				entries.getKey().setText("Ile masz zwierz¹t w domu?");
			}			
		}
		
	}
	
	private void addActionListenerToButtonContinue(final Map <JTextField, MyLabel> hmap, final PanelSummary summaryPanel,
					final JPanel panel){
		//TODO it adds, not sets it, meaning it will have the listener defined at basePanel (unexpected behaviour)
		//TODO instead of btnContinue create new button and add this listener
		
	}
	
	private void addTextFieldsAndLabelsFromMap (Map <JTextField, MyLabel> hmap, JPanel datePanel){
		for (Map.Entry<JTextField, MyLabel> set: hmap.entrySet()) {
			
			MyLabel label = set.getValue();
			JTextField field = set.getKey();
			if (label.getText().equals(FormsLabels.DZIEN) || label.getText().equals(FormsLabels.MIESIAC) || 
					label.getText().equals(FormsLabels.ROK))
				continue;
			
			c.gridy++;
			c.gridx=0;
			c.insets=labelInsets;	
			c.anchor=GridBagConstraints.WEST;
			
			add(label,c);		
			
			c.anchor=GridBagConstraints.WEST;		
			c.gridx=1;				
			c.insets=fieldInsets;
			
			if (label.getText().equals(FormsLabels.DATA_URODZENIA))								
				add(datePanel,c);		
			else	add(field,c);				
					
		}
	}
		
	private void setMaximumCharacters (final JTextField field, final int limit){
		field.addKeyListener(new KeyAdapter(){
			@Override
			public void keyTyped (KeyEvent e){
				if (field.getText().length()>=limit && field.getSelectedText()==null){
					e.consume();
				}
			}
		});		
	}
	
	private void addListenerDefaultValueIfEmpty (final JTextField field){
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
