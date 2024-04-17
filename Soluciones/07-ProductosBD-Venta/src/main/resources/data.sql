DELETE
FROM Productos;

INSERT INTO Productos (nombre, precio, stock, categoria)
VALUES ('Laptop', 1000, 100, 'ELECTRONICA'),
       ('Smartphone', 500, 200, 'ELECTRONICA'),
       ('Tablet', 300, 300, 'ELECTRONICA'),
       ('Zapatillas', 100, 400, 'DEPORTE'),
       ('Camiseta', 50, 500, 'DEPORTE'),
       ('Pantalón', 70, 600, 'DEPORTE'),
       ('Reloj', 200, 700, 'MODA'),
       ('Gafas', 150, 800, 'MODA'),
       ('Bolso', 80, 900, 'MODA'),
       ('Libro', 20, 1000, 'OTROS'),
       ('Mochila', 40, 1100, 'OTROS'),
       ('Botella', 10, 1200, 'OTROS'),
       ('Test', 10, 5, 'OTROS');

DELETE
FROM Clientes;

INSERT INTO Clientes (nombre, email)
VALUES ('Juan Pérez', 'juan@email.com'),
       ('María López', 'maria@email.com');


DELETE
FROM Ventas;

INSERT INTO Ventas (id, cliente_id, total)
VALUES ('ffa315af-7333-452f-9e17-72fc9188cabc', 1, 1800);

DELETE
From Lineas_Ventas;

INSERT INTO Lineas_Ventas (id, venta_id, producto_id, cantidad, precio)
VALUES ('162503ef-6a9d-48f2-a860-ac2554f0084b', 'ffa315af-7333-452f-9e17-72fc9188cabc', 1, 1, 1000),
       ('14a6fe51-3419-437b-8b45-ce8eaa8ce868', 'ffa315af-7333-452f-9e17-72fc9188cabc', 2, 1, 500),
       ('c590e9db-5d68-427c-bdff-c432981dfc8d', 'ffa315af-7333-452f-9e17-72fc9188cabc', 3, 1, 300);


