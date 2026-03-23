const passReal = /*[[${session.userLogger.password}]]*/ '';
const asteriscosBase = "*".repeat(passReal.length); // Genera tantos * como letras tenga la pass
let esVisible = false;

// Esta función se activa apenas carga la página gracias al script en el HTML
function inicializarPass() {
    const pText = document.getElementById('pass-text');
    const pass = window.passRealDelTurco;
    if (pText && pass) {
        pText.innerText = "*".repeat(pass.length);
    }
}

function toggleOjoPass() {
    const pText = document.getElementById('pass-text');
    const pass = window.passRealDelTurco;
    
    esVisible = !esVisible;
    
    if (esVisible) {
        pText.innerText = pass; // Muestra la clave real
        pText.style.color = "#00ff88"; // Verde
    } else {
        pText.innerText = "*".repeat(pass.length); // Vuelve a asteriscos
        pText.style.color = "white";
    }
}

function abrirEdicion() {
    document.getElementById('contenedor-edicion').classList.remove('hidden');
    document.getElementById('botones-principales').classList.add('hidden');
}

function cerrarEdicion() {
    document.getElementById('contenedor-edicion').classList.add('hidden');
    document.getElementById('botones-principales').classList.remove('hidden');
}

function actualizarPerfil() {
    const user = document.getElementById('nuevo-nombre').value;
    const pass = document.getElementById('nueva-pass').value;

    const params = new URLSearchParams();
    params.append('nuevoUser', user);
    params.append('nuevoPass', pass);

    fetch('/usuarios/actualizar-perfil', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: params
    }).then(res => {
        if (res.ok) {
            alert("¡Perfil actualizado con éxito!");
            window.location.reload(); 
        } else {
            alert("Error al actualizar");
        }
    });
}