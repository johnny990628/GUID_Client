package GUID.GUID;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tw.edu.ym.guid.client.GuidClient;
import tw.edu.ym.guid.client.PII;
import tw.edu.ym.guid.client.field.Birthday;
import tw.edu.ym.guid.client.field.Name;
import tw.edu.ym.guid.client.field.Sex;
import tw.edu.ym.guid.client.field.TWNationalId;

public class GUID {
	static String GUID_SERVER_URL = "https://120.126.47.138:8443";
	static String GUID_SERVER_USERNAME = "nycutest001";
	static String GUID_SERVER_PASSWORD = "Ksx6O%$i";
	static String GUID_SERVER_PREFIX = "NYCU01";
	
	public ArrayList<Patient> patientList;
	
	public GUID(String csvPath) throws FileNotFoundException, ParseException, URISyntaxException {
		this.patientList = readCSV(csvPath);
	}
	
	private ArrayList<Patient> readCSV(String csvPath) throws FileNotFoundException, ParseException, URISyntaxException {
		ArrayList<Patient> tempList = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(csvPath))) {
			
			String line = "";
			Boolean firstTime = true;
			while ((line = br.readLine()) != null) {
				List<String> list = Arrays.asList(line.split(","));
				
				if(!firstTime) {
					tempList.add(new Patient(list));
				}
				firstTime = false;
				
			}
		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tempList;
	}
	public static String encodePatientInfo(String firstName, String lastName, Sex sex, Birthday birthday, String nationalId)
			throws URISyntaxException, IOException {
		PII pii = new PII.Builder(new Name(firstName, lastName), sex, birthday, new TWNationalId(nationalId)).build();
		GuidClient gc = new GuidClient(new URI(GUID_SERVER_URL), GUID_SERVER_USERNAME, GUID_SERVER_PASSWORD,
				GUID_SERVER_PREFIX);
		return gc.create(pii);
	}
}
