package wikipediansbynumberofedits_en;

/**
 *
 * @author Danara
 */

import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

class APIJSONParser
{
    private final String APIurl;
    Map<String,String> ipAndInfoMap=new HashMap<String,String>();  
    Map<String, Integer> freq = new HashMap<String, Integer>();

        public APIJSONParser( String APIurl){
            this.APIurl = APIurl;
	}


//        private void getValuesFromJSON(JSONObject jobj){
//            for(Iterator iterator = jobj.keySet().iterator(); iterator.hasNext();) {
//                    String state;
//                    String key = (String) iterator.next();
//                    System.out.println(jobj.get(key));
//                    JSONObject jobj2 = (JSONObject) jobj.get(key);
//                    System.err.println(jobj2.get("stateProv"));
//                    state = (String) jobj2.get("stateProv");
////                    countryCode = (String) jobj2.get("countryCode");
//                   
//                        ipAndInfoMap.put(key, state );
//                    
//                    
//                }
//        }
        private void getFreqFromJSON(JSONObject jobj){
            try {
                                String filename = "URL-jsons.json";
                                FileWriter fw = new FileWriter(filename, true); //the true will append the new data
                                fw.write(jobj+"\n");//appends the string to the file
                                fw.close();
                            } catch (IOException ioe) {
                                System.err.println("IOException: " + ioe.getMessage());
                            }
            for(Iterator iterator = jobj.keySet().iterator(); iterator.hasNext();) {
                    String state;
                    String countryCode;
                    String key = (String) iterator.next();
//                    System.out.println(jobj.get(key));
                    JSONObject jobj2 = (JSONObject) jobj.get(key);
//                    System.err.println(jobj2.get("stateProv"));
                    state = (String) jobj2.get("stateProv");
                    countryCode = (String) jobj2.get("countryCode");
//                    System.err.println(jobj2.get("countryCode"));

                    if (countryCode != null){
                    if (countryCode.equals("US")){
                    int prev = 0;
                    if (freq.get(state) != null){
			prev = freq.get(state);
                    }
                    freq.put(state, prev + 1);
                    }
                    }
            }
//            System.err.println(freq);
        }
	public Map<String, Integer> getAPIandParseJSON()
	{   
//            List<List<String>> ipAndInfoList = new ArrayList<List<String>>();
//  map.put(100,"Amit");  
//  map.put(101,"Vijay");  
//  map.put(102,"Rahul");  

            //inline will store the JSON data streamed in string format
            String inline = "";
            try
            {
                URL url;
                url = new URL(APIurl);
                //Parse URL into HttpURLConnection in order to open the connection in order to get the JSON data
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                //Set the request to GET or POST as per the requirements
                conn.setRequestMethod("GET");
                //Use the connect method to create the connection bridge
                conn.connect();
                //Get the response status of the Rest API
                int responsecode = conn.getResponseCode();
//                System.err.println("Response code is: " +responsecode);

                //Iterating condition to if response code is not 200 then throw a runtime exception
                //else continue the actual process of getting the JSON data
                if(responsecode != 200){
                     try {
                                String filename = "URL-failed.txt";
                                FileWriter fw = new FileWriter(filename, true); //the true will append the new data
                                fw.write(APIurl+"\n");//appends the string to the file
                                fw.close();
                            } catch (IOException ioe) {
                                System.err.println("IOException: " + ioe.getMessage());
                            }
                    System.err.println("This URL threw exception: "+APIurl);
                    throw new RuntimeException("HttpResponseCode: " +responsecode);
                }
                else
                {
                    //Scanner functionality will read the JSON data from the stream
                    Scanner sc = new Scanner(url.openStream());
                    while(sc.hasNext())
                    {
                            inline+=sc.nextLine();
                    }
//                    System.out.println("\nJSON Response in String format"); 
//                    System.out.println(inline);
                    //Close the stream when reading the data has been finished
                    sc.close();
                }

                //JSONParser reads the data from string object and break each data into key value pairs
                JSONParser parse = new JSONParser();
                //Type caste the parsed json data in json object
                JSONObject jobj = (JSONObject)parse.parse(inline);

                getFreqFromJSON(jobj);
                

//                        System.out.println(jobj);


                //Disconnect the HttpURLConnection stream
                conn.disconnect();
                

            }
            catch(Exception e)
            {       
                    System.out.println("Parsing JSON from the following URL has failed: "+APIurl);
                    e.printStackTrace();
            }
            return freq;
        }
}