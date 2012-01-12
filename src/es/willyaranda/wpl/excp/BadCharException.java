package es.willyaranda.wpl.excp;

public class BadCharException extends Exception {

	private static final long serialVersionUID = -5832879890209373629L;

	public BadCharException(char ch, String linea, int line, int column) {
		System.err.println(linea);
		int columna = column;
		while ((columna--) != 0) {
			System.err.print('-');
		}
		System.err.println('^');
		System.err.println("El caracter leído es inválido: " + ch);
	}

}
