function habilitarEdicion(campo) {
    const contenedor = document.getElementById('contenedor-input');
    const tituloModal = document.getElementById('modal-titulo');
    
    // Limpiamos cualquier editor previo para que no se duplique
    tinymce.remove('#editor-admin');

    if (campo === 'description') {
        tituloModal.innerText = "Editar Descripción";
        // Obtenemos el contenido actual (con HTML) para editarlo
        const contenidoActual = document.getElementById('product-description').innerHTML;
        
        // Creamos el textarea donde se montará TinyMCE
        contenedor.innerHTML = `<textarea id="editor-admin">${contenidoActual}</textarea>`;
        
        // Mostramos el modal
        document.getElementById('modal-edicion-unica').style.display = 'flex';
        
        // Inicializamos TinyMCE sobre ese textarea
        inicializarEditor('#editor-admin');
        
        // Guardamos qué campo estamos editando para el botón Guardar
        window.campoEditando = 'description';
    } else {
        // ... Lógica para precio, stock, etc. (inputs normales) ...
        window.campoEditando = campo;
    }
}