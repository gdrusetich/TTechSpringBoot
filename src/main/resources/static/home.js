const API_URL = 'http://localhost:8081';
let categoriasData = [];
if (typeof userLogger === 'undefined') {
    var userLogger = null;
}
const tooltip = document.getElementById('tooltip-descripcion');

// 2. El Listener de Carga (Lo que se ejecuta apenas abrís la página)
document.addEventListener("DOMContentLoaded", () => {
    console.log("Página cargada. Limpiando miguita de pan...");
    localStorage.removeItem("returnUrl");     
    cargarCategorias();
    pedirProductos(`${API_URL}/products/list`);
});

function cargarProductos() {
    fetch(`${API_URL}/products/list`)
        .then(response => response.json())
        .then(data => renderizarCards(data))
        .catch(err => {
        console.error("Error:", err);
            document.getElementById('lista-productos').innerHTML = "Error al conectar con el servidor.";
        });
}

function renderizarCards(data) {
    console.log("Lo que llegó del servidor:", data);
    const div = document.getElementById('lista-productos');
    
    // Limpieza de seguridad
    if (!div) return;
    div.innerHTML = ''; 

    if (!data || data.length === 0) {
        div.innerHTML = '<p style="color:white; text-align:center;">No hay productos en esta categoría.</p>';
        return;
    }

    div.innerHTML = data.map(p => {
        // Obtenemos el ID de categoría de forma segura
        const catId = (p.categories && p.categories.length > 0) ? p.categories[0].id : '';

        // LÓGICA DE IMAGEN UNIFICADA
        let fotoUrl;
        if (p.mainImage && p.mainImage.url) {
            fotoUrl = `${API_URL}/uploads/${p.mainImage.url}`;
        } else if (p.images && p.images.length > 0) {
            const primeraFoto = p.images[0].url ? p.images[0].url : p.images[0];
            fotoUrl = `${API_URL}/uploads/${primeraFoto}`;
        } else {
            // Imagen por defecto si no hay nada en la DB
            fotoUrl = '/img/no-photo.png'; 
        }

        const urlDetalle = `/detalle?id=${p.id}`;
        const esUsuarioReal = (window.nombreUsuario && window.nombreUsuario !== 'Invitado');
        
        return `
            <div class="card" data-category-id="${catId}">
                <div class="img-container" onclick="window.location.href='${urlDetalle}'" style="cursor:pointer;">
                    <img src="${fotoUrl}" 
                         alt="${p.title}" 
                         class="card-img" 
                         onerror="this.onerror=null; this.src='/img/no-photo.png';">
                </div>
                <div class="info">
                    <h3 onmouseover="mostrarDescripcion(event, '${(p.description || '').replace(/'/g, "\\'")}')" 
                        onmouseout="ocultarDescripcion()">${p.title}</h3>
                     ${esUsuarioReal 
                        ? `<span class="price">$ ${p.price.toLocaleString('es-AR')}</span>` 
                        : `<p class="consultar-precio">Logueate para ver precio</p>`}

                    <button class="filter-btn" onclick="${esUsuarioReal ? `enviarWhatsApp('${p.title}')` : `abrirModalLogin()`}">
                        ${esUsuarioReal ? 'Pedir WA' : 'Ingresar'}
                    </button>
                </div>
            </div>`;
    }).join('');
}

function pedirProductos(url) {
    fetch(url)
        .then(res => {
            if (!res.ok) {
                // Si el servidor tira 500, esto captura el error antes de intentar el .json()
                throw new Error(`Error del servidor: ${res.status}`);
            }
            return res.json();
        })
        .then(data => {
            // Verificamos que 'data' sea realmente una lista
            if (Array.isArray(data)) {
                renderizarCards(data);
            } else {
                console.error("Los datos recibidos no son una lista:", data);
                document.getElementById('lista-productos').innerHTML = "Formato de datos incorrecto.";
            }
        })
        .catch(err => {
            console.error("Error al pedir productos:", err);
            document.getElementById('lista-productos').innerHTML = "Error al conectar con el servidor.";
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

    // Si es subcategoría ponemos HR, si es nieto algo más sutil o nada
    //container.innerHTML = containerId === 'subcategorias-nav' ? '<hr>' : '<div style="margin-top:5px"></div>';
    
    // El "Ver Todo" solo pide productos, no busca más hijos
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
    // 1. Si existe mainImage, la usamos
    if (producto.mainImage && producto.mainImage.url) {
        return `/uploads/${producto.mainImage.url}`;
    }
    // 2. Si no, si hay imágenes en el array, usamos la primera
    if (producto.images && producto.images.length > 0) {
        return `/uploads/${producto.images[0].url}`;
    }
    // 3. Si no hay nada, una imagen por defecto
    return '/img/no-photo.png';
}