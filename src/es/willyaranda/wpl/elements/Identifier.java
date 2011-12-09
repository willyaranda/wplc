package es.willyaranda.wpl.elements;

public class Identifier extends Token {
	/**
	 * <code>lexeme</code> es el nombre de la variable que tenía en el código
	 * fuente original. Por ejemplo <code><b>x</b>:=8+3</code>
	 */
	private String lexeme;

	/**
	 * el <code>type</code> de un identificador es <code>ID</code>
	 */
	public Identifier(String lexeme, int linea, int column) {
		this.type = "ID";
		this.lexeme = lexeme;
		this.line = linea;
		this.column = column;
	}

	public String getLexeme() {
		return lexeme;
	}

	public void setLexeme(String lexeme) {
		this.lexeme = lexeme;
	}

	@Override
	public String toString() {
		return getLexeme();
	}
}
