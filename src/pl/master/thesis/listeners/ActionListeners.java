package pl.master.thesis.listeners;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JTextField;
import javax.swing.SwingWorker;

import pl.master.thesis.frame.MainWindow;
import pl.master.thesis.guiElements.MyLabel;
import pl.master.thesis.keyEventHandler.KeyEventHandler;
import pl.master.thesis.others.FieldsVerifier;
import pl.master.thesis.panels.PanelData;
import pl.master.thesis.panels.PanelSummary;
import pl.master.thesis.strings.FormsLabels;
import pl.master.thesis.swingWorkers.AddUserWorker;

public class ActionListeners {

	public static ActionListener createDisposeListener(final Window window) {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				window.dispose();
			}
		};
	}

	public static ActionListener createGoHomeListener(final MainWindow frame) {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.gotoPanel(MainWindow.WELCOME_PANEL);
			}
		};
	}

	public static ActionListener createNextPanelListener(final MainWindow frame) {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.nextPanel();
			}
		};
	}

	public static ActionListener createPreviousPanelListener(final MainWindow frame) {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.previousPanel();
			}
		};
	}

	public static ActionListener createListenerConnect(final PanelSummary panel,
			Map<JTextField, MyLabel> map) {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SwingWorker s = new AddUserWorker(panel, map);
				s.execute();
				panel.showConnectingDialog();
			}
		};
	}

	public static ActionListener createListenerGoToNextPanel(final PanelData dataPanel,
			final PanelSummary summaryPanel, final KeyEventHandler handler) {

		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				summaryPanel.showFieldsValues(dataPanel.getMap());
				String errorText = FieldsVerifier.verifyFields(dataPanel.getMap());
				if (!errorText.isEmpty()) {
					dataPanel.showLongMessageDialog(errorText);
				}
				else {
					handler.done();
					dataPanel.getParentFrame().nextPanel();

				}

			}

		};
	}

	public static ActionListener createExampleInputListener(final Map<JTextField, MyLabel> hmap) {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Map<String, String> defaultValues = FormsLabels.getDefaultValues();

				for (Map.Entry<JTextField, MyLabel> entries : hmap.entrySet()) {
					String label = entries.getValue().getText();
					entries.getKey().setText(defaultValues.get(label));
				}

			}
		};
	}

}
