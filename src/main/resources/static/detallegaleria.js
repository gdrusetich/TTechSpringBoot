let indexFotoActual = 0;

function inicializarGaleria() {
    const div = document.createElement('div');
    div.id = 'modal-galeria';
    div.className = 'modal-zoom';
    div.innerHTML = `
            <span class="cerrar-zoom">&times;</span>
            <div class="modal-zoom-contenido">
                <div id="wrapper-img">
                    <img id="img-zoom" src="" alt="Zoom">
                </div>
            </div>
            <div class="controles-galeria">
                <button id="prev-foto" class="nav-foto">❮</button>
                <span id="contador-fotos"></span>
                <button id="next-foto" class="nav-foto">❯</button>
            </div>
    `;
    document.body.appendChild(div);

    const modal = document.getElementById('modal-galeria');
    const imgZoom = document.getElementById('img-zoom');
    
    let escala = 1;
    let posX = 0;
    let posY = 0;
    let startX = 0;
    let startY = 0;
    let moviendo = false;

    window.abrirModalZoom = (index) => {
    const modal = document.getElementById('modal-galeria');
    if (!modal) {
        console.error("No se encontró el elemento modal-galeria");
        return;
    }
    indexFotoActual = index;
    actualizarImagenZoom();
    modal.style.setProperty('display', 'flex', 'important'); 
    
    setTimeout(() => {
        resetearImagen();
    }, 50);
};

function resetearImagen() {
    escala = 1;
    posX = 0;
    posY = 0;
    aplicarTransform();
}

function aplicarTransform() {
    imgZoom.style.transform = `translate(${posX}px, ${posY}px) scale(${escala})`;
}

function actualizarImagenZoom() {
    const producto = window.productoActual;     
    if (!producto || !producto.images) {
        console.error("detallegaleria.js: No se encontró window.productoActual");
        return;
    }
    const folder = window.FOLDER_SYSTEM || (typeof FOLDER_SYSTEM !== 'undefined' ? FOLDER_SYSTEM : '');

    const fotos = producto.images;
    const imgObj = fotos[indexFotoActual];    
    let url = imgObj.url;
    
    let finalPath = url.startsWith('uploads') ? `/${url}` : `${folder}/${url}`;
    imgZoom.src = finalPath;
    document.getElementById('contador-fotos').innerText = `${indexFotoActual + 1} / ${fotos.length}`;
}

    // --- LÓGICA DE ARRASTRAR (PAN) ---
    imgZoom.addEventListener('mousedown', (e) => {
        if (escala > 1) {
            moviendo = true;
            startX = e.clientX - posX;
            startY = e.clientY - posY;
            imgZoom.style.cursor = 'grabbing';
        }
    });

    window.addEventListener('mousemove', (e) => {
        if (!moviendo) return;
        posX = e.clientX - startX;
        posY = e.clientY - startY;
        aplicarTransform();
    });

    window.addEventListener('mouseup', () => {
        moviendo = false;
        if(imgZoom) imgZoom.style.cursor = 'zoom-in';
    });

    // --- ZOOM CON RUEDITA (Centrado en mouse opcional, aquí simple) ---
    imgZoom.onwheel = (e) => {
        e.preventDefault();
        const delta = e.deltaY * -0.001;
        const nuevaEscala = Math.min(Math.max(1, escala + delta), 4);
        
        if (nuevaEscala === 1) resetearImagen();
        else {
            escala = nuevaEscala;
            aplicarTransform();
        }
    };

    // --- TOUCH PARA CELULARES (Mover con un dedo si hay zoom) ---
    imgZoom.addEventListener('touchstart', (e) => {
        if (e.touches.length === 1 && escala > 1) {
            moviendo = true;
            startX = e.touches[0].clientX - posX;
            startY = e.touches[0].clientY - posY;
        }
    }, {passive: true});

    imgZoom.addEventListener('touchmove', (e) => {
        if (moviendo && e.touches.length === 1) {
            posX = e.touches[0].clientX - startX;
            posY = e.touches[0].clientY - startY;
            aplicarTransform();
        }
    }, {passive: false});

    // Botones Navegación
    document.getElementById('next-foto').onclick = (e) => { e.stopPropagation(); indexFotoActual = (indexFotoActual + 1) % productoActual.images.length; actualizarImagenZoom(); resetearImagen(); };
    document.getElementById('prev-foto').onclick = (e) => { e.stopPropagation(); indexFotoActual = (indexFotoActual - 1 + productoActual.images.length) % productoActual.images.length; actualizarImagenZoom(); resetearImagen(); };

    const cerrar = () => { modal.style.display = 'none'; resetearImagen(); };
    document.querySelector('.cerrar-zoom').onclick = cerrar;
    modal.onclick = (e) => { if (e.target === modal || e.target.id === 'wrapper-img') cerrar(); };
    document.addEventListener('keydown', (e) => { if (e.key === "Escape") cerrar(); });

    const siguiente = () => {
        const producto = window.productoActual;
        if (!producto) return;
        indexFotoActual = (indexFotoActual + 1) % producto.images.length;
        actualizarImagenZoom();
        resetearImagen();
    };

    const anterior = () => {
        const producto = window.productoActual;
        if (!producto) return;
        indexFotoActual = (indexFotoActual - 1 + producto.images.length) % producto.images.length;
        actualizarImagenZoom();
        resetearImagen();
    };

    document.getElementById('next-foto').onclick = (e) => { e.stopPropagation(); siguiente(); };
    document.getElementById('prev-foto').onclick = (e) => { e.stopPropagation(); anterior(); };

    document.addEventListener('keydown', (e) => {
        if (modal.style.display === 'flex') { // Solo si el zoom está abierto
            if (e.key === "ArrowRight") siguiente();
            if (e.key === "ArrowLeft") anterior();
            if (e.key === "Escape") modal.style.display = 'none';
        }
    });
}