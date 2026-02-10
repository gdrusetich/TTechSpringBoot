let editandoId = null;
let productosCargados = [];
const API_PRODUCTS = "http://localhost:8081/products";
const API_CATEGORIES = "http://localhost:8081/categories";
const API_URL = "http://localhost:8081";

let editandoCatId = null;
let categoriasData = [];
let categoriaActualId = 'TODOS';
let idsHijasActuales = [];

document.addEventListener("DOMContentLoaded", () => {
    console.log("Inicializando Dashboard...");
    inicializarApp();
});

async function inicializarApp() {
     await Promise.all([
        cargarProductos(),
        cargarCategoriasSelector(),  // Crea los botones de niveles (Nivel 0)
        cargarSelectCategorias()
    ]);

}

function ejecutarFiltroFinal() {
    const texto = document.getElementById('busquedaAdmin').value.toLowerCase();
    const idFiltro = document.getElementById('categoriaIdInput').value;

    // Si hay un filtro de categoría, obtenemos el ID seleccionado y todos sus hijos/nietos
    let idsPermitidos = [];
    if (idFiltro && idFiltro !== "") {
        idsPermitidos = obtenerIdsDescendientes(idFiltro);
    }

    const filtrados = productosCargados.filter(p => {
        const nombreProducto = (p.title || p.name || "").toLowerCase();
        const coincideTexto = nombreProducto.includes(texto) || p.id.toString().includes(texto);
        
        if (!coincideTexto) return false;        
        if (idsPermitidos.length === 0) return true;
        if (Array.isArray(p.categories)) {
            return p.categories.some(cat => idsPermitidos.includes(Number(cat.id)));
        }         
        if (p.category) {
            const idCatProducto = p.category.id || p.category; // Maneja si es objeto o solo ID
            return idsPermitidos.includes(Number(idCatProducto));
        }
        
        return false;
    });

    renderizarTabla(filtrados);
}


function cargarProductos() {
    fetch(`${API_PRODUCTS}/list`)
        .then(res => res.json())
        .then(productos => {
            console.log("Productos recibidos del servidor:", productos);
            productosCargados = productos; 
            ejecutarFiltroFinal();
        })
        .catch(err => console.error("Error al cargar productos:", err));
}

function renderizarTabla(lista) {
    const tabla = document.getElementById('tabla-productos');
    let htmlFinal = "";

    // 1. ORDENAR SIEMPRE POR ID (Para que no salten al final al editar)
    lista.sort((a, b) => (a.id || a.id_producto) - (b.id || b.id_producto));

    if (!lista || lista.length === 0) {
        tabla.innerHTML = "<tr><td colspan='6' style='text-align:center; padding:20px;'>No hay productos en esta categoría</td></tr>";
        return;
    }

    const BASE_URL = `http://${window.location.hostname}:8081/uploads/`;

    lista.forEach(p => {
        const id = p.id || p.id_producto;
        
        // Lógica de imagen mejorada
        const nombreArchivo = (p.mainImage && (p.mainImage.url || p.mainImage.ruta)) 
            ? (p.mainImage.url || p.mainImage.ruta) 
            : (p.images && p.images.length > 0 ? (typeof p.images[0] === 'string' ? p.images[0] : p.images[0].url) : 'default.png');
        
        const rtaImagen = `${BASE_URL}${nombreArchivo}`;

        htmlFinal += `
            <tr id="fila-${id}">
                <td>${id}</td>
                <td>
                    <img src="${rtaImagen}" 
                         alt="${p.title}"
                         onclick="irADetalle(${id})" 
                         style="width: 120px; height: 80px; object-fit: contain; cursor: pointer; background: white; border: 1px solid #ddd; border-radius: 4px;"
                         onerror="this.src='${BASE_URL}default.png'">
                </td>
                
                <td>
                    <span class="view-mode"><strong>${p.title}</strong></span>
                    <input type="text" class="edit-mode d-none in-title" value="${p.title}" style="width: 100%;">
                </td>

                <td>
                    <span class="view-mode">$${p.price ? p.price.toLocaleString('es-AR') : '0'}</span>
                    <input type="number" class="edit-mode d-none in-price" value="${p.price}" style="width: 100px;">
                </td>

                <td>
                    <span class="view-mode">${p.stock}</span>
                    <input type="number" class="edit-mode d-none in-stock" value="${p.stock}" style="width: 70px;">
                </td>

                <td style="min-width: 230px;">
                    <button class="view-mode btn-tabla" style="background:#007bff; color:white;" onclick="activarEdicion(${id})">Editar</button>
                    <button class="view-mode btn-tabla" style="background:#dc3545; color:white;" onclick="eliminarProducto(${id})">Borrar</button>

                    <button class="edit-mode d-none btn-tabla" style="background:#28a745; color:white;" onclick="guardarEdicionRapida(${id})">Guardar</button>
                    <button class="edit-mode d-none btn-tabla" style="background:#6c757d; color:white;" onclick="abrirModalDesc(this)">Desc.</button>
                    <span class="edit-mode d-none" onclick="cancelarEdicion(${id})" style="cursor:pointer; margin-left:10px; font-weight:bold; color:red;">❌</span>

                    <div class="panel-desc" style="display:none; position:fixed; top:50%; left:50%; transform:translate(-50%, -50%); background: white; border: 2px solid #333; z-index: 10001; padding: 20px; width: 450px; border-radius: 8px; box-shadow: 0 0 20px rgba(0,0,0,0.5);">
                        <h4 style="margin-top:0; color: black;">Editar Descripción</h4>
                        <textarea class="in-desc" style="width: 100%; height: 180px; font-family: Arial; padding: 10px;">${p.description || ''}</textarea>
                        <button type="button" onclick="this.parentElement.style.display='none'" style="width:100%; background:#007bff; color:white; margin-top:10px; padding: 10px; border:none; border-radius:5px; cursor:pointer;">LISTO</button>
                    </div>
                </td>
            </tr>`;
    });
    tabla.innerHTML = htmlFinal;
}

