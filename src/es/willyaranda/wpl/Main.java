package es.willyaranda.wpl;

import es.willyaranda.wpl.elements.Token;

public class Main {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Tokenizer tokenizer = new Tokenizer();
		/*Token a;
		while ((a = tokenizer.getNextToken()) != null) {
			System.out.println(a);
		}*/
		Parser parser = new Parser(tokenizer);
	}
}
