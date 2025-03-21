import java.util.*;

public class LispInterpreter {
    // Mapa para almacenar variables definidas en el contexto LISP
    private static final Map<String, Object> context = new HashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Bienvenido al intérprete de LISP. Escribe 'exit' para salir.");
        System.out.println("Funciones soportadas: +, -, *, /, SETQ, CONVERTIR, FACTORIAL, FIBONACCI, MAX, MIN, PROMEDIO, CAR, CDR, CONS");
        while (true) {
            System.out.print("lisp> ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("exit")) break;
            System.out.println("Resultado: " + evaluar(eliminarComentarios(input)));
        }
        scanner.close();
    }

    // Método para evaluar una expresión en LISP
    public static Object evaluar(String expresion) {
        try {
            expresion = expresion.trim();
            if (expresion.startsWith("(") && expresion.endsWith(")")) {
                // Tokeniza la expresión eliminando los paréntesis externos
                List<String> tokens = tokenizar(expresion.substring(1, expresion.length() - 1));
                return procesarExpresion(tokens);
            }
            return expresion;
        } catch (NumberFormatException e) {
            return "Error: Formato de número inválido";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
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
                    return (celsius * 9 / 5) + 32;
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
            case "MAX":
                return argumentos.stream()
                        .mapToInt(arg -> Integer.parseInt(evaluar(arg).toString()))
                        .max()
                        .orElseThrow(() -> new IllegalArgumentException("Error: Lista vacía"));
            case "MIN":
                return argumentos.stream()
                        .mapToInt(arg -> Integer.parseInt(evaluar(arg).toString()))
                        .min()
                        .orElseThrow(() -> new IllegalArgumentException("Error: Lista vacía"));
            case "PROMEDIO":
                return argumentos.stream()
                        .mapToInt(arg -> Integer.parseInt(evaluar(arg).toString()))
                        .average()
                        .orElseThrow(() -> new IllegalArgumentException("Error: Lista vacía"));
            case "CAR": // Devuelve el primer elemento de una lista
                if (argumentos.size() == 1) {
                    List<Object> lista = (List<Object>) evaluar(argumentos.get(0));
                    return lista.isEmpty() ? "Error: Lista vacía" : lista.get(0);
                }
                return "Error: Número incorrecto de argumentos para CAR";
            case "CDR": // Devuelve la lista sin el primer elemento
                if (argumentos.size() == 1) {
                    List<Object> lista = (List<Object>) evaluar(argumentos.get(0));
                    return lista.isEmpty() ? "Error: Lista vacía" : lista.subList(1, lista.size());
                }
                return "Error: Número incorrecto de argumentos para CDR";
            case "CONS": // Agrega un elemento al inicio de una lista
                if (argumentos.size() == 2) {
                    Object elemento = evaluar(argumentos.get(0));
                    List<Object> lista = new ArrayList<>((List<Object>) evaluar(argumentos.get(1)));
                    lista.add(0, elemento);
                    return lista;
                }
                return "Error: Número incorrecto de argumentos para CONS";
            default: return "Error: Operador no reconocido";
        }
    }

    // Método auxiliar para realizar operaciones matemáticas
    private static int operar(List<String> args, Operacion op) {
        int resultado = Integer.parseInt(evaluar(args.get(0)).toString());
        for (int i = 1; i < args.size(); i++) {
            resultado = op.ejecutar(resultado, Integer.parseInt(evaluar(args.get(i)).toString()));
        }
        return resultado;
    }

    // Método recursivo para calcular el factorial
    private static int factorial(int n) {
        if (n == 0) return 1;
        return n * factorial(n - 1);
    }

    // Método optimizado para calcular el término n de la serie de Fibonacci
    private static int fibonacci(int n) {
        if (n <= 1) return n;
        int[] fib = new int[n + 1];
        fib[0] = 0;
        fib[1] = 1;
        for (int i = 2; i <= n; i++) {
            fib[i] = fib[i - 1] + fib[i - 2];
        }
        return fib[n];
    }

    // Método para eliminar comentarios de una expresión
    private static String eliminarComentarios(String expresion) {
        return expresion.replaceAll(";.*", "").trim();
    }

    // Interfaz funcional para definir operaciones matemáticas
    @FunctionalInterface
    interface Operacion {
        int ejecutar(int a, int b);
    }
}