function irADetalle(id) {
    window.location.href = `/detalle?id=${id}`;
}


function eliminarProducto(id) {
    if(confirm("¿Borrar?")) fetch(`${API_PRODUCTS}/delete/${id}`, { method: 'DELETE' }).then(() => cargarProductos());
}

function crearUsuarioDesdeAdmin() {
    const user = document.getElementById('new-username').value;
    const pass = document.getElementById('new-password').value;
    const role = document.getElementById('new-role').value;

    if (!user || !pass) {
        alert("Por favor, completa usuario y contraseña.");
        return;
    }

    const params = new URLSearchParams();
    params.append('username', user);
    params.append('password', pass);
    params.append('role', role);

    fetch('/usuarios/guardar', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: params
    })
    .then(res => {
        if (res.ok) {
            alert("Usuario creado con éxito");
            document.getElementById('new-username').value = "";
            document.getElementById('new-password').value = "";
        } else {
            alert("Error al crear el usuario. Quizás ya existe.");
        }
    });
}

function cargarUsuarios() {
    fetch('/usuarios/listar') 
        .then(res => {
            if (!res.ok) throw new Error("Error en la respuesta del servidor");
            return res.text();
        })
        .then(texto => {
            return texto ? JSON.parse(texto) : [];
        })
        .then(usuarios => {
            const tabla = document.getElementById('tabla-usuarios-body');
            if (!tabla) return;
            
            tabla.innerHTML = ""; 
            if (usuarios.length === 0) {
                tabla.innerHTML = "<tr><td colspan='5' style='text-align:center;'>No tienes permisos o no hay usuarios.</td></tr>";
                return;
            }

            usuarios.forEach(u => {
                tabla.innerHTML += `
                    <tr style="border-bottom: 1px solid #444;">
                        <td>${u.id}</td>
                        <td>${u.username}</td>
                        <td>${u.password}</td> 
                        <td><span class="badge">${u.role}</span></td>
                        <td>
                            <button onclick="prepararEdicionUsuario(${u.id}, '${u.username}')" style="color:#ff9800; cursor:pointer; background:none; border:1px solid #ff9800; border-radius:4px; padding:2px 5px; margin-right:5px;">✏️ Editar</button>
                            <button onclick="eliminarUsuario(${u.id})" style="color:#ff5252; cursor:pointer; background:none; border:none;">🗑️ Borrar</button>
                        </td>
                    </tr>`;
            });
        })
        .catch(err => console.error("Error cargando usuarios:", err));
}

