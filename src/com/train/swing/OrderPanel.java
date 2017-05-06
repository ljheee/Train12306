package com.train.swing;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.train.service.TrainService;
/**
 * 订单管理页面
 * @author lijintao
 */
public class OrderPanel extends JPanel {
	private static final long serialVersionUID = -1629696131872531893L;
	private JScrollPane jScrollPane;
	private JTable orderJTable;
	private JPanel jPanel;
	private JButton serachButton;
	private JPanel canceJPanel;
	private JButton canceJButton;
	private JPanel refundJPanel;
	private JButton refundJButton;
	public OrderPanel() {
		final DefaultTableModel orderTableModel = new DefaultTableModel(null,
				new String[] {"订单号", "订单时间", "发车时间", "车次", "出发地", "目的地", "乘客", "票种", "席别", "车厢", "座位", "票价", "状态" }) {
			private static final long serialVersionUID = -7640954343136822571L;

			public boolean isCellEditable(int row, int column) {
				Object valueAt = orderJTable.getValueAt(row, 12);
				if("待支付".equals(valueAt.toString())){
					canceJButton.setEnabled(true);
					refundJButton.setEnabled(true);
				}else{
					refundJButton.setEnabled(false);
					canceJButton.setEnabled(false);
				}
				if (column == 0) {
					return true;
				}
				return false;
			}
		};
		jPanel = new JPanel();
		orderJTable = new JTable(orderTableModel);
		orderJTable.setBorder(new LineBorder(new Color(0, 0, 0)));
		orderJTable.setRowHeight(25);
		DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();//单元格渲染器  
		tcr.setHorizontalAlignment(SwingConstants.CENTER);
		orderJTable.setDefaultRenderer(Object.class, tcr);//设置渲染器  
		orderJTable.getColumnModel().getColumn(2).setPreferredWidth(120);
		orderJTable.getColumnModel().getColumn(9).setPreferredWidth(35);
		orderJTable.getColumnModel().getColumn(11).setPreferredWidth(45);
		orderJTable.getColumnModel().getColumn(12).setPreferredWidth(45);
		orderJTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jScrollPane = new JScrollPane(orderJTable);

		JPanel panel = new JPanel();

		serachButton = new JButton("查询订单");
		serachButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				serachButton.setEnabled(false);
				for (int i = 0; i < orderTableModel.getRowCount();) {
					orderTableModel.removeRow(i);
				}
				JSONArray myOrderNoComplete = TrainService.queryMyOrderNoComplete();
				if(myOrderNoComplete!=null){
					addOrderTableData(orderTableModel, myOrderNoComplete);
				}
				JSONArray orderDTODataList = TrainService.queryMyOrder("H");
				if(orderDTODataList!=null){
					addOrderTableData(orderTableModel, orderDTODataList);
				}
				orderDTODataList = TrainService.queryMyOrder("G");
				if(orderDTODataList!=null){
					addOrderTableData(orderTableModel, orderDTODataList);
				}
				
				serachButton.setEnabled(true);
			}

