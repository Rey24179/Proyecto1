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

     // Método para dividir una expresión en tokens
     private static List<String> tokenizar(String expresion) {
        List<String> tokens = new ArrayList<>();
        int nivel = 0;
        StringBuilder token = new StringBuilder();
        for (char c : expresion.toCharArray()) {
            if (c == '(') nivel++;
            if (c == ')') nivel--;
            if (c == ' ' && nivel == 0) {
                if (token.length() > 0) {
                    tokens.add(token.toString());
                    token.setLength(0);
                }
            } else {
                token.append(c);
            }
        }
        if (token.length() > 0) tokens.add(token.toString());
        return tokens;
    }

    // Método que procesa una lista de tokens y ejecuta la operación correspondiente
    private static Object procesarExpresion(List<String> tokens) {
        if (tokens.isEmpty()) return null;
        String operador = tokens.get(0);
        List<String> argumentos = tokens.subList(1, tokens.size());
        switch (operador) {
            case "+": return operar(argumentos, (a, b) -> a + b);
            case "-": return operar(argumentos, (a, b) -> a - b);
            case "*": return operar(argumentos, (a, b) -> a * b);
            case "/": return operar(argumentos, (a, b) -> a / b);
            case "SETQ": // Definir una variable en el contexto
                if (argumentos.size() == 2) context.put(argumentos.get(0), evaluar(argumentos.get(1)));
                return context.get(argumentos.get(0));
            case "CONVERTIR": // Conversión de Celsius a Fahrenheit
                if (argumentos.size() == 1) {
                    double celsius = Double.parseDouble(evaluar(argumentos.get(0)).toString());
                    return (celsius * 9/5) + 32;
                }
                return "Error: Número incorrecto de argumentos para CONVERTIR";
            case "FACTORIAL": // Cálculo del factorial recursivo
                if (argumentos.size() == 1) {
                    int n = Integer.parseInt(evaluar(argumentos.get(0)).toString());
                    return factorial(n);
                }
                return "Error: Número incorrecto de argumentos para FACTORIAL";
            case "FIBONACCI": // Cálculo del n-ésimo término de Fibonacci
                if (argumentos.size() == 1) {
                    int n = Integer.parseInt(evaluar(argumentos.get(0)).toString());
                    return fibonacci(n);
                }
                return "Error: Número incorrecto de argumentos para FIBONACCI";
            default: return "Error: Operador no reconocido";
        }
    }

   