package es.willyaranda.wpl.excp;

import es.willyaranda.wpl.elements.Token;

public class BadSyntaxException extends Exception {


	private static final long serialVersionUID = -8187793067280036826L;
	
	public BadSyntaxException(Token t) {
		int column = t.column;
		while((column--)-1 != 0) {
			System.err.print('-');
		}
		System.err.println('^');
		System.err.println("Hay un error sintáctico en la fila " + t.line + ", columna " + t.column);
	}
}
