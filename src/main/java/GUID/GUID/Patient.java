package GUID.GUID;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	public String dcmFile;
	public String StudyDate;
	
	public Patient(List<String> patientArray) throws ParseException, URISyntaxException, IOException {
		this.AccessionNumber = patientArray.get(0);
		this.Idenifier = patientArray.get(1);
		this.BirthDate = patientArray.get(2);
		this.Gender = patientArray.get(3);
		this.Address = patientArray.get(4);
		this.Phone = patientArray.get(5);
		this.FirstName = patientArray.get(6);
		this.LastName = patientArray.get(7);
		this.dcmFile = patientArray.get(8);
		this.StudyDate = patientArray.get(9);
		this.Guid = getGUID();
		this.Datarow =getDatarow();
		
	}
	
	private String getDatarow() {
		 return this.Guid+","+this.AccessionNumber+","+this.StudyDate+","+this.Idenifier+","
				+this.BirthDate+","+this.Gender+","+this.Address+","+this.Phone+","+this.FirstName+","+this.LastName;
	}
	
	private String getGUID() throws URISyntaxException, IOException, ParseException {
		Sex sex = String.valueOf(this.Gender.charAt(0)) == "F" ? Sex.FEMALE : Sex.MALE;
		int[] birthday_Array = parseDate(this.BirthDate);
		Birthday birthDate = new Birthday(birthday_Array[0], birthday_Array[1], birthday_Array[2]);
		return GUID.encodePatientInfo(this.FirstName,this.LastName,sex,birthDate,this.Idenifier);
	}
	
	
	private static int[] parseDate(String dateString) {
        int[] result = new int[3];

        try {
            SimpleDateFormat dateFormat;

            if (dateString.contains("-")) {
                dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            } else {
                dateFormat = new SimpleDateFormat("yyyyMMdd");
            }

            Date date = dateFormat.parse(dateString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            result[0] = calendar.get(Calendar.YEAR);
            result[1] = calendar.get(Calendar.MONTH) + 1; // Month is 0-based
            result[2] = calendar.get(Calendar.DAY_OF_MONTH);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return result;
    }


	
	
}
