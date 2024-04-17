DELETE
FROM productos;

INSERT INTO productos (nombre, precio, stock, categoria)
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
       ('Botella', 10, 1200, 'OTROS');
