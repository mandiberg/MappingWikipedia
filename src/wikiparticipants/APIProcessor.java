/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wikiparticipants;

import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 *
 * @author Danara
 */

public class APIProcessor {
    private final String APIurl;
    
        public APIProcessor( String APIurl){
            this.APIurl = APIurl;
	}
        public String getData(){
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
                     conn.disconnect();
            }
            catch(Exception e)
            {       
                    System.out.println("Parsing JSON from the following URL has failed: "+APIurl);
                    e.printStackTrace();
            }
        return inline;
            
        }
        
}
