package es.willyaranda.wpl.excp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import es.willyaranda.wpl.Tokenizer;
import es.willyaranda.wpl.elements.Token;

public class BadSyntaxException extends Exception {

    private static final long serialVersionUID = -8187793067280036826L;

    public BadSyntaxException(Token t, String string) throws IOException {
	int column = t.column;
	String linea;
	int linea_cnt = 0;
	BufferedReader sourcefile = new BufferedReader(new FileReader(
		Tokenizer.SOURCE_FILE));
	while ((linea = sourcefile.readLine()) != null && ++linea_cnt != t.line) {
	}
	sourcefile.close();
	System.err.println(linea);
	while ((column--) - 2 != 0) {
	    System.err.print('-');
	}
	System.err.println('^');
	System.err.println("Hay un error sintáctico en la fila " + t.line
		+ ", columna " + t.column);
	System.err.println("Se esperaba " + string + " pero se encontró un "
		+ t.toString());
    }
}
