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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
 
class UserGroups {
 
	public static final String SYSOP = "sysop";
	public static final String BOT = "bot";
	public static final String FILE_NAME_SUFFIX = "user_groups.sql.gz";
 
	private final Map<Integer, Integer> sysops = new HashMap<Integer, Integer>();
	private final Map<Integer, Integer> bots = new HashMap<Integer, Integer>();
 
	public void initialize(InputStream inputStream) throws IOException {
		BufferedReader reader = null;
		reader = new BufferedReader(new InputStreamReader(new GZIPInputStream(inputStream)));
		final Pattern lineStartPattern = Pattern.compile("^INSERT INTO `user_groups` VALUES \\(");
		while(true){
			String line = reader.readLine();
			if (line == null) {
				break;
			}
			if (!lineStartPattern.matcher(line).find()){
				continue;
			}
			line = lineStartPattern.matcher(line).replaceFirst("");
			line = Pattern.compile("\\);$").matcher(line).replaceFirst("");
			final String[] userGroupStrings = line.split("\\),\\(");
			for (String userGroupString : userGroupStrings) {
				final StringTokenizer userGroupTokenizer = new StringTokenizer(userGroupString, ",");
				final int user = Integer.parseInt(userGroupTokenizer.nextToken());
				final String group = userGroupTokenizer.nextToken();
				if (group.equals("'" + SYSOP + "'")) {
					sysops.put(user, user);
				} else if (group.equals("'" + BOT + "'")) {
					bots.put(user, user);
				}
			}
		}
	}
 
	public String group(int user) {
		if (sysops.containsKey(user)) {
			return SYSOP;
		} else if (bots.containsKey(user)) {
			return BOT;
		} else {
			return "";
		}
	}
 
}
