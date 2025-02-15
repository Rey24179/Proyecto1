;; Programa para la Fase 1 - Intérprete de LISP en Java
;; Universidad del Valle de Guatemala
;; Curso: CC2016 - Algoritmos y Estructura de Datos

;; Conversión de Fahrenheit a Celsius
(defun fahrenheit-a-celsius (f)
  (/ (* (- f 32) 5) 9))

;; Cálculo de la serie de Fibonacci
(defun fibonacci (n)
  (if (< n 2)
      n
      (+ (fibonacci (- n 1)) (fibonacci (- n 2)))))

;; Cálculo del factorial de un número
(defun factorial (n)
  (if (= n 0)
      1
      (* n (factorial (- n 1)))))

;; Pruebas de las funciones
(format t "Fahrenheit a Celsius (100°F): ~A~%" (fahrenheit-a-celsius 100))
(format t "Fibonacci (10): ~A~%" (fibonacci 10))
(format t "Factorial (5): ~A~%" (factorial 5))