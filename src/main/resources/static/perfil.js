function mostrarPassActual() {
    const p = document.getElementById('pass-actual');
    // Usamos toggle para que sea más limpio
    p.classList.toggle('pass-hidden'); 
}

function validarPasswords() {
    const p1 = document.getElementById('pass1').value;
    const p2 = document.getElementById('pass2').value;
    const error = document.getElementById('error-pass');

    // Si el primer campo está vacío, asumimos que no quiere cambiarla (es válido)
    if (p1 === "") {
        error.style.display = 'none';
        return true;
    }

    if (p1 !== p2) {
        error.style.display = 'block';
        return false; 
    }
    return true; 
}

function actualizarPerfil() {
    const nuevoNombre = document.getElementsByName("nuevoUser")[0].value;
    const nuevaClave = document.getElementById("pass1").value;
    const nuevaClaveRepetida = document.getElementById("pass2").value;

    // Validación básica antes de mandar
    if (nuevaClave !== "" && nuevaClave !== nuevaClaveRepetida) {
        document.getElementById("error-pass").style.display = 'block';
        return;
    }

    const params = new URLSearchParams();
    params.append('nuevoUser', nuevoNombre);
    params.append('nuevoPass', nuevaClave);

    fetch('/usuarios/actualizar-perfil', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: params
    }).then(res => {
        if (res.ok) {
            alert("¡Perfil actualizado con éxito!");
            window.location.href = "/home"; // Volvemos al home
        } else {
            alert("Hubo un error al actualizar");
        }
    }).catch(err => console.error("Error en fetch:", err));
}