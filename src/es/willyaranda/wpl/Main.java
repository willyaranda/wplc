package es.willyaranda.wpl;

public class Main {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Tokenizer tokenizer = new Tokenizer();
		Parser parser = new Parser(tokenizer);
	}

}
