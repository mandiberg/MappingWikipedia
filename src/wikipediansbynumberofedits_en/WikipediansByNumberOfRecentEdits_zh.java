///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package wikipediansbynumberofedits_en;
//
///**
// *
// * @author wiki
// */
//import java.util.Comparator;
// 
//public class WikipediansByNumberOfRecentEdits_zh extends WikipediansByNumberOfEdits {
//	/**
//	 * The main() method for this application.
//	 * @param args command-line arguments
//	 */
//	public static void main(String[] args) {
//		new WikipediansByNumberOfRecentEdits_zh().execute(args);
//	}
// 
//	protected String getWikiName() {
//		return "zhwiki";
//	}
// 
//	protected boolean getIpAddressesAreToBeCounted() {
//		return true;
//	}
// 
//	protected WikipediansPrinter[] createPrinters() {
//		final Printer printer = new Printer();
//		return new WikipediansPrinter[]{printer};
//	}
// 
//	private static class Printer extends WikipediansPrinter {
// 
//		protected int getTargetEdits(User user) {
//			return user.getEditsInRecentDays();
//		}
// 
//		protected String getTableHeader() {
//			return "名次 !! 用户 !! 最近编辑次数 !! 累积编辑次数";
//		}
// 
//		protected String getSpecialText() {
//			return "Special";
//		}
// 
//		protected String getUserText() {
//			return "User";
//		}
// 
//		protected String getSortable() {
//			return SORTABLE;
//		}
// 
//		protected void printHeader() {
//			getWriter().print("期间: "
//					+ DATE_FORMAT.format(getBeginTimestamp())
//					+ " &mdash; "
//					+ DATE_FORMAT.format(getEndTimestamp())
//					+ " (UTC)");
//			getWriter().println();
//			getWriter().println();
//		}
// 
//		protected Comparator<User> createComparator() {
//			return new Comparator<User> {
//				public int compare(User user1, User user2) {
//					if (user1.getEditsInRecentDays() != user2.getEditsInRecentDays()) {
//						return user2.getEditsInRecentDays() - user1.getEditsInRecentDays();
//					} else {
//						return user2.getEdits() - user1.getEdits(); 
//					}
//				}
//			};
//		}
// 
//		protected void printEdits(User user) {
//			getWriter().print(" || " + user.getEditsInRecentDays());
//			getWriter().print(" || " + user.getEdits());
//		}
// 
//	}
// 
//}
