package pl.master.thesis.frame;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

import pl.master.thesis.guiElements.MyLabel;
import pl.master.thesis.others.ElementsMaker;
import pl.master.thesis.others.MyColors;
import pl.master.thesis.others.PanelCreator;
import pl.master.thesis.panels.PanelCongratulations;
import pl.master.thesis.panels.PanelData;
import pl.master.thesis.panels.PanelSummary;
import pl.master.thesis.panels.PanelWelcome;
import pl.master.thesis.strings.FormsLabels;
import pl.master.thesis.strings.Prompts;
import pl.master.thesis.timing.Timing;


public class MainWindow extends JFrame{
	
	private final int minimumWidth=340;
	private final int minimumHeight=313;
	private final int distanceFromEdges = 20;
	private JPanel card;
	private PanelCreator panel;
	private Timing timing;	
		
	public static final String DATA_PANEL = "data panel";
	public static final String WELCOME_PANEL = "welcome panel";
	public static final String SUMMARY_PANEL = "summary panel";
	public static final String CONGRATULATIONS_PANEL = "congratulations panel";		
	
	private class MyDispatcher implements KeyEventDispatcher {
		
		private JTextArea textArea;	
		
		private MyDispatcher (JTextArea jt){
			textArea=jt;
		}
		
        @Override
        public boolean dispatchKeyEvent(KeyEvent e) {
        	
        	
            if (e.getID() == KeyEvent.KEY_PRESSED) {       
            	timing.recordKeyPress(e);
            	System.out.println("Pressed: "+e.getKeyChar());
            } 
            else if (e.getID() == KeyEvent.KEY_RELEASED) {
            	timing.recordKeyRelease(e);
//            	textArea.append(""+e.getKeyChar()+":"+((double)System.nanoTime()-(double)time)/1000000000+"\n");
            } 
            return false;
        }                
        
    }
	
	public MainWindow (){			
		
		timing = new Timing();
		panel = new PanelCreator(MyColors.DARK_GREEN);				
		card = initializePanelWithCards();		
		panel.createRow(0,card);	
		
		initializeAllPanelsElements();									
		setWindowProperties();
	}
	
	private void initializeAllPanelsElements(){
		List <String> strings=createStrings();	
		Map <JTextField, MyLabel> hmap = createMapWithTextFieldsAndLabels(strings);		
		ElementsMaker.setTextFieldToLabelMap(hmap);
		createCardsAndPutThemInOrder(strings, hmap);
	}
	
	private JTextArea createTextAreaWithKeyPressedInfos(){
		JTextArea textInfo = new JTextArea(5,25);
		textInfo.setEditable(false);
		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new MyDispatcher(textInfo));
		return textInfo;
	}
	
	private JPanel initializePanelWithCards(){
		
		JPanel card = new JPanel();
		card.setLayout(new CardLayout());		
		
		Border blackline = BorderFactory.createLineBorder(Color.black,5);		
		card.setBorder(blackline);
		return card;
		
	}
	
	private Map <JTextField, MyLabel> createMapWithTextFieldsAndLabels (List <String> strings){
		
		Map <JTextField,MyLabel> hmap = new LinkedHashMap <JTextField,MyLabel> (); //TODO left side should be map not linkedhashmap		
		int height=15;	
		
		for (int i=0; i<strings.size();i++){			
			String str = strings.get(i);
			JTextField textField;
			MyLabel label = new MyLabel (str);
			
			if (str.matches(FormsLabels.HASLO+"|"+FormsLabels.POTWIERDZ_HASLO))
				textField = new JPasswordField (FormsLabels.HASLO, height);			
			else if (str.matches(FormsLabels.DZIEN+"|"+FormsLabels.MIESIAC+"|"+FormsLabels.ROK))
				textField = new JTextField (str,4);				
			else textField = new JTextField (str, height);
				
			hmap.put(textField, label);
				
		}
		return hmap;
	}
	
	private void createCardsAndPutThemInOrder(List <String> strings, Map <JTextField, MyLabel> hmap){
		
		PanelWelcome welcomePanel=new PanelWelcome(this);
		JTextArea textInfo = createTextAreaWithKeyPressedInfos();
		JScrollPane scrollPane =new JScrollPane(textInfo);
//		welcomePanel.getPanel().add(scrollPane);
		PanelSummary summaryPanel = new PanelSummary(this, hmap);
		PanelData dataPanel = new PanelData(this,summaryPanel,hmap, strings);
   		PanelCongratulations congratsPanel = new PanelCongratulations(this);	
						
		card.add(welcomePanel.getPanel(), MainWindow.WELCOME_PANEL);
		card.add(dataPanel.getPanel(),MainWindow.DATA_PANEL);		
		card.add(summaryPanel.getPanel(),MainWindow.SUMMARY_PANEL);
		card.add(congratsPanel.getPanel(),MainWindow.CONGRATULATIONS_PANEL);
		
	}
	
	private void setWindowProperties(){		
		setContentPane(panel.getPanel());
		setMinimumSize(new Dimension(minimumWidth,minimumHeight));
		pack();
		setMinimumSize(getSize());
		setLocationRelativeTo(null);		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle(Prompts.TITLE_APPLICATION);		
	}
	
	public void nextPanel(){
		((CardLayout)card.getLayout()).next(card);
	}
	
	public void previousPanel (){
		((CardLayout)card.getLayout()).previous(card);
	}
	
	public void gotoPanel (String panelName){
		((CardLayout)card.getLayout()).show(card, panelName);
	}
	
	private List <String> createStrings(){
		
		List <String> strings = new ArrayList <String> ();
		strings.add(FormsLabels.IMIE);
		strings.add(FormsLabels.NAZWISKO);
		strings.add(FormsLabels.DATA_URODZENIA);
		strings.add(FormsLabels.DZIEN);
		strings.add(FormsLabels.MIESIAC);
		strings.add(FormsLabels.ROK);
		strings.add(FormsLabels.ADRES_EMAIL);
		strings.add(FormsLabels.NAZWA_UZYTKOWNIKA);
		strings.add(FormsLabels.HASLO);
		strings.add(FormsLabels.POTWIERDZ_HASLO);
		strings.add(FormsLabels.PYTANIE_POMOCNICZE);
		strings.add(FormsLabels.ODPOWIEDZ);
		return strings;
		
	}
	
	
	
	

}
