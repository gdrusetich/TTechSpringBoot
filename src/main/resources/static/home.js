const API_URL = 'http://localhost:8081';
let categoriasData = [];
let productosHome = [];

if (typeof userLogger === 'undefined') {
    var userLogger = null;
}
const tooltip = document.getElementById('tooltip-descripcion');

document.addEventListener("DOMContentLoaded", () => {
    console.log("Página cargada. Limpiando miguita de pan...");
    localStorage.removeItem("returnUrl");     
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

    const rutaDefault = `${API_URL}/uploads/default.jpg`;
    const rutaWA = `${API_URL}/uploads/WhatsApp.png`;

    div.innerHTML = data.map(p => {
        const catId = (p.categories && p.categories.length > 0) ? p.categories[0].id : '';
        const esUsuarioReal = (window.nombreUsuario && window.nombreUsuario !== 'Invitado');

        // Lógica de imagen: Principal -> Primera de lista -> Default
        let fotoUrl = rutaDefault;
        if (p.mainImage && p.mainImage.url) {
            fotoUrl = `${API_URL}/uploads/${p.mainImage.url}`;
        } else if (p.images && p.images.length > 0) {
            const primeraFoto = p.images[0].url ? p.images[0].url : p.images[0];
            fotoUrl = `${API_URL}/uploads/${primeraFoto}`;
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
                // LA CLAVE: Guardamos lo que llegó del servidor en nuestra variable global
                productosHome = data; 
                // Y aplicamos el filtro/orden sobre esos nuevos datos
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
        // Posicionamos la ventana cerca del mouse
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

function cargarCategorias() {
    fetch(`${API_URL}/categories/list`)
        .then(res => res.json())
        .then(data => {
            categoriasData = data;
            const navPrincipal = document.getElementById('categorias-nav');
            // CAMBIO AQUÍ: Usamos seleccionarCategoria
            navPrincipal.innerHTML = '<button class="filter-btn active" onclick="seleccionarCategoria(null, this)">Todos</button>';
            
            data.filter(c => c.parent === null).forEach(cat => {
                // CAMBIO AQUÍ: Usamos seleccionarCategoria
                navPrincipal.innerHTML += `<button class="filter-btn" onclick="seleccionarCategoria(${cat.id}, this)">${cat.name}</button>`;
            });
        });
}

function filtrarPorTexto() {
    const texto = document.getElementById('busqueda').value.toLowerCase();
    const cards = document.querySelectorAll('.card');

    cards.forEach(card => {
        const titulo = card.querySelector('h3').innerText.toLowerCase();
        card.classList.toggle('hidden', !titulo.includes(texto));
    });
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
    } else if (contenedorActual.id === 'subcategorias-nav') {
        if (document.getElementById('nietos-nav')) document.getElementById('nietos-nav').innerHTML = '';
    }

    if (id !== null) {
        const hijos = obtenerHijos(id);
        if (hijos.length > 0) {
            const proximoDestino = esPrincipal ? 'subcategorias-nav' : 'nietos-nav';
            renderizarNivel(proximoDestino, hijos, id);
        }
    }
}

function obtenerURLImagenPrincipal(producto) {
    if (producto.mainImage && producto.mainImage.url) {
        return `/uploads/${producto.mainImage.url}`;
    }
    if (producto.images && producto.images.length > 0) {
        return `/uploads/${producto.images[0].url}`;
    }

    return '/img/no-photo.png';
}

function aplicarFiltrosYOrden() {
    const orden = document.getElementById('ordenPrecioHome')?.value || 'default';
    if (!productosHome || productosHome.length === 0) {
        renderizarCards([]);
        return;
    }

    let copiaProductos = [...productosHome];

    copiaProductos.sort((a, b) => {
        if (orden === "min") return a.price - b.price;
        if (orden === "max") return b.price - a.price;
        return a.id - b.id; 
    });

    renderizarCards(copiaProductos);
}