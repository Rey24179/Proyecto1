import java.util.*;

public class LispInterpreter {
    // Mapa para almacenar variables definidas en el contexto LISP
    private static final Map<String, Object> context = new HashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Bienvenido al intérprete de LISP. Escribe 'exit' para salir.");
        while (true) {
            System.out.print("lisp> ");
            String input = scanner.nextLine();
            if (input.equals("exit")) break;
            System.out.println("Resultado: " + evaluar(input));
        }
        scanner.close();
    }

    // Método para evaluar una expresión en LISP
    public static Object evaluar(String expresion) {
        expresion = expresion.trim();
        if (expresion.startsWith("(") && expresion.endsWith(")")) {
            // Tokeniza la expresión eliminando los paréntesis externos
            List<String> tokens = tokenizar(expresion.substring(1, expresion.length() - 1));
            return procesarExpresion(tokens);
        }
        return expresion;
    }

   