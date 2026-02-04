# 🚀 Deploy to Railway

Este proyecto está listo para desplegarse en Railway.

## Pasos para desplegar

### 1. Crear proyecto en Railway
- Ve a [railway.app](https://railway.app)
- Crea un nuevo proyecto y conecta tu repositorio de GitHub

### 2. Agregar MySQL
- En tu proyecto Railway, click "New" → "Database" → "MySQL"
- Railway configurará automáticamente las variables de MySQL

### 3. Configurar Variables de Entorno
En la configuración de tu servicio, agrega estas variables:

| Variable | Descripción | Ejemplo |
|----------|-------------|---------|
| `JWT_SECRET` | Clave secreta para JWT (min 32 chars) | `MiClaveSecreta123...` |
| `CLOUDINARY_CLOUD_NAME` | Tu cloud name de Cloudinary | `dmg0m9grl` |
| `CLOUDINARY_API_KEY` | API Key de Cloudinary | `534729533516578` |
| `CLOUDINARY_API_SECRET` | API Secret de Cloudinary | `tu-api-secret` |
| `CORS_ALLOWED_ORIGINS` | URLs del frontend (separadas por coma) | `https://tu-frontend.vercel.app` |

> **Nota:** Las variables de MySQL (`MYSQLHOST`, `MYSQLPORT`, `MYSQLDATABASE`, `MYSQLUSER`, `MYSQLPASSWORD`) se configuran automáticamente cuando agregas la base de datos.

### 4. Deploy
Railway detectará automáticamente que es un proyecto Maven y lo desplegará.

## Archivos de configuración creados

- `application-prod.properties` - Configuración de producción con variables de entorno
- `system.properties` - Especifica Java 17
- `Procfile` - Comando de inicio con perfil de producción

## URL del Backend
Una vez desplegado, tu API estará disponible en:
```
https://tu-proyecto.up.railway.app/api/
```

Actualiza tu frontend para usar esta URL como `NEXT_PUBLIC_API_URL`.
