package com.train.entity;
/**
 * 乘客信息 model
 * @author lijintao
 *
 */
public class Passenger {
	private String passenger_name;
	private String passenger_id_type_name;
	private String passenger_id_type_code;
	private String passenger_id_no;
	private String mobile_no;
	private String phone_no;
	private String passenger_type_name;
	private String passenger_type;
	public String getPassenger_name() {
		return passenger_name;
	}
	public void setPassenger_name(String passenger_name) {
		this.passenger_name = passenger_name;
	}
	public String getPassenger_id_type_name() {
		return passenger_id_type_name!=null?passenger_id_type_name:"二代身份证";
	}
	public void setPassenger_id_type_name(String passenger_id_type_name) {
		this.passenger_id_type_name = passenger_id_type_name;
	}
	public String getPassenger_id_type_code() {
		return passenger_id_type_code;
	}
	public void setPassenger_id_type_code(String passenger_id_type_code) {
		this.passenger_id_type_code = passenger_id_type_code;
	}
	public String getPassenger_id_no() {
		return passenger_id_no;
	}
	public void setPassenger_id_no(String passenger_id_no) {
		this.passenger_id_no = passenger_id_no;
	}
	public String getMobile_no() {
		return mobile_no!=null?mobile_no:"";
	}
	public void setMobile_no(String mobile_no) {
		this.mobile_no = mobile_no;
	}
	public String getPhone_no() {
		return phone_no!=null?phone_no:"";
	}
	public void setPhone_no(String phone_no) {
		this.phone_no = phone_no;
	}
	public String getPassenger_type_name() {
		return passenger_type_name;
	}
	public void setPassenger_type_name(String passenger_type_name) {
		this.passenger_type_name = passenger_type_name;
	}
	public String getPassenger_type() {
		return passenger_type;
	}
	public void setPassenger_type(String passenger_type) {
		this.passenger_type = passenger_type;
	}
	
}
