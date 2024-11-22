let variables = [];
let currentExpression = '';
let lastResult = null;

// Cargar variables al iniciar
document.addEventListener('DOMContentLoaded', () => {
    loadVariables();
});

function loadVariables() {
    fetch('http://localhost:8080/api/calculator/variables')
        .then(response => response.json())
        .then(data => {
            variables = data;
            updateVariablesList();
        })
        .catch(error => showError('Error al cargar variables: ' + error.message));
}

function updateVariablesList() {
    const variablesList = document.getElementById('variablesList');
    variablesList.innerHTML = variables.map(v =>
        `<button class="btn btn-outline-primary variable-btn" onclick="appendToExpression('${v.name}')">
            ${v.name} = ${v.value}
        </button>`).join('');
}

function showError(message, isModal = false) {
    const errorElement = document.getElementById(isModal ? 'modalError' : 'calculatorError');
    errorElement.textContent = message;
    errorElement.style.display = 'block';
    setTimeout(() => {
        errorElement.style.display = 'none';
    }, 5000);
}

function addVariable() {
    const name = document.getElementById('varName').value;
    const valueStr = document.getElementById('varValue').value;

    if (!name || !valueStr) {
        showError('Por favor complete todos los campos', true);
        return;
    }

    if (!name.match(/^[a-zA-Z][a-zA-Z0-9]*$/)) {
        showError('El nombre debe comenzar con una letra y contener solo letras y números', true);
        return;
    }

    if (name.length > 12) {
        showError('El nombre no puede tener más de 12 caracteres', true);
        return;
    }

    const value = parseInt(valueStr);

    fetch('http://localhost:8080/api/calculator/variable', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ name, value })
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Error al agregar variable');
        }
        return response.text();
    })
    .then(() => {
        loadVariables();
        const modal = bootstrap.Modal.getInstance(document.getElementById('addVariableModal'));
        modal.hide();
        document.getElementById('varName').value = '';
        document.getElementById('varValue').value = '';
    })
    .catch(error => showError(error.message, true));
}

function appendToExpression(value) {
    currentExpression += value;
    document.getElementById('expression').value = currentExpression;
}

function clearExpression() {
    currentExpression = '';
    document.getElementById('expression').value = '';
    document.getElementById('result').textContent = '';
    document.getElementById('operandStack').innerHTML = '';
    document.getElementById('operatorStack').innerHTML = '';
    document.getElementById('calculatorError').style.display = 'none';
}

function evaluate() {
    if (!currentExpression) {
        showError('Por favor ingrese una expresión');
        return;
    }

    fetch('http://localhost:8080/api/calculator/evaluate', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ expression: currentExpression })
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Error al evaluar la expresión');
        }
        return response.json();
    })
    .then(data => {
        lastResult = data.result;
        document.getElementById('result').textContent = `Resultado: ${data.result}`;

        updateStack('operandStack', data.operandStack);
        updateStack('operatorStack', data.operatorStack);
    })
    .catch(error => showError(error.message));
}

function updateStack(stackId, items) {
    const stackElement = document.getElementById(stackId);
    stackElement.innerHTML = items
        .map(item => `<div class="stack-item">${item}</div>`)
        .join('');
}

function showAssignModal() {
    if (lastResult === null) {
        showError('Primero debe evaluar una expresión');
        return;
    }
    updateAssignVariablesList();
    const modal = new bootstrap.Modal(document.getElementById('assignVariableModal'));
    modal.show();
}

function assignToVariable(varName) {
    if (lastResult === null) return;

    fetch('http://localhost:8080/api/calculator/variable', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ name: varName, value: lastResult })
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Error al asignar valor a la variable');
        }
        return response.text();
    })
    .then(() => {
        loadVariables();
        const modal = bootstrap.Modal.getInstance(document.getElementById('assignVariableModal'));
        modal.hide();
        clearExpression();
    })
    .catch(error => showError(error.message));
}
