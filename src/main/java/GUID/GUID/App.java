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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class App {
	static String GUID_SERVER_URL = "your guid_server url";
	static String GUID_SERVER_USERNAME = "your guid_server username";
	static String GUID_SERVER_PASSWORD = "your guid_server password";
	static String GUID_SERVER_PREFIX = "your guid_server prefix";
	
	static int First_Name_index = 6;
	static int Last_Name_index = 7;
	static int Gender_index = 3;
	static int Birthday_index = 2;
	static int Identifier_index = 1;

	static int[] needToEnCoder = new int[] { First_Name_index, Last_Name_index, Gender_index, Birthday_index,
			Identifier_index };

	static String csvFile = "test_patient.csv"; // 從命令行參數中獲取CSV文件的路徑和文件名稱
	static String csvSplitBy = ","; // CSV文件中的分隔符
	static String outputMappingCsvFile = "mapping.csv"; // 輸出的CSV文件名稱
	static String outputEnCoderCsvFile = "enCoder.csv"; // 輸出的CSV文件名稱

	public static void main(String[] args) throws URISyntaxException, IOException, ParseException {

		try (BufferedReader br = new BufferedReader(new FileReader(csvFile));
				BufferedWriter bwM = new BufferedWriter(new FileWriter(outputMappingCsvFile));
				BufferedWriter bwE = new BufferedWriter(new FileWriter(outputEnCoderCsvFile));) {
			
			ArrayList<List<String>> csvData = readCSV(br);
			ArrayList<List<String>> mappingData = mapping(csvData);
			ArrayList<List<String>> enCoderData = enCoder(mappingData);

			witeCSV(mappingData, bwM);
			witeCSV(enCoderData, bwE);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static ArrayList<List<String>> enCoder(ArrayList<List<String>> mappingData) {
		ArrayList<List<String>> result = new ArrayList<>();
		for (int i = 0; i < mappingData.size(); i++) {
			List<String> row = mappingData.get(i); // 獲取內層List<String>
			String GUID = row.get(0); // 獲取索引0的元素（假設第一個元素是GUID）
			row.remove(0);

			if (i != 0) {
				for (int needToEnCoder_index : needToEnCoder) {
					row.set(needToEnCoder_index, GUID);
				}
			}
			System.out.println("csv encoder : " + (i + 1) + "/" + mappingData.size() + " : " + row);
			result.add(row);
		}

		return result;

	}

	public static void witeCSV(ArrayList<List<String>> DataList, BufferedWriter csv) {
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
			System.out.println("CSV文件已成功寫入: " + outputMappingCsvFile);
		} catch (IOException e) {
			System.err.println("写入CSV文件發生錯誤: " + e.getMessage());
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
					String firstName = resultData.get(First_Name_index);
					String lastName = resultData.get(Last_Name_index);

					Sex sex = String.valueOf(resultData.get(Gender_index).charAt(0)) == "F" ? Sex.FEMALE : Sex.MALE;

					int[] birthday_Array = formatDate(resultData.get(Birthday_index));
					Birthday birthday = new Birthday(birthday_Array[0], birthday_Array[1], birthday_Array[2]);

					String nationalId = resultData.get(Identifier_index);

					// GUID enCoder
					GuidValue = GUIDenCoder(firstName, lastName, sex, birthday, nationalId);
				}

				resultData.add(0, GuidValue);

				System.out.println("GUIDenCoder : " + (i + 1) + "/" + csvData.size() + " : " + resultData);

				result.add(resultData);
			}
		} catch (Exception e) {
			// 处理可能的异常
			System.err.println("處理數據發生錯誤: " + e.getMessage());
		}

		return result;
	}

	public static int[] formatDate(String Odate) throws ParseException {

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

	public static ArrayList<List<String>> readCSV(BufferedReader br) throws IOException {
		ArrayList<List<String>> result = new ArrayList<>();

		String line = "";
		while ((line = br.readLine()) != null) {
			List<String> list = Arrays.asList(line.split(","));

			result.add(list);
		}

		return result;
	}

	public static String GUIDenCoder(String firstName, String lastName, Sex sex, Birthday birthday, String nationalId)
			throws URISyntaxException, IOException {
		PII pii = new PII.Builder(new Name(firstName, lastName), sex, birthday, new TWNationalId(nationalId)).build();
		GuidClient gc = new GuidClient(new URI(GUID_SERVER_URL), GUID_SERVER_USERNAME, GUID_SERVER_PASSWORD,
				GUID_SERVER_PREFIX);
		return gc.create(pii);
	}

}
