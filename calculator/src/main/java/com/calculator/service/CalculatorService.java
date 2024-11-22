package com.calculator.service;

import com.calculator.model.Variable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

@Service
public class CalculatorService {

    private Map<String, Integer> variables = new HashMap<>();
    private Stack<Integer> operandStack = new Stack<>();
    private Stack<Character> operatorStack = new Stack<>();

    public void addVariable(Variable variable) {
        if (!isValidVariableName(variable.getName())) {
            throw new IllegalArgumentException("Invalid variable name");
        }
        variables.put(variable.getName(), variable.getValue());
    }

    private boolean isValidVariableName(String name) {
        return name.matches("[a-zA-Z][a-zA-Z0-9]{0,11}") && name.length() <= 12;
    }

    public List<Variable> getVariables() {
        List<Variable> varList = new ArrayList<>();
        variables.forEach((name, value) -> varList.add(new Variable(name, value)));
        return varList;
    }

    public Integer evaluateExpression(String expression) {
        // Limpia las pilas antes de procesar la expresión
        operandStack.clear();
        operatorStack.clear();

        StringBuilder currentNumber = new StringBuilder();
        StringBuilder currentVariable = new StringBuilder();

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            if (Character.isDigit(c)) {
                currentNumber.append(c);
            } else if (Character.isLetter(c)) {
                currentVariable.append(c);
            } else {
                if (currentNumber.length() > 0) {
                    operandStack.push(Integer.parseInt(currentNumber.toString()));
                    currentNumber.setLength(0);
                }
                if (currentVariable.length() > 0) {
                    String varName = currentVariable.toString();
                    if (!variables.containsKey(varName)) {
                        throw new IllegalArgumentException("Variable not found: " + varName);
                    }
                    operandStack.push(variables.get(varName));
                    currentVariable.setLength(0);
                }

                if (c != ' ') {
                    processOperator(c);
                }
            }
        }

        if (currentNumber.length() > 0) {
            operandStack.push(Integer.parseInt(currentNumber.toString()));
        }
        if (currentVariable.length() > 0) {
            String varName = currentVariable.toString();
            if (!variables.containsKey(varName)) {
                throw new IllegalArgumentException("Variable not found: " + varName);
            }
            operandStack.push(variables.get(varName));
        }

        // Procesar la operación final
        while (!operatorStack.isEmpty()) {
            processOperation();
        }

        // Asegúrate de que la pila de operandos se vacíe correctamente
        if (!operandStack.isEmpty()) {
            System.out.println("Pila de operandos no vacía: " + operandStack);
        }

        return operandStack.isEmpty() ? 0 : operandStack.pop();
    }

    private void processOperator(char operator) {
        if (operator == '(') {
            operatorStack.push(operator);
        } else if (operator == ')') {
            while (!operatorStack.isEmpty() && operatorStack.peek() != '(') {
                processOperation();
            }
            if (!operatorStack.isEmpty()) {
                operatorStack.pop();
            }
        } else {
            while (!operatorStack.isEmpty() && precedence(operatorStack.peek()) >= precedence(operator)) {
                processOperation();
            }
            operatorStack.push(operator);
        }
    }

    private void processOperation() {
        if (operandStack.size() < 2) return;

        int b = operandStack.pop();
        int a = operandStack.pop();
        char operator = operatorStack.pop();

        switch (operator) {
            case '+':
                operandStack.push(a + b);
                break;
            case '-':
                operandStack.push(a - b);
                break;
            case '*':
                operandStack.push(a * b);
                break;
            case '/':
                if (b == 0) throw new ArithmeticException("Division by zero");
                operandStack.push(a / b);
                break;
        }
    }

    private int precedence(char operator) {
        switch (operator) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            default:
                return 0;
        }
    }

    public Stack<Integer> getOperandStack() {
        return operandStack;
    }

    public Stack<Character> getOperatorStack() {
        return operatorStack;
    }
}
