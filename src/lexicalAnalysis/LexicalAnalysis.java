package lexicalAnalysis;


import symboleTable.SymbolTable;
import symboleTable.Token;

import java.util.Optional;
import java.util.function.Supplier;

public class LexicalAnalysis {

    private SymbolTable symbolTable;
    private Scanner scanner;

    public LexicalAnalysis(Scanner scanner, SymbolTable symbolTable) {
        this.scanner = scanner;
        this.symbolTable = symbolTable;
    }

    /**
     * get un token. transforme le mot lu en token identifiable
     * renvoi null quand on es a la fin du fichier
     */
    public Token getNextToken() throws Exception {
        String wordRead = readNextWord();
        if (wordRead == null) return null;

        Token tokenFromTableSymbol = symbolTable.getToken(wordRead);

        Supplier<Token> supplier = (tokenFromTableSymbol == null)
                ? () -> symbolTable.addToken(wordRead)
                : () -> tokenFromTableSymbol;

        return supplier.get();
    }

    /**
     * lire un mot du text
     */
    private String readNextWord() throws Exception {
        String readWord = null;

        Runnable runnable = (scanner.isEndOfLine())
                ?
                () -> {
                    scanner.readLine();
                }
                : Optional::empty;

        runnable.run();

        if (!scanner.isEndOfLine()) {
            readWord = scanner.readWordCurrentLine();
        }

        return readWord;
    }

    /**
     * get le numero de ligne courante
     */
    public int getCurrentLineNumber() {
        return scanner.getCurrentLineNumber();
    }

}
