import components.map.Map;
import components.map.Map1L;
import components.sequence.Sequence;
import components.sequence.Sequence1L;
import components.set.Set;
import components.set.Set1L;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 * Count and input the number of word occurrences from a given input file into a
 * given output HTML file. These word occurrence will be shown on a table in the
 * HTML file.
 *
 * @author Jordyn Liegl
 */
public final class WordCounter {

    /**
     * Default constructor--private to prevent instantiation.
     */
    private WordCounter() {
        //No code needed here.
    }

    /**
     * Generates the set of characters in the string of separators into the set
     * of separators as characters.
     *
     * @param separatorStr
     *            the string consisting of separators
     * @param charSeparatorSet
     *            the set that consists of the separators as characters
     * @replaces charSeparatorSet
     * @restores separatorStr
     * @ensures charSeparatorSet = entries(separatorStr)
     */
    public static void generateSeparators(String separatorStr,
            Set<Character> charSeparatorSet) {
        assert separatorStr != null : "Violation of: str is not null";
        assert charSeparatorSet != null : "Violation of: charSet is not null";

        /*
         * Creates a set of values that are considered separators from the
         * entered string.
         */
        for (int i = 0; i < separatorStr.length(); i++) {
            char j = separatorStr.charAt(i);
            if (!charSeparatorSet.contains(j)) {
                charSeparatorSet.add(j);
            }
        }

    }

    /**
     * @param in
     *            the name of the input file
     * @param words
     *            the sequence of the words
     * @param wordsAndCounts
     *            the map of the words and their respective counts
     * @param separators
     *            the set of characters that separate words in a sentence
     * @ensures wordsAndCounts' keys will be the words from the input file,
     *          wordsAndCounts' values will be the word's count from input file,
     *          and words' elements are the same as the wordsAndCounts' keys
     * @replaces words and wordsAndCounts
     * @restores in and separators
     */
    public static void createMapAndSequence(String in, Sequence<String> words,
            Map<String, Integer> wordsAndCounts, Set<Character> separators) {
        assert in != null : "Violation of: text is not null";
        assert separators != null : "Violation of: separators is not null";

        SimpleReader inputReader = new SimpleReader1L(in);

        /*
         * Loop until the end of the text file.
         */
        while (!inputReader.atEOS()) {

            /*
             * Create a string that belongs to each line of the input file.
             */
            String line = inputReader.nextLine();

            /*
             * Loop through each line of the input file to separate the words to
             * put into the sequence and map.
             */
            int i = 0;
            int j = 0;
            while (i < line.length()) {

                /*
                 * Call nextWordOrSeparator to separate the words.
                 */
                String wordInLine = nextWordOrSeparator(line, i, separators)
                        .toLowerCase();

                /*
                 * Check to see if the sequence or map has already have the
                 * word.
                 */
                char checkSeparator = wordInLine.charAt(0);
                if (!wordsAndCounts.hasKey(wordInLine)
                        && !separators.contains(checkSeparator)) {
                    wordsAndCounts.add(wordInLine, 1);
                    words.add(j, wordInLine);
                    j++;
                } else {
                    if (!separators.contains(checkSeparator)) {
                        int count = wordsAndCounts.value(wordInLine);
                        count++;
                        wordsAndCounts.remove(wordInLine);
                        wordsAndCounts.add(wordInLine, count);
                    }
                }

                /*
                 * Increase the position to the end of each word in the
                 * definition.
                 */
                i += wordInLine.length();
            }
        }

        /*
         * Close the SimpleReader.
         */
        inputReader.close();
    }

    /**
     * Returns the first "word" (maximal length string of characters not in the
     * set of separators) or "separator string" (maximal length string of
     * characters in the set of separators) in the string definition starting at
     * the int position.
     *
     * @param line
     *            the string from which to get the word or separator string
     * @param position
     *            the starting index
     * @param separators
     *            the set of separator characters
     * @return the first word or separator string found in definition starting
     *         at index position
     * @requires 0 <= position < |text|
     * @ensures <pre>
     * nextWordOrSeparator =
     *   definition[position, position + |nextWordOrSeparator|)  and
     * if entries(definition[position, position + 1)) intersection separators = {}
     * then
     *   entries(nextWordOrSeparator) intersection separators = {}  and
     *   (position + |nextWordOrSeparator| = |definition|  or
     *    entries(definition[position, position + |nextWordOrSeparator| + 1))
     *      intersection separators /= {})
     * else
     *   entries(nextWordOrSeparator) is subset of separators  and
     *   (position + |nextWordOrSeparator| = |definition|  or
     *    entries(definition[position, position + |nextWordOrSeparator| + 1))
     *      is not subset of separators)
     * </pre>
     */
    public static String nextWordOrSeparator(String line, int position,
            Set<Character> separators) {
        assert line != null : "Violation of: text is not null";
        assert separators != null : "Violation of: separators is not null";
        assert 0 <= position : "Violation of: 0 <= position";
        assert position < line.length() : "Violation of: position < |text|";

        char ch1 = line.charAt(position);
        int i = position;
        String str = "";

        /*
         * Check if the character at the position is a separator character or a
         * character used for a word.
         */
        if (separators.contains(ch1)) {

            /*
             * Loop through the string until it is no longer a separator
             * character.
             */
            while (i < line.length() && separators.contains(line.charAt(i))) {
                ch1 = line.charAt(i);
                str = str + ch1;
                i++;
            }
        } else {

            /*
             * Loop through the string until it is a separator character.
             */
            while (i < line.length() && !separators.contains(line.charAt(i))) {
                ch1 = line.charAt(i);
                str = str + ch1;
                i++;
            }
        }
        return str;
    }

