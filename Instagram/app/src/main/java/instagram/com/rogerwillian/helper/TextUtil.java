package instagram.com.rogerwillian.helper;

import java.text.BreakIterator;

public class TextUtil {

    public static String capitalize(String sentence) {
        StringBuffer wordCapitalSentence = new StringBuffer();
        int startPosition = 0, nextWordPosition = 0;
        String word = null;
        while (startPosition < sentence.length()) {

            nextWordPosition = nextWordStartAfter(startPosition, sentence);

            // check to see if there is a next word
            if (nextWordPosition == BreakIterator.DONE) {
                nextWordPosition = sentence.length();
            }

            // get the next word
            word = sentence.substring(startPosition, nextWordPosition);

            // upper case first letter of word
            wordCapitalSentence.append(word.substring(0, 1).toUpperCase() + word.substring(1));

            // set start to the next word position
            startPosition = nextWordPosition;
        }

        return wordCapitalSentence.toString();
    }

    private static int nextWordStartAfter(int pos, String text) {
        BreakIterator wb = BreakIterator.getWordInstance();
        wb.setText(text);
        int last = wb.following(pos);
        int current = wb.next();
        while (current != BreakIterator.DONE) {
            for (int p = last; p < current; p++) {
                if (Character.isLetter(text.codePointAt(p)))
                    return last;
            }
            last = current;
            current = wb.next();
        }
        return BreakIterator.DONE;
    }
}
