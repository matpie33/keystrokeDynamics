package pl.master.thesis.frame;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

import pl.master.thesis.buttons.MyLabel;
import pl.master.thesis.others.Strings;
import pl.master.thesis.panels.BasicPanel;
import pl.master.thesis.panels.PanelCongratulations;
import pl.master.thesis.panels.PanelData;
import pl.master.thesis.panels.PanelSummary;
import pl.master.thesis.panels.PanelWelcome;


public class MainWindow extends JFrame{
	private final int minimumWidth=340;
	private final int minimumHeight=313;
	private CardLayout layout;
	
	public static final String DATA_PANEL = "data panel";
	public static final String WELCOME_PANEL = "welcome panel";
	public static final String SUMMARY_PANEL = "summary panel";
	public static final String CONGRATULATIONS_PANEL = "congratulations panel";	
	
	private JPanel card;
	
	private class MyDispatcher implements KeyEventDispatcher {
		
		private JTextArea textArea;
		private long time;
		
		private MyDispatcher (JTextArea jt){
			textArea=jt;
		}
		
        @Override
        public boolean dispatchKeyEvent(KeyEvent e) {
            if (e.getID() == KeyEvent.KEY_PRESSED) {
            	time=System.nanoTime();
            } else if (e.getID() == KeyEvent.KEY_RELEASED) {
            	textArea.append(""+e.getKeyChar()+":"+((double)System.nanoTime()-(double)time)/1000000000+"\n");
            } 
            return false;
        }
    }
	
	private static final long serialVersionUID = -8359166520752108733L;

	public MainWindow (){	
		
		setMinimumSize(new Dimension(minimumWidth,minimumHeight));
		JTextArea textInfo = new JTextArea(5,25);
		textInfo.setEditable(false);
		JScrollPane scrollPane =new JScrollPane(textInfo);
		
		JPanel wrap = new JPanel(new GridBagLayout()); // main panel, contains panel with cardlayout 
		wrap.setBackground(Color.BLUE);		
		
		layout = new CardLayout();
		card = new JPanel();
		card.setLayout(layout);		
		
		Border blackline = BorderFactory.createLineBorder(Color.black,5);		
		card.setBorder(blackline);
		
		GridBagConstraints cd = new GridBagConstraints();
		cd.anchor=GridBagConstraints.CENTER;
		cd.insets=new Insets (20,20,20,20);
		
		wrap.add (card,cd);			
						
		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new MyDispatcher(textInfo));
        		
		this.setTitle("System for content reconstruction");
		List <String> strings=createStrings();	
				
		LinkedHashMap <JTextField,MyLabel> hmap = new LinkedHashMap <JTextField,MyLabel> ();		
				
		int height=20;	
		
		for (int i=0; i<strings.size();i++){
			
			String str = strings.get(i);
			
			if (str.matches(Strings.HASLO+"|"+Strings.POTWIERDZ_HASLO))
				hmap.put (new JPasswordField (Strings.HASLO,height), new MyLabel(str));
			
			else if (str.equals(Strings.DZIEN) || str.equals(Strings.MIESIAC)||
					str.equals(Strings.ROK))
				hmap.put(new JTextField (str,4), new MyLabel(str));
			else 
				hmap.put(new JTextField (str,height), new MyLabel(str));
				
		}

//		labels.get(8).setText("PotwierdŸ has³o:");
//		String [] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
//		for ( int i = 0; i < fonts.length; i++ )
//	    {
//	      System.out.println(fonts[i]);
//	    }
		
		PanelWelcome welcomePanel=new PanelWelcome(this);
//		welcomePanel.add(scrollPane);
		PanelSummary summaryPanel = new PanelSummary(this);
//		summaryPanel.add(new JScrollPane(textInfo));
		BasicPanel dataPanel = new PanelData(this,summaryPanel,hmap, strings);
		BasicPanel congratsPanel = new PanelCongratulations(this);	
						
		card.add(welcomePanel, MainWindow.WELCOME_PANEL);
		card.add(dataPanel,MainWindow.DATA_PANEL);		
		card.add(summaryPanel,MainWindow.SUMMARY_PANEL);
		card.add(congratsPanel,MainWindow.CONGRATULATIONS_PANEL);
		
		add(wrap);
		this.pack();
		this.setMinimumSize(this.getSize());
		this.setLocationRelativeTo(null);		
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
	}
	
	public void nextPanel(){
		layout.next(card);
	}
	
	public void previousPanel (){
		layout.previous(card);
	}
	
	public void gotoPanel (String panelName){
		layout.show(card, panelName);
	}
	
	private List <String> createStrings(){
		
		List <String> strings = new ArrayList <String> ();
		strings.add(Strings.IMIE);
		strings.add(Strings.NAZWISKO);
		strings.add(Strings.DATA_URODZENIA);
		strings.add(Strings.DZIEN);
		strings.add(Strings.MIESIAC);
		strings.add(Strings.ROK);
		strings.add(Strings.ADRES_EMAIL);
		strings.add(Strings.NAZWA_UZYTKOWNIKA);
		strings.add(Strings.HASLO);
		strings.add(Strings.POTWIERDZ_HASLO);
		strings.add(Strings.PYTANIE_POMOCNICZE);
		strings.add(Strings.ODPOWIEDZ);
		return strings;
		
	}

}
