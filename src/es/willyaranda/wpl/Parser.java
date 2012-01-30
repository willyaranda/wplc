package es.willyaranda.wpl;

import java.io.IOException;

import es.willyaranda.wpl.elements.Token;
import es.willyaranda.wpl.excp.BadSyntaxException;

public class Parser {

    boolean debug = true;

    Tokenizer tokenizer;

    private void match(String string) throws BadSyntaxException, IOException {
	Token t = tokenizer.getNextToken();
	System.out.println("Estudiando " + t.toString() + " comparando con "
		+ string);
	if (string.equals("ID")
		&& t.getClass().equals(
			es.willyaranda.wpl.elements.Identifier.class)) {
	    // ID
	} else if (string.equals("NB")
		&& t.getClass()
			.equals(es.willyaranda.wpl.elements.Number.class)) {
	    // Número
	} else if (!string.equalsIgnoreCase(t.toString())) {
	    throw new BadSyntaxException(t, string);
	}
    }

    public Parser(Tokenizer tokenizer) throws BadSyntaxException, IOException {
	this.tokenizer = tokenizer;
	program();
    }

    private void program() throws BadSyntaxException, IOException {
	if (debug)
	    System.out.println("\t->Entrando en program()");
	match("PROGRAM");
	match("ID");
	match(";");
	declarations();
	subprogram_declarations();
	match(".");
	if (debug)
	    System.out.println("\t<-Saliendo de program()");

    }

    private void identifier_list() throws BadSyntaxException, IOException {
	if (debug)
	    System.out.println("\t->Entrando en identifier_list()");
	match("ID");
	if (tokenizer.getLookahead().toString().equals(",")) {
	    match(",");
	    identifier_list();
	}
	if (debug)
	    System.out.println("\t<-Saliendo de identifier_list()");

    }

    private void declarations() throws BadSyntaxException, IOException {
	if (debug)
	    System.out.println("\t->Entrando en declarations()");
	if (tokenizer.getLookahead().toString().equalsIgnoreCase("var")) {
	    match("var");
	    identifier_list();
	    match(":");
	    type();
	    match(";");
	}
	if (debug)
	    System.out.println("\t<-Saliendo de declarations()");

    }

    private void type() throws BadSyntaxException, IOException {
	if (debug)
	    System.out.println("\t->Entrando en type()");
	if (tokenizer.getLookahead().toString().equalsIgnoreCase("array")) {
	    match("array");
	    match("[");
	    match("NB");
	    match("of");
	    standard_type();
	} else {
	    standard_type();
	}
	if (debug)
	    System.out.println("\t<-Saliendo de type()");

    }

    private void standard_type() throws BadSyntaxException, IOException {
	if (debug)
	    System.out.println("\t->Entrando en standard_type()");
	if (tokenizer.getLookahead().toString().equalsIgnoreCase("boolean")) {
	    match("boolean");
	} else if (tokenizer.getLookahead().toString().equalsIgnoreCase("char")) {
	    match("char");
	} else if (tokenizer.getLookahead().toString()
		.equalsIgnoreCase("integer")) {
	    match("integer");
	} else {
	    match("string");
	}
	if (debug)
	    System.out.println("\t<-Saliendo de standard_type()");

    }

    // FIXME
    private void subprogram_declarations() throws BadSyntaxException,
	    IOException {
	if (debug)
	    System.out.println("\t->Entrando en subprogram_declarations()");
	subprogram_declaration();
	if (debug)
	    System.out.println("\t<-Saliendo de subprogram_declarations()");

    }

    private void subprogram_declaration() throws BadSyntaxException,
	    IOException {
	if (debug)
	    System.out.println("\t->Entrando en subprogram_declaration()");
	subprogram_head();
	declarations();
	compound_statement();
	if (debug)
	    System.out.println("\t<-Saliendo de subprogram_declaration()");

    }

    private void subprogram_head() throws BadSyntaxException, IOException {
	if (debug)
	    System.out.println("\t->Entrando en subprogram_head()");
	if (tokenizer.getLookahead().toString().equalsIgnoreCase("function")) {
	    match("function");
	    match("ID");
	    arguments();
	    match(":");
	    standard_type();
	    match(";");
	} else {
	    match("procedure");
	    match("ID");
	    arguments();
	}
	if (debug)
	    System.out.println("\t<-Saliendo de subprogram_head()");

    }

