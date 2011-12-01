package es.willyaranda.wpl.elements;

public class Token {
	/**
	 * <code>type</code> es el tipo de elemento encontrado. Puede ser de tres
	 * tipos: * <code>ID</code> que es un identificador * <code>NB</code> que es
	 * un número * <code>RW</code> que es una palabra reservada
	 */
	public String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
