import lexicalAnalysis.LexicalAnalysis;
import lexicalAnalysis.Scanner;
import symboleTable.SymbolTable;
import syntaxAnalysis.Parser;

public class Main {

    public static void main(String[] args) throws Exception {

        System.out.println("**** Alexandre Andze Kande, Raphaël Étang-Salé, Guillaume Maka ****\n");


        SymbolTable symbolTable = new SymbolTable();
        Scanner scanner = new Scanner();
        LexicalAnalysis lexicalAnalysis = new LexicalAnalysis(scanner, symbolTable);
        Parser parser = new Parser(symbolTable, lexicalAnalysis);
        parser.startParser();
    }
}
