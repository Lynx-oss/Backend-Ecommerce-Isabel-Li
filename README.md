# Isabel & Li Ecommerce Backend 

API RESTful completa para un ecommerce de moda, desarrollada con **Java 17** y **Spring Boot**. 

##  Características principales
- **Autenticación y Autorización:** Implementado con Spring Security y JWT (JSON Web Tokens). Manejo de roles `USER` y `ADMIN`.
- **Catálogo de Productos:** CRUD completo, paginación, filtros por precio, talla, nombre y categoría.
- **Gestión de Órdenes:** Creación de carritos de compras, checkout, historial de órdenes y cancelación con restauración automática de stock.
- **Almacenamiento en la Nube:** Integración con Cloudinary para la subida y gestión de imágenes de producto.
- **Base de Datos:** PostgreSQL en producción y soporte para validaciones Bean Validation.

## Tecnologías utilizadas
- **Core:** Java 17, Spring Boot (Web, Data JPA, Security)
- **Base de Datos:** PostgreSQL
- **Seguridad:** JJWT (JSON Web Token), Spring Security
- **Documentación:** Swagger (Springdoc OpenAPI)
- **Otros:** Lombok, Cloudinary API

##  Endpoints Principales

### Autenticación (`/api/auth`)
- `POST /register` - Registro de nuevo usuario
- `POST /login` - Inicio de sesión y generación de token

### Perfil de Usuario (`/api/usuarios`)
- `GET /me` - Obtener perfil del usuario autenticado
- `PUT /me` - Actualizar datos de contacto (nombre, dirección, etc)
- `PATCH /me/password` - Cambiar contraseña

### Productos (`/api/productos`)
- `GET /` - Listar todos los productos (con paginación)
- `GET /buscar` - Búsqueda avanzada con filtros (nombre, precioMin, precioMax, talla)
- `POST /` - Crear producto (Solo Admin)

### Carrito y Órdenes (`/api/carrito`, `/api/ordenes`)
- `POST /api/carrito/items` - Agregar producto al carrito
- `POST /api/ordenes/checkout` - Convertir carrito en orden de compra
- `POST /api/ordenes/{ordenId}/cancelar` - Cancelar orden (restaura inventario)

##  Instalación y ejecución local

1. Clonar el repositorio:
   ```bash
   git clone https://github.com/Lynx-oss/Backend-Ecommerce-Isabel-Li.git
   ```
2. Configurar variables de entorno:
   ```properties
   DB_URL=jdbc:postgresql://localhost:5432/basededatos
   DB_USERNAME=tu_usuario
   DB_PASSWORD=tu_password
   JWT_SECRET=contraseñasecret
   CLOUDINARY_URL=cloudinary://API_KEY:API_SECRET@CLOUD_NAME
   ```
3. Ejecutar el proyecto:
   ```bash
   ./mvnw spring-boot:run
   ```
4. Acceder a la documentación de la API en:
   `http://localhost:8080/swagger-ui.html`

