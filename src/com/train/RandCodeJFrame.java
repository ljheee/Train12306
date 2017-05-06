package com.train;

import com.train.entity.TicketData;
import com.train.service.TrainService;
import com.train.swing.BaseFrame;
import com.train.util.HttpsRequestNg;
import com.train.util.Images;
import com.train.util.Loading;
import com.train.util.Logger;
import com.train.util.ObjectToFile;

import javax.swing.*;
import java.awt.*;
import java.awt.Dialog.ModalExclusionType;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class RandCodeJFrame extends BaseFrame {
	private static final long serialVersionUID = -2672739792035356217L;
	private static Logger logger=new Logger();
	public static JPanel imagePanel;
	public static String userName;
	public static String password;
	public static String randCode = "";
	public static String repeat_submit_token = "";
	public static Boolean isOpenMain = true;
	public static void checkUserJob() {
		new Thread() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(10 * 1000);
						Boolean checkUser = TrainService.checkUser();
						if (!checkUser) {
							logger.info("您已被12306踢出，请重新登录");
							TicketData ticketData = (TicketData) ObjectToFile.readObject();
							RandCodeJFrame.userName = ticketData.getUsername();
							RandCodeJFrame.password = ticketData.getPassword();
							RandCodeJFrame.randCode = "";
							RandCodeJFrame.isOpenMain = false;
							new RandCodeJFrame();
							return;
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	public RandCodeJFrame() {
		Loading.getLoading().open();
		setTitle("验证码---右键直接提交");
		try {
			ImageIcon background = new ImageIcon(TrainService.getPassCodeNew("login"));// 背景图片
			JLabel label = new JLabel(background);// 把背景图片显示在一个标签里面
			// 把标签的大小位置设置为图片刚好填充整个面板
			label.setBounds(0, 0, background.getIconWidth(), background.getIconHeight());
			// 把内容窗格转化为JPanel，否则不能用方法setOpaque()来使内容窗格透明
			imagePanel = (JPanel) this.getContentPane();
			imagePanel.setOpaque(false);
			// 内容窗格默认的布局管理器为BorderLayout
			imagePanel.setLayout(new FlowLayout());
			imagePanel.addMouseListener(new MyMouseListener(this));

			this.getLayeredPane().setLayout(null);
			// 把背景图片添加到分层窗格的最底层作为背景
			this.getLayeredPane().add(label, new Integer(Integer.MIN_VALUE));
			this.setFont(new Font("Consolas", Font.PLAIN, 14));
			this.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
			this.setType(Type.UTILITY);
			this.setAlwaysOnTop(true);
			this.setAutoRequestFocus(false);
			this.setResizable(false);
			this.setSize(293, 200);
			this.setLocationRelativeTo(null);
			this.setVisible(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Loading.getLoading().close();
	}
	public static void main(String[] args) {
		new RandCodeJFrame();
	}
}

class MyMouseListener extends MouseAdapter {
	private JFrame jFrame;

	public MyMouseListener(RandCodeJFrame jFrame) {
		this.jFrame = jFrame;
	}

	public void mouseClicked(MouseEvent event) {
		if ((event.getModifiers() & InputEvent.BUTTON1_MASK) != 0) {
			int x = event.getX()-2;
			int y = event.getY() - 10;
			if(y<=20){
				return;
			}
			final JLabel jLabel = new JLabel();
			try {
				jLabel.setIcon(new ImageIcon(Images.getImage("station.png")));
			} catch (Exception e) {
				e.printStackTrace();
			}
			jLabel.setBounds(x, y, 20, 20);
			jLabel.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					RandCodeJFrame.imagePanel.remove(jLabel);
					jFrame.repaint();
					
					Component[] components = RandCodeJFrame.imagePanel.getComponents();
					RandCodeJFrame.randCode ="";
					for (int i = 0; i < components.length; i++) {
						int x2 = (int) components[i].getBounds().getX();
						int y2 = (int) components[i].getBounds().getY();
						RandCodeJFrame.randCode += x2 + "," + y2 + ",";
					}
				}
			});
			RandCodeJFrame.imagePanel.add(jLabel);
			jFrame.repaint();
			RandCodeJFrame.randCode += x + "," + y + ",";
		}
		if ((event.getModifiers() & InputEvent.BUTTON3_MASK) != 0) {
			Loading.getLoading().open();
			try {
				Boolean result = TrainService.checkRandCodeAnsyn(
						RandCodeJFrame.randCode.substring(0, RandCodeJFrame.randCode.length() - 1),
						RandCodeJFrame.repeat_submit_token);
				jFrame.dispose();
				if (result) {
					Boolean loginCheck = TrainService.loginAysnSuggest(RandCodeJFrame.userName, RandCodeJFrame.password,
							RandCodeJFrame.randCode.substring(0, RandCodeJFrame.randCode.length() - 1));
					if (loginCheck) {
						if (RandCodeJFrame.isOpenMain) {
							LoginFrame.frame.dispose();
							new MainFrame().setVisible(true);
						}
						new Thread() {
							public void run() {
								ObjectToFile.writeObject(new TicketData(RandCodeJFrame.userName,
										RandCodeJFrame.password, HttpsRequestNg.getHttpClient().cookies));
								RandCodeJFrame.checkUserJob();
							}
						}.start();
					} else {
						RandCodeJFrame.alert("用户名或者密码错误");
					}
				} else {
					new RandCodeJFrame();
					RandCodeJFrame.randCode = "";
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				Loading.getLoading().close();
			}
		}
	}
}