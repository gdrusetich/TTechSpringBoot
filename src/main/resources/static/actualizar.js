// Al cargar la página, ponemos una fecha por defecto (hace 15 días)
document.addEventListener("DOMContentLoaded", () => {
    const hoy = new Date();
    hoy.setDate(hoy.getDate() - 15);
    document.getElementById('fechaCorteAudit').value = hoy.toISOString().split('T')[0];
    cargarProductosParaAjuste();
});

async function cargarProductosParaAjuste() {
    const fechaCorte = document.getElementById('fechaCorteAudit').value;
    const tabla = document.getElementById('cuerpoTablaAjuste');
    
    if (!fechaCorte) return;

    try {
        const respuesta = await fetch('/products/all');
        const productos = await respuesta.json();

        // Filtramos: si no tiene fecha o si es anterior a la elegida
        const filtrados = productos.filter(p => {
            if (!p.fechaUltimoPrecio) return true;
            return p.fechaUltimoPrecio < fechaCorte;
        });
        document.getElementById('contador-pendientes').innerText = `${filtrados.length} pendientes`;
        
        tabla.innerHTML = "";
        filtrados.forEach(p => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${p.id}</td>
                <td><strong>${p.title}</strong></td>
                <td>
                    <span style="margin-right:5px;">$</span>
                    <input type="number" id="input-p-${p.id}" class="input-precio-rapido" value="${p.price}">
                </td>
                <td>
                    <button class="btn-guardar-cambio" onclick="actualizarPrecioSolo(${p.id})">Guardar</button>
                    <span id="check-${p.id}" class="status-ok" style="display:none;">✓</span>
                </td>
            `;
            tabla.appendChild(tr);
        });
    } catch (e) {
        console.error("Error cargando auditoría", e);
    }
}

async function actualizarPrecioSolo(id) {
    const nuevoPrecio = document.getElementById(`input-p-${id}`).value;

    try {
        const response = await fetch(`/products/update-price/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ 
                price: nuevoPrecio 
                // Ya no mandamos fechaUltimoPrecio, el servidor la pone solo
            })
        });

        if (response.ok) {
            const check = document.getElementById(`check-${id}`);
            check.style.display = "inline";
            setTimeout(() => {
                // Opacidad para indicar que ya se procesó
                document.getElementById(`input-p-${id}`).closest('tr').style.opacity = "0.4";
                document.getElementById(`input-p-${id}`).disabled = true; // Bloqueamos para evitar doble click
            }, 1000);
        } else {
            alert("Hubo un problema al guardar. Revisá los datos.");
        }
    } catch (e) {
        console.error("Error:", e);
        alert("Error de conexión al actualizar precio");
    }
}