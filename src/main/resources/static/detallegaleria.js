let indexFotoActual = 0;
let escala = 1;
let posX = 0, posY = 0;
let startX = 0, startY = 0;
let moviendo = false;

function inicializarGaleria() {
    if (document.getElementById('modal-galeria')) return;

    const div = document.createElement('div');
    div.id = 'modal-galeria';
    div.className = 'modal-zoom';
    div.innerHTML = `
            <span class="cerrar-zoom">&times;</span>
            <div class="modal-zoom-contenido">
                <div id="wrapper-img">
                    <img id="img-zoom" src="" alt="Zoom" draggable="false">
                </div>
            </div>
            <div class="controles-galeria">
                <button onclick="cambiarFoto(-1)" class="nav-foto">❮</button>
                <span id="contador-fotos"></span>
                <button onclick="cambiarFoto(1)" class="nav-foto">❯</button>
                <div class="separador-v"></div>
                <button onclick="ajustarZoom(0.3)" class="btn-lupa">➕</button>
                <button onclick="ajustarZoom(-0.3)" class="btn-lupa">➖</button>
                <button onclick="resetearImagen()" class="btn-lupa">🔄</button>
            </div>
    `;
    document.body.appendChild(div);

    const modal = document.getElementById('modal-galeria');
    const imgZoom = document.getElementById('img-zoom');
    const wrapper = document.getElementById('wrapper-img');

    // --- CERRAR MODAL ---
    // 1. Al tocar la X
    document.querySelector('.cerrar-zoom').onclick = () => cerrarModal();
    
    // 2. Al tocar afuera (en el fondo gris)
    modal.onclick = (e) => {
        if (e.target === modal || e.target.classList.contains('modal-zoom-contenido')) {
            cerrarModal();
        }
    };

    // --- LÓGICA DE ZOOM POR BOTONES ---
    window.ajustarZoom = (delta) => {
        const nuevaEscala = escala + delta;
        if (nuevaEscala >= 1 && nuevaEscala <= 4) {
            escala = nuevaEscala;
            aplicarTransformacion();
        }
    };

    window.resetearImagen = () => {
        escala = 1;
        posX = 0;
        posY = 0;
        aplicarTransformacion();
    };

    function aplicarTransformacion() {
        imgZoom.style.transform = `translate(${posX}px, ${posY}px) scale(${escala})`;
    }

    // --- NAVEGACIÓN ---
    window.cambiarFoto = (direccion) => {
        const fotos = window.productoActual.images;
        indexFotoActual = (indexFotoActual + direccion + fotos.length) % fotos.length;
        actualizarImagenZoom();
        resetearImagen();
    };

    window.actualizarImagenZoom = () => {
        const fotos = window.productoActual.images;
        const imgObj = fotos[indexFotoActual];
        const folder = window.FOLDER_SYSTEM || '';
        
        let url = imgObj.url;
        let finalPath = url.startsWith('uploads') ? `/${url}` : `${folder}/${url}`;
        
        imgZoom.src = finalPath;
        document.getElementById('contador-fotos').innerText = `${indexFotoActual + 1} / ${fotos.length}`;
    };

    window.cerrarModal = () => {
        modal.style.display = 'none';
        resetearImagen();
    };

    // --- EVENTOS DE ARRASTRE (MOUSE Y TOUCH) ---
    const startDrag = (e) => {
        if (escala <= 1) return;
        moviendo = true;
        startX = (e.pageX || e.touches[0].pageX) - posX;
        startY = (e.pageY || e.touches[0].pageY) - posY;
        wrapper.style.cursor = 'grabbing';
    };

    const doDrag = (e) => {
        if (!moviendo) return;
        e.preventDefault();
        posX = (e.pageX || e.touches[0].pageX) - startX;
        posY = (e.pageY || e.touches[0].pageY) - startY;
        aplicarTransformacion();
    };

    const stopDrag = () => {
        moviendo = false;
        wrapper.style.cursor = 'grab';
    };

    wrapper.addEventListener('mousedown', startDrag);
    window.addEventListener('mousemove', doDrag);
    window.addEventListener('mouseup', stopDrag);
    
    wrapper.addEventListener('touchstart', startDrag, {passive: false});
    window.addEventListener('touchmove', doDrag, {passive: false});
    window.addEventListener('touchend', stopDrag);

    document.addEventListener('keydown', (e) => {
        if (e.key === "Escape") cerrarModal();
        if (e.key === "ArrowLeft") cambiarFoto(-1);
        if (e.key === "ArrowRight") cambiarFoto(1);
    });
}

window.abrirModalZoom = (index) => {
    const modal = document.getElementById('modal-galeria');
    if (!modal) {
        inicializarGaleria();
    }
    indexFotoActual = index;
    actualizarImagenZoom();
    document.getElementById('modal-galeria').style.display = 'flex';
    setTimeout(resetearImagen, 50);
};