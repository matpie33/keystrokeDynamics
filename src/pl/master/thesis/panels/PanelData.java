package pl.master.thesis.panels;

import java.awt.GridBagConstraints;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.guimaker.panels.MainPanel;
import com.guimaker.row.RowMaker;

import pl.master.thesis.guiElements.MyButton;
import pl.master.thesis.guiElements.MyLabel;
import pl.master.thesis.listeners.ActionListeners;
import pl.master.thesis.listeners.FocusListeners;
import pl.master.thesis.main.MainWindow;
import pl.master.thesis.others.ElementsMaker;
import pl.master.thesis.strings.FormsLabels;
import pl.master.thesis.strings.Prompts;

public class PanelData extends BasicPanel {

	private Map<JTextField, MyLabel> hmap;

	public PanelData(final MainWindow frame, final PanelSummary summaryPanel,
			final Map<JTextField, MyLabel> hmap) {

		super(frame);
		List<JTextField> fields = new ArrayList<JTextField>();
		fields.addAll(hmap.keySet());
		this.hmap = hmap;

		JPanel datePanel = createDatePanel();
		MyLabel title = ElementsMaker.createLabel(Prompts.TITLE_DATA);
		JButton exampleInput = ElementsMaker.createButton("",
				ActionListeners.createExampleInputListener(hmap));
		// TODO REMOVE IT WHEN APPLICATION IS FINISHED

		MyButton btnContinue = ElementsMaker.createButton(Prompts.BTN_CONTINUE, ActionListeners.
		// createNextPanelListener(getParentFrame()));
				createListenerGoToNextPanel(this, summaryPanel, frame.getKeyEventHandler()));

		for (int i = 0; i < fields.size(); i++) {
			JTextField field = fields.get(i);
			field.addFocusListener(
					FocusListeners.defaultValueIfEmpty(field, frame.getKeyEventHandler()));
		}

		panel.addRow(RowMaker.createUnfilledRow(GridBagConstraints.WEST, exampleInput));
		panel.addRow(RowMaker.createUnfilledRow(GridBagConstraints.CENTER, title));
		addTextFieldsAndLabelsFromMap(hmap, datePanel);
		// panel.createRowOn2Sides(btnBack, btnContinue); //TODO create
		panel.addRow(RowMaker.createUnfilledRow(GridBagConstraints.EAST, btnBack, btnContinue));
		setFocusToTextField(fields.get(0));

	}

	private void setFocusToTextField(JTextField field) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				field.requestFocusInWindow();
			}
		});
	}

	private JPanel createDatePanel() {

		MainPanel date = new MainPanel(null);
		date.setGapsBetweenRowsTo0();
		JPanel datePanel = date.getPanel();

		JTextField days = ElementsMaker.createTextField(FormsLabels.DAY, 2);
		JTextField months = ElementsMaker.createTextField(FormsLabels.MONTH, 2);
		JTextField years = ElementsMaker.createTextField(FormsLabels.YEAR, 4);

		date.addRow(RowMaker.createHorizontallyFilledRow(years, months, days)
				.fillHorizontallyEqually());
		return datePanel;

	}

	private void addTextFieldsAndLabelsFromMap(Map<JTextField, MyLabel> hmap, JPanel datePanel) {
		for (Map.Entry<JTextField, MyLabel> set : hmap.entrySet()) {
			MyLabel label = set.getValue();
			JTextField field = set.getKey();
			if (label.getText().equals(FormsLabels.DAY) || label.getText().equals(FormsLabels.MONTH)
					|| label.getText().equals(FormsLabels.YEAR))
				continue;

			if (label.getText().equals(FormsLabels.DATE_OF_BIRTH))
				panel.addRow(RowMaker.createHorizontallyFilledRow(label, datePanel)
						.fillHorizontallySomeElements(datePanel));
			else
				panel.addRow(RowMaker.createHorizontallyFilledRow(label, field)
						.fillHorizontallySomeElements(field));
		}
	}

	public Map<JTextField, MyLabel> getMap() {
		return hmap;
	}

}
