package com.calculator.controller;

import com.calculator.model.Variable;
import com.calculator.service.CalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/calculator")
@CrossOrigin(origins = "*")
public class CalculatorController {

    @Autowired
    private CalculatorService calculatorService;

    @PostMapping("/variable")
    public ResponseEntity<String> addVariable(@RequestBody Variable variable) {
        try {
            calculatorService.addVariable(variable);
            return ResponseEntity.ok("Variable added successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/variables")
    public ResponseEntity<List<Variable>> getVariables() {
        return ResponseEntity.ok(calculatorService.getVariables());
    }

    @PostMapping("/evaluate")
    public ResponseEntity<?> evaluateExpression(@RequestBody Map<String, String> request) {
        try {
            String expression = request.get("expression");
            Integer result = calculatorService.evaluateExpression(expression);

            // Guardar y devolver las pilas junto con el resultado
            Map<String, Object> response = new HashMap<>();
            response.put("result", result);
            response.put("operandStack", calculatorService.getOperandStack());
            response.put("operatorStack", calculatorService.getOperatorStack());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

