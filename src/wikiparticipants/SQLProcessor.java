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
import org.xml.sax.SAXException;

/**
 *
 * @author Danara
 */
public class SQLProcessor {
    
    //uncomment when testing the code, so you don't run out of api trial limit in one go
//    private int limit = 32;
    public int offset;

    /**
     *
     * @throws SQLException
     */
    public SQLProcessor() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("try making classname^");
            
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
    public void selectAllRevisionsByIp(int offsetRevision){
                List<List<String>> nkRevs = new ArrayList<>();

            int range =  1000000;
            String query = "select * from revision where rev_id<"+range*(offsetRevision+1)+" and rev_id>"+range*(offsetRevision)+" and rev_user_text IN ('82.146.88.144', '175.45.176.140', '82.146.88.180', '175.45.178.12', '175.45.176.144', '175.45.176.135', '175.45.176.143', '175.45.177.147', '175.45.177.154', '82.146.88.156', '175.45.178.98', '175.45.178.102');";
            try{
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()){
            List<String> singleList = new ArrayList<>();

            System.out.print("rev_ID: ");
            System.out.println(rs.getString(1));
            singleList.add(rs.getString(1));
            System.out.print("rev_page: ");
            System.out.println(rs.getString(2));
            singleList.add(rs.getString(2));
            System.out.print("rev_text_id: ");
            System.out.println(rs.getString(3));
            singleList.add(rs.getString(3));
            System.out.print("rev_comment: ");
            System.out.println(rs.getString(4));
            singleList.add(rs.getString(4));
            System.out.print("rev_user: ");
            System.out.println(rs.getString(5));
            singleList.add(rs.getString(5));
            System.out.print("rev_user_text: ");
            System.out.println(rs.getString(6));
            singleList.add(rs.getString(6));
            System.out.print("rev_timestamp: ");
            System.out.println(rs.getString(7));
            singleList.add(rs.getString(7));
            System.out.print("rev_minor_edit: ");
            System.out.println(rs.getString(8));
            singleList.add(rs.getString(8));
            System.out.print("rev_deleted: ");
            System.out.println(rs.getString(9));
            singleList.add(rs.getString(9));
            System.out.print("rev_len: ");
            System.out.println(rs.getString(10));
            singleList.add(rs.getString(10));
            System.out.print("rev_parent_id: ");
            System.out.println(rs.getString(11));
            singleList.add(rs.getString(11));
            
            nkRevs.add(singleList);
            }
            updateNKList(nkRevs);

            }catch (SQLException ex) {
            System.err.print("Something went wrong while getting revision timestamps from database: ");
            System.err.println(ex.getMessage());
        }

    }
        public void updateNKList(List<List<String>> nkRevs){
            PreparedStatement prepQuery = null;

//        ipInfos.stream().map((List<String> ipInfo) -> "INSERT INTO ipInfos (ip_id, ip_continentCode, ip_continentName, ip_countryCode, ip_countryName, ip_stateProv, ip_city) " +
//                "VALUES ('"+ipInfo.get(0)+"', '"+ipInfo.get(1)+"', '"+ipInfo.get(2)+"', '"+ipInfo.get(3)+"', '"+ipInfo.get(4)+"', '"+ipInfo.get(5)+"', '"+ipInfo.get(6)+"')").forEachOrdered((String query) -> {
//                    try {
//                        stmt.executeUpdate(query);
//                    } catch (SQLException ex) {
//                        Logger.getLogger(SQLProcessor.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//        });
        String query = "INSERT INTO nk_revisions (rev_id, rev_page, rev_text_id, rev_comment, rev_user, rev_user_text, rev_timestamp, rev_minor_edit, rev_deleted, rev_len, rev_parent_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            prepQuery = con.prepareStatement(query);
            for (List<String> rev:nkRevs){
                prepQuery.setString(1, rev.get(0));
                prepQuery.setString(2, rev.get(1));
                prepQuery.setString(3, rev.get(2));
                prepQuery.setString(4, rev.get(3));
                prepQuery.setString(5, rev.get(4));
                prepQuery.setString(6, rev.get(5));
                prepQuery.setString(7, rev.get(6));
                prepQuery.setString(8, rev.get(7));
                prepQuery.setString(9, rev.get(8));
                prepQuery.setString(10, rev.get(9));
                prepQuery.setString(11, rev.get(10));

                prepQuery.executeUpdate();
//                con.commit();

            }

        } catch (SQLException ex) {
            Logger.getLogger(SQLProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    public void parseRevisions(int offsetRevision) throws SQLException{
        int range =  1000000;
        ArrayList<ArrayList<String>> listOLists = new ArrayList<>();
        String query = "select rev_id, rev_user, rev_user_text, rev_timestamp from revision where rev_id<"+range*(offsetRevision+1)+" and rev_id>"+range*(offsetRevision)+" and rev_user=0;";
        try {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()){
               System.out.print("revID: ");//this is rev_id which is primary key (it's not continuos)
               System.out.println(rs.getString(1));//this is rev_id which is primary key (it's not continuos)
               //System.out.println(rs.getString(2));//this is rev_user (which is always 0 in this case)
               System.out.println(rs.getString(3));//this is rev_user_text which is ip address
               System.out.println(rs.getString(4));//this is rev_timestamps and the format is yyyymmddhhmmss
               String timeYear = rs.getString(4).substring(0, 4);
               ArrayList<String> singleList = new ArrayList<>();

               singleList.add(rs.getString(3));
               singleList.add(timeYear);
               singleList.add(rs.getString(1));

               listOLists.add(singleList);
            }
            updateIpInfosTime(listOLists);
        }catch (SQLException ex) {
            System.err.print("Something went wrong while getting revision timestamps from database: ");
            System.err.println(ex.getMessage());
        }
    }
    public void updateIpInfosTime(ArrayList<ArrayList<String>> listOLists){
        String pk ="";
        for(List<String> ipLine : listOLists){ 
            if ("Conversion script".equals(ipLine.get(0))){
                continue;
            }
                            //String query = "update ipInfos set ip_"+ipLine.get(1)+"= 1 where ip_id = '"+ipLine.get(0)+"';";
            String query;
            String checkQ = "select * from ipInfos where ip_id='"+ipLine.get(0)+"' limit 1;";
            try{
                System.out.println("-----------------------------------------------------------------");
                System.out.println(ipLine.get(2));

                System.out.println(ipLine.get(1));
                System.out.println(ipLine.get(0));

                ResultSet rs = stmt.executeQuery(checkQ);
                boolean exists;
                exists = false;
                while(rs.next()){
                    exists = true;
                    pk = rs.getString(1);
                }
                if(exists){
                    query = "update ipInfos set ip_"+ipLine.get(1)+"=1 where ipkey="+pk+";";
                    System.out.println(query);
                    stmt.executeUpdate(query);
                    System.out.println("worked");
                }
               
            }catch (SQLException ex) {
                    System.err.print("Something went wrong while updating IpInfos timecolumns: ");
                    System.err.println(ex.getMessage());
            }
        }
    }
    public void getProxyIps() throws SQLException{
       String query = "select ip from proxy_ips;";
       List<String> proxyIps =  new ArrayList<>();
       try {
           ResultSet rs = stmt.executeQuery(query);
           int i = 1;
           while(rs.next()){   
               proxyIps.add(rs.getString(1));
               System.out.print("proxy ip: ("+i+") - ");
               System.out.println(rs.getString(1));
               i++;
           }           
       }catch(SQLException ex){
           System.err.println("something went wrong while getting proxy ips: "+ex);
       }
       markProxyIps(proxyIps);
    }
    public void markProxyIps(List<String> proxyIps) throws SQLException{
        for (String proxyIp: proxyIps){
            String query = "UPDATE ipInfos SET ip_proxy = 1 WHERE ip_id = '"+proxyIp+"';";
                            System.out.println(query);

            try {
                stmt.executeUpdate(query);
                System.out.println("executed mark proxy update");

            }catch(SQLException ex){
                System.err.println("something went wrong while marking proxy ip: "+ex);
            }
        }
    }
    public void getUniqueIPs(int page) throws SAXException{
        offset  = page*256;
        String query = "select ip_address from ips LIMIT 256 OFFSET '"+offset+ "';";
//        String query = "select DISTINCT rev_user_text from revision where rev_user = 0 LIMIT 256 OFFSET "+offset+ ";";

//        String query = "select DISTINCT rev_user_text from revision where rev_user = 0 and rev_user_text REGEXP '((^\\s*((([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]))\\s*$)|(^\\s*((([0-9A-Fa-f]{1,4}:){7}([0-9A-Fa-f]{1,4}|:))|(([0-9A-Fa-f]{1,4}:){6}(:[0-9A-Fa-f]{1,4}|((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){5}(((:[0-9A-Fa-f]{1,4}){1,2})|:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){4}(((:[0-9A-Fa-f]{1,4}){1,3})|((:[0-9A-Fa-f]{1,4})?:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){3}(((:[0-9A-Fa-f]{1,4}){1,4})|((:[0-9A-Fa-f]{1,4}){0,2}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){2}(((:[0-9A-Fa-f]{1,4}){1,5})|((:[0-9A-Fa-f]{1,4}){0,3}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){1}(((:[0-9A-Fa-f]{1,4}){1,6})|((:[0-9A-Fa-f]{1,4}){0,4}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(:(((:[0-9A-Fa-f]{1,4}){1,7})|((:[0-9A-Fa-f]{1,4}){0,5}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:)))(%.+)?\\s*$))' LIMIT 32 OFFSET "+offset+ ";";
        try {
            ResultSet rs = stmt.executeQuery(query);
            System.out.println("bakalim");
//                isInProggres = false;
            int batchCounter = 0;
            int totalCounter = 0;
            batchIps = "";
            while (rs.next()) {
                //group every 32 ips int one api request with url="http://api.db-ip.com/v2/free/ip1,ip2..." (rs.getString(DB_UNIQUE_IP))
                String ip = rs.getString(1);
                System.out.println(ip);

                if (batchCounter == 0){
                    batchIps = ip;
                    batchCounter++;
                }else{
                    batchIps += "," + ip;
                    batchCounter++;
                }
                totalCounter = rs.getRow();               
            }

            System.out.println(totalCounter);

            String finalURL = url + batchIps;
            System.out.println(finalURL);
            APIProcessor apiData= new APIProcessor(finalURL);
            try {
                String JSONSinline = apiData.getData();
                System.out.println(JSONSinline);
                JSONProcessor jsonDataProcessor = new JSONProcessor(JSONSinline);
                jsonDataProcessor.getJSON();
                List<List<String>> batchIpInfo = jsonDataProcessor.parseBatchJSON();
                appendIpInfo(batchIpInfo);
//                                    System.out.println(JSONS);
            } catch (Exception ex) {
                System.err.print("Something went wrong while getting api data: ");
                System.err.println(ex.getMessage());       
            }
            if(totalCounter<32){
                throw new SAXException("Reached the end");
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
                        "   ip_phonePrefix VARCHAR(255) NULL ,\n" +
                        "   ip_zipCode VARCHAR(255) NULL ,\n" +
                        "   ip_languages VARCHAR(255) NULL ,\n" +
                        "   ip_city VARCHAR(255) NULL ,\n" +
                        "   ip_geonameId VARCHAR(255) NULL ,\n" +
                        "   ip_latitude VARCHAR(255) NULL ,\n" +
                        "   ip_timeZone VARCHAR(255) NULL ,\n" +
                        "   ip_stateProv VARCHAR(255) NULL ,\n" +
                        "   ip_currencyName VARCHAR(255) NULL ,\n" +
                        "   ip_countryCode VARCHAR(255) NULL ,\n" +
                        "   ip_gmtOffset VARCHAR(255) NULL ,\n" +
                        "   io_district VARCHAR(255) NULL ,\n" +
                        "   ip_isEuMember VARCHAR(255) NULL ,\n" +
                        "   ip_countryName VARCHAR(255) NULL ,\n" +
                        "   ip_continentName VARCHAR(255) NULL ,\n" +
                        "   ip_currencyCode VARCHAR(255) NULL ,\n" +
                        "   ip_continentCode VARCHAR(255) NULL ,\n" +
                        "   ip_longitude VARCHAR(255) NULL ,\n" +

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
            PreparedStatement prepQuery = null;

//        ipInfos.stream().map((List<String> ipInfo) -> "INSERT INTO ipInfos (ip_id, ip_continentCode, ip_continentName, ip_countryCode, ip_countryName, ip_stateProv, ip_city) " +
//                "VALUES ('"+ipInfo.get(0)+"', '"+ipInfo.get(1)+"', '"+ipInfo.get(2)+"', '"+ipInfo.get(3)+"', '"+ipInfo.get(4)+"', '"+ipInfo.get(5)+"', '"+ipInfo.get(6)+"')").forEachOrdered((String query) -> {
//                    try {
//                        stmt.executeUpdate(query);
//                    } catch (SQLException ex) {
//                        Logger.getLogger(SQLProcessor.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//        });
        String query = "INSERT INTO ipInfos (ip_id, ip_phonePrefix, ip_zipCode, ip_languages, ip_city, ip_geonameId, ip_latitude, ip_timeZone, ip_stateProv, ip_currencyName, ip_countryCode, ip_gmtOffset, io_district, ip_isEuMember, ip_countryName, ip_continentName, ip_currencyCode, ip_continentCode, ip_longitude) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            prepQuery = con.prepareStatement(query);
            for (List<String> ipInfo:ipInfos){
                prepQuery.setString(1, ipInfo.get(0));
                prepQuery.setString(2, ipInfo.get(1));
                prepQuery.setString(3, ipInfo.get(2));
                prepQuery.setString(4, ipInfo.get(3));
                prepQuery.setString(5, ipInfo.get(4));
                prepQuery.setString(6, ipInfo.get(5));
                prepQuery.setString(7, ipInfo.get(6));
                prepQuery.setString(8, ipInfo.get(7));
                prepQuery.setString(9, ipInfo.get(8));
                prepQuery.setString(10, ipInfo.get(9));
                prepQuery.setString(11, ipInfo.get(10));
                prepQuery.setString(12, ipInfo.get(11));
                prepQuery.setString(13, ipInfo.get(12));
                prepQuery.setString(14, ipInfo.get(13));
                prepQuery.setString(15, ipInfo.get(14));
                prepQuery.setString(16, ipInfo.get(15));
                prepQuery.setString(17, ipInfo.get(16));
                prepQuery.setString(18, ipInfo.get(17));
                prepQuery.setString(19, ipInfo.get(18));

                prepQuery.executeUpdate();
//                con.commit();

            }

        } catch (SQLException ex) {
            Logger.getLogger(SQLProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    private static int DB_REV_ID = 1;

    private static int DB_UNIQUE_IP = 2;
    private Connection con;
    private Statement stmt;
//    private boolean isInProggres = true;
    private String dbUser = "root";
    private String dbPass = "";
    private String dbHost = "127.0.0.1";
    private int dbPort = 3306;
    private String dbName = "revisions_wiki";
//    private ArrayList<String> IPlist=new ArrayList<String>();//Creating arraylist
    //ip database url
    public String url = "";
    public String batchIps = "";
    
}

