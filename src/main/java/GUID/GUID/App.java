package GUID.GUID;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

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

public class App {

public static void main(String[] args) throws URISyntaxException, IOException {
	String GUID_SERVER_URL = " your guid_server url";
	String GUID_SERVER_USERNAME = "your guid_server username";
	String GUID_SERVER_PASSWORD = "your guid_server password";
	String GUID_SERVER_PREFIX = "your guid_server prefix";

	PII pii = new PII.Builder(new Name("楊", "大寶"), Sex.MALE, new Birthday(2015, 1, 31), new TWNationalId("A123456789")).build();
	GuidClient gc = new GuidClient(new URI(GUID_SERVER_URL),GUID_SERVER_USERNAME,GUID_SERVER_PASSWORD,GUID_SERVER_PREFIX);
	System.out.println(gc.create(pii));
  }
}
