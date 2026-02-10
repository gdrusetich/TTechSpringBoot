let campoActual = ''; 
let urlActual = '';
let productoActual = null;
let categoriasSeleccionadas = [];
let imagenSeleccionadaIndex = 0;
let currentIndex = 0;
const API_URL = "http://localhost:8081"; // Ajustá el puerto si es necesario

document.addEventListener("DOMContentLoaded", async () => {
    const params = new URLSearchParams(window.location.search);
    const productId = params.get("id");

    configurarInterfazUsuario();
    configurarBotonInicio();
    if (productId) {
        await cargarDatosDelProducto(productId);
    } else {
        console.error("No se encontró el ID del producto en la URL");
    }
});

async function cargarSimilares(categoriaId, idActual) {
    try {
        const res = await fetch(`http://localhost:8081/products/categoria/${categoriaId}`);
        const data = await res.json();
        const similares = Array.isArray(data) ? data : (data.content || []);
        const filtrados = similares.filter(p => p.id !== idActual);

        const container = document.getElementById('related-container');
        container.innerHTML = "";

        filtrados.forEach(p => {
            let imgUrl = "";
            if (p.images && p.images.length > 0) {
                const mainImg = p.images.find(i => i.isMain) || p.images[0];
                imgUrl = mainImg.url;
            }

            if (imgUrl && !imgUrl.startsWith('http')) {
                imgUrl = `http://localhost:8081/uploads/${imgUrl}`; 
            } else if (!imgUrl) {
                imgUrl = 'http://localhost:8081/uploads/default.jpg';
            }
            const card = document.createElement('div');
            card.className = 'related-card';
            card.onclick = () => window.location.href = `/detalle?id=${p.id}`;
            
            card.innerHTML = `
                    <img src="${imgUrl}" alt="${p.title}">
                    <div class="related-info">
                        <h4>${p.title}</h4>
                        <p class="price">$${p.price}</p>
                    </div>
                `;
            container.appendChild(card);
        });

        if (filtrados.length === 0) {
            container.innerHTML = "<p>No hay productos similares por ahora.</p>";
        } else {
            setTimeout(iniciarAnimacionSlider, 500); 
        }
        } catch (e) {
        console.error("Error en similares:", e);
    }
}

function iniciarAnimacionSlider() {
    const slider = document.getElementById("related-container");
    const cards = document.querySelectorAll(".related-card");
    if (!slider || cards.length <= 4) return;

    setInterval(() => {
        const firstChild = slider.firstElementChild;
        if (!firstChild) return;

        const desplazamiento = firstChild.offsetWidth + 20;
        slider.style.transition = "transform 0.5s ease";
        slider.style.transform = `translateX(-${desplazamiento}px)`;
        setTimeout(() => {
            slider.style.transition = "none"; // Quitamos transición para el salto invisible
            slider.style.transform = "translateX(0)"; // Reseteamos posición
            slider.appendChild(firstChild); // Mandamos la primera tarjeta al final
        }, 500); //500ms
    }, 3000); //3 segundos
}

function irAlLoginConRetorno() {
    localStorage.setItem("returnUrl", window.location.pathname + window.location.search);
    window.location.href = '/login';
}

function cerrarModalCategorias() {
    const modal = document.getElementById("modal-categorias");
    if(modal) modal.style.display = "none";
}

async function abrirEditorCategorias() {
    const modal = document.getElementById("modal-categorias");
    const container = document.getElementById("lista-todas-categorias");
    
    if (!productoActual) return alert("Error: Producto no cargado");
    try {
        const resAll = await fetch('http://localhost:8081/categories/all');
        const todas = await resAll.json();        
        const actualesIds = productoActual.categories ? productoActual.categories.map(c => c.id) : [];
        container.innerHTML = "";
        todas.forEach(cat => {
            const isChecked = actualesIds.includes(cat.id) ? "checked" : "";
            const div = document.createElement("div");
            div.className = "cat-item-admin"; // Cambié el nombre para no chocar con tu CSS viejo
            div.innerHTML = `
                <input type="checkbox" id="cat-${cat.id}" value="${cat.id}" ${isChecked}>
                <label for="cat-${cat.id}">${cat.name}</label>
            `;
            container.appendChild(div);
        });

        modal.style.display = "flex";
    } catch (error) {
        console.error("Error al abrir editor:", error);
    }
}

