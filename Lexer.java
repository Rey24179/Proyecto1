import java.util.ArrayList;
import java.util.List;

public class Lexer {
    public static List<String> tokenize(String input) {
        List<String> tokens = new ArrayList<>();
        StringBuilder token = new StringBuilder();
        int level = 0;

        for (char c : input.toCharArray()) {
            if (c == '(') {
                if (token.length() > 0) {
                    tokens.add(token.toString());
                    token.setLength(0);
                }
                tokens.add("(");
                level++;
            } else if (c == ')') {
                if (token.length() > 0) {
                    tokens.add(token.toString());
                    token.setLength(0);
                }
                tokens.add(")");
                level--;
            } else if (Character.isWhitespace(c)) {
                if (token.length() > 0) {
                    tokens.add(token.toString());
                    token.setLength(0);
                }
            } else {
                token.append(c);
            }
        }

        if (token.length() > 0) {
            tokens.add(token.toString());
        }

        if (level != 0) {
            throw new IllegalArgumentException("Error: Paréntesis desbalanceados");
        }

        return tokens;
    }
}
