package es.willyaranda.wpl.excp;

public class BadCharException extends Exception {

	public BadCharException(char ch) {
		System.err.println("El caracter leído es inválido: " + ch);
	}

	private static final long serialVersionUID = -5832879890209373629L;

}
