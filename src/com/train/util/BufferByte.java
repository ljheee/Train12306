package com.train.util;

public class BufferByte {
	private byte[] buffer;
	private int length = 0;
	public BufferByte() {
		buffer = new byte[512];
	}

	public void append(byte[] bs){
		append(bs,0,bs.length);
	}
	public void append(byte[] bs,int offset,int len){
		while(length+len>buffer.length){
			byte[] temp = new byte[buffer.length*3/2];
			System.arraycopy(buffer, 0, temp, 0, length);
			buffer = temp;
		}
		System.arraycopy(bs, offset, buffer, length, len);
		length = length+len;
	}
	
	public byte[] getBuffer(){
		byte[] temp = new byte[length];
		System.arraycopy(buffer, 0, temp, 0, length);
		return temp;
	}
}
