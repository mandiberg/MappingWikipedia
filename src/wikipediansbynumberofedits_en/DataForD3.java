/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wikipediansbynumberofedits_en;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import static wikipediansbynumberofedits_en.WikipediansPrinter.splitArray;

/**
 *
 * @author Danara
 */

// d3.js WANTS THE DATA TO BE AS FOLLOWING --> STATE/RPOVINCE,NUMBER OF EDITS

public class DataForD3 {

    private Map <String, Integer> data;
    private Map<String, String> fipsdata;
    Map <String, Integer> finalData = new HashMap<String, Integer>();
    Map<String, Integer> collectiveApiOutcome = new HashMap<String, Integer>();
    Map<String, Integer> collectiveApiOutcomeFIPS = new HashMap<String, Integer>();
    List<String> ipUsers = new ArrayList<String>();    
    User[] users;
    
    public DataForD3(User[] users){ 
        this.users = users;
    }
    private void getIpUserList(){
        for (User user : users) {
           if(user.getId()<=3){
              ipUsers.add(user.getText());
 
           } 
        }
    }
    public void output(){
        List<String> URLlist = new LinkedList<String>(); 
//                        try{
                        getIpUserList();
                        String[] ipUsersArray = ipUsers.toArray(new String[0]); 
                        String userChunks[][] = splitArray(ipUsersArray,32);
                        for (String[] userss : userChunks) {
                            
                            String joinedChunk = String.join(",", userss);
                            String chunkURL = "http://api.db-ip.com/v2/free/"+joinedChunk;
                            URLlist.add(chunkURL);
                          System.err.println(chunkURL);
                          System.err.println("chunk end-------------------------------------------------------");
                        }
                        Map <String,Integer> temporaryData = new HashMap<String,Integer>();
                        for (int i = 0; i < 2; i++) {
//    			System.err.println(URLlist.get(i));
                            APIJSONParser apiReq = new APIJSONParser(URLlist.get(i));
                            temporaryData = apiReq.getAPIandParseJSON();
                            for (Map.Entry<String, Integer> entry : temporaryData.entrySet()){
                                int prev = 0;
                                if (collectiveApiOutcome.get(entry.getKey()) != null){
                                    prev = collectiveApiOutcome.get(entry.getKey());
                                }
                                collectiveApiOutcome.put(entry.getKey(),prev+entry.getValue());
                            } 
    //                        collectiveApiOutcome.putAll(temporaryData);       
                        }
//                        System.err.println(collectiveApiOutcome);

                        CSVReader FIPSCodes = new CSVReader();
                        fipsdata = FIPSCodes.parse();
                       
                        collectiveApiOutcomeFIPS = getFIPSCode();
                        System.err.println(collectiveApiOutcomeFIPS);
                        for (Map.Entry<String, Integer> entry : collectiveApiOutcomeFIPS.entrySet()){
                            System.out.println(entry.getKey()+","+entry.getValue());
                        }
//                        }catch (Exception e){
//                            System.err.println("lalalalala"+e);
//                        }
        
    }
    public Map<String, Integer> getFIPSCode(){
        data = collectiveApiOutcome;
        for (Map.Entry<String, Integer> output : data.entrySet()){
            for (Map.Entry<String, String> fips : fipsdata.entrySet()){
                if(output.getKey().equalsIgnoreCase(fips.getKey())){
                    finalData.put(fips.getValue(), output.getValue());
//                    System.err.println(fips.getValue()+","+ output.getValue());
                } 
            }
        }
    return finalData;
    }
    public void parseDataForD3(){
        
    }
}
