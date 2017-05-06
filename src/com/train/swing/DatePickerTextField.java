package com.train.swing;

import javax.swing.*;
import javax.swing.text.Document;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;
/**
 * 定制日历控件
 * @author lijintao
 *
 */
public class DatePickerTextField extends JTextField implements Observer {
	
	private static final long serialVersionUID = 7634373807891982020L;


	{
		this.setEditable(false);
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				MyDatePicker dp = new MyDatePicker(DatePickerTextField.this, Locale.CHINA);
		        Date selectedDate = dp.parseDate(DatePickerTextField.this.getText());
		        dp.setSelectedDate(selectedDate);
		        dp.start(DatePickerTextField.this);
			}
			
		});
	}
	
	
    /**
	 * 
	 */
	public DatePickerTextField() {
		super();
	}


	/**
	 * @param doc
	 * @param text
	 * @param columns
	 */
	public DatePickerTextField(Document doc, String text, int columns) {
		super(doc, text, columns);
	}


	/**
	 * @param columns
	 */
	public DatePickerTextField(int columns) {
		super(columns);
	}


	/**
	 * @param text
	 * @param columns
	 */
	public DatePickerTextField(String text, int columns) {
		super(text, columns);
		// TODO Auto-generated constructor stub
	}


	/**
	 * @param text
	 */
	public DatePickerTextField(String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}


	public void update(Observable o, Object arg) {
        Calendar calendar = (Calendar) arg;
        MyDatePicker dp = (MyDatePicker) o;
        setText(dp.formatDate(calendar,"yyyy-MM-dd"));
    }
    
    
}
