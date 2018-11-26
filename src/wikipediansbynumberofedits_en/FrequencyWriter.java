/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wikipediansbynumberofedits_en;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Danara
 */
public class FrequencyWriter {
    Date date;
    String ip;
    public FrequencyWriter(Date date, String ip){
        this.date = date;
        this.ip = ip;
    }
    public void usingBufferedWriter() throws IOException
        {   
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH)+1;
            
            String fileName = String.valueOf(year)+"_"+String.valueOf(month);
//            System.err.println(ip);
            String fileContent = ip+"\n";
            
             // BufferedReader object for input.txt 
            BufferedWriter writer = new BufferedWriter(new FileWriter("Outputs/freq/"+fileName+".txt",true));

            BufferedReader reader = new BufferedReader(new FileReader("Outputs/freq/"+fileName+".txt")); 
            String line = reader.readLine(); 
 
            boolean exist = false;
            // loop for each line of the file
            while(line != null) 
            { 
                String lineCompare = line.replace("\n","");
                String fileContentCompare = fileContent.replace("\n","");

                if(lineCompare.equalsIgnoreCase(fileContentCompare)){
                    exist = true;
                }
                line = reader.readLine(); 
            } 
            // write only if not 
            // present in any lines of a file
            if (!exist){
                writer.write(fileContent); 
            }

                reader.close();
                writer.close();
        }

}
