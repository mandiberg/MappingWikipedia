/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wikiparticipants;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Danara
 */
public class SQLProcessor {
    
    //uncomment when testing the code, so you don't run out of api trial limit in one go
    private int limit = 32;
    public int page;
        public int offset;


    /**
     *
     * @throws SQLException
     */
    public SQLProcessor(int offset) throws SQLException {
        this.page = page;
        this.offset = page*limit;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("try makin classname^");
            
        } catch (java.lang.ClassNotFoundException e) {
            System.err.print("ClassNotFoundException: ");
            System.err.println(e.getMessage());
        }

        String jdbcUrl = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName + "?useUnicode=true&characterEncoding=utf8&useLegacyDatetimeCode=false&serverTimezone=UTC";
        System.out.println(dbName);
        System.out.println(dbPass);
        con = DriverManager.getConnection(jdbcUrl, dbUser, dbPass);
        System.out.println(con);
        stmt = con.createStatement();
    }
    public void getUniqueIPs(){
        String query = "select DISTINCT rev_user_text from revision where rev_user = 0 and rev_user_text REGEXP '^[0-9]{1,3}\\\\.[0-9]{1,3}\\\\.[0-9]{1,3}\\\\.[0-9]{1,3}$' ORDER BY rev_id LIMIT 32 OFFSET"+offset+ ";";
            try {
                ResultSet rs = stmt.executeQuery(query);
//                isInProggres = false;
                int batchCounter = 0;
                int totalCounter = 0;

                while (rs.next()) {
                    //group every 32 ips int one api request with url="http://api.db-ip.com/v2/free/ip1,ip2..." (rs.getString(DB_UNIQUE_IP))
                    String ip = rs.getString(DB_UNIQUE_IP);
                    System.out.println(ip);
                    if (batchCounter<32){
                        if (batchCounter == 0){
                            batchIps = ip;
                            batchCounter++;
                            totalCounter++;
                        }else{
                            batchIps += "," + ip;
                            batchCounter++;
                            totalCounter++;
                        }
                    }
                    else{
                        String finalURL = url + batchIps;
                        APIProcessor apiData= new APIProcessor(finalURL);
                                try {
                                    String JSONSinline = apiData.getData();
                                    JSONProcessor jsonDataProcessor = new JSONProcessor(JSONSinline);
                                    jsonDataProcessor.getJSON();
                                    List<List<String>> batchIpInfo = jsonDataProcessor.parseBatchJSON();
                                    appendIpInfo(batchIpInfo);
//                                    System.out.println(JSONS);
                                } catch (Exception ex) {
                                    System.err.print("Something went wrong while getting api data: ");
                                    System.err.println(ex.getMessage());       
                                }
                        batchCounter = 0;
                    }
                    if (totalCounter>limit){
                        break;
                    }
                }
            } catch (SQLException ex) {
                System.err.print("Something went wrong while getting ips from database: ");
                System.err.println(ex.getMessage());
            }
    }
    public void createTableForIpInfo(){
        String query = " CREATE  TABLE ipInfos (\n" +
                        "   ipkey INT NOT NULL AUTO_INCREMENT ," +
                        "   ip_id VARCHAR(255) NULL ,\n" +
                        "   ip_continentCode VARCHAR(255) NULL ,\n" +
                        "   ip_continentName VARCHAR(255) NULL ,\n" +
                        "   ip_countryCode VARCHAR(255) NULL ,\n" +
                        "   ip_countryName VARCHAR(255) NULL ,\n" +
                        "   ip_stateProv VARCHAR(255) NULL ,\n" +
                        "   ip_city VARCHAR(255) NULL ,\n" +
                        "   PRIMARY KEY (ipkey) )\n" +
                        " ENGINE = MyISAM\n" +
                        " DEFAULT CHARACTER SET = utf8mb4;";
        try {
            stmt.executeUpdate(query);
        } catch (SQLException ex) {
            Logger.getLogger(SQLProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Created table in given database...");
    }
    public void appendIpInfo(List<List<String>> ipInfos){
        ipInfos.stream().map((List<String> ipInfo) -> "INSERT INTO ipInfos (ip_id, ip_continentCode, ip_continentName, ip_countryCode, ip_countryName, ip_stateProv, ip_city) " +
                "VALUES ('"+ipInfo.get(0)+"', '"+ipInfo.get(1)+"', '"+ipInfo.get(2)+"', '"+ipInfo.get(3)+"', '"+ipInfo.get(4)+"', '"+ipInfo.get(5)+"', '"+ipInfo.get(6)+"')").forEachOrdered((String query) -> {
                    try {
                        stmt.executeUpdate(query);
                    } catch (SQLException ex) {
                        Logger.getLogger(SQLProcessor.class.getName()).log(Level.SEVERE, null, ex);
                    }
        });
    }

    private static int DB_UNIQUE_IP = 1;
    private Connection con;
    private Statement stmt;
//    private boolean isInProggres = true;
    private String dbUser = "root";
    private String dbPass = "Shinizzle.89";
    private String dbHost = "localhost";
    private int dbPort = 3306;
    private String dbName = "testing";
//    private ArrayList<String> IPlist=new ArrayList<String>();//Creating arraylist    
    public String url = "http://api.db-ip.com/v2/free/";
    public String batchIps = "";
    
}

