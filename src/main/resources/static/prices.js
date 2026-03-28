const API_URL = window.location.hostname === "localhost" || window.location.hostname === "127.0.0.1"
    ? "http://localhost:8081" 
    : "https://dinastia-6gsq.onrender.com";

let cambiosPendientes = {};
let productosCargados = [];
let todasLasCategorias = []; // Usamos este nombre consistentemente

document.addEventListener("DOMContentLoaded", () => {
    console.log("Iniciando Gestión de Precios...");
    cargarTabla();
    llenarSelectorCategorias();
});

function cargarTabla() {
    fetch(`${API_URL}/products/list`)
    .then(res => res.json())
    .then(productos => {
        productosCargados = productos;
        renderizarTabla(productos);
    });
}

function renderizarTabla(lista) {
    const tbody = document.getElementById('cuerpo-tabla');
    let html = "";
    lista.forEach(p => {
        html += `
            <tr id="fila-${p.id}" class="fila-producto">
                <td>${p.id}</td>
                <td class="nombre-tabla">
                    <input type="text" value="${p.title || ''}" onchange="registrarCambio(${p.id}, 'title', this)">
                </td>
                <td>
                    <input type="number" value="${p.price || 0}" onchange="registrarCambio(${p.id}, 'price', this, ${p.price})">
                    <span id="anterior-${p.id}" class="precio-anterior"></span>
                </td>
                <td><input type="number" value="${p.stock || 0}" onchange="registrarCambio(${p.id}, 'stock', this)"></td>
            </tr>`;
    });
    tbody.innerHTML = html;
}

function llenarSelectorCategorias() {
    fetch(`${API_URL}/categories/list`)
        .then(res => res.json())
        .then(data => {
            todasLasCategorias = data; // Guardamos todo el árbol
            const selectMadres = document.getElementById('filtro-categoria');
            
            // Filtramos solo las que no tienen padre
            const madres = data.filter(c => c.parent === null);
            
            madres.forEach(cat => {
                const opt = new Option(cat.name, cat.id);
                selectMadres.appendChild(opt);
            });
        });
}

function manejarCambioCategoria() {
    const idPadre = document.getElementById('filtro-categoria').value;
    const selectHijas = document.getElementById('filtro-subcategoria');
    
    // 1. Limpiar selector de subcategorías
    selectHijas.innerHTML = '<option value="todas">Todas las subcategorías</option>';
    
    // 2. Buscar hijas usando el nombre correcto de la variable: todasLasCategorias
    const hijas = todasLasCategorias.filter(c => c.parent && c.parent.id == idPadre);

    if (hijas.length > 0 && idPadre !== 'todas') {
        hijas.forEach(h => {
            selectHijas.add(new Option(h.name, h.id));
        });
        selectHijas.style.display = 'block';
    } else {
        selectHijas.style.display = 'none';
    }

    // 3. Filtrar la tabla inmediatamente
    filtrarTodo();
}

function filtrarTodo() {
    const texto = document.getElementById('busqueda-precios').value;
    const catMadre = document.getElementById('filtro-categoria').value;
    const catHija = document.getElementById('filtro-subcategoria').value;

    // Si hay hija, manda la hija. Si no, manda la madre.
    const categoriaId = (catHija !== 'todas') ? catHija : catMadre;

    productosCargados.forEach(p => {
        // Esta función vive en utils.js
        const mostrar = cumpleFiltros(p, texto, categoriaId);
        
        const fila = document.getElementById(`fila-${p.id}`);
        if (fila) {
            fila.style.display = mostrar ? "" : "none";
        }
    });
}

function registrarCambio(id, campo, input, valorOriginal = null) {
    const fila = document.getElementById(`fila-${id}`);
    if (fila) fila.classList.add('fila-modificada');

    if (campo === 'price' && valorOriginal !== null) {
        document.getElementById(`anterior-${id}`).innerText = ` (Antes: $${valorOriginal})`;
    }

    if (!cambiosPendientes[id]) cambiosPendientes[id] = { id: id };
    cambiosPendientes[id][campo] = input.value;
}

async function guardarCambios() {
    const lista = Object.values(cambiosPendientes);
    if (lista.length === 0) return alert("No hay cambios.");

    try {
        for (let p of lista) {
            // Construimos un objeto simple, sin anidaciones
            const bodyEnviar = {
                title: p.title,
                price: parseFloat(p.price),
                stock: parseInt(p.stock)
            };

            const res = await fetch(`${API_URL}/products/actualizar-rapido/${p.id}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(bodyEnviar)
            });

            if (!res.ok) throw new Error(`Error en ID ${p.id}: ${res.status}`);
        }
        alert("¡Precios actualizados con éxito!");
        location.reload();
    } catch (error) {
        console.error("Error detallado:", error);
        alert("Hubo un error al guardar. Revisá la consola.");
    }
}