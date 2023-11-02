package GUID.GUID;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import tw.edu.ym.guid.client.GuidClient;
import tw.edu.ym.guid.client.PII;
import tw.edu.ym.guid.client.PII.Builder;
import tw.edu.ym.guid.client.field.Birthday;
import tw.edu.ym.guid.client.field.Name;
import tw.edu.ym.guid.client.field.Sex;
import tw.edu.ym.guid.client.field.TWNationalId;
import tw.edu.ym.guid.client.hashcode.GuidHashcodeGenerator;
import java.io.FileReader;
import java.util.stream.IntStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class App {
	
	public static void main(String[] args) throws URISyntaxException, IOException, ParseException {

		GUID guid = new GUID("test_patient.csv");
		for (Patient patient : guid.patientList) {
		    DICOM dicom = new DICOM("test.dcm");
		    String uid = patient.Guid;
		    dicom.setGUIDAttributes(uid);
		    dicom.writeMetadata(uid+".dcm");
		    System.out.println("Finished file : "+uid+".dcm");
		}
		
		String csvFilePath = "patient_mapping_list.csv";
		
		// 寫入 CSV 檔案
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFilePath))) {
			writer.write("GUID,Accession number,Identifier,Birthday,Gender,Address,Phone,First Name,Last Name");
			writer.newLine();
		    for (Patient patient : guid.patientList) {
		        writer.write(patient.Datarow);
		        writer.newLine();
		    }
		} catch (IOException e) {
		    e.printStackTrace();
		}

	}

}
