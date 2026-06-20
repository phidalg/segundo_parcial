# Segundo Parcial de Programación III

<img src="LogoUTN.png" width="60" align="left" />

**Universidad Tecnológica Nacional<br>
Tecnicatura Universitaria en Programación<br>
Programación III**

Estudiante: Pedro Hidalgo - pedro.hidalgo@tupad.utn.edu.ar <br>
Segundo Parcial de Programación III.

Este proyecto consiste en una refactorización del sistema desarrollado en los Trabajos Prácticos anteriores.

---
## Links:

Repositorio en GitHub: https://github.com/phidalg/segundo_parcial
Video explicativo: https://drive.google.com/drive/folders/10WsJ5UgbKgAL3vrv79epG4FY3hkPHkBn?usp=sharing

## Menú en consola:

Desde el menú interactivo se pueden realizar operaciones ABM sobre las entidades producto y categoría. Además, se pueden generar reportes.

## Base de datos H2

La primera vez que se ejecute el proyecto, se creará automáticamente la base de datos en el directorio /data.

## Lombok:

Este proyecto utiliza la librería Lombok para reducir el código repetitivo en las entidades mediante el uso de anotaciones, mejorando la legibilidad y mantenibilidad del código.

## Identidad de las entidades:

Cada entidad define su identidad a través de equals() y hashCode() según el criterio que tiene sentido semántico en el dominio, no necesariamente por el ID  autogenerado.

Categoria y Producto se identifican por nombre. En un catálogo, el nombre funciona como identificador natural de negocio: no tiene sentido que existan dos categorías "Bebidas" o dos productos "Coca-Cola 500ml". Usar el nombre facilita además la búsqueda en colecciones sin necesidad de conocer el ID del producto.

DetallePedido se identifica por su producto. La razón es estructural: dentro de un pedido no puede haber dos líneas para el mismo producto, si se quisieran agregar más unidades, se debería incrementar la cantidad del detalle existente. Al estar almacenados en un HashSet<DetallePedido> dentro de Pedido, esta definición de igualdad garantiza la inexistencia de duplicados.

Pedido y Usuario se identifican por id. A diferencia de los casos anteriores, ninguno tiene un atributo de negocio que garantice unicidad: dos pedidos pueden tener exactamente los mismos productos y el mismo total, y dos usuarios pueden compartir nombre y apellido. El id autogenerado por Base es la única garantía de distinción, ya que representa una transacción o una persona irrepetibles aunque todos sus otros atributos coincidan.

## Implementación de JPA, Hibernate y H2:

- Unidad de persistencia: el proyecto define una persistence unit llamada "persistenceUnit" en src/main/resources/META-INF/persistence.xml. Esa misma unidad se instancia en Main mediante:

      EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistenceUnit");

  y a partir de ella se obtiene el EntityManager para todas las operaciones.

- Proveedor y configuración: se usa Hibernate como proveedor de JPA. El driver configurado es org.h2.Driver y la URL es jdbc:h2:file:./data/jpa_db, por lo que la base H2 se guarda en disco dentro de la carpeta data del proyecto. La base de datos se crea al ejecutar el programa la primera vez.

- Mapeo de entidades y herencia:
  - La clase Base está anotada con @MappedSuperclass y contiene el id marcado con @Id y @GeneratedValue(strategy = GenerationType.IDENTITY). Esto hace que todas las entidades que extienden Base hereden la columna id autogenerada.
  - Las entidades (Categoria, Producto, Pedido, DetallePedido, Usuario) están anotadas con @Entity y @Table y las columnas/relaciones relevantes (Column, JoinColumn) se definen explícitamente.

- Relaciones y cascades:
  - Categoria tiene @OneToMany(mappedBy = "categoria", cascade = {PERSIST, MERGE, REMOVE}, orphanRemoval = true) hacia Producto. Esto asegura que al persistir o eliminar una categoría los cambios se propaguen a sus productos y que productos huérfanos se eliminen.
  - Producto tiene @ManyToOne hacia Categoria con la columna categoria_id (lado propietario).
  - Pedido tiene @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true) hacia DetallePedido; DetallePedido es el lado propietario con @ManyToOne hacia Pedido.
  - Usuario tiene @OneToMany hacia Pedido con cascades PERSIST y MERGE.

- Estado de la base entre ejecuciones: por la propiedad hibernate.hbm2ddl.auto = create, cada ejecución borra y recrea las tablas, por lo que los datos no persisten entre ejecuciones. Para mantener datos cambiar a "update".

