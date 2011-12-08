package es.willyaranda.wpl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import es.willyaranda.wpl.elements.Identifier;
import es.willyaranda.wpl.elements.Number;
import es.willyaranda.wpl.elements.ReservedWord;
import es.willyaranda.wpl.elements.Token;
import es.willyaranda.wpl.excp.BadTokenException;

public class Tokenizer {

	static final String TOKENS_FILE = "/home/willyaranda/workspace/wpl/tokens";
	static final String SOURCE_FILE = "/home/willyaranda/workspace/wpl/source.wpl";

	static final String VALID_CHARS = "abcdefghijklmnopqrstuvwxyz"
			+ "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "+-/*><=:.,;:()[]{}\n\t "
			+ "0123456789";

	static public ArrayList<ReservedWord> TOKENS = new ArrayList<ReservedWord>();

	static public ArrayList<Token> listTokensSourceFile = new ArrayList<Token>();

	static String sourcecode = "";
	static int ptr = 0;
	static int actualline = 1;
	static int actualcolumn;

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
			throw new Exception("El caracter leído es inválido: " + ch);
		}
		ptr++;
		actualcolumn++;
		return ch;
	}

	public static char ungetchar() {
		actualcolumn--;
		return sourcecode.charAt(--ptr);
	}

	public static void initializeEverything() throws Exception {
		// Inicio del parseo del fichero de configuración
		BufferedReader is = new BufferedReader(new FileReader(TOKENS_FILE));
		String linea;
		while ((linea = is.readLine()) != null) {
			if ((linea.length() == 0) || (linea.charAt(0) == '['))
				continue;
			int eq = linea.indexOf("=");
			// ReservedWord(lexeme, text);
			ReservedWord rw = new ReservedWord(linea.substring(0, eq), linea.substring(eq + 2, linea.length() - 1), 0, 0);
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
		actualcolumn = 0;
		sourcecode = sourcecode.concat(linea + "\n");
		char a;
		// Ya comprueba si el caracter es válido o no
		while ((a = getchar()) != '$') {
			// Líneas no válidas: comentarios, vacías. Aumentamos contador de
			// líneas
			if (linea.length() == 0) {
				actualline++;
				return;
			}
			//Comentario
			else if (a == '/') {
				if (getchar() == '/') {
					actualline++;
					System.out.println("Una línea de comentario");
					return;
				} else {
					String string = "";
					string += a;
					emitToken("RW", string, actualline, actualcolumn);
					ungetchar();
				}
			} else if (Character.isLetter(a)) {
				String lexeme = "";
				lexeme += a;
				while (true) {
					a = getchar();
					if (!isSymbol(a) && !Character.isWhitespace(a)) {
						lexeme += a;
					} else {
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
			// FIXME: aquí si hay número y después letra, qué? error?
			else if (Character.isDigit(a)) {
				String value = "";
				value += a;
				while (true) {
					a = getchar();
					if (Character.isDigit(a)) {
						value += a;
					} else {
						ungetchar();
						break;
					}
				}
				emitToken("NB", value, actualline, actualcolumn);
			} else if (isSymbol(a)) {
				String symbol = "";
				symbol += a;
				if (a == '>') {
					a = getchar();
					if (a == '=') {
						symbol += a;
						emitToken("RW", symbol, actualline, actualcolumn);
					} else {
						ungetchar();
					}
				} else if (a == '<') {
					a = getchar();
					if (a == '=') {
						symbol += a;
						emitToken("RW", symbol, actualline, actualcolumn);
					} else {
						ungetchar();
					}
				} else if (a == ':') {
					a = getchar();
					if (a == '=') {
						symbol += a;
						emitToken("RW", symbol, actualline, actualcolumn);
					} else {
						ungetchar();
					}
				} else {
					emitToken("RW", symbol, actualline, actualcolumn);
				}
			} else if (Character.isWhitespace(a)) {
				// Do a roll-roll!
				// Woooby wooby dooooo
			}
		}
	}

	private static boolean isReservedWord(String lexeme) {
		for (ReservedWord tok: TOKENS) {
			//System.out.println("Comparando " + lexeme + " con " + tok.getText());
			if (tok.getText().equalsIgnoreCase(lexeme)) return true;
		}
		return false;
	}

	private static void emitToken(String string, String value, int actualline2,
			int actualcolumn2) {
		if (string == "RW") {
			ReservedWord token = new ReservedWord(string, value, actualline2,
					actualcolumn2);
			listTokensSourceFile.add(token);
		} else if (string == "NB") {
			Number token = new Number(Long.parseLong(value), actualline2,
					actualcolumn2);
			listTokensSourceFile.add(token);
		} else if (string == "ID") {
			Identifier token = new Identifier(value, actualline2, actualcolumn2);
			listTokensSourceFile.add(token);
		}
	}

	private static boolean isSymbol(char a) {
		final String symbols = "+-/*><=:.,;:()[]{}";
		if (symbols.indexOf(a) != -1)
			return true;
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
		for (Token t : listTokensSourceFile) {
			System.out.println(t.getClass() + " -- " + t.toString() + " --> l=" + t.line + " c=" + t.column);
		}
		System.out.println(sourcecode);
		/*for (Token t : TOKENS) {
			System.out.println(t);
		}*/
	}
}
