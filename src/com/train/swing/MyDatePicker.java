package com.train.swing;

import com.train.util.DatePicker;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.Locale;
import java.util.Observer;
/**
 * 自定义日历控件
 * @author lijintao
 */
public class MyDatePicker extends DatePicker {
	{
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param observer
	 * @param selecteddate
	 * @param locale
	 */
	public MyDatePicker(Observer observer, Date selecteddate, Locale locale) {
		super(observer, selecteddate, locale);
	}

	/**
	 * @param observer
	 * @param selecteddate
	 */
	public MyDatePicker(Observer observer, Date selecteddate) {
		super(observer, selecteddate);
	}

	/**
	 * @param observer
	 * @param locale
	 */
	public MyDatePicker(Observer observer, Locale locale) {
		super(observer, locale);
	}

	/**
	 * @param observer
	 */
	public MyDatePicker(Observer observer) {
		super(observer);
	}

	public void start(Component c) {
		if (c != null) {
			Component p = c.getParent();
			int x = c.getX(), y = c.getY() + c.getHeight();
			while (p != null) {
				x += p.getX();
				y += p.getY();
				p = p.getParent();
			}
			// System.out.println("x="+x+ " y="+y);
			getScreen().setLocation(x, y);
		} else {
			Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
			getScreen().setLocation((int) (dim.getWidth() - getScreen().getWidth()) / 2,
					(int) (dim.getHeight() - getScreen().getHeight()) / 2);
		}
		SwingUtilities.invokeLater(this);
	}
}
