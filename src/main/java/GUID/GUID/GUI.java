package GUID.GUID;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class GUI extends JFrame {
	private JTextArea textArea;
	private JLabel dcmPath;
	private JLabel outPath;
	private JProgressBar progressBar;
	private String CURRENT_PATH = "./";

    public GUI() {
        super("File Reader");

        // 创建组件
        JButton selectDCMButton = new JButton("Select dicoms folder");
        dcmPath = new JLabel("");
        
        JButton selectOutputButton = new JButton("Select output folder");
        outPath = new JLabel("");
        JButton convertButton = new JButton("Convert!");
        progressBar = new JProgressBar(0, 100);
        
        
        textArea = new JTextArea(20, 30);
        JScrollPane scrollPane = new JScrollPane(textArea);

        // 设置布局
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // 添加组件到容器
        add(selectDCMButton);
        add(dcmPath);
        add(selectOutputButton);
        add(outPath);
        add(convertButton);
        add(progressBar);
        add(scrollPane);

        // 添加事件监听器
        selectDCMButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	chooseDCMFolder();
            }
        });
        selectOutputButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	chooseOutputFolder();
            }
        });
        
        convertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	try {
					convert();
				} catch (IOException | ParseException | URISyntaxException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });

        // 设置窗口属性
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null); // 将窗口置于屏幕中央
    }

    private void chooseDCMFolder() {
        JFileChooser folderChooser = new JFileChooser();
        folderChooser.setCurrentDirectory(new File(CURRENT_PATH));
        folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = folderChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFolder = folderChooser.getSelectedFile();
//            File[] files = selectedFolder.listFiles();
//            String fileNames = "";
//            for (File file : files) {
//            	String fileName = file.getName();
//            	if(!fileName.equals(".DS_Store")) {
//            		 fileNames += fileName+"\n";
//            	}
//            }      
            dcmPath.setText(selectedFolder.getAbsolutePath()+"/");;
           
        }
    }
    
    private void chooseOutputFolder() {
        JFileChooser folderChooser = new JFileChooser();
        folderChooser.setCurrentDirectory(new File(CURRENT_PATH));
        folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = folderChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFolder = folderChooser.getSelectedFile();
            outPath.setText(selectedFolder.getAbsolutePath()+"/");;
        }
    }
    
    private void convert() throws IOException, ParseException, URISyntaxException {
    	progressBar.setValue(0);
		ArrayList<DICOM> dcmList = ReadFile.readDCMFolder(new File(dcmPath.getText()));
		progressBar.setValue(40);
		ArrayList<Patient> patientList = ReadFile.genPatientListFromDCMList(dcmList);
		progressBar.setValue(60);
		ArrayList<String> fileList = ReadFile.setGUIDFromPatientList(outPath.getText(),patientList);
		for(String file : fileList) {
			textArea.append(file+"\n");
		}
		progressBar.setValue(80);
		String columns = "GUID,Accession number,StudyDate,Identifier,Birthday,Gender,Address,Phone,First Name,Last Name";
		ReadFile.genMappingFile("patient_mapping_list.csv",patientList,columns);
		textArea.append("Finished mapping file. \n");
		progressBar.setValue(100);
		textArea.append("Done.");
    }
}
