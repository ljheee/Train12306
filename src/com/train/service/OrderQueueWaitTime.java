package com.train.service;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.train.swing.BaseFrame;
import com.train.swing.TimerCloseFrame;
import com.train.util.Logger;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * 提交订单自动获取排队信息
 * @author lijintao
 */
public class OrderQueueWaitTime extends Thread{
	private Logger logger=new Logger();
	private int dispTime = 1;
	private int nextRequestTime = 1;
	private boolean isFinished = false;
	private JSONObject waitObj;
	private String repeat_submit_token;
	private TimerCloseFrame timerCloseFrame;
	
	public TimerCloseFrame getTimerCloseFrame() {
		if (timerCloseFrame == null) {
			timerCloseFrame = BaseFrame.tip("",System.currentTimeMillis());
		}
		return timerCloseFrame;
	}
	/**
	 * @param mainFrame
	 */
	public OrderQueueWaitTime(String repeat_submit_token) {
		this.repeat_submit_token = repeat_submit_token;
	}

	public void waitFunc(int return_time, String show_time) {
		if (return_time <= 5) {
			getTimerCloseFrame().setMessage("您的订单已经提交，系统正在处理中，请稍等。");
		} else if (return_time > 30 * 60) {
			getTimerCloseFrame().setMessage("您的订单已经提交，预计等待时间超过30分钟，请耐心等待。");
		} else {
			getTimerCloseFrame().setMessage("您的订单已经提交，最新预估等待时间" + show_time + "，请耐心等待。");
		}
	}
	
	// 跳转-单程
	public void  finishFun(int time, JSONObject returnObj) throws JSONException {
		if (time==-1) {
			if (BaseFrame.prompt("订票成功","订单号："+returnObj.getJSONObject("data").getString("orderId")+" 前往12306进行支付？")) {
				try {
					Desktop desktop = Desktop.getDesktop();
					if (Desktop.isDesktopSupported() && desktop.isSupported(Desktop.Action.BROWSE)) {
						URI uri = new URI("https://kyfw.12306.cn/otn/leftTicket/init");
						desktop.browse(uri);
					}
				} catch (URISyntaxException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			BaseFrame.alert("订单预定失败!");
		}
	}
	@Override
	public void run() {
		while(!isFinished) {
			try {
				timerJob();
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void timerJob() throws JSONException{
		if(this.dispTime ==-1){
			this.isFinished = true;
			this.finishFun(this.dispTime,this.waitObj);
			return;
		}
		
		if(this.dispTime == this.nextRequestTime){
			this.getWaitTime();
		}
		//格式化时间，把秒转换为时分秒
		int second = this.dispTime;
		String show_time = "";
		int minute = (int)(second / 60);
		if (minute >= 1) {
			show_time = minute + "分";
			second = second % 60;
		} else {
			show_time = "1分";
		}
		this.waitFunc(this.dispTime>1?--this.dispTime:1, show_time);
	}
	
	public void getWaitTime(){
		try {
			String queryOrderWaitTime = TrainService.queryOrderWaitTime(repeat_submit_token);
			waitObj = JSONObject.parseObject(queryOrderWaitTime);
			logger.info("订单提交中");
			System.out.println(queryOrderWaitTime);
			if(waitObj!=null) {
				this.dispTime = waitObj.getJSONObject("data").getIntValue("waitTime");
				int flashWaitTime = (int)(this.dispTime/1.5);
				flashWaitTime = flashWaitTime>60?60:flashWaitTime;
				int nextTime = this.dispTime - flashWaitTime;
				this.nextRequestTime = nextTime<=0?1:nextTime;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}

