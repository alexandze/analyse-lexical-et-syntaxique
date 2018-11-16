package syntaxAnalysis;

import symboleTable.Token;

public class ErrorParser {

    /***********************************procedure ***********************************************************************/
    public Exception getExceptionProcedure(int lineError) {
        return new Exception(String.format("Erreur a la ligne %d : une procedure doit commencer par le mot Procedure", lineError));
    }

    /********************************* identifier procedure ************************************************************/
    public Exception getExceptionIdentifierProcedure(int lineError) {
        return new Exception(String.format("Erreur a la ligne %d : le nom d'une procedure doit etre un identificateur (8 lettre maximum)", lineError));
    }

    public Exception getExceptionIdentifierProcedureLength(int lineError) {
        return new Exception(String.format("Erreur a la ligne %d : un identificateur de procedure doit avoir une taille maximum de 8 caracteres (lettre et chiffre)", lineError));
    }

    /************************************************ variable ***********************************************/
    public Exception getExceptionVariable(int lineError) {
        return new Exception(String.format("Erreur a la ligne %d : une variable est compose de caracteres et de chiffre commancant par un caracteres", lineError));
    }

    public Exception getExceptionVariableLength(int lineError) {
        return new Exception(String.format("Erreur a la ligne %d : une variable doit avoir 8 lettre maximum", lineError));
    }

    public Exception getExceptionVariableAlreadyDeclared(int lineError) {
        return new Exception(String.format("Erreur a la ligne %d : Impossible de declarer une variable qui est deja declare", lineError));
    }

    public Exception getExceptionVariableTwoPoint(int lineError) {
        return new Exception(String.format("Erreur a la ligne %d : une declation doit avoir les deux point ( : ) pour identifier le type", lineError));
    }

    public Exception getExceptionVariableType(int lineError) {
        return new Exception(String.format("Erreur a la ligne %d : une variable doit etre du type entier ou reel", lineError));
    }

    public Exception getExceptionVariableSemicolon(int lineError) {
        return new Exception(String.format("Erreur a la ligne %d : une declaration doit toujours se terminer par un point virgule", lineError));
    }

    public Exception getExceptionNoVariableDecalred(int lineError) {
        return new Exception(String.format("Erreur a la ligne %d : erreur de declaration, vous devez declarer correctement des variables avant de pouvoir faire des instructions affectations", lineError));
    }

    public Exception getExceptionAssignmentStatementSemicolon(int lineError) {
        return new Exception(String.format("Erreur a la ligne %d : mettre un point virgule a la fin de l'instruction precedente", lineError));
    }

    public Exception getExceptionAssignmentStatementVariable(int lineError) {
        return new Exception(String.format("Erreur a la ligne %d : on doit avoir une variable, une variable est compose de caracteres et de chiffre commancant par un caracteres", lineError));
    }

    public Exception getExceptionAssignmentStatementVariableNotDeclared(int lineError, Token tokenVariable) {
        return new Exception(String.format("Erreur a la ligne %d : vous n'avez pas declare la variable %s", lineError, tokenVariable.getWord()));
    }

    public Exception getExceptionAssignmentStatementEqualitySign(int lineError) {
        return new Exception(String.format("Erreur a la ligne %d : une instruction d'affectation doit avoir un signe egale", lineError));
    }

    public Exception getExceptionClosingParenthesis(int lineError) {
        return new Exception(String.format("Erreur a la ligne %d : vous avez oubliee de re fermer une parenthese", lineError));
    }

    public Exception getExceptionFinProcedure(int lineError) {
        return new Exception(String.format("Erreur a la ligne %d : une fin de procedure est defini : Fin_Procedure <identificateur>, l'identificateur doit etre le meme que celui du debut", lineError));
    }

    public Exception getExceptionArithmeticSign(int lineError) {
         return new Exception(String.format("Erreur a la ligne %d : expression arithmetique incorrect, impossible d'avoir plusieur signe arithmetique consecutif", lineError));
    }

    public Exception getExceptionArithmeticId(int lineError) {
        return new Exception(String.format("Erreur a la ligne %d : expression arithmetique incorrect, impossible d'avoir plusieurs variable consecutif", lineError));
    }

    public Exception getExceptionArithmeticNum(int lineError) {
        return new Exception(String.format("Erreur a la ligne %d : expression arithmetique incorrect, impossible d'avoir plusieurs numeros consecutif", lineError));
    }

}