function prepararEdicionUsuario(id, nombreActual) {
    const nuevoNombre = prompt("Nuevo nombre de usuario:", nombreActual);
    const nuevaClave = prompt("Nueva contraseña:");

    if (nuevoNombre && nuevaClave) {
        const params = new URLSearchParams();
        params.append('id', id);
        params.append('username', nuevoNombre);
        params.append('password', nuevaClave);

        fetch('/usuarios/editar-desde-admin', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: params
        }).then(res => {
            if (res.ok) {
                alert("Usuario actualizado");
                cargarUsuarios();
            }
        });
    }
}

function eliminarUsuario(id) {
if (confirm("¿Estás seguro de que querés eliminar a este usuario?")) {
    fetch(`/usuarios/eliminar/${id}`)
        .then(res => {
            alert("Usuario eliminado con éxito");
            cargarUsuarios();
        })
        .catch(err => {
            console.error("Error:", err);
            alert("No se pudo eliminar el usuario");
        });
    }
}

function mostrarSeccion(seccion) {
    if (seccion === 'usuarios') {
        document.getElementById('seccion-usuarios').style.display = 'block';
        document.getElementById('seccion-productos').style.display = 'none';
        cargarUsuarios();
    } else {
        document.getElementById('seccion-usuarios').style.display = 'none';
        document.getElementById('seccion-productos').style.display = 'block';
    }
}


function toggleSidebar() {
    const sidebar = document.querySelector('.sidebar');
    sidebar.classList.toggle('oculto');
}

function cargarCategoriasSelector() {
    fetch(API_CATEGORIES + "/list")
        .then(res => res.json())
        .then(data => {
            categoriasData = data;
            const principales = data.filter(c => !c.parent);
            renderizarNivel(0, principales);
        });
}

function renderizarNivel(nivel, lista) {
    const contenedorPadre = document.getElementById('niveles-categorias');
    const divNivel = document.createElement('div');
    divNivel.id = `nivel-${nivel}`;
    divNivel.className = "nav-nivel";
    divNivel.style.display = "flex";
    divNivel.style.gap = "5px";
    divNivel.style.marginTop = "10px";

    lista.forEach(cat => {
        const btn = document.createElement('button');
        btn.type = "button";
        btn.className = "filter-btn";
        btn.innerText = cat.name;
        btn.onclick = () => manejarSeleccion(cat.id, cat.name, nivel, btn);
        divNivel.appendChild(btn);
    });

    contenedorPadre.appendChild(divNivel);
}

function manejarSeleccion(id, nombre, nivelActual, btn) {
    const filaActual = btn.parentElement;
        filaActual.querySelectorAll('.filter-btn, .btn-categoria').forEach(b => {
            b.classList.remove('active');
        });
    btn.classList.add('active');

    document.getElementById('categoriaIdInput').value = id;
    document.getElementById('nombre-seleccionada').innerText = nombre;

    const contenedorPadre = document.getElementById('niveles-categorias');
    const hijosContenedor = Array.from(contenedorPadre.children);
    hijosContenedor.forEach(child => {
        const nivelDelChild = parseInt(child.id.split('-')[1]);
        if (nivelDelChild > nivelActual) child.remove();
    });

    const subcats = categoriasData.filter(c => c.parent && Number(c.parent.id) === Number(id));
    
    if (subcats.length > 0) renderizarNivel(nivelActual + 1, subcats);

    categoriaActualId = id;

    if (typeof ejecutarFiltroFinal === 'function') ejecutarFiltroFinal();
}

function limpiarFiltros() {
    document.getElementById('categoriaIdInput').value = '';
    document.getElementById('nombre-seleccionada').innerText = 'Ninguna';
    const contenedor = document.getElementById('niveles-categorias');
    contenedor.innerHTML = '';
    
    const principales = categoriasData.filter(c => !c.parent);
    renderizarNivel(0, principales);
    
    if (typeof ejecutarFiltroFinal === 'function') {
        ejecutarFiltroFinal();
    }
}

async function cargarSelectCategorias() {
    const selectPadre = document.getElementById('cat-parent');
    if (!selectPadre) {
        console.error("No se encontró el elemento cat-parent en el HTML");
        return;
    }

    try {
        console.log("Intentando cargar categorías desde el servidor...");
        const res = await fetch(`${API_CATEGORIES}/list`);
        const data = await res.json();
        
        categoriasData = data; 
        console.log("Categorías recibidas:", data);

        selectPadre.innerHTML = '<option value="">-- Ninguna (Principal) --</option>';
        data.forEach(cat => {
            const option = document.createElement('option');
            option.value = cat.id;
            option.textContent = cat.name;
            selectPadre.appendChild(option);
        });
    } catch (e) {
        console.error("Error en cargarSelectCategorias:", e);
    }
}