async function guardarCategorias() {
    const checkboxes = document.querySelectorAll('#lista-todas-categorias input[type="checkbox"]:checked');
    const nuevosIds = Array.from(checkboxes).map(cb => parseInt(cb.value));

    const response = await fetch(`http://localhost:8081/products/${productoActual.id_producto || productoActual.id}/categories`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(nuevosIds)
    });

    if (response.ok) {
        alert("¡Categorías actualizadas!");
        location.reload();
    }
}

function configurarInterfazUsuario() {
    const nombreUsuario = localStorage.getItem("userName");
    const display = document.getElementById("username-display");
    const userGreeting = document.getElementById("user-greeting");
    const btnPerfil = document.getElementById("btn-perfil");
    const btnCerrarSesion = document.getElementById("btn-logout");
    const btnLogin = document.getElementById("btn-login");

    const esLogueado = nombreUsuario && nombreUsuario !== "null";

    if (display) display.innerText = esLogueado ? nombreUsuario : "Invitado";
    if (userGreeting) userGreeting.innerText = esLogueado ? `Hola, ${nombreUsuario}` : "Hola, Invitado";
    
    if (btnPerfil) btnPerfil.style.display = esLogueado ? "inline-block" : "none";
    if (btnCerrarSesion) btnCerrarSesion.style.display = esLogueado ? "inline-block" : "none";
    if (btnLogin) btnLogin.style.display = esLogueado ? "none" : "inline-block";
}

// En tu home.js
function configurarBotonInicio() {
    const btnInicio = document.querySelector(".logo-title"); // Cambiado de .brand a .logo-title
    if (btnInicio) {
        btnInicio.style.cursor = "pointer"; 
        btnInicio.onclick = () => {
            const role = localStorage.getItem("role");
            window.location.href = (role === "ADMIN") ? "/admin" : "/home";
        };
    }
}

async function cargarDatosDelProducto(productId) {
    try {
        const response = await fetch(`http://localhost:8081/products/find-id/${productId}`);
        const producto = await response.json();
        if (!response.ok) throw new Error("Producto no encontrado");
        
        productoActual = producto;

        renderizarInformacionBasica(productoActual);
        renderizarCategorias(productoActual.categories);
        renderizarGaleria(productoActual.images);
        configurarBotonWhatsApp(productoActual.title);

        if (productoActual.categories && productoActual.categories.length > 0) {
            cargarSimilares(productoActual.categories[0].id, productId);
        }

        if (producto.categories && producto.categories.length > 0) {
            const categoriaHija = producto.categories[producto.categories.length - 1];
            
            await cargarSimilares(categoriaHija.id, producto.id);
            
            if (producto.categories.length > 1) { 
                iniciarAnimacionSlider(); 
            }
        }
    } catch (error) {
        console.error("Error cargando el producto:", error);
    }
}

function renderizarInformacionBasica(producto) {
    // Buscamos el H1 que está dentro de product-info (el del producto, no el logo)
    const titleEl = document.querySelector(".product-info h1");
    const descEl = document.getElementById("product-description");
    const priceEl = document.getElementById("product-price");

    if (titleEl) titleEl.innerText = producto.title;
    if (descEl) descEl.innerText = producto.description;
    
    // Si el elemento existe en el HTML (porque Thymeleaf lo permitió), le ponemos el valor
    if (priceEl) {
        priceEl.innerText = `$ ${producto.price}`;
    }
}

function renderizarCategorias(categories) {
    const catContainer = document.getElementById("categories-container");
    if (!catContainer || !Array.isArray(categories)) return;

    catContainer.innerHTML = "";
    categories.forEach(cat => {
        const span = document.createElement("span");
        span.className = "category-badge";
        span.innerText = (typeof cat === 'object') ? cat.name : cat;
        catContainer.appendChild(span);
    });
}

