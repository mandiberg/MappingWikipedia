/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wikiparticipants;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Danara
 */
public class JSONProcessor {
    private final String inline;
    private JSONObject batchJobj;
    
    public JSONProcessor(String inline){
        this.inline = inline;
    }
    public void getJSON(){
        JSONParser parse = new JSONParser();
        try {
            //Type caste the parsed json data in json object
            batchJobj = (JSONObject)parse.parse(inline);
        } catch (ParseException ex) {
            Logger.getLogger(JSONProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public List<List<String>> parseBatchJSON() {
        List<List<String>> ipInfoRows = new ArrayList<>();
        for(Iterator iterator = batchJobj.keySet().iterator(); iterator.hasNext();) {
            List <String> ipInfo = new ArrayList<>();
            String ip_id, ip_continentCode, ip_continentName, ip_countryCode, ip_countryName, ip_stateProv, ip_city;
            String key = (String) iterator.next();
            System.out.println(key);
            ip_id = key;
            JSONObject jobj = (JSONObject) batchJobj.get(key);
//          System.err.println(jobj2.get("stateProv"));
            ip_continentCode = (String) jobj.get("continentCode");
            ip_continentName = (String) jobj.get("continentName");
            ip_countryCode = (String) jobj.get("countryCode");
            ip_countryName = (String) jobj.get("countryName");
            ip_stateProv = (String) jobj.get("stateProv");
            ip_city = (String) jobj.get("city"); 
            ipInfo.add(ip_id);
            ipInfo.add(ip_continentCode);
            ipInfo.add(ip_continentName);
            ipInfo.add(ip_countryCode);
            ipInfo.add(ip_countryName);
            ipInfo.add(ip_stateProv);
            ipInfo.add(ip_city);
            
            ipInfoRows.add(ipInfo);

//          System.err.println(jobj2.get("countryCode"));
    }
    return ipInfoRows;
}
}
