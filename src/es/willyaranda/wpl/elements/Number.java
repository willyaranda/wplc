package es.willyaranda.wpl.elements;

public class Number extends Token {
	/**
	 * <code>value</code> es el valor del número leído.
	 */
	private long value = 0;

	/**
	 * Se inicializa su <code>type</code> como <code>NB</code>, que indica
	 * número
	 */
	public Number(long value, int linea, int column) {
		this.type = "NB";
		this.value = value;
		this.line = linea;
		this.column = column;
	}

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return String.valueOf(getValue());
	}
}
