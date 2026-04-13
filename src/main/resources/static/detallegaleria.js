let indexFotoActual = 0;

function inicializarGaleria() {
    // Inyectar  HTML al final del body
    const div = document.createElement('div');
    div.id = 'modal-galeria';
    div.className = 'modal-zoom';
    div.innerHTML = `
        <span class="cerrar-zoom">&times;</span>
        <div class="modal-zoom-contenido">
            <button id="prev-foto" class="nav-foto">❮</button>
            <div id="wrapper-img" style="overflow:hidden; width:100%; height:100%; display:flex; justify-content:center; align-items:center;">
                <img id="img-zoom" src="" alt="Zoom">
            </div>
            <button id="next-foto" class="nav-foto">❯</button>
        </div>
        <div id="contador-fotos"></div>
    `;
    document.body.appendChild(div);

    const modal = document.getElementById('modal-galeria');
    const imgZoom = document.getElementById('img-zoom');
    let escala = 1;

    // Función para abrir
    window.abrirModalZoom = (index) => {
        indexFotoActual = index;
        actualizarImagenZoom();
        modal.style.display = 'flex';
        escala = 1;
        imgZoom.style.transform = `scale(${escala})`;
    };

    function actualizarImagenZoom() {
        const fotos = productoActual.images;
        const imgObj = fotos[indexFotoActual];
        let url = imgObj.url;
        let finalPath = url.startsWith('uploads') ? `/${url}` : `${FOLDER_SYSTEM}/${url}`;
        
        imgZoom.src = finalPath;
        document.getElementById('contador-fotos').innerText = `${indexFotoActual + 1} / ${fotos.length}`;
    }
    // Botones
    document.getElementById('next-foto').onclick = () => {
        indexFotoActual = (indexFotoActual + 1) % productoActual.images.length;
        actualizarImagenZoom();
    };

    document.getElementById('prev-foto').onclick = () => {
        indexFotoActual = (indexFotoActual - 1 + productoActual.images.length) % productoActual.images.length;
        actualizarImagenZoom();
    };

    document.querySelector('.cerrar-zoom').onclick = () => modal.style.display = 'none';

    // Zoom con ruedita
    imgZoom.onwheel = (e) => {
        e.preventDefault();
        escala += e.deltaY * -0.001;
        escala = Math.min(Math.max(1, escala), 4);
        imgZoom.style.transform = `scale(${escala})`;
    };
    // --- ZOOM CON DOS DEDOS (Pinch-to-Zoom) ---
    let distanciaInicial = 0;
    let escalaInicial = 1;
    imgZoom.addEventListener('touchstart', (e) => {
        if (e.touches.length === 2) {
            distanciaInicial = Math.hypot(
                e.touches[0].pageX - e.touches[1].pageX,
                e.touches[0].pageY - e.touches[1].pageY
            );
            escalaInicial = escala;
        }
    }, {passive: true});

    imgZoom.addEventListener('touchmove', (e) => {
        if (e.touches.length === 2) {
            e.preventDefault(); // Evita que la página se mueva mientras hacés zoom
            
            const distanciaActual = Math.hypot(
                e.touches[0].pageX - e.touches[1].pageX,
                e.touches[0].pageY - e.touches[1].pageY
            );

            // Calculamos la nueva escala basada en la diferencia de distancia
            const ratio = distanciaActual / distanciaInicial;
            escala = Math.min(Math.max(1, escalaInicial * ratio), 4);
            
            imgZoom.style.transform = `scale(${escala})`;
        }
    }, {passive: false});

    // Cerrar al tocar el fondo negro o con escape
    modal.onclick = (e) => {
        if (e.target === modal) modal.style.display = 'none';
    };
    document.addEventListener('keydown', (e) => {
        if (e.key === "Escape") modal.style.display = 'none';
    });
}