package GUID.GUID;

import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.ElementDictionary;
import org.dcm4che3.data.Tag;
import org.dcm4che3.data.VR;
import org.dcm4che3.io.DicomInputStream;
import org.dcm4che3.io.DicomOutputStream;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class DICOM {
	private static final ElementDictionary DICT = ElementDictionary.getStandardElementDictionary();
	public Attributes attrs;
	public File dcmFile;
	DicomInputStream dis;
	
	public DICOM(String srcPath) throws IOException {
		dcmFile = new File(srcPath);
		try (DicomInputStream dis = new DicomInputStream(dcmFile)) {
			attrs =  dis.readDataset();
			this.dis = dis;
		}
	}
	
	public void writeMetadata(String folder,String fileName) throws IOException {
		File modifiedFile = new File(folder+fileName);
		DicomOutputStream dos = new DicomOutputStream(modifiedFile);
        dos.writeDataset(dis.getFileMetaInformation(), attrs);
        dos.close();
        dis.close();
	}
	
	public void setGUIDAttributes(String pii) {
		setAttribute(Tag.PatientName,pii);
		setAttribute(Tag.PatientID,pii);
		setAttribute(Tag.PatientSex,pii);
		setAttribute(Tag.PatientBirthDate,pii);
		setAttribute(Tag.PatientAddress,pii);
		setAttribute(Tag.PatientTelephoneNumbers,pii);
	}
	
	public void setAttribute(int tag,String value) {
		if(value!=null) {
			this.attrs.setString(tag, DICT.vrOf(tag), value);
		}
	}
	
	public ArrayList<String> getAttributes(){
		ArrayList<String> tempList = new ArrayList<>();
		
		tempList.add(this.attrs.getString(Tag.AccessionNumber));
		tempList.add(this.attrs.getString(Tag.PatientID));
		tempList.add(this.attrs.getString(Tag.PatientBirthDate));
		tempList.add(this.attrs.getString(Tag.PatientSex));
		tempList.add(this.attrs.getString(Tag.PatientAddress));
		tempList.add(this.attrs.getString(Tag.PatientTelephoneNumbers));
		tempList.add(this.attrs.getString(Tag.PatientName));
		tempList.add(this.attrs.getString(Tag.PatientName));
		tempList.add(this.dcmFile.toString());
		return tempList;
	}
       
    
}


