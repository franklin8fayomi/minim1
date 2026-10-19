# MINIM1 - Tienda del campus (DSA)

Prueba piloto de la zona de restauración del campus PMT: productos, usuarios y pedidos.

## Estructura

```
src/main/java/edu/upc/dsa/
├── ProductManager.java        Interfaz con las operaciones (Parte I.1)
├── ProductManagerImpl.java    Fachada Singleton con log4j (Parte I.2)
├── Main.java                  Arranca el servidor REST (Grizzly + Jersey + Swagger)
├── models/                    Product, User, Order, LP (línea de pedido)
├── exceptions/                UserNotFound, ProductNotFound, EmptyOrder
└── services/ShopService.java  Servicio REST (Parte II.1)
src/main/resources/log4j.properties
src/test/java/edu/upc/dsa/ProductManagerTest.java   Tests JUnit (Parte I.3)
Minim1.postman_collection.json                      Colección Postman (Parte II.2)
parteIII.txt                                        Tablas y relaciones (Parte III)
```

## Operaciones

| Operación | Método fachada | Endpoint REST |
|---|---|---|
| Productos por precio (ascendente) | `getProductsByPrice()` | `GET /dsaApp/shop/products/price` |
| Realizar un pedido | `addOrder(Order)` | `POST /dsaApp/shop/orders` |
| Servir un pedido (orden de llegada) | `deliverOrder()` | `PUT /dsaApp/shop/orders/deliver` |
| Pedidos realizados de un usuario | `getOrdersByUser(id)` | `GET /dsaApp/shop/users/{id}/orders` |
| Productos por ventas (descendente) | `getProductsBySales()` | `GET /dsaApp/shop/products/sales` |
| Añadir producto / usuario / listar | `addProduct`, `addUser`, `getAllProducts` | `POST /shop/products`, `POST /shop/users`, `GET /shop/products` |

Ejemplo de cuerpo para `POST /dsaApp/shop/orders`:

```json
{ "userId": "NIF1", "lines": [ { "quantity": 2, "productId": "C1" }, { "quantity": 1, "productId": "B1" } ] }
```

## Cómo ejecutar

```bash
mvn test        # tests JUnit
mvn exec:java   # servidor en http://localhost:8080/dsaApp/
```

- Swagger UI: http://localhost:8080/swagger/?url=/dsaApp/swagger.json
- Postman: importar `Minim1.postman_collection.json` y ejecutarla en orden con el servidor recién arrancado.
