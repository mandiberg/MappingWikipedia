/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wikipediansbynumberofedits_en;

import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.Int;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Danara
 */

// d3.js WANTS THE DATA TO BE AS FOLLOWING --> STATE/RPOVINCE,NUMBER OF EDITS

public class DataForD3 {

    private final Map <String, Integer> data;
    private final Map<String, String> fipsdata;
    Map <String, Integer> finalData = new HashMap<String, Integer>();


    
    public DataForD3(Map <String, Integer> data, Map <String, String> fipsdata){ 
        this.data = data;
        this.fipsdata = fipsdata;
    }

    public Map<String, Integer> getFIPSCode(){
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
