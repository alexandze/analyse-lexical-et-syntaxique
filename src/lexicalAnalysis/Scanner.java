package lexicalAnalysis;

import java.io.*;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class Scanner {
    private BufferedReader bufferedReader;
    private File file;
    private String currentLineSentence;
    private int cursorCurrentLine;
    private int currentLineNumber;
    private boolean endOfLine;
    private boolean endOfFile;

    public Scanner() {
        file = new File("myFile.txt");
        currentLineSentence = "";
        currentLineNumber = 0;
        cursorCurrentLine = 0;

        try {
            System.out.println();
            bufferedReader = new BufferedReader(new FileReader(file));
            endOfFile = false;
            endOfLine = false;
            readLine();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * lire une ligne dans le fichier txt
     */
    public String readLine() {
        UtilsReadLine utilsReadLine = _readLineFunc
                .apply(bufferedReader)
                .apply(currentLineNumber)
                .get();

        return Optional.ofNullable(utilsReadLine)
                .map(utilsReadLine1 -> {
                    String lineRead = null;
                    endOfFile = false;
                    cursorCurrentLine = 0;
                    currentLineSentence = utilsReadLine1.lineRead;
                    endOfLine = currentLineSentence.length() == 0;
                    currentLineNumber = utilsReadLine1.numberLineRead;
                    lineRead = utilsReadLine1.lineRead;

                    if (endOfLine) {
                        lineRead = readLine();
                    }

                    return lineRead;
                })
                .orElseGet(() -> {
                    currentLineSentence = null;
                    endOfFile = true;
                    endOfLine = true;
                    return null;
                });

    }

    /**
     * lire un mots commencant par une lettre (lire identifiant ou keyWord)
     */
    private String _readLetterWord(Character firstCharacter) {
        StringBuilder wordBuilder = new StringBuilder();

        wordBuilder.append(firstCharacter);
        Character characterRead = null;

        for (; cursorCurrentLine < currentLineSentence.length(); cursorCurrentLine++) {
            characterRead = currentLineSentence.charAt(cursorCurrentLine);

            if (Character.isLetter(characterRead) || Character.isDigit(characterRead) || characterRead == '_') {
                wordBuilder.append(characterRead);
            } else {
                break;
            }
        }

        return wordBuilder.toString();
    }

    /**
     * lire un mots commencant par un chiffre (lire digit)
     */
    private String _readNumberWord(Character firstCharacter) throws Exception {
        StringBuilder digitBuilder = new StringBuilder();
        digitBuilder.append(firstCharacter);
        Character digitRead = null;
        boolean isAlreadyReadPoint = false;

        for (; cursorCurrentLine < currentLineSentence.length(); cursorCurrentLine++) {
            digitRead = currentLineSentence.charAt(cursorCurrentLine);

            if (Character.isDigit(digitRead) || digitRead == '.') {
                if (digitRead == '.') {
                    if (isAlreadyReadPoint) {
                        throw new Exception(String.format("Erreur a la ligne %d, un digit ne peut pas avoir 2 points", currentLineNumber));
                    } else {
                        isAlreadyReadPoint = true;
                    }
                }

                digitBuilder.append(digitRead);
            } else if (digitRead == ' ' || digitRead == ';' || digitRead == '(' || digitRead == ')' || digitRead == '+' || digitRead == '-' || digitRead == '/' || digitRead == '*') {
                break;
            } else {
                throw new Exception(String.format("Erreur a la ligne %d, un digit doit etre composee de chiffre seulement", currentLineNumber));
            }
        }

        return digitBuilder.toString();
    }

    /**
     * Lire un mots de la ligne courante
     */
    public String readWordCurrentLine() throws Exception {
        String wordRead = null;

        Character characterRead = _readCharacter();

        if (characterRead == null) {
            endOfLine = true;
            return null;
        }

        if (Character.isDigit(characterRead)) {
            wordRead = _readNumberWord(characterRead);

        } else if (Character.isLetter(characterRead)) {
            wordRead = _readLetterWord(characterRead);
        } else {
            wordRead = characterRead.toString();
        }

        if (cursorCurrentLine == currentLineSentence.length())
            endOfLine = true;

        return wordRead;
    }

    /**
     * lit un character sur la ligne courrant.
     *
     */
    private Character _readCharacter() {
        UtilsReadChar utilsReadChar = null;

        do {
            if (currentLineSentence != null && cursorCurrentLine < currentLineSentence.length()) {
                utilsReadChar = _readCharFunc
                        .apply(currentLineSentence)
                        .apply(cursorCurrentLine)
                        .get();

                cursorCurrentLine = utilsReadChar.cursorPositionCurrentLine;
            }
        }
        while (utilsReadChar != null && utilsReadChar.characterRead == ' ' && cursorCurrentLine < currentLineSentence.length());

        return (utilsReadChar == null) ? null : (utilsReadChar.characterRead == ' ') ? null : utilsReadChar.characterRead;
    }

    /**
     * lire un character
     */
    private Function<String, Function<Integer, Supplier<UtilsReadChar>>> _readCharFunc =
            lineRead -> cursorLinePosition -> () -> {
                char characterRead = lineRead.charAt(cursorLinePosition);
                UtilsReadChar utilsReadChar = new UtilsReadChar();
                utilsReadChar.characterRead = characterRead;
                utilsReadChar.cursorPositionCurrentLine = cursorCurrentLine + 1;
                return utilsReadChar;
            };


    public String getCurrentLineSentence() {
        return currentLineSentence;
    }

    public boolean isEndOfFile() {
        return endOfFile;
    }

    public boolean isEndOfLine() {
        return endOfLine;
    }

    /**
     * recupere le numero de la ligne
     */
    public int getCurrentLineNumber() {
        return currentLineNumber;
    }

    /**
     * lire une ligne
     */
    private Function<BufferedReader, Function<Integer, Supplier<UtilsReadLine>>> _readLineFunc =
            bufferedReader1 -> nbLineRead -> () -> {
                UtilsReadLine utilsReadLine = null;

                try {
                    utilsReadLine = Optional.ofNullable(bufferedReader1.readLine())
                            .map(lineRead1 -> {
                                UtilsReadLine utilsReadLine1 = new UtilsReadLine();
                                utilsReadLine1.lineRead = lineRead1.trim();
                                utilsReadLine1.numberLineRead = nbLineRead + 1;
                                return utilsReadLine1;
                            })
                            .orElse(null);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                return utilsReadLine;
            };

    private class UtilsReadLine {
        public int numberLineRead;
        public String lineRead;
    }

    private class UtilsReadChar {
        public int cursorPositionCurrentLine;
        public char characterRead;
    }
}
