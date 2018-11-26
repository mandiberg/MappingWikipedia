/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wikipediansbynumberofedits_en;

/**
 *
 * @author wiki
 */
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
 
abstract class WikipediansPrinter {
 
	private PrintWriter writer = null;
        
	public PrintWriter getWriter() {
		return writer;
	}
 
	public void setWriter(PrintWriter writer) {
		this.writer = writer;
	}
 
	private Date beginTimestamp = null;
	private Date endTimestamp = null;
 
	public Date getBeginTimestamp() {
		return beginTimestamp;
	}
 
	public void setBeginTimestamp(Date beginTimestamp) {
		this.beginTimestamp = beginTimestamp;
	}
 
	public Date getEndTimestamp() {
		return endTimestamp;
	}
 
	public void setEndTimestamp(Date endTimestamp) {
		this.endTimestamp = endTimestamp;
	}
 
	private int totalEdits = 0;
 
	public void setTotalEdits(int totalEdits) {
		this.totalEdits = totalEdits;
	}
 
	public int getTotalEdits() {
		return totalEdits;
	}
 
	private int totalEditsInPeriod = 0;
 
	public void setTotalEditsInPeriod(int totalEditsInPeriod) {
		this.totalEditsInPeriod = totalEditsInPeriod;
	}
 
	protected int getTargetTotalEdits() {
		return totalEditsInPeriod;
	}
        public static String[][] splitArray(String[] arrayToSplit, int chunkSize){
    if(chunkSize<=0){
        return null;  // just in case :)
    }
    // first we have to check if the array can be split in multiple 
    // arrays of equal 'chunk' size
    int rest = arrayToSplit.length % chunkSize;  // if rest>0 then our last array will have less elements than the others 
    // then we check in how many arrays we can split our input array
    int chunks = arrayToSplit.length / chunkSize + (rest > 0 ? 1 : 0); // we may have to add an additional array for the 'rest'
    // now we know how many arrays we need and create our result array
            String[][] arrays = new String[chunks][];
    // we create our resulting arrays by copying the corresponding 
    // part from the input array. If we have a rest (rest>0), then
    // the last array will have less elements than the others. This 
    // needs to be handled separately, so we iterate 1 times less.
    for(int i = 0; i < (rest > 0 ? chunks - 1 : chunks); i++){
        // this copies 'chunk' times 'chunkSize' elements into a new array
        arrays[i] = Arrays.copyOfRange(arrayToSplit, i * chunkSize, i * chunkSize + chunkSize);
    }
    if(rest > 0){ // only when we have a rest
        // we copy the remaining elements into the last chunk
        arrays[chunks - 1] = Arrays.copyOfRange(arrayToSplit, (chunks - 1) * chunkSize, (chunks - 1) * chunkSize + rest);
    }
    return arrays; // that's it
}
 
	public void print(User[] users, UserGroups userGroups, int limit) {
                try {
                    

//                    User[] ipUsers = {};
			printHeader();
			Arrays.sort(users, createComparator());
//			writer.print("{| class=\"wikitable" + getSortable() + "\"");
//			writer.println();
//			writer.print("! " + getTableHeader());
//			writer.println();
			int rank = 0;
			int prevCount = 0;
			int sameRank = 0;
			int totalEditsByListedUsers = 0;
			int numberOfListedEditors = 0;
			for (User user : users) {
                            
                            if(user.getEdits()==0){
                                    break;
                                }
//                            if(user.getId()>3){
//                                continue;
//                            }
     
				final String group = getGroup(user, userGroups.group(user.getId()));
				final String groupText = (group.equals("") ? "" : " (" + group + ")");
				final String rankText;
				if (!group.equals(UserGroups.BOT)) {
					if (rank == 0) {
						rank++;
						sameRank = 1;
					} else if (getTargetEdits(user) < prevCount) {
						rank += sameRank;
						sameRank = 1;
					} else {
						sameRank++;
					}
					rankText = Integer.toString(rank);
					numberOfListedEditors++;
					totalEditsByListedUsers += getTargetEdits(user);
					prevCount = getTargetEdits(user);
				} else {
					rankText = "";
				}
//				if (rank > limit) {
//					break;
//				}
//				writer.print("|-");
//				writer.println();
//				writer.print("| " + rankText);
//				writer.print(" || ");
				processAnonymous(user);
				if (user.getId() == 0) {
//					writer.print("[[" + getSpecialText() + ":Contributions/" + user.getText() + "|" + user.getText() + "]]");
				} else {
//					writer.print("[[" + getUserText() + ":" + user.getText() + "|" + user.getText() + "]]");
                                        if (user.getId() > 3) {//after it's put youcan go ahead and get the uniqueyboy
                                            try {
                                                String filename = "Outputs/usernames.txt";
                                                FileWriter fw = new FileWriter(filename, true); //the true will append the new data
                                                fw.write(user.getText() + ","+user.getEdits()+"\n");//appends the string to the file
                                                fw.close();
                                            } catch (IOException ioe) {
                                                System.err.println("IOException: " + ioe.getMessage());
                                            }
                                    
                                        }

                                        if (user.getId() <= 3) {//after it's put youcan go ahead and get the uniqueyboy
                                            try {
                                                String filename = "Outputs/ip-or-other.txt";
                                                FileWriter fw = new FileWriter(filename, true); //the true will append the new data
                                                fw.write(user.getText() + ","+user.getEdits()+"\n");//appends the string to the file
                                                fw.close();
//                                                APIJSONParser apiReq = new APIJSONParser("http://maps.googleapis.com/maps/api/geocode/json?address=chicago&sensor=false&#8221");
//                                                apiReq.getAPIandParseJSON();
                                      
                                            } catch (IOException ioe) {
                                                System.err.println("IOException: " + ioe.getMessage());
                                            }
                                        }
				}
//				writer.print(groupText);
				printEdits(user);
//				writer.println();
                                String the_user = user.getText();
//                                writer.print(the_user+"\n");
                                
 
			}

                        
//                        DataForD3 d3DataParser = new DataForD3(collectiveApiOutcome);
//                        d3DataParser.numberOfIpsPerLocation();                      
//			writer.print("|}");
//			writer.println();
			System.err.println("This list of " + limit + " editors represents " + totalEditsByListedUsers + " total edits,"
					+ " with an average of " + (int)(totalEditsByListedUsers / numberOfListedEditors) + " per editor.");
			System.err.println("This accounts for "
					+ new DecimalFormat("#0.0").format(((float)totalEditsByListedUsers / (float)getTargetTotalEdits()) * 100) + "%"
					+ " of the " + getTargetTotalEdits() + " total edits made to the Wikipedia.");
                        System.err.println("edits in period "
					+ getTargetTotalEdits());
		} finally {
//			writer.flush();
			System.err.flush();
		}
	}
 
	protected abstract int getTargetEdits(User user);
 
	protected abstract String getTableHeader();
 
	protected abstract String getSpecialText();
 
	protected abstract String getUserText();
 
	protected abstract Comparator<User> createComparator();
 
	protected void printHeader() {
		return;
	}
 
	protected abstract void printEdits(User user);
 
	protected void processAnonymous(User user) {
		return;
	}
 
	protected String getGroup(User user, String group) {
		return group;
	}
 
	protected final String SORTABLE = " sortable";
 
	protected String getSortable() {
		return "";
	}
 
}
