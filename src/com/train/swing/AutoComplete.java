package com.train.swing;

import com.train.entity.Station;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
/**
 * 自动搜索Jtext
 * @author lijintao
 *
 */
public class AutoComplete {
	private final static Station[] items = Station.create12306Station();

	private static boolean isAdjusting(JComboBox<Station> cbInput) {
		if (cbInput.getClientProperty("is_adjusting") instanceof Boolean) {
			return (Boolean) cbInput.getClientProperty("is_adjusting");
		}
		return false;
	}
	private static void setAdjusting(JComboBox<Station> cbInput, boolean adjusting) {
		cbInput.putClientProperty("is_adjusting", adjusting);
	}
	public static void setupAutoComplete(final JTextField txtInput,final String defaultValue) {
		txtInput.setColumns(30);
		final DefaultComboBoxModel<Station> model = new DefaultComboBoxModel<Station>();
		final JComboBox<Station> cbInput = new JComboBox<Station>(model) {
			private static final long serialVersionUID = -611240359347226500L;
			public Dimension getPreferredSize() {
				return new Dimension(super.getPreferredSize().width, 0);
			}
		};
		setAdjusting(cbInput, false);
		for (Station item : items) {
			model.addElement(item);
		}
		cbInput.setSelectedItem(null);
		cbInput.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!isAdjusting(cbInput)) {
					if (cbInput.getSelectedItem() != null) {
						txtInput.setText(cbInput.getSelectedItem().toString());
					}
				} 
			}
		});
		txtInput.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if ("".equals(txtInput.getText().trim())||!txtInput.getText().trim().contains("-")) {
					txtInput.setText(defaultValue);
					txtInput.setForeground(new Color(153, 153, 153));
				}
			}
			@Override
			public void focusGained(FocusEvent e) {
				if (defaultValue.equals(txtInput.getText())) {
					txtInput.setText("");
					txtInput.setForeground(new Color(0, 0, 0));
				}
			}
		});
		txtInput.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				setAdjusting(cbInput, true);
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					if (cbInput.isPopupVisible()) {
						e.setKeyCode(KeyEvent.VK_ENTER);
					}
				}
				if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_UP
						|| e.getKeyCode() == KeyEvent.VK_DOWN) {
					e.setSource(cbInput);
					cbInput.dispatchEvent(e);
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						txtInput.setText(cbInput.getSelectedItem().toString());
						cbInput.setPopupVisible(false);
					}
				}
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					cbInput.setPopupVisible(false);
				}
				setAdjusting(cbInput, false);
			}
		});
		txtInput.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				updateList();
			}

			public void removeUpdate(DocumentEvent e) {
				updateList();
			}

			public void changedUpdate(DocumentEvent e) {
				updateList();
			}

			private void updateList() {
				setAdjusting(cbInput, true);
				cbInput.setPopupVisible(false);
				model.removeAllElements();
				String input = txtInput.getText();
				if (!input.isEmpty()) {
					for (Station station : items) {
						if (station.getJianPin().toLowerCase().startsWith(input.toLowerCase())
								|| station.getQuanPin().toLowerCase().startsWith(input.toLowerCase())
								|| station.getStationName().toLowerCase().startsWith(input.toLowerCase())) {
							model.addElement(station);
						}
					}
					cbInput.setPopupVisible(model.getSize() > 0);
				}
				setAdjusting(cbInput, false);
			}
		});
		txtInput.setLayout(new BorderLayout());
		txtInput.add(cbInput, BorderLayout.SOUTH);
	}
}