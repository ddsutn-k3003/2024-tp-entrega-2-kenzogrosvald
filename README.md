# Entrega 2 

[![Autograding Tests](https://github.com/ddsutn-k3003/2024-tp-entrega-2-kenzogrosvald/actions/workflows/classroom.yml/badge.svg?branch=main)](https://github.com/ddsutn-k3003/2024-tp-entrega-2-kenzogrosvald/actions/workflows/classroom.yml)
## Módulo Logística 
### Alumno
- Grosvald, Kenzo

### API REST
```
https://two024-tp-entrega-2-kenzogrosvald.onrender.com
```
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
<hr>

<div id="footer" align="center">
  <a href="https://www.frba.utn.edu.ar/">
  <img src="https://github.com/ddsutn-k3003/2024-tp-entrega-2-kenzogrosvald/assets/94919997/f35d82b8-fd1a-435a-b1d9-3aad2785b732" style="width:70%; height:auto;">
  </a>
</div>
