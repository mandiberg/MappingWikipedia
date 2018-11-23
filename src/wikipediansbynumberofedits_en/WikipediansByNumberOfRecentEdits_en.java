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
 
public class WikipediansByNumberOfRecentEdits_en extends WikipediansByNumberOfEdits {
 
	private static AnonymousUsers ANONYMOUS_USERS = null;
 
	/**
	 * The main() method for this application.
	 * @param args command-line arguments
	 */
	public static void main(String[] args) {
 
		ANONYMOUS_USERS = new AnonymousUsers();
		try {
			ANONYMOUS_USERS.initialize();
			new WikipediansByNumberOfRecentEdits_en().execute(args);
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
		return false;
	}
 
	protected WikipediansPrinter[] createPrinters() {
		final Printer printer = new Printer();
		printer.setAnonymousUsers(ANONYMOUS_USERS);
		return new WikipediansPrinter[]{printer};
	}
 
	private static class Printer extends WikipediansPrinter {
 
		private AnonymousUsers anonymousUsers = null;
 
		public void setAnonymousUsers(AnonymousUsers anonymousUsers) {
			this.anonymousUsers = anonymousUsers;
		}
 
		protected int getTargetEdits(User user) {
			return user.getEditsInRecentDays();
		}
 
		protected String getTableHeader() {
			return "Rank !! User !! Total Edits !! Recent Edits";
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
 
		protected void printHeader() {
			getWriter().print("Period: "
					+ DATE_FORMAT.format(getBeginTimestamp())
					+ " &mdash; "
					+ DATE_FORMAT.format(getEndTimestamp())
					+ " (UTC)");
			getWriter().println();
			getWriter().println();
		}
 
		protected void processAnonymous(User user) {
			if (anonymousUsers.contains(user.getText())) {
				user.setText("Place holder");
			}
		}
 
		protected Comparator<User> createComparator() {
			return new Comparator<User>() {
				public int compare(User user1, User user2) {
					if (user1.getEditsInRecentDays() != user2.getEditsInRecentDays()) {
						return user2.getEditsInRecentDays() - user1.getEditsInRecentDays();
					} else {
						return user2.getEdits() - user1.getEdits(); 
					}
				}
			};
		}
 
		protected void printEdits(User user) {
			getWriter().print(" || " + user.getEdits());
			getWriter().print(" || " + user.getEditsInRecentDays());
		}
	}
 
}
