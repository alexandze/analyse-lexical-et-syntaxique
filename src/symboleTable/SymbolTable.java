package symboleTable;

import java.util.Hashtable;
import java.util.function.Function;
import java.util.function.Supplier;

public class SymbolTable {
    private Hashtable<String, Token> symbolTable;

    public SymbolTable() {
        symbolTable = new Hashtable<String, Token>();
        initSymbolTableWithKeyWord();
    }


    public Token getToken(String lexeme) {
        return symbolTable.get(lexeme);
    }

    /**
     * ajouter un token
     *
     * @param lexeme
     * @return
     */
    public Token addToken(String lexeme) {
        Token token = _createToken.apply(lexeme);
        symbolTable.put(lexeme, token);
        return token;
    }

    /**
     * creer un token en fonction du lexeme
     */
    private Function<String, Token> _createToken =
            lexeme -> {
                Supplier<Token> supplier = (Character.isLetter(lexeme.charAt(0)))
                        ? //si identificateur
                        () -> new Token(Tag.ID, lexeme, Type.INCONNU)
                        : //si digit
                        (Character.isDigit(lexeme.charAt(0)))
                        ? //si contien un point c'est reel sinon c'est un entier
                        () -> {
                            Supplier<Token> supplier1 = (lexeme.contains("."))
                                    ?
                                    () -> new Token(Tag.NUM, lexeme, Type.REEL)

                                    :
                                    () -> new Token(Tag.NUM, lexeme, Type.ENTIER);

                            return supplier1.get();
                        }
                        :
                        () -> null;

                return supplier.get();
            };

    /****************** func init table symbol **************************************************************/

    /**
     * initialise la table des symbole avec les keyWord du langage
     */
    public void initSymbolTableWithKeyWord() {
        _addTokenKeyWord(KeyWord.PROCEDURE, Tag.PROCEDURE, Type.KEYWORD);
        _addTokenKeyWord(KeyWord.FIN_PROCEDURE, Tag.FIN_PROCEDURE, Type.KEYWORD);
        _addTokenKeyWord(KeyWord.DECLARE, Tag.DECLARE, Type.KEYWORD);
        _addTokenKeyWord(KeyWord.ENTIER, Tag.ENTIER, Type.KEYWORD_TYPE);
        _addTokenKeyWord(KeyWord.REEL, Tag.REEL, Type.KEYWORD_TYPE);
        _addTokenKeyWord(KeyWord.EQUALITY_SIGN, Tag.EQUALITY_SIGN, Type.KEYWORD_SIGN);
        _addTokenKeyWord(KeyWord.LESS_SIGN, Tag.LESS_SIGN, Type.KEYWORD_SIGN);
        _addTokenKeyWord(KeyWord.ADDITION_SIGN, Tag.ADDITION_SIGN, Type.KEYWORD_SIGN);
        _addTokenKeyWord(KeyWord.DIVISION_SIGN, Tag.DIVISION_SIGN, Type.KEYWORD_SIGN);
        _addTokenKeyWord(KeyWord.MULTIPLICATION_SIGN, Tag.MULTIPLICATION_SIGN, Type.KEYWORD_SIGN);
        _addTokenKeyWord(KeyWord.SEMICOLON, Tag.SEMICOLON, Type.KEYWORD);
        _addTokenKeyWord(KeyWord.TWO_POINTS, Tag.TWO_POINTS, Type.KEYWORD);
        _addTokenKeyWord(KeyWord.OPENING_PARENTHESIS, Tag.OPENING_PARENTHESIS, Type.KEYWORD);
        _addTokenKeyWord(KeyWord.CLOSING_PARENTHESIS, Tag.CLOSING_PARENTHESIS, Type.KEYWORD);
    }

    /**
     * ajouter un token keyWord dans la table de symbol
     */
    private void _addTokenKeyWord(String keyWord, Tag tag, Type type) {
        symbolTable.put(keyWord, new Token(tag, keyWord, type));
    }
}
