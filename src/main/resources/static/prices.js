const API_URL = 'http://localhost:8081';
let cambiosPendientes = {};

document.addEventListener("DOMContentLoaded", () => {
    console.log("DOM Cargado. Iniciando carga de tabla...");
    cargarTabla();
});

function cargarTabla() {
    console.log("Intentando fetch a:", `${API_URL}/products/list`);
    
    fetch(`${API_URL}/products/list`, {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            'Cache-Control': 'no-cache'
        }
    })
    .then(res => {
        console.log("Status Code:", res.status);
        if (!res.ok) throw new Error("Error HTTP: " + res.status);
        return res.json();
    })
    .then(productos => {
        console.log("Productos en JS:", productos);
        const tbody = document.getElementById('cuerpo-tabla');
        
        if (!tbody) {
            console.error("ERROR: No se encontró el elemento 'cuerpo-tabla' en el HTML");
            return;
        }

        if (productos.length === 0) {
            tbody.innerHTML = '<tr><td colspan="4">El servidor devolvió una lista vacía.</td></tr>';
            return;
        }

        let html = "";
        productos.forEach(p => {
            html += `
                <tr id="fila-${p.id}">
                    <td>${p.id}</td>
                    <td><input type="text" value="${p.title || ''}" onchange="registrarCambio(${p.id}, 'title', this)"></td>
                    <td>
                        <input type="number" value="${p.price || 0}" onchange="registrarCambio(${p.id}, 'price', this, ${p.price})">
                        <span id="anterior-${p.id}" class="precio-anterior"></span>
                    </td>
                    <td><input type="number" value="${p.stock || 0}" onchange="registrarCambio(${p.id}, 'stock', this)"></td>
                </tr>`;
        });
        tbody.innerHTML = html;
    })
    .catch(err => {
        console.error("Error en el catch de cargarTabla:", err);
        const tbody = document.getElementById('cuerpo-tabla');
        if(tbody) tbody.innerHTML = `<tr><td colspan="4" style="color:red">Error: ${err.message}</td></tr>`;
    });
}

function registrarCambio(id, campo, input, valorOriginal = null) {
    const fila = document.getElementById(`fila-${id}`);
    fila.classList.add('fila-modificada');

    if (campo === 'price' && valorOriginal !== null) {
        document.getElementById(`anterior-${id}`).innerText = ` (Antes: $${valorOriginal})`;
    }

    if (!cambiosPendientes[id]) cambiosPendientes[id] = { id: id };
    cambiosPendientes[id][campo] = input.value;
}

async function guardarCambios() {
    const lista = Object.values(cambiosPendientes);
    if (lista.length === 0) return alert("Sin cambios.");

    for (let p of lista) {
        await fetch(`${API_URL}/products/update/${p.id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(p)
        });
    }
    alert("¡Precios actualizados!");
    location.reload();
}