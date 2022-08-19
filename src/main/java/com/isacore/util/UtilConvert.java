package com.isacore.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class UtilConvert {

	public static LocalDate toLocalDate(Date date) {
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}
	
}
