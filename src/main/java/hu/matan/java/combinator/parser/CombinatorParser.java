package hu.matan.java.combinator.parser;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class CombinatorParser {

    public static void main(String[] args) throws IOException {
        new CombinatorParser().tryParse();
    }

    private void tryParse() {
    }
}

class Parser implements Predicate<Reader> {

    private static final int EOS = -1;
    private Predicate<String> predicate;

    private char[] buffer = new char[11];

    Parser(String regex) {
        predicate = Pattern.compile(regex).asMatchPredicate();
    }

    static Predicate<Reader> par(String regex) {
        return new Parser(regex);
    }

    static boolean parse(Predicate<Reader> p, String s) throws IOException {
        StringReader reader = new StringReader(s);
        return p.test(reader) && reader.read() == EOS;
    }

    public boolean test(Reader reader) {
        try {
            var found = false;
            reader.mark(10);
            for (int i = 0; i < 10; i++) {
                int read = reader.read(buffer, i, 1);
                String s = new String(buffer, 0, i + 1);
                if (read == 1 && predicate.test(s)) {
                    reader.mark(10);
                    found = true;
                } else {
                    reader.reset();
                }
            }
            return found;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}