function obtenerIdsDescendientes(idPadre) {
    let ids = [Number(idPadre)];
    // Buscamos todas las categorías que tengan este id como parent
    const hijas = categoriasData.filter(c => c.parent && Number(c.parent.id) === Number(idPadre));
    
    hijas.forEach(hija => {
        ids = ids.concat(obtenerIdsDescendientes(hija.id));
    });
    return ids;
}

function actualizarBotones() {
    const select = document.getElementById('cat-parent');
    const btnEditar = document.getElementById('btn-preparar-edicion');
    const btnEliminar = document.getElementById('btn-eliminar-cat');
    const btnGuardar = document.getElementById('btn-guardar-cat');

    if (select.value !== "") {
        btnEditar.style.display = "block";
        btnEliminar.style.display = "block";
        btnGuardar.innerText = "Guardar como Subcategoría";
    } else {
        btnEditar.style.display = "none";
        btnEliminar.style.display = "none";
        btnGuardar.innerText = "Guardar como Nueva Raíz";
    }
}

function prepararEdicionDesdeSelect() {
    const select = document.getElementById('cat-parent');
    const idSeleccionado = select.value;

    const cat = categoriasData.find(c => c.id == idSeleccionado);
    
    if (cat) {
        editandoCatId = cat.id;
        document.getElementById('new-category-name').value = cat.name;
        document.getElementById('titulo-form-cat').innerText = "Modificando Categoría";
        document.getElementById('titulo-form-cat').style.color = "#ffc107"; // Amarillo para avisar
        
        document.getElementById('btn-cancelar-cat').style.display = "block";
        document.getElementById('btn-preparar-edicion').style.display = "none"; // Ya estamos editando
        
        // El select ahora muestra el padre de la categoría que estamos editando
        select.value = cat.parent ? cat.parent.id : "";
        document.getElementById('new-category-name').focus();
    }
}

// Asegurate de llamar a actualizarBotones() dentro de cancelarEdicionCat
function cancelarEdicionCat() {
    editandoCatId = null;
    document.getElementById('new-category-name').value = "";
    document.getElementById('cat-parent').value = "";
    document.getElementById('titulo-form-cat').innerText = "Crear Nueva Categoría";
    document.getElementById('titulo-form-cat').style.color = "#aaa";
    document.getElementById('btn-cancelar-cat').style.display = "none";
    actualizarBotones(); // Resetea la visual de los botones
}

