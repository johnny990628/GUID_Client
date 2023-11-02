package GUID.GUID;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import tw.edu.ym.guid.client.GuidClient;
import tw.edu.ym.guid.client.PII;
import tw.edu.ym.guid.client.field.Birthday;
import tw.edu.ym.guid.client.field.Name;
import tw.edu.ym.guid.client.field.Sex;
import tw.edu.ym.guid.client.field.TWNationalId;

public class Patient {
	public String Guid;
	public String AccessionNumber;
	public String Idenifier;
	public String BirthDate;
	public String Gender;
	public String Address;
	public String Phone;
	public String FirstName;
	public String LastName;
	public String Datarow;
	
	public Patient(List<String> patientArray) throws ParseException, URISyntaxException, IOException {
		this.AccessionNumber = patientArray.get(0);
		this.Idenifier = patientArray.get(1);
		this.BirthDate = patientArray.get(2);
		this.Gender = patientArray.get(3);
		this.Address = patientArray.get(4);
		this.Phone = patientArray.get(5);
		this.FirstName = patientArray.get(6);
		this.LastName = patientArray.get(7);
		this.Guid = getGUID();
		this.Datarow =getDatarow();
		
	}
	
	
	
	private String getDatarow() {
		 return this.Guid+","+this.AccessionNumber+","+this.Idenifier+","
				+this.BirthDate+","+this.Gender+","+this.Address+","+this.Phone+","+this.FirstName+","+this.LastName;
	}
	
	private String getGUID() throws URISyntaxException, IOException, ParseException {
		Sex sex = String.valueOf(this.Gender.charAt(0)) == "F" ? Sex.FEMALE : Sex.MALE;
		int[] birthday_Array = formatDate(this.BirthDate);
		Birthday birthDate = new Birthday(birthday_Array[0], birthday_Array[1], birthday_Array[2]);
		return GUID.encodePatientInfo(this.FirstName,this.LastName,sex,birthDate,this.Idenifier);
	}
	
	
	private int[] formatDate(String Odate) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date parsedDate = dateFormat.parse(Odate);
		java.sql.Date sqlDate = new java.sql.Date(parsedDate.getTime());
		SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
		SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
		SimpleDateFormat dayFormat = new SimpleDateFormat("dd");

		int year = Integer.parseInt(yearFormat.format(sqlDate));
		int month = Integer.parseInt(monthFormat.format(sqlDate));
		int day = Integer.parseInt(dayFormat.format(sqlDate));
		int dayArray[] = { year, month, day };
		return dayArray;
	}
	
	
}
