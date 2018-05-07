package service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

@Stateless
public class PublicHolidayService {
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	private List<Date> getAllPublicHolidays() {
		List<Date> list = new ArrayList<Date>();
		try {
			Date anzacDay = sdf.parse("25/04/2018");
			list.add(anzacDay);
			
			Date labourDay = sdf.parse("07/05/2018");
			list.add(labourDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public Boolean isThisDateAHoliday(Date currentDate) {
		for (Date holiday : getAllPublicHolidays()) {
			if(currentDate.equals(holiday)) {
				return true;
			}
		}
		return false;
	}
	
}
