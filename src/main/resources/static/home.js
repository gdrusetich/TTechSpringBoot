let categoriasData = [];
let productosHome = [];

if (typeof userLogger === 'undefined') {
    var userLogger = null;
}
const tooltip = document.getElementById('tooltip-descripcion');

document.addEventListener("DOMContentLoaded", () => {
    console.log("Página cargada. Limpiando miguita de pan...");
    localStorage.removeItem("returnUrl");
    if (window.productosDestacadosIniciales) {
        renderizarDestacados(window.productosDestacadosIniciales);
    }
    cargarCategorias();
    pedirProductos(`${API_URL}/products/list`);
});

function cargarProductos() {
    fetch(`${API_URL}/products/list`)
        .then(response => response.json())
        .then(data => {
            productosHome = data; // Guardamos la copia
            aplicarFiltrosYOrden(); // Llamamos a la nueva función
        })
        .catch(err => {
            console.error("Error:", err);
            document.getElementById('lista-productos').innerHTML = "Error al conectar.";
        });
}

function renderizarCards(data) {
    const div = document.getElementById('lista-productos');
    if (!div) return;
    div.innerHTML = ''; 

    if (!data || data.length === 0) {
        div.innerHTML = '<p style="color:white; text-align:center;">No hay productos en esta categoría.</p>';
        return;
    }

    div.innerHTML = data.map(p => {
    const catId = (p.categories && p.categories.length > 0) ? p.categories[0].id : '';
    const esUsuarioReal = (window.nombreUsuario && window.nombreUsuario !== 'Invitado');

    let fotoUrl = rutaDefault; // Viene de config.js (/images/default.jpg)
    
    let nombreImagen = null;
    if (p.mainImage && p.mainImage.url) {
        nombreImagen = p.mainImage.url;
    } else if (p.images && p.images.length > 0) {
        nombreImagen = p.images[0].url ? p.images[0].url : p.images[0];
    }

    if (nombreImagen) {
        let cleanUrl = nombreImagen.startsWith('/') ? nombreImagen.substring(1) : nombreImagen;
        
        if (cleanUrl === "default.jpg") {
            fotoUrl = rutaDefault;
        } else if (cleanUrl.startsWith('images/')) {
            fotoUrl = `/${cleanUrl}`;
        } else if (cleanUrl.startsWith('uploads/')) {
            fotoUrl = `/${cleanUrl}`;
        } else {
             fotoUrl = `${FOLDER_SYSTEM}/${cleanUrl}`;
        }
    }
        return `
            <div class="card" data-category-id="${catId}">
                <div class="img-container" onclick="window.location.href='/detalle?id=${p.id}'">
                    <img src="${fotoUrl}" alt="${p.title}" class="card-img" 
                         onerror="if (this.src != '${rutaDefault}') this.src = '${rutaDefault}';">
                </div>
                <div class="info">
                    <h3>${p.title}</h3>
                    
                    ${esUsuarioReal 
                        ? `<span class="price">$ ${p.price.toLocaleString('es-AR')}</span>` 
                        : ''
                    }

                    <div class="botones-container" style="display: flex; justify-content: center; align-items: center; gap: 10px;">
                        <img src="${rutaWA}" alt="WhatsApp" class="btn-wa-icon" 
                            onclick="enviarWhatsApp('${p.title}')" 
                            style="width: 35px; height: 35px;"> ${!esUsuarioReal ? `<button class="filter-btn small" onclick="abrirModalLogin()">Ingresar</button>` : ''}
                    </div>
                </div>
            </div>`;
    }).join('');
}

function pedirProductos(url) {
    fetch(url)
        .then(res => {
            if (!res.ok) throw new Error(`Error: ${res.status}`);
            return res.json();
        })
        .then(data => {
            if (Array.isArray(data)) {
                productosHome = data; 
                aplicarFiltrosYOrden(); 
            } else {
                console.error("No es una lista:", data);
            }
        })
        .catch(err => {
            console.error("Error al pedir productos:", err);
            document.getElementById('lista-productos').innerHTML = "Error al conectar.";
        });
}


function mostrarDescripcion(event, texto) {
    tooltip.innerText = texto;
    tooltip.style.display = 'block';
    tooltip.style.left = (event.pageX + 10) + 'px';
    tooltip.style.top = (event.pageY + 10) + 'px';
}

function ocultarDescripcion() {
    tooltip.style.display = 'none';
}

function enviarWhatsApp(producto) {
    const numero = "5491137869814"; // TU NUMERO
    const mensaje = encodeURIComponent(`Hola! Quisiera pedir el producto: ${producto}`);
    window.open(`https://wa.me/${numero}?text=${mensaje}`, '_blank');
}

function abrirModalLogin() {
    window.location.href = "/login";
}

    function abrirModalPerfil() {
    window.location.href = "/perfil";
}

let categoriasPrincipalesFavoritas = []; 
let subcategoriasFavoritas = {}; // Guardamos por ID de padre

