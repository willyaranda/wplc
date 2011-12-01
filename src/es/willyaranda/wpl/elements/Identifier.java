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
	public Identifier() {
		this.type = "ID";
	}

	public String getLexeme() {
		return lexeme;
	}

	public void setLexeme(String lexeme) {
		this.lexeme = lexeme;
	}
}
