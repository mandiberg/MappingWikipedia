/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wikipediansbynumberofedits_en;

/**
 *
 * @author wiki
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TimeZone;
import java.util.zip.GZIPInputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public abstract class WikipediansByNumberOfEdits {

    private static final String YEARMONTH_FORMAT_STRING = "yyyy-MM";
    private static final String DATE_FORMAT_STRING = YEARMONTH_FORMAT_STRING + "-dd";
    public static final DateFormat DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT_STRING);
    private static final String TIME_FORMAT_STRING = "HH:mm:ss";
    private final Date dateStarted = new Date();
    private static final String LIMIT_PROPERTY_KEY = "limit";
    private int limit = 0;
    private static int numberOfRevisionsToBeHandled = 28000;

    protected void execute(String[] args) {

        try {
            final int VALID_ARGUMENT_LENGTH = 2;
            if (args.length < VALID_ARGUMENT_LENGTH) {
                printUsage();
                System.exit(1);
            }
            System.err.println("Started. " + dateStarted);
            String limitText = System.getProperty(LIMIT_PROPERTY_KEY, "500000000");
             System.err.print(limitText + " this is limittext");
            limit = Integer.parseInt(limitText);
             System.err.print(limit + " this is limit***");

            limit = limit * 300000;
                         System.err.print(limit + " this is limit*****");

            limit = limit * -1;
            
            System.err.print(limit + " this is limit");
            
            final File dumpFile = new File("Resources/"+args[0]);
            fileNameCheck(dumpFile);
            final File userGroupsFile = new File("Resources/"+args[1]);
            fileNameCheck(userGroupsFile);
            final PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out, "UTF-8"));
            final UserGroups userGroups = new UserGroups();
            InputStream userGroupsInputStream = null;
            try {
                userGroups.initialize(new FileInputStream(userGroupsFile));
            } finally {
                if (userGroupsInputStream != null) {
                    try {
                        userGroupsInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            final DumpHandler dumpHandler = new DumpHandler();
            dumpHandler.setIpAddressesAreToBeCounted(true);
            InputStream dumpInputStream = null;
            try {
                dumpInputStream = new GZIPInputStream(new FileInputStream(dumpFile));
                SAXParserFactory.newInstance().newSAXParser().parse(dumpInputStream, dumpHandler);
            } catch(SAXException e){
                
                if(e.getMessage().equals("Limit reached.")){
                
            
            }else{
            if (e.getCause() instanceof ParseException) {
                System.err.println(e);
            } else {
                e.printStackTrace();
            }
            System.exit(1);
            }
                
                
        
                
            }finally {
                if (dumpInputStream != null) {
                    try {
                        dumpInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            final WikipediansPrinter[] printers = createPrinters();
            for (WikipediansPrinter printer : printers) {
                printer.setWriter(writer);
                printer.setBeginTimestamp(dumpHandler.getBeginTimestamp());
                printer.setEndTimestamp(dumpHandler.getEndTimestamp());
                printer.setTotalEdits(dumpHandler.getRevisionCounter());
                printer.setTotalEditsInPeriod(dumpHandler.getRevisionInPeriodCounter());
                printer.print(dumpHandler.getUsers(), userGroups, limit);
                if (!printer.equals(printers[printers.length - 1])) {
//                    writer.println();
                }
            }
        } catch (NumberFormatException e) {
            System.err.println("The specified system property \"" + LIMIT_PROPERTY_KEY + "\" is not a valid integer.");
            System.err.println(e);
            System.exit(1);
        } catch (FileNotFoundException e) {
            System.err.println(e);
            System.exit(1);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            System.exit(1);
//        } catch (SAXException e) {
//            
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        } finally {
            final Date dateEnded = new Date();
            System.err.println("Ended. " + dateEnded);
            final SimpleDateFormat dateFormat = new SimpleDateFormat(TIME_FORMAT_STRING);
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            System.err.println("Elapsed: " + dateFormat.format(new Date(dateEnded.getTime() - dateStarted.getTime())));
        }

    }

    private void printUsage() {
        System.err.print("Usage (example): java -Xmx500m -Dbegin.date=2008-04-01 -Dend.date=2008-04-30 -Dlimit=5000");
        System.err.print(" " + getClass().getName());
        System.err.print(" " + getWikiName() + "-20080501-stub-meta-history.xml.gz");
        System.err.print(" " + getWikiName() + "-20080501-" + UserGroups.FILE_NAME_SUFFIX);
        System.err.print(" > result.txt");
        System.err.println();
    }

    private void fileNameCheck(File file) {
        if (!file.getName().startsWith(getWikiName())) {
            System.err.println("WARNING: The specified file name '" + file.getName() + "' does not start with '" + getWikiName() + "'.");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
        }
    }

    protected abstract String getWikiName();

    protected abstract WikipediansPrinter[] createPrinters();

    protected boolean getIpAddressesAreToBeCounted() {
        return true;
    }

    private static class DumpHandler extends DefaultHandler {

        private final Namespaces namespaces = new Namespaces();
        private final Stack<String> elementStack = new Stack<String>();
        private Date beginTimestamp = null;
        private Date endTimestamp = null;

        public Date getBeginTimestamp() {
            return beginTimestamp;
        }

        public Date getEndTimestamp() {
            return endTimestamp;
        }
        private static final DateFormat TIMESTAMP_DUMP_FORMAT = new SimpleDateFormat(DATE_FORMAT_STRING + "'T'" + TIME_FORMAT_STRING + "'Z'z");
        private static final String BEGIN_DATE_PROPERTY_KEY = "begin.date";
        private static final String END_DATE_PROPERTY_KEY = "end.date";
        private boolean ipAddressesAreToBeCounted = false;

        public void setIpAddressesAreToBeCounted(boolean ipAddressesAreToBeCounted) {
            this.ipAddressesAreToBeCounted = ipAddressesAreToBeCounted;
        }
        private int editsInLastMonth = 0;
        private Calendar beginCalendar = Calendar.getInstance();
        private Set<String> usersEditedInLastMonth = new HashSet<String>();

        public void startDocument() throws SAXException {
            beginTimestamp = getDateProperty(BEGIN_DATE_PROPERTY_KEY);
            final Calendar endTimestampCalendar = Calendar.getInstance();
            endTimestampCalendar.setTime(getDateProperty(END_DATE_PROPERTY_KEY));
            endTimestampCalendar.add(Calendar.HOUR, 23);
            endTimestampCalendar.add(Calendar.MINUTE, 59);
            endTimestampCalendar.add(Calendar.SECOND, 59);
            endTimestamp = endTimestampCalendar.getTime();
            beginCalendar.setTime(beginTimestamp);
        }

        public void endDocument() throws SAXException {
            System.err.println("Processed: " + revisionCounter);
            System.err.println("As of the last month"
                    + " (" + new SimpleDateFormat(YEARMONTH_FORMAT_STRING).format(beginTimestamp) + "),"
                    + " the Wikipedia received "
                    + (int) (editsInLastMonth / beginCalendar.getActualMaximum(Calendar.DATE))
                    + " edits a day.");
            System.err.println("As of the last month"
        + " (" + new SimpleDateFormat(YEARMONTH_FORMAT_STRING).format(beginTimestamp) + "),"
        + " the Wikipedia received "
        + editsInLastMonth
        + " edits in last month.");
            System.err.println(usersEditedInLastMonth.size()
                    + " registered people (including bots) edited the Wikipedia in that month.");
//			System.err.println("Timestamp ParseException: " + timestampParseExceptionCount + " occured.");
//			System.err.println("User ID error: " + userIdErrorCount + " occured.");
            System.err.flush();
        }

        private static Date getDateProperty(String key) throws SAXException {
            String property = System.getProperty(key);
            try {
                return DATE_FORMAT.parse(property);
            } catch (ParseException e) {
                throw new SAXException(e);
            }
        }

        public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
            String name = localName.equals("") ? qName : localName;
            elementStack.push(name);
            if (name.equals("namespace")) {
                String key = "";
                try {
                    key = atts.getValue("key");
                    ns = Integer.parseInt(key);
                } catch (NumberFormatException e) {
                    throw new SAXException("ns: " + key, e);
                }
            }
        }
        private int revisionCounter = 0;

        int getRevisionCounter() {
            return revisionCounter;
        }
        private int revisionInPeriodCounter = 0;

        int getRevisionInPeriodCounter() {
            return revisionInPeriodCounter;
        }
        private int ns = 0;
        private String namespace = "";
        private String pageTitle = "";
        private int userId = 0;
        private String userIdString = "";
        private String userText = "";
        private Date timestamp = null;
        private String timestampString = "";
        private String revId = "";
        private String comment = "";
        private boolean ignoreRevision = false;
        private Map<String, User> map = new HashMap<String, User>();

        public User[] getUsers() {
            return map.values().toArray(new User[map.size()]);
        }
        private int timestampParseExceptionCount = 0;
        private int userIdErrorCount = 0;

        public void endElement(String uri, String localName, String qName) throws SAXException {
          
            final String name = elementStack.pop();
           
            if (name.equals("namespace")) {
                namespaces.add(namespace, ns);
                ns = 0;
                namespace = "";
            } else if (name.equals("page")) {
                pageTitle = "";
//                                System.err.println(namespaces + " this is namessss");
                return;
            } else if (name.equals("timestamp")) {
                ignoreRevision = false;
                try {
                    timestamp = TIMESTAMP_DUMP_FORMAT.parse(timestampString + "UTC");
                    timestampString = "";
                } catch (ParseException e) {
                    timestampParseExceptionCount++;
                    ignoreRevision = true;
                }
            } else if(name.equals("comment")){
//                System.err.println("foundcomment");
//                System.err.println(comment);
                comment = "";

                
            }
            else if(name.equals("id")){
//                System.err.println("found id");
//                System.err.println(revId);
                revId = "";

                
            }else if (name.equals("revision")) {
                if (!userIdString.equals("")) {
                    try {
                        userId = Integer.parseInt(userIdString);
                    } catch (NumberFormatException e) {
                        ignoreRevision = true;
                    }
                }
                if (ignoreRevision) {
                    return;
                }
                
                User user = null;
                if (ipAddressesAreToBeCounted || userId != 0) {
                    user = map.get(userText);
                    if (user == null) {
                        user = new User(userId, userText);
                        map.put(userText, user);
//                                                System.out.println("id " + userId + " text " + userText);
                        //userid > 3 is actual named users
                        //userid <3 is ip addresses and anons. sometimes weird ones that i don't know exactly what they are? like this user: Damian Yerrick
                        
                    }
                    if (user.getId() < userId) {
                        user.setId(userId);
                    }
                    if (user.getId() != userId) {
                        userIdErrorCount++;
                    }
///check it out heree for the timestamp
                    if (timestampBeroreOrEquals(timestamp)) {
                        user.incrementEdits();
                        if (timestampIsInPeriod(timestamp)) {
                            user.incrementEditsInRecentDays();
                            System.err.println(timestamp);
                        }
//                        else{
//                            user = null;
//                            return;
//                        }
                            if (namespaces.ns(pageTitle) == Namespaces.MAIN_NAMESPACE) {
                            user.incrementEditsMain();
                            if (timestampIsInPeriod(timestamp)) {
                                user.incrementEditsMainInRecentDays();
                            }
                        }
                    }
//                    if (userId > 3) {//after it's put youcan go ahead and get the uniqueyboy
//                            try {
//                                String filename = "6.txt";
//                                FileWriter fw = new FileWriter(filename, true); //the true will append the new data
//                                fw.write(userText + " | "+user.getEdits()+"\n");//appends the string to the file
//                                fw.close();
//                            } catch (IOException ioe) {
//                                System.err.println("IOException: " + ioe.getMessage());
//                            }
//                        }
//                        
//                        if (userId <= 3) {//after it's put youcan go ahead and get the uniqueyboy
//                            try {
//                                String filename = "ip-or-other2.txt";
//                                FileWriter fw = new FileWriter(filename, true); //the true will append the new data
//                                fw.write(userText + " | "+user.getEdits()+"\n");//appends the string to the file
//                                fw.close();
//                            } catch (IOException ioe) {
//                                System.err.println("IOException: " + ioe.getMessage());
//                            }
//                        }
                }
                                    
                final Calendar calendar = Calendar.getInstance();
                calendar.setTime(timestamp);
                if (calendar.get(Calendar.YEAR) == beginCalendar.get(Calendar.YEAR)
                        && calendar.get(Calendar.MONTH) == beginCalendar.get(Calendar.MONTH)) {
                    editsInLastMonth++;
                    if (user != null) {
                        usersEditedInLastMonth.add(user.getText());
                    }
                }
                if (timestampIsInPeriod(timestamp)) {
                    revisionInPeriodCounter++;
                }
                userId = 0;
                userIdString = "";
                userText = "";
                timestamp = null;
                revisionCounter++;
                final int LOG_INTERVAL = 10000;
                
                if (revisionCounter % LOG_INTERVAL == 0) {
                    
                    System.err.println("Processed ^__^: " + revisionCounter);
                }
                
                if (revisionCounter > numberOfRevisionsToBeHandled){
                    throw new SAXException("Limit reached.");
                }
            }
        }

        private boolean timestampIsInPeriod(Date timestamp) {
            return (timestamp.equals(beginTimestamp) || timestamp.after(beginTimestamp))
                    && timestampBeroreOrEquals(timestamp);
        }

        private boolean timestampBeroreOrEquals(Date timestamp) {
            return (timestamp.before(endTimestamp) || timestamp.equals(endTimestamp));
        }

        public void characters(char[] ch, int start, int length) {
            try {
                final String elementName = elementStack.peek();
                final String parentElementName = elementStack.elementAt(elementStack.size() - 2);
                final String string = new String(ch, start, length);
                if (elementName.equals("namespace")) {
                    namespace += string;
                }
                if (elementName.equals("title")) {
                    pageTitle += string;
                }
                if (elementName.equals("comment")) {
                    comment += string;
                }
                if (elementName.equals("id")) {
                    revId += string;
                }
                if (elementName.equals("timestamp")) {
                    timestampString += string;
//					if (revisionCounter % 10000 == 0) {
//						System.err.println(ch.length);
//					}
                } else if (parentElementName.equals("contributor")) {
                    if (elementName.equals("id")) {
                        userIdString += string;
                    } else if (elementName.equals("username")) {
                        userText += string;
                    } else if (userText.equals("") && elementName.equals("ip")) {
                        userId = 3;
                        userText += string;
                    }
                }
            } catch (EmptyStackException e) {
                // NOP
            } catch (IndexOutOfBoundsException e) {
                // NOP
            }
        }
    }
}
