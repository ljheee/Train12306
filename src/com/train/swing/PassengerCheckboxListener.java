package com.train.swing;

import com.train.MainFrame;
import com.train.entity.Passenger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 * 乘客信息监听器
 * @author lijintao
 *
 */
public class PassengerCheckboxListener implements ActionListener {

	private Passenger passenger;
	private MainFrame mainFrame;
	public PassengerCheckboxListener(Passenger passenger, MainFrame mainFrame) {
		super();
		this.passenger = passenger;
		this.mainFrame = mainFrame;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JCheckBox checkBox = (JCheckBox) e.getSource();
		if (checkBox.isSelected())
			mainFrame.addPassengerRow(passenger);
		else{
			mainFrame.delPassengerRow(passenger);
		}
	}
}