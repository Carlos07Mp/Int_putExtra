package com.calculator.service;

import com.calculator.model.Variable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorServiceTest {

    private CalculatorService calculatorService;

    @BeforeEach
    void setUp() {
        calculatorService = new CalculatorService();
        // Si es necesario, se pueden inicializar mocks con MockitoAnnotations.openMocks(this);
    }

    @Test
    void testOperandStackAfterEvaluation() {
        String expression = "5+3";  // Expresión simple para probar

        // Ejecutar la evaluación
        calculatorService.evaluateExpression(expression);

        // Verifica que la pila de operandos esté vacía después de la evaluación
        assertTrue(calculatorService.getOperandStack().isEmpty(), "La pila de operandos no debería estar vacía después de la evaluación.");
    }

    @Test
    void testEvaluateExpressionWithVariables() {
        calculatorService.addVariable(new Variable("x", 4));  // Añadir variable
        String expression = "x*4";  // Usar la variable x

        // Ejecutar la evaluación
        Integer result = calculatorService.evaluateExpression(expression);

        // Verifica que el resultado sea correcto
        assertEquals(16, result, "La evaluación de x*4 donde x=4 debería ser 16.");
    }

    @Test
    void testEvaluateExpressionWithDivisionByZero() {
        String expression = "5/0";  // Expresión con error de división por cero

        // Ejecutar la evaluación y verificar la excepción
        ArithmeticException thrown = assertThrows(ArithmeticException.class, () -> {
            calculatorService.evaluateExpression(expression);
        });

        assertEquals("Division by zero", thrown.getMessage(), "Se esperaba una excepción por división por cero.");
    }
}
