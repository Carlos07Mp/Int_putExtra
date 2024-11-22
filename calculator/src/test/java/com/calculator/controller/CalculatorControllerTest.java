package com.calculator.controller;

import com.calculator.service.CalculatorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CalculatorControllerTest {

    @Mock
    private CalculatorService calculatorService;  // Mock del servicio

    @InjectMocks
    private CalculatorController calculatorController;  // Controlador con los mocks inyectados

    @BeforeEach
    void setUp() {
        // Inicializa los mocks
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testEvaluateExpression() {
        String expression = "5+3";  // Expresión simple

        // Configuración del mock para que devuelva 8 al evaluar la expresión
        when(calculatorService.evaluateExpression(expression)).thenReturn(8);

        // Llamada al controlador
        ResponseEntity<?> response = calculatorController.evaluateExpression(Map.of("expression", expression));

        // Verificaciones
        assertEquals(200, response.getStatusCodeValue(), "El código de estado debe ser 200");
        assertTrue(response.getBody().toString().contains("8"), "El cuerpo de la respuesta debe contener el resultado de la operación.");
    }

    @Test
    void testEvaluateExpressionWithError() {
        String expression = "5/0";  // Expresión con error de división por cero

        // Configuración del mock para lanzar una excepción en caso de error
        when(calculatorService.evaluateExpression(expression)).thenThrow(new ArithmeticException("Division by zero"));

        // Llamada al controlador
        ResponseEntity<?> response = calculatorController.evaluateExpression(Map.of("expression", expression));

        // Verificaciones
        assertEquals(400, response.getStatusCodeValue(), "El código de estado debe ser 400 en caso de error.");
        assertTrue(response.getBody().toString().contains("Division by zero"), "El mensaje de error debe estar presente.");
    }
}
