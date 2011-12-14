package es.willyaranda.wpl;

import es.willyaranda.wpl.elements.Token;

public class Main {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Tokenizer tokenizer = new Tokenizer();
		Token a;
		while ((a = tokenizer.getNextToken()) != null) {
			System.out.println(a);
		}
		//tokenizer.printTokens();
	}

}
