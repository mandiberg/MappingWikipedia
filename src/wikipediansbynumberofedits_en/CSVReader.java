/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wikipediansbynumberofedits_en;

/**
 *
 * @author Danara
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CSVReader {
    Map<String, String> locationInfo = new HashMap<String, String> ();
    public Map<String, String> parse() {

        String csvFile = "Resources/statefips.csv";
        String line = "";
        String cvsSplitBy = ",";

        try {
            BufferedReader br;
            br = new BufferedReader(new FileReader(csvFile));

            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] info = line.split(cvsSplitBy);

//                System.err.println("State [code= " + info[0] + " , name=" + info[2] + "]");
                locationInfo.put(info[2], info[0]);
                
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return locationInfo;

    }

}
