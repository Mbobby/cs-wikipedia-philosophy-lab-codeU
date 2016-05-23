package com.flatironschool.javacs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
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
	public static void main(String[] args) throws IOException 
	{
		
        // some example code to get you started

		HashSet<String> urls = new HashSet<String>();



		String url = "https://en.wikipedia.org/wiki/Java_(programming_language)".toLowerCase();
		Elements paragraphs;

		while(!url.equals("https://en.wikipedia.org/wiki/philosophy"))
		{
			System.out.println(url);
			urls.add(url);
			paragraphs = wf.fetchWikipedia(url);
			url = parseUrl(paragraphs, urls);
		}

		//Print final Url
		System.out.println(url);

	

		// Elements paragraphs = wf.fetchWikipedia(url);

		// Element firstPara = paragraphs.get(0);
		
		// Iterable<Node> iter = new WikiNodeIterable(firstPara);
		
		// int brackets = 0;
		// for (Node node: iter) 
		// {
		// 	if(node instanceof Element)
		// 	{
		// 		Element ele = (Element)node;

		// 		//Check 
		// 		if(ele.tag().toString().equals("a") && brackets == 0 && !checkParents(ele) && !inBrackets(String.valueOf(node)))
		// 		{
		// 			String elementUrl = ele.attr("abs:href");
		// 			System.out.print("-("+ elementUrl + ")-");
		// 		}
		// 	}
			
		// 	if (node instanceof TextNode) 
		// 	{
		// 		// node = (TextNode)node;
		// 		String text = String.valueOf(node);
				
		// 		//Keep track of brackets
		// 		for(int i =0; i < text.length(); i++)
		// 		{
		// 			if(text.charAt(i) == '(')
		// 				brackets +=1;
		// 			else if(text.charAt(i) == ')')
		// 				brackets -= 1;
		// 			System.out.print(text.charAt(i));
		// 		}
		// 		System.out.println();
		// 	}
        // }

        // the following throws an exception so the test fails
        // until you update the code
        // String msg = "Complete this lab by adding your code and removing this statement.";
        // throw new UnsupportedOperationException(msg);
	}

	public static String parseUrl(Elements paragraphs, HashSet<String> urls)
	{
		for(int j = 0; j < paragraphs.size(); j++)
		{
			Element paragraph = paragraphs.get(j);
			
			Iterable<Node> iter = new WikiNodeIterable(paragraph);
			
			int brackets = 0;

			for (Node node: iter) 
			{
				if(node instanceof Element)
				{
					Element ele = (Element)node;

					//Check that it is a link, it is not in a bracket and it is not in an i or em tag
					if(ele.tag().toString().equals("a") && brackets == 0 && !checkParents(ele) && !inBrackets(String.valueOf(node)))
					{
						String elementUrl = ele.attr("abs:href").toLowerCase();
						if(!urls.contains(elementUrl.toLowerCase()))
						{
							return elementUrl;
						}
					}
				}
				
				if (node instanceof TextNode) 
				{
					// node = (TextNode)node;
					String text = String.valueOf(node);
					
					//Keep track of brackets
					for(int i =0; i < text.length(); i++)
					{
						if(text.charAt(i) == '(')
							brackets +=1;
						else if(text.charAt(i) == ')')
							brackets -= 1;
						// System.out.print(text.charAt(i));
					}
					// System.out.println();
				}
			}
		}
		return "Failed";
	}

	//Returns true if it has <it> or <em> tags in parent; 
	//Returns false if it does not
	public static boolean checkParents(Element element)
	{
		while(!element.tag().toString().equals("body"))
		{
			if(element.tag().toString().equals("i") || element.tag().toString().equals("em"))
				return true;
			element = element.parent();
		}
		return false;
	}

	public static boolean inBrackets(String value)
	{
		if(value.charAt(0) == '(' || value.charAt(value.length()-1) == ')')
			return true;
		return false;
	}
}
