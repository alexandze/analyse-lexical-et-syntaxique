package syntaxAnalysis;

import lexicalAnalysis.LexicalAnalysis;
import symboleTable.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Parser {

    private Token lookahead;
    private SymbolTable symbolTable;
    private LexicalAnalysis lexicalAnalysis;
    private ErrorParser errorParser;

    /**
     * pour savoir si on a deja fait une declations de variable
     */
    private boolean hasAlreadyDeclaredVariable;

    /**
     * pour savoir si on a deja fait une instruction d'affection
     * utile pour pouvoir mettre un point virgule a partir de la 2eme instruction d'affectation
     */
    private boolean hasAlreadyAssignmentStatement;

    /**
     * pour savoir si c'etait une variable avant, si c'etait avant possible de mettre un signe mathematique
     */
    private boolean hasVariableOrNumBefore;



    public Parser(SymbolTable symbolTable, LexicalAnalysis lexicalAnalysis) {
        this.symbolTable = symbolTable;
        this.lexicalAnalysis = lexicalAnalysis;
        this.errorParser = new ErrorParser();
        hasAlreadyDeclaredVariable = false;
        hasAlreadyAssignmentStatement = false;
        hasVariableOrNumBefore = false;
    }

    public void startParser() {
        try {
            nextToken();
            procedure();
            identifierStartProcedure();
            declarations();
            assignmentInstruction();

            finProcedure();
            identifierEndProcedure();
            System.out.println("Aucune erreur syntaxique");


        } catch (Exception e) {
            System.out.println("***** " + e.getMessage() + " ******");
        }
    }


    private void identifierEndProcedure() throws Exception {
        Supplier<Exception> supplier = (lookahead == null || !lookahead.getTag().equals(Tag.ID_NAME_PROCEDURE))
                ?
                () -> errorParser.getExceptionFinProcedure(lexicalAnalysis.getCurrentLineNumber())
                :
                () -> null;

        Exception exception = supplier.get();

        if (exception != null) throw exception;

        nextToken();
    }

    private void finProcedure() throws Exception {
        Supplier<Exception> exceptionSupplier = (lookahead == null || !lookahead.getTag().equals(Tag.FIN_PROCEDURE))
                ?
                () -> errorParser.getExceptionFinProcedure(lexicalAnalysis.getCurrentLineNumber())
                :
                () -> null;

        Exception exception = exceptionSupplier.get();

        if (exception != null) throw exception;

        nextToken();
    }


    private void nextToken() throws Exception {
        lookahead = lexicalAnalysis.getNextToken();
    }


    private void assignmentInstruction() throws Exception {
        throwExceptionIfNoVariableDelared();
        hasVariableOrNumBefore = false;

        switch (lookahead.getTag()) {
            case ID:
                if (!hasAlreadyAssignmentStatement) {
                    assignmentStatementVariable();
                    assignmentStatementEquality();
                    arithmeticExpression();
                    hasAlreadyAssignmentStatement = true;
                    assignmentInstruction();
                }
                break;
            case SEMICOLON:
                if (hasAlreadyAssignmentStatement) {
                    nextToken();
                    assignmentStatementVariable();
                    assignmentStatementEquality();
                    arithmeticExpression();
                    assignmentInstruction();
                }
                break;
        }
    }


    private Token assignmentStatementVariable() throws Exception {
        Token tokenVariable = lookahead;

        Supplier<Exception> supplier = (!lookahead.getTag().equals(Tag.ID))
                ? // si pas un identificateur
                () -> errorParser.getExceptionAssignmentStatementVariable(lexicalAnalysis.getCurrentLineNumber())
                :
                !(isVariableIsDeclared())
                        ? //si deja declared
                        () -> errorParser.getExceptionAssignmentStatementVariableNotDeclared(lexicalAnalysis.getCurrentLineNumber(), tokenVariable)
                        :
                        () -> null;

        Exception exception = supplier.get();

        if (exception != null) throw exception;

        nextToken();

        return tokenVariable;
    }

    private void assignmentStatementEquality() throws Exception {
        Supplier<Exception> exceptionSupplier = (!lookahead.getTag().equals(Tag.EQUALITY_SIGN))
                ?
                () -> errorParser.getExceptionAssignmentStatementEqualitySign(lexicalAnalysis.getCurrentLineNumber())
                :
                () -> null;

        Exception exception = exceptionSupplier.get();

        if (exception != null) throw exception;

        nextToken();
    }

    private void arithmeticExpression() throws Exception {
        term();

    }

    private void term() throws Exception {

        switch (lookahead.getTag()) {
            case ID:
                if (!hasVariableOrNumBefore) {
                    assignmentStatementVariable();
                    hasVariableOrNumBefore = true;
                    term();
                } else {
                    throw errorParser.getExceptionArithmeticId(lexicalAnalysis.getCurrentLineNumber());
                }
                break;
            case NUM:
                if (!hasVariableOrNumBefore) {
                    hasVariableOrNumBefore = true;
                    nextToken();
                    term();
                } else {
                    throw errorParser.getExceptionArithmeticNum(lexicalAnalysis.getCurrentLineNumber());
                }
                break;

            case OPENING_PARENTHESIS:
                if (!hasVariableOrNumBefore) {
                    nextToken();
                    term();
                    closingParenthesis();
                    term();
                    hasVariableOrNumBefore = true;
                }
                break;

            case ADDITION_SIGN:
            case DIVISION_SIGN :
            case MULTIPLICATION_SIGN:
            case LESS_SIGN:
                if (hasVariableOrNumBefore) {
                    hasVariableOrNumBefore = false;
                    nextToken();
                    term();
                } else {
                    throw errorParser.getExceptionArithmeticSign(lexicalAnalysis.getCurrentLineNumber());
                }
                break;
        }

    }

    /**
     * identification tu parenthese fermante
     */
    private void closingParenthesis() throws Exception {
        Supplier<Exception> supplier = (!lookahead.getTag().equals(Tag.CLOSING_PARENTHESIS))
                ?
                () -> errorParser.getExceptionClosingParenthesis(lexicalAnalysis.getCurrentLineNumber())
                :
                () -> null;

        Exception exception = supplier.get();

        if (exception != null) throw exception;

        nextToken();
    }

    /**
     * verifie si un token est une procedure
     */
    private void procedure() throws Exception {
        Supplier<Exception> supplierException = (!lookahead.getTag().equals(Tag.PROCEDURE))
                ?
                () -> errorParser.getExceptionProcedure(lexicalAnalysis.getCurrentLineNumber())
                :
                () -> null;

        Exception exception = supplierException.get();

        if (exception != null) throw exception;
        nextToken();
    }

    /**
     * verifie si un token
     */
    private void identifierStartProcedure() throws Exception {
        Predicate<Token> isCorrectLength = (Token token1) -> token1.getWord().length() <= 8;

        Supplier<Exception> supplierException = (!lookahead.getTag().equals(Tag.ID))
                ?
                () -> errorParser.getExceptionIdentifierProcedure(lexicalAnalysis.getCurrentLineNumber())
                :
                (!isCorrectLength.test(lookahead))
                        ?
                        () -> errorParser.getExceptionIdentifierProcedureLength(lexicalAnalysis.getCurrentLineNumber())
                        :
                        () -> null;

        Exception exception = supplierException.get();

        if (exception != null) throw exception;

        lookahead.setTag(Tag.ID_NAME_PROCEDURE);
        nextToken();
    }

    private void declarations() throws Exception {
        switch (lookahead.getTag()) {
            case DECLARE:
                hasAlreadyDeclaredVariable = true;
                nextToken();
                variableDeclaration();
                declarations();
                break;
        }
    }


    private void variableDeclaration() throws Exception {
        Token tokenVariable = identifierVariableDeclaration();
        identifierVariableTwoPoint();
        Token tokenType = identifierVariableType();
        identifierVariableSemicolon();

        setTypeVariable
                .apply(tokenVariable)
                .apply(tokenType)
                .run();
    }

    /**
     * declaration variable, nom de variable
     */
    private Token identifierVariableDeclaration() throws Exception {
        Token tokenVariable = lookahead;

        Supplier<Exception> supplier = (lookahead == null || !lookahead.getTag().equals(Tag.ID))
                ? // si pas un identificateur
                () -> errorParser.getExceptionVariable(lexicalAnalysis.getCurrentLineNumber())
                : // si taille superieur a 8
                (lookahead.getWord().length() > 8)
                        ? () -> errorParser.getExceptionVariableLength(lexicalAnalysis.getCurrentLineNumber())
                        :
                        isVariableIsDeclared()
                                ? //si deja declared
                                () -> errorParser.getExceptionVariableAlreadyDeclared(lexicalAnalysis.getCurrentLineNumber())
                                :
                                () -> null;

        Exception exception = supplier.get();

        if (exception != null) throw exception;

        nextToken();

        return tokenVariable;
    }

    /**
     * declaration variable deux points
     */
    private void identifierVariableTwoPoint() throws Exception {
        Supplier<Exception> exceptionSupplierTwoPoint = (!lookahead.getTag().equals(Tag.TWO_POINTS))
                ?
                () -> errorParser.getExceptionVariableTwoPoint(lexicalAnalysis.getCurrentLineNumber())
                :
                () -> null;

        Exception exception = exceptionSupplierTwoPoint.get();

        if (exception != null) throw exception;

        nextToken();
    }

    /**
     * declaration variable type
     */
    private Token identifierVariableType() throws Exception {
        Token tokenType = lookahead;

        Supplier<Exception> exceptionSupplier = (!lookahead.getTag().equals(Tag.ENTIER) && !lookahead.getTag().equals(Tag.REEL))
                ?
                () -> errorParser.getExceptionVariableType(lexicalAnalysis.getCurrentLineNumber())
                :
                () -> null;

        Exception exception = exceptionSupplier.get();

        if (exception != null) throw exception;

        nextToken();

        return tokenType;
    }

    /**
     * declaration variable point virgule
     */
    private void identifierVariableSemicolon() throws Exception {
        Supplier<Exception> exceptionSupplier = (!lookahead.getTag().equals(Tag.SEMICOLON))
                ?
                () -> errorParser.getExceptionVariableSemicolon(lexicalAnalysis.getCurrentLineNumber())
                :
                () -> null;

        Exception exception = exceptionSupplier.get();

        if (exception != null) throw exception;

        nextToken();
    }

    /**
     * verifie si la variable du lookahead est declare
     */
    private boolean isVariableIsDeclared() {
        Token tokenVariableFromSymbolTable = symbolTable.getToken(lookahead.getWord());
        return !tokenVariableFromSymbolTable.getType().equals(Type.INCONNU);
    }

    /**
     * set le type de la variable en fonction d'un token avec le tag type
     */
    private Function<Token, Function<Token, Runnable>> setTypeVariable =
            tokenVariableToSet -> tokenTypeVariable -> () -> {
                Supplier<Type> getType = (tokenTypeVariable.getTag().equals(Tag.ENTIER))
                        ?
                        () -> Type.ENTIER
                        :
                        () -> Type.REEL;

                tokenVariableToSet.setType(getType.get());
            };

    /**
     * si aucune variable declare, il est impossible de faire des affectations
     */
    private void throwExceptionIfNoVariableDelared() throws Exception {
        Supplier<Exception> supplierException = !hasAlreadyDeclaredVariable
                ?
                () -> errorParser.getExceptionNoVariableDecalred(lexicalAnalysis.getCurrentLineNumber())
                :
                () -> null;

        Exception exception = supplierException.get();

        if (exception != null) throw exception;
    }


}
