package pl.master.thesis.listeners;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import pl.master.thesis.panels.BasicPanel;

public class KeyListeners {

	public static KeyAdapter removeErrorWhenTyped(final BasicPanel panel) {
		return new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (panel.isErrorShowing()) {
					panel.removeError();
				}
			}
		};
	}

	public static KeyAdapter textfieldLimitAdapter(final JTextField field, final int limit) {
		return new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (field.getText().length() >= limit && field.getSelectedText() == null) {
					e.consume();
				}
			}
		};
	}

	public static void addKeyBindings(KeyStroke key, AbstractAction action, JComponent... c) {
		for (JComponent c1 : c) {
			c1.getInputMap().put(key, "name");
			c1.getActionMap().put("name", action);
		}
	}

	public static void addListeners(KeyAdapter action, JTextField... c) {
		for (JTextField c1 : c) {
			c1.addKeyListener(action);
		}
	}

}
