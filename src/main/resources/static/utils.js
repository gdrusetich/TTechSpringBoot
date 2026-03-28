// utils.js

/**
 * Verifica si una categoría (o sus ancestros) coinciden con el ID buscado.
 */
function perteneceAFamilia(categoriaProducto, idBuscado) {
    if (!categoriaProducto) return false;
    if (Number(categoriaProducto.id) === Number(idBuscado)) return true;
    if (categoriaProducto.parent) {
        return perteneceAFamilia(categoriaProducto.parent, idBuscado);
    }
    
    return false;
}


function cumpleFiltros(producto, textoBusqueda, categoriaId = 'todas') {
    const coincideTexto = producto.title.toLowerCase().includes(textoBusqueda.toLowerCase());
    
    let coincideCategoria = true;
    if (categoriaId !== 'todas') {
        coincideCategoria = producto.categories?.some(cat => 
            perteneceAFamilia(cat, categoriaId)
        );
    }
    return coincideTexto && coincideCategoria;
}


function ejecutarFiltradoDinamico(selectorItems, inputId, selectCatId = null) {
    const textoInput = document.getElementById(inputId).value.toLowerCase();
    const elementos = document.querySelectorAll(selectorItems);

    elementos.forEach(el => {
        const contenedorNombre = el.querySelector('h3') || el.querySelector('.nombre-tabla');
        let nombre = "";

        if (contenedorNombre) {
            const inputInterno = contenedorNombre.querySelector('input');
            nombre = inputInterno ? inputInterno.value : contenedorNombre.innerText;
        }

        const coincide = nombre.toLowerCase().includes(textoInput);
        el.style.display = coincide ? "" : "none";
    });
}