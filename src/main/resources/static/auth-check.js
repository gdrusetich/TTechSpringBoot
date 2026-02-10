document.addEventListener("DOMContentLoaded", () => {
    // 1. ¿Venimos del login? Si hay nombre en la URL, lo guardamos para siempre
    const urlParams = new URLSearchParams(window.location.search);
    const nombreDesdeURL = urlParams.get('loginSuccess');
    
    if (nombreDesdeURL) {
        localStorage.setItem("userName", nombreDesdeURL);
        // Limpiamos la URL para que no quede el nombre ahí a la vista
        window.history.replaceState({}, document.title, window.location.pathname);
    }

    // 2. LEER LA VARIABLE GENERAL
    const userLogger = localStorage.getItem("userName");

    // 3. APLICAR LÓGICA A LOS ELEMENTOS (Si existen en la página actual)
    const greeting = document.getElementById("user-greeting");
    const precio = document.getElementById("product-price");
    const btnLogin = document.getElementById("btn-login");
    const btnLogout = document.getElementById("btn-logout");

    if (btnLogout) {
        btnLogout.onclick = () => {
            localStorage.removeItem("userName"); // Borramos la variable general
            window.location.href = "/logout"; // Vamos al controller de Java
        };
    }

    if (userLogger) {
        if (greeting) greeting.innerText = `Hola, ${userLogger}`;
        if (btnLogin) btnLogin.style.display = "none";
        if (btnLogout) btnLogout.style.display = "block";
        // En el detalle, el precio se verá porque userLogger es true
    } else {
        if (greeting) greeting.innerText = "Hola, Invitado";
        if (precio) precio.innerText = "Iniciá sesión para ver el precio";
        if (btnLogin) btnLogin.style.display = "block";
        if (btnLogout) btnLogout.style.display = "none";
    }
});

