package es.willyaranda.wpl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import es.willyaranda.wpl.elements.ReservedWord;
import es.willyaranda.wpl.elements.Token;
import es.willyaranda.wpl.excp.BadTokenException;

public class Tokenizer {

	static final String TOKENS_FILE = "/home/willyaranda/workspace/wpl/tokens";
	static final String SOURCE_FILE = "/home/willyaranda/workspace/wpl/source.wpl";

	static final String VALID_CHARS = "abcdefghijklmnopqrstuvwxyz"
			+ "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "+-/*><=:.,;:()[]";

	static public ArrayList<ReservedWord> TOKENS = new ArrayList<ReservedWord>();

	static public ArrayList<Token> listTokensSourceFile = new ArrayList<Token>();

	static String sourcecode;
	static int ptr_inicio = 0;
	static int ptr_actual = 0;

	public static void main(String[] args) throws IOException {
		initializeEverything();
		printTokens();
	}

	public static char getchar() {
		char ch;
		try {
			ch = sourcecode.charAt(ptr_actual);
		} catch (StringIndexOutOfBoundsException e) {
			return '$';
		}
		if (VALID_CHARS.indexOf(ch) == -1) {
		}
		;
		ptr_actual++;
		return ch;
	}

	public static char ungetchar() {
		return sourcecode.charAt(--ptr_actual);
	}

	public static void initializeEverything() throws IOException {
		BufferedReader is = new BufferedReader(new FileReader(TOKENS_FILE));
		String linea;
		while ((linea = is.readLine()) != null) {
			if ((linea.length() == 0) || (linea.charAt(0) == '['))
				continue;
			int eq = linea.indexOf("=");
			ReservedWord rw = new ReservedWord();
			rw.setLexeme(linea.substring(0, eq));
			rw.setText(linea.substring(eq + 2, linea.length() - 1));
			TOKENS.add(rw);
		}
		is.close();

		BufferedReader sourcefile = new BufferedReader(new FileReader(
				SOURCE_FILE));
		sourcecode = "";
		while ((linea = sourcefile.readLine()) != null) {
			if (linea.length() == 0
					||
					// Comment (simple line)
					(linea.charAt(0) == '/' && linea.charAt(1) == '/')
					|| (linea.charAt(0) == '\n') || (linea.charAt(0) == '\t'))
				continue;
			sourcecode += linea;
		}
		sourcefile.close();
	}

	public static void tokenizeEverything() {
		char a;
		while ((a = getchar()) != '$') {

			// lookForToken(id)
		}
	}

	public static ReservedWord lookForToken(String id) throws BadTokenException {
		int searching = 0;
		while (TOKENS.size() <= searching++) {
			if (TOKENS.get(searching).getText() == id) {
				return TOKENS.get(searching);
			}
		}
		throw new BadTokenException(); // :(
	}

	public static void printTokens() {
		// for (ReservedWord reservedWord : TOKENS) {
		// System.out.println(reservedWord.getText());
		// System.out.println(reservedWord.getLexeme());
		// System.out.println("------");
		// }

		System.out.println("---FINAL---");
		System.out.println(sourcecode);
		tokenizeEverything();
	}
}
