package com.train.swing;

import javax.swing.*;
/**
 * Base类 BaseFrame
 * @author lijintao
 *
 */
public class BaseFrame extends JFrame {

	private static final long serialVersionUID = 2001106141525784709L;

	public static TimerCloseFrame tip(Object message) {
		return new TimerCloseFrame(message);
	}
	
	public static TimerCloseFrame tip(String title,Object message) {
		return new TimerCloseFrame(title,message);
	}
	
	public static TimerCloseFrame tip(Object message,long millisecond) {
		return new TimerCloseFrame(message,millisecond);
	}
	
	public static TimerCloseFrame tip(String title,Object message,long millisecond) {
		return new TimerCloseFrame(title,message,millisecond);
	}

	public static void alert(Object message) {
		alert("提示", message);
	}

	public static void alert(String title, Object message) {
		alert(title, message, JOptionPane.WARNING_MESSAGE);
	}
	
	public static void alert(String title, Object message, int messageType) {
		JOptionPane.showMessageDialog(null, message, title, messageType);
	}

	public static boolean prompt(Object message) {
		return prompt("提示", message);
	}

	public static boolean prompt(String title, Object message) {
		int answer = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (answer == JOptionPane.YES_OPTION) {
			return true;
		} else {
			return false;
		}
	}
}