function renderizarGaleria(images) {
    const thumbContainer = document.getElementById("thumbnails-container");
    const mainImg = document.getElementById("main-product-image");
    
    if (!thumbContainer || !mainImg || !images) return;

    thumbContainer.innerHTML = ""; // Esto limpia el HTML previo

    // 1. Creamos el Set AFUERA para llevar la cuenta
    const idsVistos = new Set();

    images.forEach((img, index) => {
        // 2. Si el ID ya está en el Set, saltamos esta imagen (es repetida)
        if (idsVistos.has(img.id)) {
            return; 
        }
        
        // 3. Si es nueva, guardamos el ID en el Set para que no se repita
        idsVistos.add(img.id);

        const imgElement = document.createElement("img");
        const fotoId = img.id;
        const urlFinal = `/uploads/${img.url}`;

        imgElement.src = urlFinal;
        imgElement.className = "thumb-box";
        imgElement.setAttribute('data-image-id', fotoId);

        // Lógica de imagen principal
        if (idsVistos.size === 1) { // El primero que logre entrar al Set
            mainImg.src = urlFinal;
            mainImg.setAttribute('data-image-id', fotoId);
        }

        imgElement.onclick = () => { 
            mainImg.src = urlFinal; 
            mainImg.setAttribute('data-image-id', fotoId);
            console.log("Foto seleccionada para principal ID:", fotoId);
        };
        
        thumbContainer.appendChild(imgElement);
    });
}

function configurarBotonWhatsApp(titulo) {
    const waBtn = document.getElementById("whatsapp-btn");
    if (waBtn) {
        waBtn.onclick = () => {
            const mensaje = `Hola! Me interesa el producto: ${titulo}`;
            window.open(`https://wa.me/TUNUMERO?text=${encodeURIComponent(mensaje)}`, "_blank");
        };
    }
}

async function guardarCambioUnico() {
    const input = document.getElementById('input-dinamico');
    if (!input) return;
    
    const nuevoValor = input.value.trim();
    if (!nuevoValor) return alert("El campo no puede estar vacío");

    try {
        // En Java el endpoint espera el valor como parámetro de URL
        const response = await fetch(urlActual + encodeURIComponent(nuevoValor), { 
            method: 'PUT' 
        });

        if (response.ok) {
            location.reload(); // Recarga para ver los cambios reflejados
        } else {
            alert("Error al actualizar. Status: " + response.status);
        }
    } catch (error) {
        console.error("Error de red:", error);
        alert("Error de conexión al servidor.");
    }
}

function cerrarModalUnico() {
    document.getElementById('modal-edicion-unica').style.display = 'none';
}


function habilitarEdicion(campo) {
    const modal = document.getElementById('modal-edicion-unica');
    const tituloModal = document.getElementById('modal-titulo');
    const contenedor = document.getElementById('contenedor-input');
    const idProducto = window.productId; 

    if (!modal) return;

    let valorActual = "";

    switch (campo) {
        case 'title':
            tituloModal.innerText = "Editar Título";
            urlActual = `/products/update-title/${idProducto}?title=`;
            // IMPORTANTE: Buscamos el H1 correcto
            const h1Producto = document.querySelector(".product-info h1");
            valorActual = h1Producto ? h1Producto.innerText : "";
            contenedor.innerHTML = `<input type="text" id="input-dinamico" style="width:100%; padding:8px;" value="${valorActual}">`;
            break;
            
        case 'price':
            tituloModal.innerText = "Editar Precio";
            urlActual = `/products/update-price/${idProducto}?price=`;
            const pPrecio = document.getElementById("product-price");
            // Limpiamos el "$" y espacios para el input
            valorActual = pPrecio ? pPrecio.innerText.replace('$', '').trim() : "";
            contenedor.innerHTML = `<input type="number" id="input-dinamico" style="width:100%; padding:8px;" value="${valorActual}">`;
            break;
            
        case 'stock':
            tituloModal.innerText = "Editar Stock";
            urlActual = `/products/update-stock/${idProducto}?stock=`;
            const spanStock = document.getElementById("display-stock");
            valorActual = spanStock ? spanStock.innerText : "0";
            contenedor.innerHTML = `<input type="number" id="input-dinamico" style="width:100%; padding:8px;" value="${valorActual}">`;
            break;
            
        case 'description':
            tituloModal.innerText = "Editar Descripción";
            urlActual = `/products/update-description/${idProducto}?description=`;
            const pDesc = document.getElementById('product-description');
            valorActual = pDesc ? pDesc.innerText : "";
            contenedor.innerHTML = `<textarea id="input-dinamico" rows="6" style="width:100%; padding:8px;">${valorActual}</textarea>`;
            break;
    }

    modal.style.display = 'flex';
}

