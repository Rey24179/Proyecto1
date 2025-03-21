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
            if (input.equalsIgnoreCase("exit")) break;
            System.out.println("Resultado: " + evaluar(input));
        }
        scanner.close();
    }

    // Método para evaluar una expresión en LISP
    public static Object evaluar(String expresion) {
        try {
            // Eliminar comentarios y espacios innecesarios
            expresion = eliminarComentarios(expresion).trim();

            // Tokenizar la expresión usando el Lexer
            List<String> tokens = Lexer.tokenize(expresion);

            // Parsear los tokens en una estructura de árbol
            Object parsedExpression = Parser.parse(tokens);

            // Procesar la estructura de árbol para obtener el resultado
            return procesar(parsedExpression);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    // Método para procesar la estructura de árbol generada por el parser
    private static Object procesar(Object expresion) {
        if (expresion instanceof String) {
            // Si es un número o variable, devolverlo directamente
            return expresion;
        } else if (expresion instanceof List) {
            // Si es una lista, procesar la operación
            List<?> lista = (List<?>) expresion;
            if (lista.isEmpty()) {
                return "Error: Expresión vacía";
            }
            String operador = lista.get(0).toString();
            List<String> argumentos = new ArrayList<>();
            for (int i = 1; i < lista.size(); i++) {
                argumentos.add(lista.get(i).toString());
            }
            return procesarExpresion(operador, argumentos);
        }
        return "Error: Expresión no válida";
    }

    // Método que procesa una lista de tokens y ejecuta la operación correspondiente
    private static Object procesarExpresion(String operador, List<String> argumentos) {
        switch (operador) {
            case "+": return operar(argumentos, (a, b) -> a + b);
            case "-": return operar(argumentos, (a, b) -> a - b);
            case "*": return operar(argumentos, (a, b) -> a * b);
            case "/": return operar(argumentos, (a, b) -> a / b);
            case "SETQ": // Definir una variable en el contexto
                if (argumentos.size() == 2) {
                    context.put(argumentos.get(0), evaluar(argumentos.get(1)));
                    return context.get(argumentos.get(0));
                }
                return "Error: Número incorrecto de argumentos para SETQ";
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
            default:
                return "Error: Operador no reconocido: " + operador;
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