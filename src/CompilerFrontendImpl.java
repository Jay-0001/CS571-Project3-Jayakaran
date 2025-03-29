public class CompilerFrontendImpl extends CompilerFrontend {
    public CompilerFrontendImpl() {
        super();
    }

    public CompilerFrontendImpl(boolean debug_) {
        super(debug_);
    }

    /*
     * Initializes the local field "lex" to be equal to the desired lexer.
     * The desired lexer has the following specification:
     * 
     * NUM: [0-9]*\.[0-9]+
     * PLUS: \+
     * MINUS: -
     * TIMES: \*
     * DIV: /
     * WHITE_SPACE (' '|\n|\r|\t)*
     */
    @Override
    protected void init_lexer() {
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'init_lexer'");
        
        //Float numbers
        int state=0;
        Automaton fnum = new AutomatonImpl();
    
        fnum.addState(state, true, false);        
        for (int st = 1; st <= 10; st++) {
            fnum.addState(st, false, false);       
        }
        fnum.addState(11, false, true);           

        //self loop for chars 0to9
        for (char d = '0'; d <= '9'; d++) {
            fnum.addTransition(0, d, 0);
        }
        // decimal point
        fnum.addTransition(0, '.', 1);
        //chars after decimal point
        for (char d = '0'; d <= '9'; d++) {
            fnum.addTransition(1, d, 2);  // at least one digit
            fnum.addTransition(2, d, 2);  // loop
        }

        fnum.addState(2, false, true); // Accepting after valid float
        
        //desired lexer format
        lex = new LexerImpl();
        lex.add_automaton(TokenType.NUM, fnum);
        lex.add_automaton(TokenType.PLUS, makeCharAutomaton('+'));
        lex.add_automaton(TokenType.MINUS, makeCharAutomaton('-'));
        lex.add_automaton(TokenType.TIMES, makeCharAutomaton('*'));
        lex.add_automaton(TokenType.DIV, makeCharAutomaton('/'));
        lex.add_automaton(TokenType.LPAREN, makeCharAutomaton('('));
        lex.add_automaton(TokenType.RPAREN, makeCharAutomaton(')'));

        //white space chars
        // WHITE_SPACE: (' '|\n|\r|\t)*
        Automaton wSpace = new AutomatonImpl();
        wSpace.addState(0, true, true); // start is accepting
        wSpace.addTransition(0, ' ', 0);
        wSpace.addTransition(0, '\n', 0);
        wSpace.addTransition(0, '\r', 0);
        wSpace.addTransition(0, '\t', 0);
        lex.add_automaton(TokenType.WHITE_SPACE, wSpace);
    }

    protected Automaton makeCharAutomaton(char c) {
        Automaton a = new AutomatonImpl();
        a.addState(0, true, false);
        a.addState(1, false, true);
        a.addTransition(0, c, 1);
        return a;
    }
}



