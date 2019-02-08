/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wikipediansbynumberofedits_en;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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

// d3.js WANTS THE DATA TO BE AS FOLLOWING --> STATE/PROVINCE,NUMBER OF EDITS

public class DataForD3 {

    private Map <String, Integer> data;
    private Map<String, String> fipsdata;
    Map <String, Integer> finalData = new HashMap<String, Integer>();
    Map<String, Integer> collectiveApiOutcome = new HashMap<String, Integer>();
    Map<String, Integer> collectiveApiOutcomeFIPS = new HashMap<String, Integer>();
    List<String> ipUsersText = new ArrayList<String>();    
    User[] users;
    
    public DataForD3(User[] users){ 
        this.users = users;
    }
    private void getUserTextArray(){
        for (User user : users) {
              ipUsersText.add(user.getText());
 
        }
    }

    public void output() throws IOException{
        List<String> URLlist = new LinkedList<String>(); 
                        try{
                        getUserTextArray();

                        String[] ipUsersArray = ipUsersText.toArray(new String[0]);
                        String userChunks[][] = splitArray(ipUsersArray,32);
                        System.err.println(userChunks[0][0]);
                        for (String[] usersChunk : userChunks) {
                            String joinedChunk = String.join(",", usersChunk);
                            String chunkURL = "http://api.db-ip.com/v2/youAPIkey/"+joinedChunk;
                            URLlist.add(chunkURL);
                            try {
                                String filename = "URL-list.txt";
                                FileWriter fw = new FileWriter(filename, true); //the true will append the new data
                                fw.write(chunkURL+"\n");//appends the string to the file
                                fw.close();
                            } catch (IOException ioe) {
                                System.err.println("IOException: " + ioe.getMessage());
                            }
                            System.err.println(chunkURL);
                            System.err.println("chunk end-------------------------------------------------------");
                        }
                        Map <String,Integer> temporaryData = new HashMap<String,Integer>();
                        String line = null;

                        try {
                            // FileReader reads text files in the default encoding.
                            FileReader fileReader = 
                                new FileReader("URL-list.txt");

                            // Always wrap FileReader in BufferedReader.
                            BufferedReader bufferedReader = 
                                new BufferedReader(fileReader);
                            int lineNo = 0;
                            while((line = bufferedReader.readLine()) != null&&lineNo<33000) {
                                
                                APIJSONParser apiReq = new APIJSONParser(line);
                                temporaryData = apiReq.getAPIandParseJSON();
                                for (Map.Entry<String, Integer> entry : temporaryData.entrySet()){
                                    int prev = 0;
                                    if (collectiveApiOutcome.get(entry.getKey()) != null){
                                        prev = collectiveApiOutcome.get(entry.getKey());
                                    }
                                    collectiveApiOutcome.put(entry.getKey(),prev+entry.getValue());
                                } 
                                    System.out.println(line);
                                    lineNo++;
                            }   

                            // Always close files.
                            bufferedReader.close();         
                        }
                        catch(FileNotFoundException ex) {
                            System.out.println(
                                "Unable to open file '" + 
                                "URL-list.txt" + "'");                
                        }
                        catch(IOException ex) {
                            System.out.println(
                                "Error reading file '" 
                                + "URL-list.txt" + "'");                  
                            // Or we could just do this: 
                            // ex.printStackTrace();
                        }
                        
//    			System.err.println(URLlist.get(i));

    //                        collectiveApiOutcome.putAll(temporaryData);       
                       
//                        System.err.println(collectiveApiOutcome);

                        CSVReader FIPSCodes = new CSVReader();
                        fipsdata = FIPSCodes.parse();
                       
                        collectiveApiOutcomeFIPS = getFIPSCode();
                        System.err.println(collectiveApiOutcomeFIPS);
                        for (Map.Entry<String, Integer> entry : collectiveApiOutcomeFIPS.entrySet()){
                            System.out.println(entry.getKey()+","+entry.getValue());
                        }
                        }catch (Exception e){
                            System.err.println("lalalalala"+e);
                        }
        
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
