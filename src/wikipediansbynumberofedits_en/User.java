/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wikipediansbynumberofedits_en;

/**
 *
 * @author wiki
 */
class User {
 
	private int id = 0;
 
	private String text = null;
 
	private int edits = 0;
 
	private int editsInRecentDays = 0;
 
	private int editsMain = 0;
 
	private int editsMainInRecentDays = 0;
 
	public int getId() {
		return id;
	}
 
	public void setId(int id) {
		this.id = id;
	}
 
	public String getText() {
		return text;
	}
 
	public void setText(String text) {
		this.text = text;
	}
 
	public int getEdits() {
		return edits;
	}
 
	public int getEditsInRecentDays() {
		return editsInRecentDays;
	}
 
	public int getEditsMain() {
		return editsMain;
	}
 
	public int getEditsMainInRecentDays() {
		return editsMainInRecentDays;
	}
 
	public void incrementEdits(){
		edits++;
	}
 
	public void incrementEditsInRecentDays(){
		editsInRecentDays++;
	}
 
	public void incrementEditsMain(){
		editsMain++;
	}
 
	public void incrementEditsMainInRecentDays(){
		editsMainInRecentDays++;
	}
 
	public User(){
	}
 
	public User(int id, String text){
		this.id = id;
		this.text = text;
	}
 
	public boolean isIpAddress(){
		return id == 0;
	}
 
	public String toString() {
		return "id: " + id
			+ ", text: " + text
			+ ", edits: " + edits
			+ ", editsRecentDays: " + editsInRecentDays;
	}
 
}
