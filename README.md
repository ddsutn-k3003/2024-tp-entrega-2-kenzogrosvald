# Entrega 2
[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-24ddc0f5d75046c5622901739e7c5dd533143b0c8e959d652212380cedb1ea36.svg)](https://classroom.github.com/a/KXg_hGCY)

## Módulo Logística
### Alumno
- Grosvald, Kenzo

### API REST
Se implemtan los siguientes endpoints:
- **Agregar una nueva ruta**

```
    - Método: POST
    - URL: https://two024-tp-entrega-2-kenzogrosvald.onrender.com/rutas
    - Headers: 
        {
            "Content-Type": "application/json"
        }
    - Request body:
        {
            "colaboradorId": 1,
            "heladeraIdOrigen": 1,
            "heladeraIdDestino": 2
        }
    - Response code: 201 Created
    - Response body:
        {
            "id": 0,
            "colaboradorId": 1,
            "heladeraIdOrigen": 1,
            "heladeraIdDestino": 2
        }
```

- **Asignar un nuevo traslado a un colaborador**

```
    - Método: POST
    - URL: https://two024-tp-entrega-2-kenzogrosvald.onrender.com/traslados
    - Headers: 
        {
            "Content-Type": "application/json"
        }
    - Request body:
        {
            "qrVianda": "unQRQueExiste",
            "status": "CREADO",
            "fechaTraslado": "2024-05-15T21:10:40Z",
            "heladeraOrigen": 1,
            "heladeraDestino": 2,
        }
    - Response code: 201 Created
    - Response body:
        {
            "id": 0,
            "colaboradorId": 1,
            "heladeraIdOrigen": 1,
            "heladeraIdDestino": 2
        }
```

- **Obtener un traslado por su ID**

```
    - Método: GET
    - URL: https://two024-tp-entrega-2-kenzogrosvald.onrender.com/traslados/{id}
    - Response code: 200 OK
    - Response body:
        {
            "id": 0,
            "qrVianda": "unQRQueExiste",
            "status": "ENTREGADO",
            "fechaTraslado": "2024-05-15T21:10:40Z",
            "heladeraOrigen": 1,
            "heladeraDestino": 2,
            "colaboradorId": 1
        }
```

- **Modificar el estado de un traslado por su ID**

```
    - Método: PATCH
    - URL: https://two024-tp-entrega-2-kenzogrosvald.onrender.com/traslados/{id}
    - Headers: 
        {
            "Content-Type": "application/json"
        }
    - Request body:
        {
            "qrVianda": "unQRQueExiste",
            "status": "EN_VIAJE",
            "fechaTraslado": "2024-05-15T21:10:40Z",
            "heladeraOrigen": 1,
            "heladeraDestino": 2,
            "colaboradorId": 1
        }
    - Response code: 200 OK
```
