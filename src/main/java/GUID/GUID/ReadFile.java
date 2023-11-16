package GUID.GUID;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JTextArea;

import org.dcm4che3.data.Tag;

public class ReadFile {
	
	public static ArrayList<Patient> readPatientCSV(String csvPath) throws FileNotFoundException, ParseException, URISyntaxException {
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
	
	public static ArrayList<String> setGUIDFromPatientList(String folder,ArrayList<Patient> patientList) throws IOException {
		ArrayList<String> tempList = new ArrayList<>();
		for (Patient patient : patientList) {
		    DICOM dicom = new DICOM(patient.dcmFile);
		    String uid = patient.Guid;
		    dicom.setGUIDAttributes(uid);
		    String fileName = uid+".dcm";
		    dicom.writeMetadata(folder,fileName);
		    tempList.add("Finished file : "+fileName);
		}
		return tempList;
	}
	
	
	public static ArrayList<DICOM> readDCMFolder(File folder) throws IOException {
		ArrayList<DICOM> tempList = new ArrayList<>();
		File[] files = folder.listFiles(new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        return !name.equals(".DS_Store");
		    }
		});
		for (File fileEntry : files) {
			String fileName = fileEntry.getName();
			DICOM dicom = new DICOM(folder.getName()+"/"+fileName);
        	tempList.add(dicom);
	    }
		return tempList;
	}
	
	public static ArrayList<Patient> genPatientListFromDCMList(ArrayList<DICOM> dcmList) throws ParseException, URISyntaxException, IOException {
		ArrayList<Patient> tempList = new ArrayList<>();
		for (DICOM dicom : dcmList) {
			ArrayList<String> attrs = dicom.getAttributes();
			tempList.add(new Patient(attrs));
		}
		return tempList;
	}
	
	public static void genMappingFile(String filePath,ArrayList<Patient> patientList,String columns) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
			writer.write(columns);
			writer.newLine();
		    for (Patient patient : patientList) {
		        writer.write(patient.Datarow);
		        writer.newLine();
		    }
		    
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}
	
}