    private void arguments() throws BadSyntaxException, IOException {
	if (debug)
	    System.out.println("\t->Entrando en arguments()");
	if (tokenizer.getLookahead().toString().equalsIgnoreCase("(")) {
	    match("(");
	    parameter_list();
	    match(")");
	}
	if (debug)
	    System.out.println("\t<-Saliendo de arguments()");

    }

    private void parameter_list() throws BadSyntaxException, IOException {
	if (debug)
	    System.out.println("\t->Entrando en parameter_list()");
	identifier_list();
	match(":");
	type();
	if (debug)
	    System.out.println("\t<-Saliendo de parameter_list()");

    }

    private void compound_statement() throws BadSyntaxException, IOException {
	if (debug)
	    System.out.println("\t->Entrando en compound_statement()");
	match("begin");
	optional_statements();
	match("end");
	if (debug)
	    System.out.println("\t<-Saliendo de compound_statement()");

    }

    private void optional_statements() throws BadSyntaxException, IOException {
	if (debug)
	    System.out.println("\t->Entrando en optional_statements()");
	statement_list();
	if (debug)
	    System.out.println("\t<-Saliendo de optional_statements()");
    }

    private void statement_list() throws BadSyntaxException, IOException {
	if (debug)
	    System.out.println("\t->Entrando en statement_list()");
	statement();
	if (tokenizer.getLookahead().toString().equalsIgnoreCase(";")) {
	    match(";");
	    statement_list();
	}
	if (debug)
	    System.out.println("\t<-Saliendo de statement_list()");
    }

    private void statement() throws BadSyntaxException, IOException {
	if (debug)
	    System.out.println("\t->Entrando en statement()");
	if (tokenizer.getLookahead().toString().equalsIgnoreCase("begin")) {
	    compound_statement();
	} else if (tokenizer.getLookahead().toString().equalsIgnoreCase("if")) {
	    match("if");
	    expression();
	    match("then");
	    statement();
	    if (tokenizer.getLookahead().toString().equalsIgnoreCase("else")) {
		statement();
	    }
	} else if (tokenizer.getLookahead().toString()
		.equalsIgnoreCase("repeat")) {
	    match("repeat");
	    statement();
	    match("until");
	    expression();
	} else if (tokenizer.getLookahead().toString()
		.equalsIgnoreCase("while")) {
	    match("while");
	    expression();
	    match("do");
	    statement();
	} else if (tokenizer.getLookahead().toString().equalsIgnoreCase("read")) {
	    match("read");
	    match("(");
	    match("ID");
	    match(")");
	} else if (tokenizer.getLookahead().toString()
		.equalsIgnoreCase("write")) {
	    match("write");
	    match("(");
	    expression();
	    match(")");
	} else if (tokenizer.getLookahead().toString().equalsIgnoreCase("for")) {
	    match("for");
	    statement();
	    match("to");
	    match("ID");
	    match("do");
	    compound_statement();
	} else if (tokenizer.getLookahead().toString().equalsIgnoreCase("end")) {
	    // trollface;
	} else {
	    match("ID");
	    match(":=");
	    expression();
	}
	if (debug)
	    System.out.println("\t<-Saliendo de statement()");
    }

    private void variable() throws BadSyntaxException, IOException {
	if (debug)
	    System.out.println("\t->Entrando en variable()");
	match("ID");
	if (tokenizer.getLookahead().toString().equalsIgnoreCase("(")) {
	    match("(");
	    expression_list();
	    match(")");
	}
	if (debug)
	    System.out.println("\t<-Saliendo de variable()");
    }

    private void expression_list() throws BadSyntaxException, IOException {
	if (debug)
	    System.out.println("\t->Entrando en expression_list()");
	expression();
	if (tokenizer.getLookahead().toString().equalsIgnoreCase(",")) {
	    expression();
	}
	if (debug)
	    System.out.println("\t<-Saliendo de expression_list()");
    }

    private void expression() throws BadSyntaxException, IOException {
	if (debug)
	    System.out.println("\t->Entrando en expression()");
	simple_expression();
	if (isrelop(tokenizer.getLookahead().toString())) {
	    relop();
	    simple_expression();
	}
	if (debug)
	    System.out.println("\t<-Saliendo de expression()");
    }

