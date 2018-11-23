/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wikipediansbynumberofedits_en;

/**
 *
 * @author wiki
 */
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Comparator;
 
public class WikipediansByNumberOfEdits_en extends WikipediansByNumberOfEdits {
 
	private static AnonymousUsers ANONYMOUS_USERS = null;
 
	private static UnflaggedBots UNFLAGGED_BOTS = null;
 
	/**
	 * The main() method for this application.
	 * @param args command-line arguments
	 */
	public static void main(String[] args) {
 
		ANONYMOUS_USERS = new AnonymousUsers();
		UNFLAGGED_BOTS = new UnflaggedBots();
		try {
			ANONYMOUS_USERS.initialize();
			UNFLAGGED_BOTS.initialize();
			new WikipediansByNumberOfEdits_en().execute(args);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
 
	}
 
	protected String getWikiName() {
		return "enwiki";
	}
 
	protected boolean getIpAddressesAreToBeCounted() {
		return true;
	}
 
	protected WikipediansPrinter[] createPrinters() {
		final Printer printer = new Printer();
		printer.setAnonymousUsers(ANONYMOUS_USERS);
		printer.setUnflaggedBots(UNFLAGGED_BOTS);
		return new WikipediansPrinter[]{printer};
	}
 
	private static class Printer extends WikipediansPrinter {
 
		private AnonymousUsers anonymousUsers = null;
 
		public void setAnonymousUsers(AnonymousUsers anonymousUsers) {
			this.anonymousUsers = anonymousUsers;
		}
 
		private UnflaggedBots unflaggedBots = null;
 
		public void setUnflaggedBots(UnflaggedBots unflaggedBots) {
			this.unflaggedBots = unflaggedBots;
		}
 
		protected int getTargetEdits(User user) {
			return user.getEdits();
		}
 
		protected int getTargetTotalEdits() {
			return getTotalEdits();
		}
 
		protected String getTableHeader() {
			return "Rank !! User !! Edits !! Edits in the past 30 days";
		}
 
		protected String getSpecialText() {
			return "Special";
		}
 
		protected String getUserText() {
			return "User";
		}
 
		protected String getSortable() {
			return SORTABLE;
		}
 
		protected void processAnonymous(User user) {
			if (anonymousUsers.contains(user.getText())) {
				user.setText("Place holder");
			}
		}
 
		protected String getGroup(User user, String group) {
			if (group.equals("") && unflaggedBots.contains(user.getText())) {
				return UserGroups.BOT;
			} else {
				return group;
			}
		}
 
		protected Comparator<User> createComparator() {
			return new Comparator<User>() {
				public int compare(User user1, User user2) {
					if (user1.getEdits() != user2.getEdits()) {
						return user2.getEdits() - user1.getEdits(); 
					} else {
						return user2.getEditsInRecentDays() - user1.getEditsInRecentDays();
					}
				}
			};
		}
 
		protected void printEdits(User user) {
//			getWriter().print(" || " + user.getEdits());
//			getWriter().print(" || " + user.getEditsInRecentDays());
		}
 
	}
 
}
