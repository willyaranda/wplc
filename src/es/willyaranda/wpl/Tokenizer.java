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
			+ "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "+-/*><=:.,;:()[]{} " +
			"0123456789 ";

	static public ArrayList<ReservedWord> TOKENS = new ArrayList<ReservedWord>();

	static public ArrayList<Token> listTokensSourceFile = new ArrayList<Token>();

	static String sourcecode = "";
	static int ptr = 0;
	static int actualline = 1;
	static int actualcolumn = 1;

	public static void main(String[] args) throws Exception {
		initializeEverything();
		printTokens();
	}
	
	public Tokenizer() {
		// TODO Auto-generated constructor stub
		// Debería pasar el TOKENS_FILE Y EL SOURCE_FILE como parámetros
	}

	public static char getchar() throws Exception {
		char ch;
		try {
			ch = sourcecode.charAt(ptr);
		} catch (StringIndexOutOfBoundsException e) {
			return '$';
		}
		if (VALID_CHARS.indexOf(ch) == -1) {
			throw new Exception("El caracter es inválido: " + ch);
		}
		ptr++;
		return ch;
	}

	public static char ungetchar() {
		return sourcecode.charAt(--ptr);
	}

	public static void initializeEverything() throws Exception {
		//Inicio del parseo del fichero de configuración
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

		// Inicio del parseo del código fuente
		BufferedReader sourcefile = new BufferedReader(new FileReader(
				SOURCE_FILE));
		while ((linea = sourcefile.readLine()) != null) {
			tokenizeLine(linea);
		}
		sourcefile.close();
	}
	
	// TODO
	public static void tokenizeLine(String linea) throws Exception {
		/* Inicio del tokenizer como tal */
		actualcolumn = 1;
		sourcecode += linea;
		char a;
		//Ya comprueba si el caracter es válido o no
		while ((a = getchar()) != '$') {
			//Líneas no válidas: comentarios, vacías. Aumentamos contador de líneas
			if (linea.length() == 0	||
					(linea.charAt(0) == '/' && linea.charAt(1) == '/')
					|| (linea.charAt(0) == '\n')) {
				actualline++; return;
			}
			else if (Character.isLetter(a)) {
				String lexeme = "";
				lexeme += a;
				while(true) {
					a = getchar();
					if (isSymbol(a) && !Character.isWhitespace(a)) {
						lexeme += a;
					}
					else {
						ungetchar();
						break;
					}
				}
				if (isReservedWord(lexeme)) {
					emitToken("RW", lexeme, actualline, actualcolumn);
				} else {
					emitToken("ID", lexeme, actualline, actualcolumn);
				}
			}
			//FIXME: aquí si hay número y después letra, qué? error?
			else if (Character.isDigit(a)) {
				String value = "";
				value += a;
				while(true) {
					a = getchar();
					if (Character.isDigit(a)) {
						value += a;
					}
					else {
						ungetchar();
						break;
					}
				}
				emitToken("NB", value, actualline, actualcolumn);
			}
			else if (isSymbol(a)) {
				
			}
			else if (Character.isWhitespace(a)) {
				
			}
		}
	}

	private static boolean isReservedWord(String lexeme) {
		// TODO Auto-generated method stub
		return false;
	}

	private static void emitToken(String string, String value, int actualline2,
			int actualcolumn2) {
		// TODO Auto-generated method stub
		
	}

	private static boolean isSymbol(char a) {
		final String symbols = "+-/*><=:.,;:()[]{}";
		if (symbols.indexOf(a) != -1) return true;
		return false;
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
	}
}
