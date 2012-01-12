package es.willyaranda.wpl;

import java.io.IOException;

import es.willyaranda.wpl.elements.Token;
import es.willyaranda.wpl.excp.BadSyntaxException;

public class Parser {
	
	Tokenizer tokenizer;

	public Parser(Tokenizer tokenizer) throws BadSyntaxException, IOException {
		this.tokenizer =  tokenizer;
		match("PROGRAM");
		//id();
		match("ID");
		match(";");
		varlistdef();
		main_begin();
	}
	
	private void main_begin() throws BadSyntaxException, IOException {
		match("BEGIN");
		assignlistdef();
		if (tokenizer.getLookahead().toString().equals("FOR")) {
			//for();
		}
		else if (tokenizer.getLookahead().toString().equals("WHILE")) {
			//while();
		}
		else if (tokenizer.getLookahead().toString().equals("IF")) {
			//if();
		}
		else if (tokenizer.getLookahead().getClass().equals(es.willyaranda.wpl.elements.Identifier.class)) {
			assignlistdef();
		}
	}
	
	private void id() throws BadSyntaxException, IOException {
		match("ID");
	}
	
	private void varlistdef() throws BadSyntaxException, IOException {
		match("VAR");
		varlist();
		match(":");
		//FIXME: aritmética entera sólamente
		match("INTEGER");
		match(";");
	}
	
	private void assignlistdef() throws BadSyntaxException, IOException {
		id();
		match(":=");
		statement();
		match(";");
	}
	
	private void statement() throws BadSyntaxException, IOException {
		if (tokenizer.getLookahead().getClass().equals(es.willyaranda.wpl.elements.Number.class)) {
			match("NB");
			statement();
		}
		else if(tokenizer.getLookahead().toString().equals("(")) {
			match("(");
			statement();
			match(")");
			statement();
		}
		else if(tokenizer.getLookahead().toString().equals("+")) {
			match("+");
			statement();
		}
		else if(tokenizer.getLookahead().toString().equals("-")) {
			match("-");
			statement();
		}
		else if(tokenizer.getLookahead().toString().equals("*")) {
			match("*");
			statement();
		}
		else if(tokenizer.getLookahead().toString().equals("/")) {
			match("/");
			statement();
		}
	}
	
	private void listid() throws BadSyntaxException, IOException {
		id();
		if (tokenizer.getLookahead().toString().equals(",")) {
			match(",");
			listid();
		}
	}
	
	private void varlist() throws BadSyntaxException, IOException {
		id();
		if (tokenizer.getLookahead().toString().equals(",")) {
			match(",");
			varlist();
		}
	}

	private void match(String string) throws BadSyntaxException, IOException {
		Token t = tokenizer.getNextToken();
		System.out.println("Estudiando " + t.toString());
		if (string.equals("ID") && t.getClass().equals(es.willyaranda.wpl.elements.Identifier.class)) {
			//ID
		}
		else if (string.equals("NB") && t.getClass().equals(es.willyaranda.wpl.elements.Number.class)) {
			//Número
		}
		else if (!string.equalsIgnoreCase(t.toString())) {
			throw new BadSyntaxException(t, string);
		}
		
	}

}
