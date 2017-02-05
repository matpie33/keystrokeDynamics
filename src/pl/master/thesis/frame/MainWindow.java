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

import com.guimaker.panels.MainPanel;
import com.guimaker.row.RowMaker;

import pl.master.thesis.guiElements.MyLabel;
import pl.master.thesis.others.ElementsMaker;
import pl.master.thesis.others.MyColors;
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
	private MainPanel panel;
	private Timing timing;	
	private PanelCongratulations congratsPanel;
		
	public static final String DATA_PANEL = "data panel";
	public static final String WELCOME_PANEL = "welcome panel";
	public static final String SUMMARY_PANEL = "summary panel";
	public static final String CONGRATULATIONS_PANEL = "congratulations panel";		
	
	private class MyDispatcher implements KeyEventDispatcher {
		
		
        @Override
        public boolean dispatchKeyEvent(KeyEvent e) {
        	
        	
            if (e.getID() == KeyEvent.KEY_PRESSED) {       
            	timing.recordKeyPress(e);
            	System.out.println("Pressed: "+e.getKeyChar());
            } 
            else if (e.getID() == KeyEvent.KEY_RELEASED) {
            	timing.recordKeyRelease(e);
            } 
            return false;
        }                
        
    }
	
	public MainWindow (){			
		
		timing = new Timing();
		panel = new MainPanel(MyColors.DARK_GREEN);				
		card = initializePanelWithCards();
		panel.addRow(RowMaker.createBothSidesFilledRow(card));
		
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
        manager.addKeyEventDispatcher(new MyDispatcher());
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
			
			if (str.matches(FormsLabels.PASSWORD+"|"+FormsLabels.REPEAT_PASSWORD))
				textField = new JPasswordField (FormsLabels.PASSWORD, height);			
			else if (str.matches(FormsLabels.DAY+"|"+FormsLabels.MONTH+"|"+FormsLabels.YEAR))
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
   		congratsPanel = new PanelCongratulations(this, timing);	
						
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
		congratsPanel.update();
		System.out.println("updating");
		
	}
	
	public void previousPanel (){
		((CardLayout)card.getLayout()).previous(card);
	}
	
	public void gotoPanel (String panelName){
		((CardLayout)card.getLayout()).show(card, panelName);	
		congratsPanel.update();
	}
	
	
	private List <String> createStrings(){
		
		List <String> strings = new ArrayList <String> ();
		strings.add(FormsLabels.FIRST_NAME);
		strings.add(FormsLabels.LAST_NAME);
		strings.add(FormsLabels.DATE_OF_BIRTH);
		strings.add(FormsLabels.DAY);
		strings.add(FormsLabels.MONTH);
		strings.add(FormsLabels.YEAR);
		strings.add(FormsLabels.EMAIL_ADDRESS);
		strings.add(FormsLabels.USERNAME);
		strings.add(FormsLabels.PASSWORD);
		strings.add(FormsLabels.REPEAT_PASSWORD);
		strings.add(FormsLabels.RECOVERY_QUESTION);
		strings.add(FormsLabels.ANSWER);
		return strings;
		
	}	

}
