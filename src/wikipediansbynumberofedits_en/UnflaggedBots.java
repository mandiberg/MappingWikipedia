/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wikipediansbynumberofedits_en;

/**
 *
 * @author wiki
 */
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
 
public class UnflaggedBots {
 
	private Map<String, String> users = new HashMap<String, String>();
 
	private static final String INPUT_FILE_NAME = "Resources/unflagged-bots.txt";
 
	public void initialize() throws FileNotFoundException, IOException {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(INPUT_FILE_NAME));
			final Pattern pattern = Pattern.compile("^\\s*\\d*\\.?\\s*");
			while (true) {
				final String line = reader.readLine();
				if (line == null || line.length() == 0) {
					break;
				}
				final Matcher matcher = pattern.matcher(line);
				final String user = matcher.replaceFirst("");
				users.put(user, user);
			}
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
	}
 
	public boolean contains(String user) {
		return users.containsKey(user);
	}
 
	public String toString() {
		return users.toString();
	}
 
}
