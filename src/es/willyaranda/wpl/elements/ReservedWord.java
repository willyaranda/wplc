package es.willyaranda.wpl.elements;

public class ReservedWord extends Token {
	/**
	 * <code>lexeme</code> es el valor del token a devolver. Por ejemplo,
	 * devolver <code>STRING</code> para una cadena de tipo <code>text</code>
	 * que sea <code>string</code>.
	 */
	private String lexeme;
	/**
	 * ver <code>lexeme</code>
	 */
	private String text;

	/**
	 * El <code>type</code> de una palabra reservada es <code>RW</code>
	 */
	public ReservedWord(String lexeme, String text, int linea, int column) {
		this.type = "RW";
		this.lexeme = lexeme;
		this.text = text;
		this.line = linea;
		this.column = column;
	}

	public String getLexeme() {
		return lexeme;
	}

	public String getText() {
		return text;
	}

	public void setLexeme(String lexeme) {
		this.lexeme = lexeme;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return getText();
	}
}
