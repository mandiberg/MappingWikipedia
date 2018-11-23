/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wikipediansbynumberofedits_en;

/**
 *
 * @author wiki
 */
import java.util.HashMap;
import java.util.Map;
 
class Namespaces {
 
	public static final int MAIN_NAMESPACE = 0;
 
	private final Map<String, Integer> map = new HashMap<String, Integer>();
 
	public void add(String key, int ns) {
		map.put(key, ns);
	}
 
	public int ns(String text) {
		final String NAMESPACE_SEPARATOR = ":";
		if (!text.contains(NAMESPACE_SEPARATOR)) {
			return MAIN_NAMESPACE;
		}
		Integer ns = map.get(text.split(NAMESPACE_SEPARATOR)[0]);
		if (ns == null) {
			return MAIN_NAMESPACE;
		}
		return ns;
	}
 
}