# Sistema de Inventario (Android Studio)

# Descripcion
-Aplicacion Movil desarrollado en Android Studio para gestion de inventario interno.
-Permite crear usuarios y darle un rol en especifico.
-Administra tanto usuarios,materiales, movimientos y generacion de reporte.

# Funcionalidades Principales

## Gestion de Usuarios
-Crear, Ver lista y eliminar Usuario.
-Roles disponibles:
-Administrador
-Supervisor
-Empleado

## Formas de creacion de Usuario

### 1.Creado por el Administrador
El administrador puede agregar usuarios y asignarles un rol:
-Administrador
-Supervisor
-Empleado

### 2.Registro directo por parte del Usuario
El Usuario puede crear su cuenta, pero en los roles solo puede seleccionar:
-Supervisor
-Empleado

# Gestion de Materiales
-Agregar Materiales
-Comentarios (Opcionales)
-Ver lista completa 
-Visualizacion de Stock actual y minimo
-Eliminar Materiales

## Registro de Movimiento
-Registrar Entradas o Salidas
-Ver solo materiales que esten agregados
-Cantidad de material
-Comentario opcional

## Reporte
-Seleccion de tipo de movimiento (entrada, salida o ajuste)
-Seleccion de Fecha 
-Generacion de reporte 

# Permisos y Restricciones por Rol
-Se aplica un control de acceso segun el rol del Usuario.Cada Usuario tiene privilegios dentro de la aplicacion.

## Administrador
Acceso Total:
-Gestion Completa de Usuarios (Crear, Ver Lista, Eliminar)
-Gestion de Materiales
-Registro de Movimiento
-Generar Reporte

## Supervisor 
Acceso Intermedio:
-Gestion de Materiales
-Registro de Movimiento
-Generar Reporte
-No tiene acceso a Usuarios (no puede Crear, Ver Lista, Eliminar)

## Empleado
Acceso Limitado:
-Registrar Movimiento
-Consultar Reporte
-No puede gestionar usuarios
-No puede gestionar materiales

# Tecnologias Utilizadas
-Java (Android)
-Android Studio
-SQLite 
-Xml (Iterfaces)
-Gradle

# Script de Tablas SQL
-Tabla Usuarios
CREATE TABLE Usuarios (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  nombre TEXT,
  usuario TEXT,
  contrasena TEXT,
  rol TEXT
  );

-Tabla Materiales
CREATE TABLE materiales (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  nombre TEXT,
  descripcion TEXT,
  stock_actual INTEGER,
  stock_minimo INTEGER
);

-Table Movimientos
CREATE TABLE movimientos (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  tipo TEXT;
  material TEXT,
  cantidad INTEGER,
  comentario TEXT,
  fecha TEXT
  );

# Usuario Administrador creado por defecto
INSERT INTO usuarios (nombre, usuario, contrasena, rol)
VALUES ('Administrador', 'Supervisor', 'Empleado')

# Nota importante sobre el script SQL
>[!WARNING]
El proyecto utiliza SQLite, una base de datos interna integrada en Android.<br><br>
El script SQL mostrado anteriormente en este repositorio está diseñado exclusivamente para SQLite, ya que utiliza sintaxis como:
- INTEGER PRIMARY KEY AUTOINCREMENT
- Tipos como TEXT y INTEGER
- Sin soporte de claves foráneas complejas

>[!TIP]
Importante:
Si deseas usar MySQL, MariaDB o PostgreSQL, este script NO es compatible directamente.
Será necesario adaptarlo a la sintaxis y características del motor de base de datos que desees probar.











