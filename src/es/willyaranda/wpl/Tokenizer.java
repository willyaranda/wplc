package es.willyaranda.wpl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import es.willyaranda.wpl.elements.Identifier;
import es.willyaranda.wpl.elements.Number;
import es.willyaranda.wpl.elements.ReservedWord;
import es.willyaranda.wpl.elements.Token;
import es.willyaranda.wpl.excp.BadCharException;

public class Tokenizer {

	private static final String TOKENS_FILE = "/home/willyaranda/workspace/wpl/tokens";
	public static final String SOURCE_FILE = "/home/willyaranda/workspace/wpl/source.wpl";

	private static final String VALID_CHARS = "abcdefghijklmnopqrstuvwxyz"
			+ "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "+-/*><=:.,;:()[]{} \n\t"
			+ "0123456789";

	static private ArrayList<ReservedWord> TOKENS = new ArrayList<ReservedWord>();

	private static ArrayList<Token> listTokensSourceFile = new ArrayList<Token>();

	private static String sourcecode = "";
	private static String line = "";
	private static int ptr = 0;
	private static int actualline;
	private static int actualcolumn;
	private int actualToken = -1;

	public Tokenizer() throws Exception {
		// Inicio del parseo del fichero de configuración
		BufferedReader is = new BufferedReader(new FileReader(TOKENS_FILE));
		String linea;
		while ((linea = is.readLine()) != null) {
			if ((linea.length() == 0) || (linea.charAt(0) == '['))
				continue;
			int eq = linea.indexOf("=");
			// ReservedWord(lexeme, text);
			ReservedWord rw = new ReservedWord(linea.substring(0, eq),
					linea.substring(eq + 2, linea.length() - 1), 0, 0);
			TOKENS.add(rw);
		}
		is.close();

		// Inicio del parseo del código fuente
		BufferedReader sourcefile = new BufferedReader(new FileReader(
				SOURCE_FILE));
		while ((linea = sourcefile.readLine()) != null) {
			line = linea;
			tokenizeLine(linea);
		}
		sourcefile.close();
	}

	private void emitToken(String string, String value, int actualline2,
			int actualcolumn2) {
		// System.out.println("Emitiendo '" + value + "' como " + string);
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

	private char getchar() throws Exception {
		char ch;
		try {
			ch = sourcecode.charAt(ptr);
		} catch (StringIndexOutOfBoundsException e) {
			return '$';
		}
		if (VALID_CHARS.indexOf(ch) == -1) {
			throw new BadCharException(ch, line, actualline, actualcolumn);
		}
		ptr++;
		actualcolumn++;
		return ch;
	}

	public Token getLookahead() {
		try {
			return listTokensSourceFile.get(actualToken + 1);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	public Token getNextToken() {
		try {
			return listTokensSourceFile.get(++actualToken);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	private boolean isReservedWord(String lexeme) {
		for (ReservedWord tok : TOKENS) {
			if (tok.getText().equalsIgnoreCase(lexeme))
				return true;
		}
		return false;
	}

	private boolean isSymbol(char a) {
		final String symbols = "+-/*><=:.,;:()[]{}";
		if (symbols.indexOf(a) != -1)
			return true;
		return false;
	}

	public void printTokens() {
		for (Token t : listTokensSourceFile) {
			System.out.println(t.getClass() + " -- " + t.toString() + " --> l="
					+ t.line + " c=" + t.column);
		}
		System.out.println(sourcecode);
	}

	private void tokenizeLine(String linea) throws Exception {
		/* Inicio del tokenizer como tal */
		actualcolumn = 0;
		actualline += 1;
		sourcecode = sourcecode.concat(linea + "\n");
		char a;
		// Ya comprueba si el caracter es válido o no
		while ((a = getchar()) != '$') {
			// Líneas no válidas: comentarios, vacías. Aumentamos contador de
			// líneas
			if (linea.length() == 0) {
				return;
			}
			// Comentario
			else if (a == '/') {
				if (getchar() == '/') {
					ptr += linea.length() - 1;
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
			} else if (Character.isDigit(a)) {
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
				while (isSymbol(a = getchar())) {
					symbol += a;
				}
				// Como el último no es símbolo, ungetchar para volver
				ungetchar();
				if (isReservedWord(symbol)) {
					emitToken("RW", symbol, actualline, actualcolumn);
				} else {
					// System.out.println("Analizando " + symbol);
					for (int i = 0; i <= symbol.length(); i++) {
						String substring;
						try {
							substring = symbol.substring(i, i + 2);
						} catch (StringIndexOutOfBoundsException e) {
							substring = symbol.substring(i, i + 1);
						}
						if (isReservedWord(substring)) {
							emitToken("RW", substring, actualline, actualcolumn);
							i++;
						} else {
							emitToken("RW", symbol.substring(i, i + 1),
									actualline, actualcolumn);
						}
					}
				}
			} else if (Character.isWhitespace(a)) {
				// Do a roll-roll!
				// Woooby wooby dooooo
			}
		}
	}

	private char ungetchar() {
		actualcolumn--;
		return sourcecode.charAt(--ptr);
	}
}
