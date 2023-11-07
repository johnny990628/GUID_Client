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

import javax.imageio.ImageIO;

public class DICOM {
	private static final ElementDictionary DICT = ElementDictionary.getStandardElementDictionary();
	public Attributes attrs;
	private File f;
	DicomInputStream dis;
	
	public DICOM(String srcPath) throws IOException {
		f = new File(srcPath);
		try (DicomInputStream dis = new DicomInputStream(f)) {
			attrs =  dis.readDataset();
			this.dis = dis;
		}
	}
	
	public void writeMetadata(String desPath) throws IOException {
		File modifiedFile = new File("./output/"+desPath);
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
       
    
}


