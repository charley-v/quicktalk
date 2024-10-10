package com.quicktalk.utilities;

public class Utility {

	public static boolean isNull(String obj) {
		if(obj==null || obj.trim().equals("") || obj.isEmpty())
			return true;
		else
			return false;
	}

}
