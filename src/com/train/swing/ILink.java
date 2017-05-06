package com.train.swing;

import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;

public class ILink extends JLabel{
	
	private static final long serialVersionUID = 7290185089331225418L;
	private String text;
	public ILink(final String text){
		this.text = text;
		this.setCursor(new Cursor(Cursor.HAND_CURSOR));
		this.setText("<html><font color='blue'>"+text+"</font></html>");
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				if (ILink.this.isEnabled())
					ILink.this.setText("<html><font color='blue' style='text-decoration: underline;'>"+text+"</font></html>");
			}
			@Override
			public void mouseExited(MouseEvent e) {
				if (ILink.this.isEnabled())
					ILink.this.setText("<html><font color='blue' style='text-decoration: none;'>"+text+"</font></html>");
			}
		});
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		if (!enabled) {
			this.setText("<html><font color='gray'>"+text+"</font></html>");
		} else {
			this.setText("<html><font color='blue'>"+text+"</font></html>");
		}
	}
}