async function guardarCategoria() {
    const nombre = document.getElementById('new-category-name').value.trim();
    const padreId = document.getElementById('cat-parent').value;

    if (!nombre) { alert("Poné un nombre"); return; }

    const payload = {
        name: nombre,
        // Si no hay padreId o es string vacío, mandamos null literal
        parent: (padreId && padreId !== "") ? { id: parseInt(padreId) } : null
    };

    try {
        const url = editandoCatId ? `${API_CATEGORIES}/update/${editandoCatId}` : `${API_CATEGORIES}/add`;
        const metodo = editandoCatId ? 'PUT' : 'POST';

        const res = await fetch(url, {
            method: metodo,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

    if (res.ok) {
        alert("¡Categoría guardada!"); // Ya no intentamos procesar la respuesta como objeto
        cancelarEdicionCat();
        await cargarSelectCategorias(); 
    } else {
        const errorText = await res.text();
        console.error("Error detallado:", errorText);
        alert("Error 500: Revisa la consola del servidor (IntelliJ)");
    }
    } catch (e) {
        console.error("Error de conexión:", e);
    }
}

function eliminarCategoriaSeleccionada() {
    const select = document.getElementById('cat-parent');
    const id = select.value;
    const nombre = select.options[select.selectedIndex].text;

    if (!id) {
        alert("Por favor, selecciona una categoría para eliminar.");
        return;
    }

    if (confirm(`¿Estás seguro de eliminar "${nombre}"?\n\n- Las subcategorías subirán de nivel.\n- Los productos se reasignarán al padre.`)) {
        
        fetch(`http://localhost:8081/categories/delete/${id}`, {
            method: 'DELETE'
        })
        .then(response => {
            if (!response.ok) throw new Error("Error en la respuesta del servidor");
            return response.json();
        })
        .then(data => {
            if (data.status === "success") {
                alert("Categoría eliminada con éxito.");
                location.reload(); 
            } else {
                alert("Error: " + data.error);
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert("No se pudo eliminar la categoría. Revisa la consola.");
        });
    }
}

const todasLasCategorias = /*[[${categoriasPadre}]]*/ [];

function agregarFila() {
    const tbody = document.getElementById("cuerpo-carga");
    const tr = document.createElement("tr");

    tr.innerHTML = `
        <td><input type="text" class="in-nombre" placeholder="Ej: Radio Panacom"></td>
        <td><input type="number" class="in-precio" placeholder="0.00"></td>
        <td><input type="number" class="in-stock" placeholder="10"></td>
        <td>
            <div class="dropdown-categorias">
                <button type="button" onclick="toggleCats(this)">Seleccionar ▼</button>
                <div class="lista-checkbox" style="display:none; position:absolute; background:#2a2a2a; border:1px solid #555; z-index:10;">
                    ${todasLasCategorias.sort((a,b) => a.nombre.localeCompare(b.nombre)).map(cat => `
                        <label style="display:block; padding:5px;">
                            <input type="checkbox" value="${cat.id}"> ${cat.nombre}
                        </label>
                    `).join('')}
                </div>
            </div>
        </td>
        <td>
            <input type="file" class="in-fotos" multiple accept="image/*">
        </td>
        <td>
            <button onclick="this.parentElement.parentElement.remove()" class="btn-borrar">❌</button>
        </td>
    `;
    tbody.appendChild(tr);
}

async function guardarTodo() {
    const filas = document.querySelectorAll("#cuerpo-carga tr");
    
    for (let [index, fila] of filas.entries()) {
        const formData = new FormData();
        
        // Mapeo exacto según tu @RequestParam en Java
        formData.append("title", fila.querySelector(".in-nombre").value);
        formData.append("price", fila.querySelector(".in-precio").value);
        formData.append("stock", fila.querySelector(".in-stock").value);
        formData.append("description", fila.querySelector(".in-descripcion").value);

        // CATEGORÍAS: Java espera @RequestParam("category")
        const checks = fila.querySelectorAll(".cat-check:checked");
        if (checks.length === 0) {
            console.error(`Fila ${index + 1} no tiene categorías seleccionadas.`);
            continue; // O avisar al usuario
        }
        checks.forEach(cb => {
            formData.append("category", cb.value); // <--- CAMBIADO: Coincide con Java
        });

        // IMÁGENES: Java espera @RequestParam("images")
        const inputFotos = fila.querySelector(".in-fotos");
        if (inputFotos && inputFotos.files.length > 0) {
            for (let i = 0; i < inputFotos.files.length; i++) {
                formData.append("images", inputFotos.files[i]); // <--- CAMBIADO: Coincide con Java
            }
        }

        try {
            // Asegurate de que la URL sea la correcta (/products o /admin/productos)
            const resp = await fetch("/products/nuevo-producto", { 
                method: "POST",
                body: formData
            });

            if (resp.ok) {
                console.log(`✅ Fila ${index + 1} guardada: ${fila.querySelector(".in-nombre").value}`);
                fila.style.backgroundColor = "#d4edda"; // Pintamos de verde si sale bien
            } else {
                const errorData = await resp.text();
                console.error(`❌ Error 400 en Fila ${index + 1}:`, errorData);
                alert(`Error en fila ${index + 1}: Verificá que todos los campos obligatorios estén llenos.`);
            }
        } catch (err) {
            console.error("Error de red:", err);
        }
    }
}

function toggleCats(btn) {
    // Buscamos el div que está DESPUÉS del botón
    const lista = btn.nextElementSibling;
    if (lista) {
        // Forzamos el cambio de estilo
        if (lista.style.display === "none" || lista.style.display === "") {
            lista.style.display = "block";
        } else {
            lista.style.display = "none";
        }
    }
}

function agregarFila() {
    const tbody = document.getElementById("cuerpo-carga");
    const categorias = categoriasData || []; 

    const tr = document.createElement("tr");
    tr.innerHTML = `
        <td><input type="text" class="in-nombre" placeholder="Título"></td>
        <td><input type="number" class="in-precio" placeholder="0.00" style="width:80px"></td>
        <td><input type="number" class="in-stock" placeholder="10" style="width:60px"></td>
        
        <td>
            <button type="button" onclick="toggleCats(this)" style="padding: 5px 10px; cursor:pointer;">Editar Desc. 📝</button>
            <div class="panel-desc" style="display:none; position:fixed; top:50%; left:50%; transform:translate(-50%, -50%); background: white; border: 3px solid #444; z-index: 10001; padding: 25px; width: 90%; max-width: 600px; box-shadow: 0px 0px 50px rgba(0,0,0,0.8); border-radius: 12px;">
                <h4 style="color: black; margin-top: 0;">Descripción del Producto</h4>
                <textarea class="in-descripcion" style="width: 100%; height: 300px; padding: 10px; font-family: sans-serif; font-size: 14px; border: 1px solid #ccc; border-radius: 5px; resize: none;" placeholder="Escribí aquí la descripción..."></textarea>
                <button type="button" onclick="this.parentElement.style.display='none'" style="width: 100%; background: #007bff; color: white; border: none; padding: 12px; margin-top: 15px; cursor: pointer; border-radius: 6px; font-weight: bold;">ACEPTAR</button>
            </div>
        </td>

        <td>
            <button type="button" onclick="toggleCats(this)" style="padding: 5px 10px; cursor:pointer;">Categorías ▼</button>
            <div class="lista-desplegable" style="display:none; position:fixed; top:50%; left:50%; transform:translate(-50%, -50%); background: white; border: 3px solid #444; z-index: 10000; padding: 25px; width: 80%; max-width: 500px; box-shadow: 0px 0px 50px rgba(0,0,0,0.8); border-radius: 12px;">
                <h4 style="color: black; margin-top: 0;">Seleccionar Categorías</h4>
                <div style="max-height: 300px; overflow-y: auto; margin-bottom: 20px; display: grid; grid-template-columns: 1fr 1fr; gap: 10px;">
                    ${categorias.map(cat => `
                        <label style="display:flex; align-items:center; color: #000; font-size: 14px; cursor: pointer; background: #f9f9f9; padding: 8px; border: 1px solid #ddd; border-radius: 4px;">
                            <input type="checkbox" value="${cat.id}" class="cat-check" style="margin-right: 10px; width: 18px; height: 18px;"> 
                            ${cat.name} 
                        </label>
                    `).join('')}
                </div>
                <button type="button" onclick="this.parentElement.style.display='none'" style="width: 100%; background: #28a745; color: white; border: none; padding: 12px; cursor: pointer; border-radius: 6px; font-weight: bold;">ACEPTAR</button>
            </div>
        </td>

        <td><input type="file" class="in-fotos" multiple></td>
        <td><button type="button" onclick="this.closest('tr').remove()" style="background:none; border:none; cursor:pointer; font-size:20px;">❌</button></td>
    `;
    tbody.appendChild(tr);
}

function activarEdicion(id) {
    const fila = document.getElementById(`fila-${id}`);
    fila.querySelectorAll('.view-mode').forEach(el => el.classList.add('d-none'));
    fila.querySelectorAll('.edit-mode').forEach(el => el.classList.remove('d-none'));
}

function cancelarEdicion(id) {
    const fila = document.getElementById(`fila-${id}`);
    fila.querySelectorAll('.view-mode').forEach(el => el.classList.remove('d-none'));
    fila.querySelectorAll('.edit-mode').forEach(el => el.classList.add('d-none'));
}

function abrirModalDesc(btn) {
    const panel = btn.parentElement.querySelector('.panel-desc');
    panel.style.display = 'block';
}

async function guardarEdicionRapida(id) {
    const fila = document.getElementById(`fila-${id}`);    
    const data = {
        title: fila.querySelector('.in-title').value,
        price: parseFloat(fila.querySelector('.in-price').value),
        stock: parseInt(fila.querySelector('.in-stock').value),
        description: fila.querySelector('.in-desc').value
    };

    try {
        const res = await fetch(`${API_PRODUCTS}/actualizar-rapido/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });

        if (res.ok) {
            await cargarProductos(); 
            console.log("Producto actualizado con éxito");
        } else {
            const errorText = await res.text();
            alert("Error: " + errorText);
        }
    } catch (err) {
        console.error("Error en la petición:", err);
        alert("No se pudo conectar con el servidor");
    }
}