    private void relop() throws BadSyntaxException, IOException {
	if (debug)
	    System.out.println("\t->Entrando en relop()");
	if (tokenizer.getLookahead().toString().equalsIgnoreCase(">")) {
	    match(">");
	} else if (tokenizer.getLookahead().toString().equalsIgnoreCase("<")) {
	    match("<");
	} else if (tokenizer.getLookahead().toString().equalsIgnoreCase("<=")) {
	    match("<=");
	} else {
	    match(">=");
	}
	if (debug)
	    System.out.println("\t<-Saliendo de relop()");
    }

    private boolean isrelop(String string) {
	if (string.equals("<") || string.equals(">") || string.equals("<=")
		|| string.equals(">=")) {
	    return true;
	}
	return false;
    }

    // FIXME
    private void simple_expression() throws BadSyntaxException, IOException {
	if (debug)
	    System.out.println("\t->Entrando en simple_expression()");
	if (issign(tokenizer.getLookahead().toString())) {
	    sign();
	    term();
	}
	term();
	if (isaddop(tokenizer.getLookahead().toString())) {
	    addop();
	    term();
	}
	if (debug)
	    System.out.println("\t<-Saliendo de simple_expression()");
    }

    private void addop() throws BadSyntaxException, IOException {
	if (debug)
	    System.out.println("\t->Entrando en addop()");
	if (tokenizer.getLookahead().toString().equalsIgnoreCase("+")) {
	    match("+");
	} else {
	    match("-");
	}
	if (debug)
	    System.out.println("\t<-Saliendo de addop()");
    }

    private boolean isaddop(String string) {
	if (string.equals("+") || string.equals("-")) {
	    return true;
	}
	return false;
    }

    private boolean issign(String string) {
	if (string.equals("+") || string.equals("-")) {
	    return true;
	}
	return false;
    }

    private void term() throws BadSyntaxException, IOException {
	if (debug)
	    System.out.println("\t->Entrando en term()");
	factor();
	if (ismulop(tokenizer.getLookahead().toString())) {
	    mulop();
	    factor();
	}
	if (debug)
	    System.out.println("\t<-Saliendo de term()");
    }

    private boolean ismulop(String string) {
	if (string.equals("*") || string.equals("/")) {
	    return true;
	}
	return false;
    }

    private void mulop() throws BadSyntaxException, IOException {
	if (debug)
	    System.out.println("\t->Entrando en mulop()");
	if (tokenizer.getLookahead().toString().equalsIgnoreCase("*")) {
	    match("*");
	} else {
	    match("/");
	}
	if (debug)
	    System.out.println("\t<-Saliendo de mulop()");
    }

    private void factor() throws BadSyntaxException, IOException {
	if (debug)
	    System.out.println("\t->Entrando en factor()");
	if (tokenizer.getLookahead().getClass()
		.equals(es.willyaranda.wpl.elements.Identifier.class)) {
	    match("ID");
	    if (tokenizer.getLookahead().toString().equalsIgnoreCase("(")) {
		match("(");
		expression_list();
		match(")");
	    }
	} else if (tokenizer.getLookahead().getClass()
		.equals(es.willyaranda.wpl.elements.Number.class)) {
	    match("NB");
	} else if (tokenizer.getLookahead().toString().equalsIgnoreCase("(")) {
	    match("(");
	    expression();
	    match(")");
	} else if (isboolean(tokenizer.getLookahead().toString())) {
	    boolean_value();
	} else {
	    match("not");
	}
	if (debug)
	    System.out.println("\t<-Saliendo de factor()");
    }

    private boolean isboolean(String string) {
	if (string.equals("true") || string.equals("false")) {
	    return true;
	}
	return false;
    }

    private void sign() throws BadSyntaxException, IOException {
	if (debug)
	    System.out.println("\t->Entrando en sign()");
	if (tokenizer.getLookahead().toString().equalsIgnoreCase("+")) {
	    match("+");
	} else {
	    match("-");
	}
	if (debug)
	    System.out.println("\t<-Saliendo de sign()");
    }

    private void boolean_value() throws BadSyntaxException, IOException {
	if (debug)
	    System.out.println("\t->Entrando en boolean_value()");
	if (tokenizer.getLookahead().toString().equalsIgnoreCase("true")) {
	    match("true");
	} else {
	    match("false");
	}
	if (debug)
	    System.out.println("\t<-Saliendo de boolean_value()");
    }

}