function cerrarModalUnico() {
    const modal = document.getElementById('modal-edicion-unica');
    if (modal) modal.style.display = 'none';
}


async function guardarCambiosFotos() {
    const listaUrls = productData.images.map(img => typeof img === 'string' ? img : img.url);

    try {
        const response = await fetch(`/products/${window.productId}/images`, {
            method: 'PATCH',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(listaUrls)
        });
        
        if (response.ok) console.log("Prioridad actualizada en el servidor");
    } catch (error) {
        console.error("Error al guardar:", error);
    }
}

async function marcarComoPrincipal() {
    const imgElement = document.getElementById('main-product-image');
    const imagenId = imgElement.getAttribute('data-image-id'); 
    const pId = productoActual.idProducto || productoActual.id;

    if (!imagenId || imagenId === "null") {
        alert("Error: Esta imagen no tiene un ID válido en la base de datos.");
        return;
    }

    try {
        const response = await fetch(`/products/${pId}/main-image/${imagenId}`, {
            method: 'PUT'
        });

        if (response.ok) {
            alert("¡Portada actualizada!");
            location.reload();
        } else {
            alert("Error al actualizar la portada.");
        }
    } catch (error) {
        console.error("Error:", error);
    }
}

async function borrarImagenActual() {
    const mainImg = document.getElementById('main-product-image');
    const imageId = mainImg.getAttribute('data-image-id'); 

    if(!imageId) return alert("No se puede identificar la imagen para borrar");

    if(confirm("¿Estás seguro de que quieres eliminar esta imagen?")) {
        try {
            // USAR API_URL para que pegue al puerto 8081
            const response = await fetch(`${API_URL}/products/images/${imageId}`, {
                method: 'DELETE'
            });

            if(response.ok) {
                alert("Imagen eliminada con éxito");
                // En lugar de recargar todo, podrías redirigir o limpiar el src
                location.reload(); 
            } else {
                const errorData = await response.json().catch(() => ({}));
                alert("Error al borrar: " + (errorData.message || "Consulte al administrador"));
            }
        } catch (error) {
            console.error("Error de red:", error);
            alert("No se pudo conectar con el servidor");
        }
    }
}

async function subirNuevaImagen(input) {
    if (input.files && input.files[0]) {
        const productId = new URLSearchParams(window.location.search).get('id');
        const file = input.files[0];

        // 1. Creamos el "paquete" con el archivo
        const formData = new FormData();
        formData.append("file", file);
        formData.append("productId", productId);

        try {
            console.log(`Subiendo imagen para el producto ${productId}...`);
            
            // 2. Enviamos el POST
            const response = await fetch(`${API_URL}/products/images/uploads`, {
                method: 'POST',
                body: formData // No ponemos Headers de Content-Type, el navegador lo hace solo
            });

            if (response.ok) {
                alert("¡Imagen subida con éxito!");
                location.reload(); // Recargamos para ver la nueva foto
            } else {
                alert("Error al subir la imagen");
            }
        } catch (error) {
            console.error("Error:", error);
            alert("No se pudo conectar con el servidor.");
        }
    }
}