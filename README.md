# TTech - Catálogo Digital Autogestionable 🚀

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black)

Sistema de gestión de productos y catálogo online desarrollado con **Spring Boot 3**. Este proyecto permite a un administrador gestionar un inventario complejo con categorías jerárquicas y múltiples imágenes por producto.

## 🎯 Propósito del Proyecto
Este sistema nació como una evolución de los fundamentos adquiridos en el curso **Talento Tech**, desarrollado durante un proceso de aprendizaje autónomo y aplicado a un caso de uso real de consultoría **Freelance**. 

El objetivo es proveer a comercios locales un **muestrario digital profesional** de alto rendimiento, eliminando la fricción de las pasarelas de pago y priorizando una administración de inventario intuitiva y robusta.

## 🛠 Metodología de Desarrollo: Iterativo e Incremental (Agile)
El desarrollo sigue un enfoque **Agile**, priorizando la entrega de valor constante:
* **MVP (Mínimo Viable):** Gestión centralizada de productos y visualización pública.
* **Iteraciones Actuales:** Refinamiento de la lógica de categorías jerárquicas y optimización de la UX en la vista de detalle (Slider dinámico).
* **Futuros Incrementos:** Capa de seguridad con Spring Security y migración de almacenamiento de imágenes a la nube (Cloudinary/S3).

## 🛠️ Stack Tecnológico
* **Backend:** Java 17, Spring Boot 3, Spring Data JPA.
* **Frontend:** HTML5, CSS3 (Bootstrap), JavaScript Vanilla (ES6+).
* **Base de Datos:** PostgreSQL.

## 🗄️ Persistencia y Lógica de Datos
* **Motor:** **PostgreSQL**, seleccionado por su capacidad superior en el manejo de integridad referencial y consultas recursivas.
* **Estructura Jerárquica:** Implementación de un modelo de datos tipo "Árbol" para categorías, permitiendo niveles infinitos de subcategorización.

## 🚀 Desafíos Técnicos Resueltos

### 1. Integridad en Categorías (Self-Referencing)
Se desarrolló una lógica en la capa de servicios para manejar la **recursividad de categorías**. 
> **Punto clave:** Al eliminar una categoría, el sistema ejecuta una reasignación automática de los productos hijos hacia el nivel superior ("Padre"), evitando registros huérfanos y garantizando que el catálogo nunca pierda visibilidad.

### 2. Manejo Multimedia
Gestión de múltiples imágenes por producto con almacenamiento físico en servidor y mapeo dinámico en BD, asegurando una carga eficiente de recursos.

### 3. Frontend Asíncrono
Comunicación optimizada mediante **API Fetch**. El panel administrativo funciona como una Single Page Application (SPA) simplificada, gestionando el estado del DOM sin recargas de página.

## 📐 Estructura de Datos (ERD)
El modelo se basa en tres entidades clave con relaciones normalizadas:
* **Product:** Datos comerciales y técnicos.
* **Category:** Entidad autoreferenciada (`parent_id`).
* **Image:** Relación `@ManyToOne` con productos para galería dinámica.

## 🔧 Instalación y Ejecución
1.  Clonar el repositorio: `git clone https://github.com/gdrusetich/TTechSpringBoot/`
2.  Configurar credenciales de PostgreSQL en `src/main/resources/application.properties`.
3.  Compilar y ejecutar: `mvn spring-boot:run`.
4.  Acceder a: `http://localhost:8081/home`.