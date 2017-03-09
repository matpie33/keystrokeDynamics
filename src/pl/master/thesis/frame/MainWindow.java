	package pl.master.thesis.frame;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

import com.guimaker.panels.MainPanel;
import com.guimaker.row.RowMaker;

import pl.master.thesis.guiElements.MyLabel;
import pl.master.thesis.keyEventHandler.KeyEventHandler;
import pl.master.thesis.others.ElementsMaker;
import pl.master.thesis.others.FieldsInitializer;
import pl.master.thesis.others.MyColors;
import pl.master.thesis.panels.PanelCongratulations;
import pl.master.thesis.panels.PanelData;
import pl.master.thesis.panels.PanelSummary;
import pl.master.thesis.panels.PanelWelcome;
import pl.master.thesis.strings.Prompts;


public class MainWindow extends JFrame{
	
	private final int minimumWidth=340;
	private final int minimumHeight=313;
	private final int distanceFromEdges = 20;
	private JPanel card;
	private MainPanel panel;
	private KeyEventHandler keyHandler;	
	private PanelCongratulations congratsPanel;
	private PanelData panelData;
	private FieldsInitializer fieldsInitializer;
		
	public static final String DATA_PANEL = "data panel";
	public static final String WELCOME_PANEL = "welcome panel";
	public static final String SUMMARY_PANEL = "summary panel";
	public static final String CONGRATULATIONS_PANEL = "congratulations panel";		
	
	private class MyDispatcher implements KeyEventDispatcher {
        @Override
        public boolean dispatchKeyEvent(KeyEvent e) {
            if (e.getID() == KeyEvent.KEY_PRESSED) {       
            	keyHandler.recordKeyPress(e);
            } 
            else if (e.getID() == KeyEvent.KEY_RELEASED) {
            	keyHandler.recordKeyRelease(e);
            } 
            return false;
        }                
    }
	
	public MainWindow (){	
		keyHandler = new KeyEventHandler();
		fieldsInitializer = new FieldsInitializer(keyHandler);		
		keyHandler.setFieldsInitializer(fieldsInitializer);
		
		panel = new MainPanel(MyColors.DARK_GREEN);				
		card = initializePanelWithCards();
		panel.addRow(RowMaker.createBothSidesFilledRow(card));
		
		initializeAllPanelsElements();									
		setWindowProperties();
	}
	
	private void initializeAllPanelsElements(){
		Map <JTextField, MyLabel> hmap = fieldsInitializer.getFieldsToLabelMap();
		ElementsMaker.setTextFieldToLabelMap(hmap); 
		createCardsAndPutThemInOrder(hmap);
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
	
	private void createCardsAndPutThemInOrder(Map <JTextField, MyLabel> hmap){
		
		PanelWelcome welcomePanel=new PanelWelcome(this);
		JTextArea textInfo = createTextAreaWithKeyPressedInfos();
		JScrollPane scrollPane =new JScrollPane(textInfo);
		PanelSummary summaryPanel = new PanelSummary(this);
		panelData = new PanelData(this, summaryPanel, hmap);
   		congratsPanel = new PanelCongratulations(this, keyHandler);	
		card.add(welcomePanel.getPanel(), MainWindow.WELCOME_PANEL);
		card.add(panelData.getPanel(),MainWindow.DATA_PANEL);		
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
	}
	
	public void clearData(){
		fieldsInitializer.initializeFieldsWithDefaultValues();
	}
	
	public void previousPanel (){
		((CardLayout)card.getLayout()).previous(card);
	}
	
	public void gotoPanel (String panelName){
		((CardLayout)card.getLayout()).show(card, panelName);	
		congratsPanel.update();
	}

	public KeyEventHandler getKeyEventHandler(){
		return keyHandler;
	}
	
}
