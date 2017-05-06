package com.train.util;

import com.train.MainFrame;

import javax.swing.*;
import java.util.Date;
/**
 * 窗口打印日志
 * @author lijintao
 *
 */
public class Logger {
	private JTextArea logger=MainFrame.textArea;
	public void info(String str){
		if(logger!=null){
			logger.append(DateUtils.longDate(new Date())+str+"\r\n");
			logger.setCaretPosition(logger.getText().length());
		}
	}
}
