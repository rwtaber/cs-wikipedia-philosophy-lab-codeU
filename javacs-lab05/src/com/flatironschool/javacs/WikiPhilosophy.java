package com.flatironschool.javacs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import org.jsoup.select.Elements;

public class WikiPhilosophy {

	final static WikiFetcher wf = new WikiFetcher();
	final static String wiki = "https://en.wikipedia.org";

	/**
	 * Tests a conjecture about Wikipedia and Philosophy.
	 *
	 * https://en.wikipedia.org/wiki/Wikipedia:Getting_to_Philosophy
	 *
	 * 1. Clicking on the first non-parenthesized, non-itahttps://en.wikipedia.org/wiki/William_Shakespearelicized link
     * 2. Ignoring external links, links to the current page, or red links
     * 3. Stopping when reaching "Philosophy", a page with no links or a page
     *    that does not exist, or when a loop occurs
	 *
	 * @param args
	 * @throws IOException
	 */
	public static int main(String[] args) throws IOException {

		String current_page = "/wiki/Java_(programming_language)";
		String end_page = "/wiki/Philosophy";
		List<String> route = new ArrayList<String>();
	  Set<String> visited = new HashSet<String>();

		while(true) {
			route.add(current_page);
			visited.add(current_page);
			if (current_page.equals(end_page)) break;

			Elements paragraphs = wf.fetchWikipedia(wiki + current_page);
			paragraphs.select("i").remove();
			paragraphs.select("em").remove();
			boolean has_page = false;
			int paren_balance = 0;

			elementLoop:
			for(Element p : paragraphs) {
				Iterable<Node> iter = new WikiNodeIterable(p);
				for(Node node : iter) {
					if (node instanceof TextNode) {
						TextNode t = (TextNode) node;
						paren_balance += updateParenBalance(t.text());
					} else {
						Element e = (Element) node;
						if (e.tagName() == "a" && isValidPage(e, current_page, paren_balance)) {
							has_page = true;
							String this_page = e.attr("href").split("#")[0];
							if (visited.contains(this_page)) {
								System.out.println("This page has been seen, exiting...");
								return -1;
							}
							current_page = this_page;
							break elementLoop;
						}
					}
				}
			}
			if (!has_page) {
				System.out.println("Found page with no other pages, exiting...");
				return -1;
			}
		}

		for(int i = 1; i < route.size() + 1; i++) {
			System.out.println(i + ")\t" + wiki + route.get(i-1));
		}

		return 0;
	}

	private static int updateParenBalance(String text) {
		int ret = 0;
		ret += text.length() - text.replace(")", "").length();
		ret -= text.length() - text.replace("(", "").length();
		return ret;
	}

	private static boolean isValidPage(Element link, String current_page, int paren_balance) {
		String text = link.text();
		String url = link.absUrl("href");

		boolean internal = url.contains(wiki);
		boolean current = url.contains(current_page);
		boolean redlink = url.contains("redlink=1");
		boolean uppercase = Character.isUpperCase(text.charAt(0));

		return internal && !current && !redlink && !uppercase && paren_balance == 0;
	}
}