			private void addOrderTableData(final DefaultTableModel orderTableModel, JSONArray orderDTODataList) {
				if(orderDTODataList.size()>0){
					Object[] rowData = new Object[13];
					for (int i = 0; i < orderDTODataList.size(); i++) {
						JSONObject orderDto = orderDTODataList.getJSONObject(i);
						JSONArray tickets = orderDto.getJSONArray("tickets");
						for (int j = 0; j < tickets.size(); j++) {
							JSONObject ticket = tickets.getJSONObject(i);
							JSONObject passengerDTO = ticket.getJSONObject("passengerDTO");
							rowData[0]=orderDto.getString("sequence_no");
							rowData[1]=orderDto.getString("order_date").split(" ")[0];
							rowData[2]=orderDto.getString("start_train_date_page");
							rowData[3]=orderDto.getString("train_code_page");
							rowData[4]=orderDto.getJSONArray("from_station_name_page").get(0);
							rowData[5]=orderDto.getJSONArray("to_station_name_page").get(0);
							rowData[6]=passengerDTO.getString("passenger_name");
							rowData[7]=ticket.getString("ticket_type_name");
							rowData[8]=ticket.getString("seat_type_name");
							rowData[9]=ticket.getString("coach_name");
							rowData[10]=ticket.getString("seat_name");
							rowData[11]=ticket.getString("str_ticket_price_page");
							String ticket_status_name = ticket.getString("ticket_status_name");
							rowData[12]=ticket_status_name;
							orderTableModel.addRow(rowData);
						}
					}
				}
			}
		});

		GroupLayout panel1Layout = new GroupLayout(panel);
		panel1Layout.setHorizontalGroup(panel1Layout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING,
				panel1Layout.createSequentialGroup().addContainerGap(33, Short.MAX_VALUE).addComponent(serachButton)
						.addGap(22)));
		panel1Layout.setVerticalGroup(
				panel1Layout.createParallelGroup(Alignment.LEADING).addGroup(panel1Layout.createSequentialGroup()
						.addComponent(serachButton).addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		panel.setBorder(BorderFactory.createTitledBorder("操作"));
		panel.setLayout(panel1Layout);

		canceJPanel = new JPanel();
		canceJPanel.setBorder(BorderFactory.createTitledBorder("未完成操作"));

		canceJButton = new JButton("取消订单");
		canceJButton.setEnabled(false);
		canceJButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedRow = orderJTable.getSelectedRow();
				if(selectedRow<0){
					BaseFrame.alert("请选择要操作的订单");
					return;
				}
				if(BaseFrame.prompt("确定取消订单，每个用户每天只可取消三次订单！！")){
					String orderId = orderJTable.getValueAt(selectedRow,0).toString();
					Boolean result = TrainService.cancelNoCompleteMyOrder(orderId);
					BaseFrame.alert(result?"取消订单成功":"取消订单失败");
				}
			}
		});
		GroupLayout gl_panel_1 = new GroupLayout(canceJPanel);
		gl_panel_1.setHorizontalGroup(gl_panel_1.createParallelGroup(Alignment.LEADING).addGap(0, 127, Short.MAX_VALUE)
				.addGroup(Alignment.TRAILING, gl_panel_1.createSequentialGroup().addContainerGap(33, Short.MAX_VALUE)
						.addComponent(canceJButton).addGap(22)));
		gl_panel_1.setVerticalGroup(gl_panel_1.createParallelGroup(Alignment.LEADING).addGap(0, 50, Short.MAX_VALUE)
				.addGroup(gl_panel_1.createSequentialGroup().addComponent(canceJButton)
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		canceJPanel.setLayout(gl_panel_1);

		refundJPanel = new JPanel();
		refundJPanel.setBorder(BorderFactory.createTitledBorder("已完成操作"));

		refundJButton = new JButton("退票");
		refundJButton.setEnabled(false);
		refundJButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BaseFrame.alert("暂未开放");
			}
		});
		GroupLayout gl_panel_2 = new GroupLayout(refundJPanel);
		gl_panel_2.setHorizontalGroup(gl_panel_2.createParallelGroup(Alignment.TRAILING).addGap(0, 127, Short.MAX_VALUE)
				.addGroup(gl_panel_2.createSequentialGroup().addContainerGap(12, Short.MAX_VALUE).addComponent(refundJButton)
						.addGap(22)));
		gl_panel_2.setVerticalGroup(gl_panel_2.createParallelGroup(Alignment.LEADING).addGap(0, 50, Short.MAX_VALUE)
				.addGroup(gl_panel_2.createSequentialGroup().addComponent(refundJButton)
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		refundJPanel.setLayout(gl_panel_2);

		GroupLayout panel7Layout = new GroupLayout(jPanel);
		panel7Layout.setHorizontalGroup(
			panel7Layout.createParallelGroup(Alignment.LEADING)
				.addGroup(panel7Layout.createSequentialGroup()
					.addGroup(panel7Layout.createParallelGroup(Alignment.LEADING)
						.addGroup(panel7Layout.createSequentialGroup()
							.addContainerGap()
							.addComponent(jScrollPane, GroupLayout.DEFAULT_SIZE, 1024, Short.MAX_VALUE))
						.addGroup(panel7Layout.createSequentialGroup()
							.addGap(18)
							.addComponent(panel, GroupLayout.PREFERRED_SIZE, 127, GroupLayout.PREFERRED_SIZE)
							.addGap(35)
							.addComponent(canceJPanel, GroupLayout.PREFERRED_SIZE, 127, GroupLayout.PREFERRED_SIZE)
							.addGap(35)
							.addComponent(refundJPanel, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		panel7Layout.setVerticalGroup(
			panel7Layout.createParallelGroup(Alignment.LEADING)
				.addGroup(panel7Layout.createSequentialGroup()
					.addContainerGap()
					.addGroup(panel7Layout.createParallelGroup(Alignment.LEADING)
						.addComponent(panel, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
						.addComponent(canceJPanel, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
						.addComponent(refundJPanel, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addComponent(jScrollPane, GroupLayout.PREFERRED_SIZE, 351, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(13, Short.MAX_VALUE))
		);
		jPanel.setLayout(panel7Layout);
		add(jPanel);
	}
}
