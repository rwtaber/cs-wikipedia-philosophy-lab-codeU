package com.flatironschool.javacs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import org.jsoup.select.Elements;

public class WikiPhilosophy {

	final static WikiFetcher wf = new WikiFetcher();

	/**
	 * Tests a conjecture about Wikipedia and Philosophy.
	 *
	 * https://en.wikipedia.org/wiki/Wikipedia:Getting_to_Philosophy
	 *
	 * 1. Clicking on the first non-parenthesized, non-italicized link
     * 2. Ignoring external links, links to the current page, or red links
     * 3. Stopping when reaching "Philosophy", a page with no links or a page
     *    that does not exist, or when a loop occurs
	 *
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

    ArrayList route = new ArrayList();
		Set<String> visited = new HashSet<String>();
		String current_page = "https://en.wikipedia.org/wiki/Java_(programming_language)";

		while(true) {
			if current_page.contains("wiki/Philosophy") break;

			route.add(current_page);
			visited.add(current_page);

			for (Element link: getLinks(current_page)) {
				String url = link.attr("abs:href");
				String text = link.text();

				if validLink(current_page, link) {
					current_page = url;
				}
			}

<a href="/w/index.php?title=Parallel_scavenge_garbage_collector&amp;action=edit&amp;redlink=1"
class="new" title="Parallel scavenge garbage collector (page does not exist)">parallel scavenge garbage collector</a>

		}
	}

	private Elements getLinks(String url) {
		Elements paragraphs = wf.fetchWikipedia(url);
		Elements para_no_italics = paragraphs.select("i").remove();
		Elements para_no_parens = para_no_italics.toString().replaceAll("\\(.*\\)", "");
		Document cleaned_pages = Jsoup.parse(para_no_parens);
		return cleaned_pages.select("a[href]");
	}

	private boolean validLink(String current_page, String url) {
		internal = url.contains(wikipedia.org);
		current = url.contains(current_page);
		redlink = url.contains("redlink=1");
		uppercase = Character.isUpperCase(text.charAt(0));

		return internal && !current && !redlink && !uppercase;
	}
}
