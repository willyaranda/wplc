package es.willyaranda.wpl;

import es.willyaranda.wpl.elements.Token;
import es.willyaranda.wpl.excp.BadSyntaxException;

public class Parser {
	
	Tokenizer tokenizer;

	public Parser(Tokenizer tokenizer) throws BadSyntaxException {
		this.tokenizer =  tokenizer;
		match("PROGRAM");
	}

	private void match(String string) throws BadSyntaxException {
		Token t = tokenizer.getNextToken();
		if (!string.equalsIgnoreCase(t.toString())) {
			throw new BadSyntaxException(t);
		}
	}

}
