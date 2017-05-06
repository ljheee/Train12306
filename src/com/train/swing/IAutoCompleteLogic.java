package com.train.swing;

import javax.swing.JList;

import com.train.entity.Station;

public interface IAutoCompleteLogic {
	
	public void logic(String input,Object[] notes,JList<Station> list);
	
}
