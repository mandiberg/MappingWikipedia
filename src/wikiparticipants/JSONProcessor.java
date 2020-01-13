/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wikiparticipants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;

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
            try{
            List <String> ipInfo = new ArrayList<>();
            String ip_id, ip_phonePrefix, ip_zipCode, ip_languages, ip_city, ip_geonameId, ip_latitude, ip_timeZone, ip_stateProv, ip_currencyName, ip_countryCode, ip_gmtOffset, io_district, ip_isEuMember, ip_countryName, ip_continentName, ip_currencyCode, ip_continentCode, ip_longitude;
            String key = (String) iterator.next();
//            System.out.println(key);
            ip_id = key;
            JSONObject jobj = (JSONObject) batchJobj.get(key);
//          System.err.println(jobj2.get("stateProv"));
//                ip_continentCode = (String) jobj.get("continentCode");
//                ip_continentName = (String) jobj.get("continentName");
//                ip_countryCode = (String) jobj.get("countryCode");
//                ip_countryName = (String) jobj.get("countryName");
//                ip_stateProv = (String) jobj.get("stateProv");
//                ip_city = (String) jobj.get("city"); 
            ip_phonePrefix = (String) jobj.get("phonePrefix");
//            ip_phonePrefix = ip_phonePrefix.trim().replaceAll(" +", " ");
            System.out.println("ip_phonePrefix: "+ ip_phonePrefix);

            ip_zipCode = (String) jobj.get("zipCode");
//            ip_zipCode = ip_zipCode.trim().replaceAll(" +", " ");
            System.out.println("ip_zipCode: "+ ip_zipCode);

            //ip_languages = (String) jobj.get("languages");
            JSONArray ip_languages_array = (JSONArray) jobj.get("languages");

            ip_languages = ip_languages_array.toString();
//            ip_languages = ip_languages.trim().replaceAll(" +", " ");
            System.out.println("ip_languages: "+ ip_languages);

            ip_city = (String) jobj.get("city");
//            ip_city = ip_city.trim().replaceAll(" +", " ");
            System.out.println("ip_city: "+ ip_city);

            Long ip_geonameId_int = (Long) jobj.get("geonameId");
            ip_geonameId =  String.valueOf(ip_geonameId_int);
//            ip_geonameId = ip_geonameId.trim().replaceAll(" +", " ");
            System.out.println("ip_geonameId: "+ ip_geonameId);

            Double latitude = (Double) jobj.get("latitude");
            ip_latitude = String.valueOf(latitude);
//            ip_latitude = ip_latitude.trim().replaceAll(" +", " ");
            System.out.println("ip_latitude: "+ ip_latitude);

            ip_timeZone = (String) jobj.get("timeZone");
//            ip_timeZone = ip_timeZone.trim().replaceAll(" +", " ");
            System.out.println("ip_timeZone: "+ ip_timeZone);

            ip_stateProv = (String) jobj.get("stateProv");
//            ip_stateProv = ip_stateProv.trim().replaceAll(" +", " ");
            System.out.println("ip_stateProv: "+ ip_stateProv);

            ip_currencyName = (String) jobj.get("currencyName");
//            ip_currencyName = ip_currencyName.trim().replaceAll(" +", " ");
            System.out.println("ip_currencyName: "+ ip_currencyName);

            
            ip_countryCode = (String) jobj.get("countryCode");
//            ip_countryCode = ip_countryCode.trim().replaceAll(" +", " ");
            System.out.println("ip_countryCode: "+ ip_countryCode);
            System.out.println(jobj.get("gmtOffset").getClass().getName());
            if ("java.lang.Long".equals(String.valueOf(jobj.get("gmtOffset").getClass().getName()))){
            Long ip_gmtOffset_long = (Long) jobj.get("gmtOffset");
            ip_gmtOffset = String.valueOf(ip_gmtOffset_long);}
            else{
            Double ip_gmtOffset_long = (Double) jobj.get("gmtOffset");
            ip_gmtOffset = String.valueOf(ip_gmtOffset_long);

            }
//            ip_gmtOffset = ip_gmtOffset.trim().replaceAll(" +", " ");
            System.out.println("ip_gmtOffset: "+ ip_gmtOffset);
            

            io_district = (String) jobj.get("district");
//            io_district = io_district.trim().replaceAll(" +", " ");
            System.out.println("io_district: "+ io_district);

            Boolean ip_isEuMember_boolean = (Boolean) jobj.get("isEuMember");
            ip_isEuMember = String.valueOf(ip_isEuMember_boolean);
//            ip_isEuMember = ip_isEuMember.trim().replaceAll(" +", " ");
            System.out.println("ip_isEuMember: "+ ip_isEuMember);

            ip_countryName = (String) jobj.get("countryName");
//            ip_countryName = ip_countryName.trim().replaceAll(" +", " ");
            System.out.println("ip_countryName: "+ ip_countryName);

            ip_continentName = (String) jobj.get("continentName");
//            ip_continentName = ip_continentName.trim().replaceAll(" +", " ");
            System.out.println("ip_continentName: "+ ip_continentName);

            ip_currencyCode = (String) jobj.get("currencyCode");
//            ip_currencyCode = ip_currencyCode.trim().replaceAll(" +", " ");
            System.out.println("ip_currencyCode: "+ ip_currencyCode);


            ip_continentCode = (String) jobj.get("continentCode");
            System.out.println("ip_continentCode: "+ ip_continentCode);

            Double longitude = (Double) jobj.get("longitude");
            ip_longitude = String.valueOf(longitude);
            System.out.println("ip_longitude: "+ ip_longitude);

            ipInfo.add(ip_id);
            ipInfo.add(ip_phonePrefix);
            ipInfo.add(ip_zipCode);
            ipInfo.add(ip_languages);
            ipInfo.add(ip_city);
            ipInfo.add(ip_geonameId);
            ipInfo.add(ip_latitude);
            ipInfo.add(ip_timeZone);
            ipInfo.add(ip_stateProv);
            ipInfo.add(ip_currencyName);
            ipInfo.add(ip_countryCode);
            ipInfo.add(ip_gmtOffset);
            ipInfo.add(io_district);
            ipInfo.add(ip_isEuMember);
            ipInfo.add(ip_countryName);
            ipInfo.add(ip_continentName);
            ipInfo.add(ip_currencyCode);
            ipInfo.add(ip_continentCode);
            ipInfo.add(ip_longitude);
            
            ipInfoRows.add(ipInfo); 
        }
        catch(Exception e){
                            System.out.println("failed to parse this portion of json: "+e);

                }
//            System.out.println("====================================================");
//          System.err.println(jobj2.get("countryCode"));
    }
    return ipInfoRows;
}
}
