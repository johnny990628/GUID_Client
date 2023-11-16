package GUID.GUID;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
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

import javax.swing.SwingUtilities;

import org.dcm4che3.data.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class App {
	
	public static void main(String[] args) throws URISyntaxException, IOException, ParseException {
		String PATIENT_LIST = "test_patient.csv";
		File DCM_FOLDER = new File("dicoms");
		SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GUI().setVisible(true);
            }
        });
		
//		ArrayList<Patient> patientList = ReadFile.readPatientCSV(PATIENT_LIST);
//		ReadFile.setGUIDFromPatientList(patientList);
		
//		ArrayList<DICOM> dcmList = ReadFile.readDCMFolder(DCM_FOLDER);
//		ArrayList<Patient> patientList = ReadFile.genPatientListFromDCMList(dcmList);
//		ReadFile.setGUIDFromPatientList(patientList);
		
//		String columns = "GUID,Accession number,Identifier,Birthday,Gender,Address,Phone,First Name,Last Name";
//		ReadFile.genMappingFile("patient_mapping_list.csv",patientList,columns);
				
	}

}
