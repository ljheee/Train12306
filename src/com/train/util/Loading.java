package com.train.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
/**
 * loading 工具类
 * @author lijintao
 *
 */
public class Loading extends JWindow implements Runnable {
	private static final long serialVersionUID = -5855159852918342232L;
	Thread splashThread; // 进度条更新线程
	JProgressBar progress; // 进度条
	private static Loading loading;
	public static Loading getLoading(){
		if(loading==null){
			loading=new Loading();
		}
		return loading;
	}
	public Loading() {
		Container container = getContentPane(); // 得到容器
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)); // 设置光标
//		URL url = getClass().getResource("login.jpg"); // 图片的位置
//		if (url != null) {
//			container.add(new JLabel(new ImageIcon(url)), BorderLayout.CENTER); // 增加图片
//		}
		progress = new JProgressBar(1, 100); // 实例化进度条
		progress.setStringPainted(true); // 描绘文字
		progress.setIndeterminate(true); 
		progress.setString("加载程序中,请稍候......"); // 设置显示文字
		progress.setBackground(Color.white); // 设置背景色
		container.add(progress, BorderLayout.SOUTH); // 增加进度条到容器上

		Dimension screen = getToolkit().getScreenSize(); // 得到屏幕尺寸
		pack(); // 窗口适应组件尺寸
		setLocation((screen.width - getSize().width) / 2,
				(screen.height - getSize().height) / 2); // 设置窗口位置
	}

	public void open() {
		this.toFront(); // 窗口前端显示
		splashThread = new Thread(this); // 实例化线程
		splashThread.start(); // 开始运行线程
	}

	public void run() {
		setVisible(true); // 显示窗口
		try {
			for (int i = 0; i < 100; i++) {
				Thread.sleep(1000); // 线程休眠
				progress.setValue(progress.getValue() + 1); // 设置进度条值
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		dispose(); // 释放窗口
//		showFrame(); // 运行主程序
	}
	public void close(){
		dispose();
	}
	static void showFrame() {
		JFrame frame = new JFrame("程序启动界面演示"); // 实例化JFrame对象
		frame.setSize(300, 200); // 设置窗口尺寸
		frame.setVisible(true); // 窗口可视
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 关闭窗口时退出程序
	}

	public static void main(String[] args) {
		Loading loading = getLoading();
		loading.open(); // 运行启动界面
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		loading.close();
		Loading.showFrame();
	}
}

	    			