    /**
     * Updating the sequence to have the terms be in alphabetical order.
     *
     * @param words
     *            the sequence of words
     * @ensures words to be in alphabetical order
     * @updates words
     */
    public static void sequenceAlphabeticalOrder(Sequence<String> words) {
        /*
         * loop and compare two consecutive elements and switch if one comes
         * before the other alphabetically
         */
        for (int first = 0; first < words.length(); first++) {
            for (int second = first + 1; second < words.length(); second++) {
                if ((words.entry(first)).compareTo(words.entry(second)) > 0) {

                    String temp = words.entry(first);
                    words.replaceEntry(first, words.entry(second));
                    words.replaceEntry(second, temp);
                }
            }
        }
    }

    /**
     * Outputs the "opening" tags in the output HTML file.
     *
     * @param outputFile
     *            the output stream to given HTML file
     * @param inputFile
     *            the string name of the given input file
     * @updates outputFile.content
     * @requires outputFile.is_open
     * @ensures outputFile.content = #outputFile.content * [the HTML "opening"
     *          tags]
     * @restores inputFile
     */
    public static void outputHeader(SimpleWriter outputFile, String inputFile) {
        assert outputFile != null : "Violation of: out is not null";
        assert outputFile.isOpen() : "Violation of: out.is_open";

        /*
         * Print the header in HTML format, while creating a table.
         */
        outputFile.println("<html>");
        outputFile.println("<head>");
        outputFile.println("<title>Word Counter</title>");
        outputFile.println("</head>");
        outputFile.println("<h2>Words Counted in " + inputFile + "</h2>");
        outputFile.println("<body>");
        outputFile.println("    <hr>");
        outputFile.println("    <table border = \"2\">");
        outputFile.println("    <tr>");
        outputFile.println("    <td>Word</td>");
        outputFile.println("    <td>Count</td>");
        outputFile.println("    <tr>");

    }

    /**
     * Complete the table by adding the words in a TXT file and their respective
     * counts.
     *
     * @param outputFile
     *            the output stream to the given HTML
     * @param alphabeticalWordList
     *            the sequence of the words in alphabetical order
     * @param wordsAndCounts
     *            the map of the words and their respective counts
     * @param separators
     *            the set of characters that separate words in a sentence
     * @requires outputFile.is_open
     * @ensures outputFile.content = #outputFile.content * [links to the term's
     *          HTML pages]
     * @restores alphabeticalWordList, wordsAndCounts, and separators
     * @updates outputFile.contents
     */
    public static void outputBody(SimpleWriter outputFile,
            Sequence<String> alphabeticalWordList,
            Map<String, Integer> wordsAndCounts, Set<Character> separators) {
        assert outputFile != null : "Violation of: out is not null";
        assert outputFile.isOpen() : "Violation of: out.is_open";

        /*
         * Loop through the sequence's elements to input the words
         * alphabetically.
         */
        for (int i = 0; i < alphabeticalWordList.length(); i++) {

            outputFile.println("        <tr>");
            /*
             * Input the Word column through the sequence.
             */
            outputFile.println(
                    "        <td>" + alphabeticalWordList.entry(i) + "</td>");
            /*
             * Input the Count column through getting the word's value in the
             * map.
             */
            outputFile.println("        <td>"
                    + wordsAndCounts.value(alphabeticalWordList.entry(i))
                    + "</td>");
            outputFile.println("        </tr>");

        }

    }

    /**
     * Outputs the "closing" tags in the output HTML file.
     *
     * @param outputFile
     *            the output stream to the given output HTML
     * @updates outputFile.contents
     * @requires outputFile.is_open
     * @ensures outputFile.content = #outputFile.content * [the HTML "closing"
     *          tags]
     */
    public static void outputFooter(SimpleWriter outputFile) {
        assert outputFile != null : "Violation of: out is not null";
        assert outputFile.isOpen() : "Violation of: out.is_open";

        /*
         * Print the footer.
         */
        outputFile.println("    </table>");
        outputFile.println("</html>");

    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments; unused here
     */
    public static void main(String[] args) {
        //PROBS I NEED TO FIX
        //separating words that were originally capitalized from the ones that weren't
        /*
         * Initialize the SimpleReaders and SimpleWriter.
         */
        SimpleReader inputReader = new SimpleReader1L();
        SimpleReader outputReader = new SimpleReader1L();
        SimpleWriter out = new SimpleWriter1L();

        /*
         * Prompt the user to enter an input and output file.
         */
        out.println("Enter an input file.");
        String inputName = inputReader.nextLine();
        out.println("Enter an output file.");
        SimpleWriter outputFile = new SimpleWriter1L(outputReader.nextLine());

        /*
         * Create a set of separators for the nextWordOrSeparator method.
         */
        final String separatorStr = " \t, .-!_";
        Set<Character> separatorSet = new Set1L<>();
        generateSeparators(separatorStr, separatorSet);

        /*
         * Create a map and sequence to hold the words with their counts and
         * organize the words alphabetically. Call a method to fill the sequence
         * and map
         */
        Map<String, Integer> wordsAndCounts = new Map1L<>();
        Sequence<String> alphabeticalWordList = new Sequence1L<>();
        createMapAndSequence(inputName, alphabeticalWordList, wordsAndCounts,
                separatorSet);

        /*
         * Alphabetize the sequence to alphabetize the output file
         */
        sequenceAlphabeticalOrder(alphabeticalWordList);

        /*
         * Call the output header, body and footer methods to format the results
         * in an HTML.
         */
        outputHeader(outputFile, inputName);
        outputBody(outputFile, alphabeticalWordList, wordsAndCounts,
                separatorSet);
        outputFooter(outputFile);

        /*
         * Close the SimpleReaders and SimpleWriters.
         */
        out.close();
        inputReader.close();
        outputReader.close();
        outputFile.close();
    }

}