function cargarCategorias() {
    fetch(`${API_URL}/categories/list`)
        .then(res => res.json())
        .then(data => {
            categoriasData = data;
            const padres = data.filter(c => c.parent === null);
            renderizarBarraNavegacion('categorias-nav', padres, null);
        });
}

function renderizarBarraNavegacion(containerId, listaCompleta, idPadreActual) {
    const container = document.getElementById(containerId);
    if (!container) return;
    container.innerHTML = '';

    const esCelular = window.innerWidth < 768;
    const limite = esCelular ? 3 : 7;
    const labelTodo = (containerId === 'categorias-nav') ? 'Todos' : 'Ver Todo';
    const btnTodo = document.createElement('button');
    btnTodo.className = "filter-btn active";
    btnTodo.innerText = labelTodo;
    btnTodo.onclick = () => {
        const url = (containerId === 'categorias-nav') ? `${API_URL}/products/list` : `${API_URL}/products/categoria/${idPadreActual}`;
        if (containerId === 'categorias-nav') {
            limpiarNivelesInferiores(true); 
        } else if (containerId === 'subcategorias-nav') {
            const nietoNav = document.getElementById('nietos-nav');
            if (nietoNav) nietoNav.innerHTML = '';
        }
        pedirProductos(url);
        marcarActivo(btnTodo);
    };
    container.appendChild(btnTodo);
    let listaParaMostrar = [...listaCompleta];
    const visibles = listaParaMostrar.slice(0, limite);
    const extras = listaParaMostrar.slice(limite);

    visibles.forEach(cat => {
        const btn = document.createElement('button');
        btn.className = "filter-btn";
        btn.innerText = cat.name;
        btn.onclick = () => seleccionarCategoria(cat.id, btn);
        container.appendChild(btn);
    });

    if (extras.length > 0) {
        const btnMas = document.createElement('button');
        btnMas.className = "filter-btn mas-btn";
        btnMas.innerHTML = `Más (${extras.length}) ▾`;
        btnMas.onclick = (e) => mostrarDropdownExtra(e, extras, containerId, listaCompleta);
        container.appendChild(btnMas);
    }
}

function mostrarDropdownExtra(event, extras, containerId, listaOriginal) {
    event.stopPropagation(); // Evita que el click se propague y cierre el menú al instante
    
    const dropdown = document.getElementById('dropdown-categorias');
    const listaDiv = document.getElementById('lista-extra-items');
    
    if (!dropdown || !listaDiv) {
        console.error("No se encontró el contenedor del dropdown en el HTML");
        return;
    }
    if (!dropdown.classList.contains('hidden')) {
        dropdown.classList.add('hidden');
        return;
    }

    dropdown.classList.remove('hidden');
    
    const rect = event.target.getBoundingClientRect();
    dropdown.style.top = `${rect.bottom + window.scrollY + 5}px`;
    dropdown.style.left = `${rect.left}px`;

    listaDiv.innerHTML = extras.map(cat => `
        <div class="extra-item" onclick="elegirExtra(${cat.id}, '${containerId}')">
            ${cat.name}
        </div>
    `).join('');

    document.addEventListener('click', function cerrarMenu(e) {
        if (!dropdown.contains(e.target) && e.target !== event.target) {
            dropdown.classList.add('hidden');
            document.removeEventListener('click', cerrarMenu);
        }
    });
}

function elegirExtra(id, containerId) {
    const elegida = categoriasData.find(c => c.id === id);
    
    const index = categoriasData.indexOf(elegida);
    categoriasData.splice(index, 1);
    categoriasData.unshift(elegida);

    if (containerId === 'categorias-nav') {
        const padres = categoriasData.filter(c => c.parent === null);
        renderizarBarraNavegacion('categorias-nav', padres, null);
    } else {
        const padreId = elegida.parent ? elegida.parent.id : (elegida.parentId || null);
        const hermanos = categoriasData.filter(c => (c.parentId || (c.parent && c.parent.id)) === padreId);
        renderizarBarraNavegacion(containerId, hermanos, padreId);
    }
    const nuevoBtn = document.querySelector(`#${containerId} button:nth-child(2)`);
    seleccionarCategoria(id, nuevoBtn);
}

function filtrarPorTexto() {
    aplicarFiltrosYOrden();
}

function actualizarInterfazFiltros() {
    const tipo = document.getElementById('tipoFiltroPrecio').value;
    const container = document.getElementById('inputs-precio');
    container.innerHTML = '';

    if (tipo === 'menor' || tipo === 'mayor') {
        container.innerHTML = `<input type="number" id="precioUno" placeholder="Precio" class="price-input">`;
    } else if (tipo === 'entre') {
        container.innerHTML = `
            <input type="number" id="precioUno" placeholder="Min" class="price-input">
            <span style="color:#888">-</span>
            <input type="number" id="precioDos" placeholder="Max" class="price-input">
        `;
    }
}

function limpiarNivelesInferiores(esPrincipal) {
    const subNav = document.getElementById('subcategorias-nav');
    const nietoNav = document.getElementById('nietos-nav');
    
    if (esPrincipal) subNav.innerHTML = '';
    if (nietoNav) nietoNav.innerHTML = '';
}

