/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wikipediansbynumberofedits_en;

/**
 *
 * @author wiki
 */
import java.util.Comparator;
 
public class WikipediansByNumberOfRecentEdits_ja extends WikipediansByNumberOfEdits {
 
	/**
	 * The main() method for this application.
	 * @param args command-line arguments
	 */
	public static void main(String[] args) {
		new WikipediansByNumberOfRecentEdits_ja().execute(args);
	}
 
	protected String getWikiName() {
		return "jawiki";
	}
 
	protected WikipediansPrinter[] createPrinters() {
		return new WikipediansPrinter[]{new MainNamespacePrinter(), new AllNamespacePrinter()};
	}
 
	private static abstract class Printer extends WikipediansPrinter {
 
		protected String getTableHeader() {
			return "順位 !! 利用者 !! 編集回数 !! 総編集回数";
		}
 
		protected String getSpecialText() {
			return "特別";
		}
 
		protected String getUserText() {
			return "利用者";
		}
 
		protected String getSortable() {
			return SORTABLE;
		}
 
		protected void printHeader() {
			getWriter().print("== " + getSectionTitle() + " ==\n");
			getWriter().print("期間: "
					+ DATE_FORMAT.format(getBeginTimestamp())
					+ " &mdash; "
					+ DATE_FORMAT.format(getEndTimestamp())
					+ " (UTC)");
			getWriter().println();
			getWriter().println();
		}
 
		protected abstract String getSectionTitle();
 
	}
 
	private static class MainNamespacePrinter extends Printer {
 
		protected int getTargetEdits(User user) {
			return user.getEditsMainInRecentDays();
		}
 
		public String getSectionTitle() {
			return "記事名前空間";
		}
 
		protected void printEdits(User user) {
			getWriter().print(" || " + user.getEditsMainInRecentDays());
			getWriter().print(" || " + user.getEditsMain());
		}
 
		protected Comparator<User> createComparator() {
			return new Comparator<User>() {
				public int compare(User user1, User user2) {
					if (user1.getEditsMainInRecentDays() != user2.getEditsMainInRecentDays()) {
						return user2.getEditsMainInRecentDays() - user1.getEditsMainInRecentDays();
					} else {
						return user2.getEditsMain() - user1.getEditsMain(); 
					}
				}
			};
		}
 
	}
 
	private static class AllNamespacePrinter extends Printer {
 
		protected int getTargetEdits(User user) {
			return user.getEditsInRecentDays();
		}
 
		public String getSectionTitle() {
			return "全名前空間";
		}
 
		protected void printEdits(User user) {
			getWriter().print(" || " + user.getEditsInRecentDays());
			getWriter().print(" || " + user.getEdits());
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
 
	}
 
}
