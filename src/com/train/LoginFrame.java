package com.train;

import com.train.entity.TicketData;
import com.train.service.TrainService;
import com.train.swing.BaseFrame;
import com.train.util.Images;
import com.train.util.Loading;
import com.train.util.ObjectToFile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginFrame extends BaseFrame {
	private static final long serialVersionUID = -6541864654653129335L;
	static LoginFrame frame;
	
	
	public static void main(String[] args) {
		Loading loading = Loading.getLoading();
		loading.open();
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			if(TrainService.checkUser()){
				new MainFrame().setVisible(true);
				RandCodeJFrame.checkUserJob();
				return;
			}
			frame= new LoginFrame();
			init();
			SwingUtilities.updateComponentTreeUI(frame);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			loading.close();
		}
	}
 
	public static void init() {
		frame.setType(Type.UTILITY);
		frame.setAlwaysOnTop(true);
		frame.setAutoRequestFocus(false);
		frame.setResizable(false);
		frame.setIconImage(Toolkit.getDefaultToolkit().createImage(Images.getImage("logo.png")));
		JLabel jl = new JLabel("欢迎使用12306抢票", SwingUtilities.CENTER);
		Font font = new Font("宋体", Font.BOLD, 24);
		jl.setFont(font);
		jl.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		frame.getContentPane().add(jl, BorderLayout.NORTH);

		font = new Font("宋体", Font.PLAIN, 12);

		JLabel jl_name = new JLabel("用户名：", SwingUtilities.RIGHT);
		jl_name.setFont(font);

		JLabel jl_pass = new JLabel("密　码：", SwingUtilities.RIGHT);
		jl_pass.setFont(font);

		JPanel jp_center_left = new JPanel();
		jp_center_left.setLayout(new GridLayout(5, 1));
		jp_center_left.add(jl_name);
		jp_center_left.add(jl_pass);

		final JTextField jt_name = new JTextField();
		final JPasswordField jt_pass = new JPasswordField();
		jt_pass.setEchoChar('*');
		Object readObject = ObjectToFile.readObject();
		if(readObject!=null){
		TicketData ticketData=(TicketData)readObject;
			jt_name.setText(ticketData.getUsername());
			jt_pass.setText(ticketData.getPassword());
		}
		JPanel jp_center_right = new JPanel();
		jp_center_right.setLayout(new GridLayout(5, 1));
		jp_center_right.add(jt_name);
		jp_center_right.add(jt_pass);

		JPanel jp_center = new JPanel();
		jp_center.setLayout(new GridLayout(1, 2));
		jp_center.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 60));
		jp_center.add(jp_center_left);
		jp_center.add(jp_center_right);
		final JButton jb1 = new JButton("确认");
		jb1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				String userName = jt_name.getText();
				if (userName == null || "".equals(userName)) {
					LoginFrame.alert("请填写用户名");
					return;
				}
				String password = new String(jt_pass.getPassword());
				if (password == null || "".equals(password)) {
					LoginFrame.alert("请填写密码");
					return;
				}
				new RandCodeJFrame();
				RandCodeJFrame.password = password;
				RandCodeJFrame.userName = userName;
				RandCodeJFrame.randCode = "";
				RandCodeJFrame.repeat_submit_token = "";
			}
		});
		JButton jb2 = new JButton("退出");
		jb2.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent event) {
				System.exit(0);
			}
		});
		JPanel jp_south = new JPanel();
		jp_south.add(jb1);
		jp_south.add(jb2);
		jp_south.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		frame.getContentPane().add(jp_center);
		frame.getContentPane().add(jp_south, BorderLayout.SOUTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setSize(370, 280);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
	}
}
