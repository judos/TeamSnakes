package ch.judos.generic.data;

import java.io.*;
import java.util.Arrays;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @since 14.09.2011
 * @author Julian Schelker
 */

public class StringUtils {

	/**
	 * the new line string value
	 */
	public static String newline = System.getProperty("line.separator");

	/**
	 * the standard character to separate entries
	 */
	public static String linebreak = "\n";

	public static String returnChar = "\t";


	public static String repeat(String x, int times) {
		StringBuffer s = new StringBuffer();
		for (int i = 0; i < times; i++)
			s.append(x);
		return s.toString();
	}

	/**
	 * Link: http://rosettacode.org/wiki/Levenshtein_distance#Java
	 *
	 * @param string1
	 * @param string2
	 *
	 * @return the levenshtein distance between two strings measured in number
	 *         of characters removed/edited
	 */
	public static int getLevenshteinDistance(String string1, String string2) {
		String a = string1.toLowerCase();
		String b = string2.toLowerCase();
		// i == 0
		int[] costs = new int[b.length() + 1];
		for (int j = 0; j < costs.length; j++)
			costs[j] = j;
		for (int i = 1; i <= a.length(); i++) {
			// j == 0; nw = lev(i - 1, j)
			costs[0] = i;
			int nw = i - 1;
			for (int j = 1; j <= b.length(); j++) {
				int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]), a.charAt(i - 1) == b
						.charAt(j - 1) ? nw : nw + 1);
				nw = costs[j];
				costs[j] = cj;
			}
		}
		return costs[b.length()];
	}

	/**
	 * Link: http://rosettacode.org/wiki/Levenshtein_distance#Java<br>
	 * This is a recursive variant translated from C
	 *
	 * @param s
	 * @param t
	 * @return the levenshtein distance between two strings measured in number
	 *         of characters removed/edited
	 */
	public static int distanceRec(String s, String t) {
		/*
		 * if either string is empty, difference is inserting all chars from the
		 * other
		 */
		if (s.length() == 0)
			return t.length();
		if (t.length() == 0)
			return s.length();

		/*
		 * if first letters are the same, the difference is whatever is required
		 * to edit the rest of the strings
		 */
		if (s.charAt(0) == t.charAt(0))
			return distanceRec(s.substring(1), t.substring(1));

		/*
		 * else try: changing first letter of s to that of t, remove first
		 * letter of s, or remove first letter of t
		 */
		int a = distanceRec(s.substring(1), t.substring(1));
		int b = distanceRec(s, t.substring(1));
		int c = distanceRec(s.substring(1), t);

		if (a > b)
			a = b;
		if (a > c)
			a = c;

		// any of which is 1 edit plus editing the rest of the strings
		return a + 1;
	}

	/**
	 * @param text
	 * @return capitalizes the first char of text
	 */
	public static String capitalizeWord(String text) {
		return Character.toUpperCase(text.charAt(0)) + text.substring(1);
	}

	/**
	 * converts the string consisting of zeros and ones to a binary array<br>
	 * any other chars than zero and one are ignored
	 *
	 * @param bin
	 * @return the boolean array
	 */
	public static boolean[] binaryStringToBoolean(String bin) {
		int anz = 0;
		for (char c : bin.toCharArray())
			if (c == '0' || c == '1')
				anz++;
		boolean[] arr = new boolean[anz];
		Arrays.fill(arr, false);
		int index = 0;
		for (char c : bin.toCharArray()) {
			if (c == '1')
				arr[index] = true;
			index++;
		}
		return arr;
	}

	/**
	 * shortens the text, if charCount &gt; 12 it cuts of the end and adds "..."
	 * to match the given charCount<br>
	 * if charCount &lt;= 12 it cuts of the end and adds "." to match the given
	 * charCount.
	 *
	 * @param text
	 * @param charCount
	 *            the maximum of characters in the string
	 * @return the shortened text
	 */
	public static String shorten(String text, int charCount) {
		if (text.length() > charCount) {
			if (charCount > 12)
				return text.substring(0, charCount - 3) + "...";
			// else
			return text.substring(0, charCount - 1) + ".";
		}
		// else
		return text;
	}

	/**
	 * <b>Note:</b> the method remove of the iterator is empty
	 *
	 * @param string
	 * @return an iterable object that will return one character at a time
	 */
	public static Iterable<String> getCharIterableString(String string) {
		return new CharIterableString(string);
	}

	private static class CharIterableString implements Iterable<String> {

		private String string;

		public CharIterableString(String string) {
			this.string = string;
		}

		@Override
		public Iterator<String> iterator() {
			return new CharIterator(this.string);
		}

	}

	private static class CharIterator implements Iterator<String> {

		private StringBuffer s;

		public CharIterator(String s) {
			this.s = new StringBuffer();
			this.s.append(s);
		}

		@Override
		public boolean hasNext() {
			return this.s.length() > 0;
		}

		@Override
		public String next() {
			String result = this.s.substring(0, 1);
			this.s.deleteCharAt(0);
			return result;
		}

	}

	/**
	 * @param text
	 * @param findReplaces
	 *            an arbitrary number of arrays with 2 elements [0]=find
	 *            [1]=replace
	 * @return
	 */
	@SuppressWarnings("all")
	public static String replaceAll(String text, String[]... findReplaces) {
		for (String[] findReplace : findReplaces) {
			String find = Pattern.quote(findReplace[0]);
			String replace = Matcher.quoteReplacement(findReplace[1]);
			text = text.replaceAll(find, replace);
		}
		return text;
	}

	/**
	 * replaces all occurances of find[i] for any i by the replace string
	 *
	 * @param text
	 * @param find
	 * @param replace
	 * @return the new text
	 */
	@SuppressWarnings("all")
	public static String replaceAll(String text, String[] find, String replace) {
		for (String findX : find) {
			findX = Pattern.quote(findX);
			text = text.replaceAll(findX, Matcher.quoteReplacement(replace));
		}
		return text;
	}

	/**
	 * @param s
	 *            the text
	 * @param start
	 *            position in string, might be negative. A negative start
	 *            position means to start at the end.<br>
	 *            substr(s,-1) will return the last letter
	 * @param length
	 *            of output string. Might also be negative to get back all
	 *            characters except n.<br>
	 *            substr(s,0,-5) will return everything except the last 5
	 *            characters.
	 * @return the cut out string
	 */
	@SuppressWarnings("all")
	public static String substr(String s, int start, int length) {
		if (start < 0)
			start += s.length();
		s = s.substring(start);
		if (length < 0)
			length += s.length();
		if (length > s.length())
			length = s.length();
		return s.substring(0, length);
	}

	/**
	 * @param s
	 *            the text
	 * @param start
	 *            position in string, might be negative. A negative start
	 *            position means to start at the end.<br>
	 *            substr(s,-1) will return the last letter
	 * @return the cut out string
	 */
	public static String substr(String s, int start) {
		return substr(s, start, s.length());
	}

	/**
	 * returns the toString of the Object o extended with the toString of itself
	 * to match the given length
	 *
	 * @param obj
	 *            provides the String via the toString method
	 * @param length
	 * @return the repeated String
	 */
	public static String extendRepeated(Object obj, int length) {
		String s = obj.toString();
		StringBuffer b = new StringBuffer(s);
		int index = 0;
		while (b.length() < length) {
			b.append(s.charAt(index));
			index = (index + 1) % s.length();
		}
		return b.toString();
	}

	/**
	 * flips the string
	 *
	 * @param str
	 * @return the reversed string where char[i] = oldChars[n-i-1];
	 */
	public static String reverse(String str) {
		char[] inArr = str.toCharArray();
		char[] outArr = new char[inArr.length];
		int len = inArr.length;
		for (int i = 0; i < len; i++) {
			outArr[len - i - 1] = inArr[i];
		}
		String bar = new String(outArr);
		return bar;
	}

	/**
	 * @param obj
	 *            the Object witch provides the String via toString method
	 * @param length
	 * @param ex
	 *            the extension String to insert on the left<br>
	 *            the first n chars are used, where n is
	 *            length-obj.toString().length<br>
	 *            if n is bigger than ex.length, the inserted chars are again
	 *            used from the beginning of ex
	 * @return the extended string
	 */
	@SuppressWarnings("all")
	public static String extendLeftWith(Object obj, int length, String ex) {
		StringBuffer b = new StringBuffer(obj.toString());
		ex = reverse(ex);
		int index = 0;
		while (b.length() < length) {
			b.insert(0, ex.charAt(index));
			index = (index + 1) % ex.length();
		}
		return b.toString();
	}

	/**
	 * @param obj
	 *            the Object witch provides the String via toString method
	 * @param length
	 * @param ex
	 *            the extension String to insert on the right<br>
	 *            the first n chars are used, where n is
	 *            length-obj.toString().length<br>
	 *            if n is bigger than ex.length, the inserted chars are again
	 *            used from the beginning of ex
	 * @return the extended string
	 */
	public static String extendRightWith(Object obj, int length, String ex) {
		StringBuffer b = new StringBuffer(obj.toString());
		int index = 0;
		while (b.length() < length) {
			b.append(ex.charAt(index));
			index = (index + 1) % ex.length();
		}
		return b.toString();
	}

	/**
	 * @param f
	 *            a text file
	 * @return the plain text content of the file
	 * @throws IOException
	 *             if the file is not found or an exception occured during
	 *             reading the content
	 */
	public static StringBuffer file2String(File f) throws IOException {
		try (BufferedReader read = new BufferedReader(new FileReader(f))) {
			StringBuffer result = new StringBuffer();
			String line;
			while ((line = read.readLine()) != null) {
				result.append(line);
				result.append(newline);
			}
			read.close();
			result.setLength(result.length() - newline.length());
			return result;
		}
	}

	/**
	 * writes text into a file
	 *
	 * @param content
	 * @param file
	 * @throws IOException
	 */
	public static void string2File(String content, File file) throws IOException {
		try (BufferedWriter write = new BufferedWriter(new FileWriter(file))) {
			write.write(content);
			write.close();
		}
	}

	public static int countMatches(String text, String key) {
		int matches = 0;
		int currentIndex = 0;
		final int NOT_FOUND = -1;
		while (true) {
			int pos = text.indexOf(key, currentIndex);
			if (pos == NOT_FOUND)
				break;
			matches++;
			currentIndex = pos + 1;
		}
		return matches;
	}

	public static String stringFromException(Throwable exception) {
		StringWriter exceptionStackTrace = new StringWriter();
		PrintWriter printWriter = new PrintWriter(exceptionStackTrace);
		exception.printStackTrace(printWriter);
		return exceptionStackTrace.toString();
	}

	public static String takeMaxLinesFrom(String string, int maxLines) {
		String[] splitToLines = string.split(Pattern.quote(linebreak));
		if (splitToLines.length > maxLines) {
			splitToLines = Arrays.copyOf(splitToLines, maxLines);
		}
		return String.join(linebreak, splitToLines);
	}

}