function obtenerHijos(idPadre) {
    return categoriasData.filter(c => (c.parentId || (c.parent && c.parent.id)) === idPadre);
}

function renderizarNivel(containerId, listaHijos, idPadreActual) {
    const container = document.getElementById(containerId);
    if (!container) return;

    container.innerHTML += `<button class="filter-btn sub-btn active" onclick="pedirProductos('${API_URL}/products/categoria/${idPadreActual}'); marcarActivo(this)">Ver Todo</button>`;

    listaHijos.forEach(h => {
        container.innerHTML += `<button class="filter-btn sub-btn" onclick="seleccionarCategoria(${h.id}, this)">${h.name}</button>`;
    });
}

function marcarActivo(btn) {
    btn.parentElement.querySelectorAll('.filter-btn').forEach(b => b.classList.remove('active'));
    btn.classList.add('active');
}

function seleccionarCategoria(id, btn) {
    const contenedorActual = btn.parentElement;
    marcarActivo(btn);

    const url = id === null ? `${API_URL}/products/list` : `${API_URL}/products/categoria/${id}`;
    pedirProductos(url);

    const esPrincipal = (contenedorActual.id === 'categorias-nav');
    
    if (esPrincipal) {
        document.getElementById('subcategorias-nav').innerHTML = '';
        if (document.getElementById('nietos-nav')) document.getElementById('nietos-nav').innerHTML = '';
    }

    if (id !== null) {
        const hijos = obtenerHijos(id);
        if (hijos.length > 0) {
            const proximoDestino = esPrincipal ? 'subcategorias-nav' : 'nietos-nav';
            renderizarBarraNavegacion(proximoDestino, hijos, id);
        }
    }
}

function obtenerURLImagenPrincipal(producto) {
    let nombreArchivo = "default.jpg";
    if (producto.mainImage && producto.mainImage.url) {
        nombreArchivo = producto.mainImage.url;
    } else if (producto.images && producto.images.length > 0) {
        nombreArchivo = producto.images[0].url ? producto.images[0].url : producto.images[0];
    }
    return `${FOLDER_SYSTEM}/${nombreArchivo}`;
}

function aplicarFiltrosYOrden() {
    const texto = document.getElementById('busqueda')?.value.toLowerCase() || "";
    const orden = document.getElementById('ordenPrecioHome')?.value || 'default';
    const tipoFiltro = document.getElementById('tipoFiltroPrecio')?.value || 'todos';
    const p1 = parseFloat(document.getElementById('precioUno')?.value);
    const p2 = parseFloat(document.getElementById('precioDos')?.value);

    if (!productosHome) return;

    let resultado = productosHome.filter(p => {
        const coincideTexto = p.title.toLowerCase().includes(texto);
        let coincidePrecio = true;
        if (tipoFiltro === 'menor' && !isNaN(p1)) coincidePrecio = (p.price <= p1);
        else if (tipoFiltro === 'mayor' && !isNaN(p1)) coincidePrecio = (p.price >= p1);
        else if (tipoFiltro === 'entre' && !isNaN(p1) && !isNaN(p2)) {
            coincidePrecio = (p.price >= p1 && p.price <= p2);
        }

        return coincideTexto && coincidePrecio;
    });

    resultado.sort((a, b) => {
        if (orden === "min") return a.price - b.price;
        if (orden === "max") return b.price - a.price;
        return a.id - b.id; 
    });

    renderizarCards(resultado);
}

function renderizarDestacados(data) {
    const track = document.getElementById('lista-destacados');
    if (!track || !data || data.length === 0) return;
    const listaParaSlider = [...data, ...data];

    track.innerHTML = listaParaSlider.map(p => {
        let desc = p.description || "Sin descripción disponible";
        if (desc.length > 100) desc = desc.substring(0, 97) + "...";
        let fotoUrl = p.imageUrl ? `/images/${p.imageUrl}` : '/images/default.png';
        return `
            <div class="card-destacado" onclick="window.location.href='/detalle?id=${p.productId}'">
                <h3 class="featured-title-top">${p.title}</h3>
                <div class="featured-img-container">
                    <img src="${fotoUrl}" alt="${p.title}" class="card-img" 
                         onerror="this.src='/images/default.png';">
                </div>
                <p class="desc-corta" style="font-size: 0.85rem; color: #bbb; margin: 10px 0;">
                    ${desc}
                </p>
                <div class="featured-actions-container">
                    <p class="product-price">$ ${p.price.toLocaleString('es-AR')}</p>
                    
                    <a href="https://wa.me/5491137869814?text=Hola! Me interesa el ${p.title}" 
                       class="btn-wa-featured" 
                       onclick="event.stopPropagation();" 
                       target="_blank">
                        <img src="/images/WhatsApp.png" alt="WhatsApp" class="wa-icon-featured">
                    </a>
                </div>
            </div>
        `;
    }).join('');
}