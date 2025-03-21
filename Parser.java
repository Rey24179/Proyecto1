import java.util.ArrayList;
import java.util.List;

public class Parser {
    public static Object parse(List<String> tokens) {
        if (tokens.isEmpty()) {
            throw new IllegalArgumentException("Error: Expresión vacía");
        }

        String token = tokens.remove(0);

        if (token.equals("(")) {
            List<Object> list = new ArrayList<>();
            while (!tokens.get(0).equals(")")) {
                list.add(parse(tokens));
            }
            tokens.remove(0); // Eliminar el paréntesis de cierre
            return list;
        } else if (token.equals(")")) {
            throw new IllegalArgumentException("Error: Paréntesis desbalanceados");
        } else {
            return token; // Devuelve el token como un número, variable o símbolo
        }
    }
}
