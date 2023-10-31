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

public class App {
	static String GUID_SERVER_URL = "your guid_server url";
	static String GUID_SERVER_USERNAME = "your guid_server username";
	static String GUID_SERVER_PASSWORD = "your guid_server password";
	static String GUID_SERVER_PREFIX = "your guid_server prefix";

	static int Name_index = 1;
	static int[] needToEnCoder = new int[] { 1, 4 };

	static String csvFile = "112students.csv"; // 從命令行參數中獲取CSV文件的路徑和文件名稱
	static String csvSplitBy = ","; // CSV文件中的分隔符
	static String outputMappingCsvFile = "mapping.csv"; // 輸出的CSV文件名稱
	static String outputEnCoderCsvFile = "enCoder.csv"; // 輸出的CSV文件名稱

	public static void main(String[] args) throws URISyntaxException, IOException {

		try (BufferedReader br = new BufferedReader(new FileReader(csvFile));
				BufferedWriter bwM = new BufferedWriter(new FileWriter(outputMappingCsvFile));
				BufferedWriter bwE = new BufferedWriter(new FileWriter(outputEnCoderCsvFile));) {

			ArrayList<List<String>> csvData = readCSV(br);
			ArrayList<List<String>> mappingData = mapping(csvData);
			ArrayList<List<String>> enCoderData = enCoder(mappingData);
			
			witeCSV(mappingData,bwM);
			witeCSV(enCoderData,bwE);


		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static ArrayList<List<String>> enCoder(ArrayList<List<String>> mappingData) {
		ArrayList<List<String>> result = new ArrayList<>();
		// List<String> row : mappingData) {
		for (int i = 0; i < mappingData.size(); i++) {
			List<String> row = mappingData.get(i); // 獲取內層List<String>
			String GUID = row.get(0); // 獲取索引0的元素（假設第一個元素是GUID）
			row.remove(0);

			if(i!=0) {
				for (int needToEnCoder_index : needToEnCoder) {
	                row.set(needToEnCoder_index, GUID);
				}
			}
			System.out.println(row);
			result.add(row);
		}

	return result;

	}

	public static void witeCSV(ArrayList<List<String>> DataList,BufferedWriter csv) {
		try (BufferedWriter writer = csv) {
			for (List<String> row : DataList) {
				StringBuilder line = new StringBuilder();
				for (String value : row) {
					line.append(value).append(",");
				}
				// 移除最后一个逗号
				line.deleteCharAt(line.length() - 1);
				writer.write(line.toString());
				writer.newLine();
			}
			System.out.println("CSV文件已成功写入: " + outputMappingCsvFile);
		} catch (IOException e) {
			System.err.println("写入CSV文件时发生错误: " + e.getMessage());
		}
	}

	public static ArrayList<List<String>> mapping(ArrayList<List<String>> csvData)
			throws URISyntaxException, IOException {
		ArrayList<List<String>> result = new ArrayList<>();

		try {
			for (int i = 0; i < csvData.size(); i++) {
				String GuidValue = "GUID";
				List<String> resultData = new ArrayList<>(csvData.get(i));

				if (i != 0) {
					// 根据Name_index获取firstName和lastName
					int Name_index = 0; // 你需要正确地定义和赋值Name_index
					String firstName = String.valueOf(resultData.get(Name_index).charAt(0));
					String lastName = resultData.get(Name_index).substring(1);

					// 使用内置类型替代自定义的Sex和Birthday类
					Sex sex = Sex.MALE;
					Birthday birthday = new Birthday(2001, 1, 1);
					String nationalId = "A123456789";

					// GUID enCoder
					GuidValue = GUIDenCoder(firstName, lastName, sex, birthday, nationalId);
				}

				resultData.add(0, GuidValue);
				System.out.println(resultData);
				result.add(resultData);
			}
		} catch (Exception e) {
			// 处理可能的异常
			System.err.println("处理数据时发生错误: " + e.getMessage());
		}

		return result;
	}

	public static ArrayList<List<String>> readCSV(BufferedReader br) throws IOException {
		ArrayList<List<String>> result = new ArrayList<>();

		String line = "";
		while ((line = br.readLine()) != null) {
			List<String> list = Arrays.asList(line.split(","));

			result.add(list);
		}

		return result;
	}

	public static void witeEnCoderCSV(String[] data, int row_index, String piiString, int[] needToEnCoder,
			BufferedWriter bwE) {
		// 使用三元表达式替换指定索引的元素
		for (int index : needToEnCoder) {
			data[index] = index >= 0 && index < data.length ? piiString : data[index];
		}
		for (String value : data) {
			System.out.println(value);
		}
	}

	public static String GUIDenCoder(String firstName, String lastName, Sex sex, Birthday birthday, String nationalId)
			throws URISyntaxException, IOException {
		PII pii = new PII.Builder(new Name(firstName, lastName), sex, birthday, new TWNationalId(nationalId)).build();
		GuidClient gc = new GuidClient(new URI(GUID_SERVER_URL), GUID_SERVER_USERNAME, GUID_SERVER_PASSWORD,
				GUID_SERVER_PREFIX);
		return gc.create(pii);
	}

}
