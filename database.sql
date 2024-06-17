CREATE TABLE proveedor (
    id SERIAL PRIMARY KEY,
    razonsocial VARCHAR(255) NOT NULL,
    ruc VARCHAR(18) NOT NULL,
    contacto VARCHAR(255) NOT NULL
);

CREATE TABLE grupo (
    id SERIAL PRIMARY KEY,
    nombregrupo VARCHAR(255) NOT NULL,
    descripgrupo VARCHAR(255) NOT NULL
);

CREATE TABLE articulo (
    id SERIAL PRIMARY KEY,
    nombrearticulo VARCHAR(255) NOT NULL,
    descriparticulo VARCHAR(255) NOT NULL
);

CREATE TABLE responsable (
    id SERIAL PRIMARY KEY,
    arearesponsable VARCHAR(255) NOT NULL,
    nombresyapellidos VARCHAR(255) NOT NULL,
    correo VARCHAR(100) NULL,
    nombreusuario VARCHAR(255) NULL
);

CREATE TABLE tipo (
    id SERIAL PRIMARY KEY,
    nombretipo VARCHAR(255) NOT NULL,
    descriptipo VARCHAR(255) NOT NULL
);

CREATE TABLE dependencia (
    id SERIAL PRIMARY KEY,
    nombredependencia VARCHAR(255) NOT NULL,
    descripdependencia VARCHAR(255) NOT NULL
);

CREATE TABLE comun (
    id SERIAL PRIMARY KEY,
    responsableId INTEGER NOT NULL,
    tipoId INTEGER NOT NULL,
    grupoId INTEGER NOT NULL,
    descripcomun VARCHAR(255) NOT NULL,
    FOREIGN KEY (responsableId) REFERENCES responsable(id),
    FOREIGN KEY (tipoId) REFERENCES tipo(id),
    FOREIGN KEY (grupoId) REFERENCES grupo(id)
);

CREATE TABLE atributo (
    id SERIAL PRIMARY KEY,
    responsableId INTEGER NOT NULL,
    articuloId INTEGER NOT NULL,
    tipoId INTEGER NOT NULL,
    grupoId INTEGER NOT NULL,
    FOREIGN KEY (responsableId) REFERENCES responsable(id),
    FOREIGN KEY (articuloId) REFERENCES articulo(id),
    FOREIGN KEY (tipoId) REFERENCES tipo(id),
    FOREIGN KEY (grupoId) REFERENCES grupo(id)
);

CREATE TABLE atributos (
    id SERIAL PRIMARY KEY,
    atributoid INTEGER NULL,
    nombreatributo VARCHAR(255) NOT NULL,
    FOREIGN KEY (atributoid) REFERENCES atributo(id)
);

CREATE TABLE especifico (
    id SERIAL PRIMARY KEY,
    responsableId INTEGER NOT NULL,
    articuloId INTEGER NOT NULL,
    tipoId INTEGER NOT NULL,
    grupoId INTEGER NOT NULL,
    FOREIGN KEY (responsableId) REFERENCES responsable(id),
    FOREIGN KEY (articuloId) REFERENCES articulo(id),
    FOREIGN KEY (tipoId) REFERENCES tipo(id),
    FOREIGN KEY (grupoId) REFERENCES grupo(id)
);

CREATE TABLE especificos (
    id SERIAL PRIMARY KEY,
    especificoid INTEGER NULL,
    nombreespecifico VARCHAR(255) NOT NULL,
    FOREIGN KEY (especificoid) REFERENCES especifico(id)
);

CREATE TABLE activo (
    id SERIAL PRIMARY KEY,
    responsableId INTEGER NOT NULL,
    proveedorId INTEGER NULL,
    tipoId INTEGER NOT NULL,
    grupoId INTEGER NOT NULL,
    articuloId INTEGER NOT NULL,
    codinventario VARCHAR(50)  NULL,
    modelo VARCHAR(255)  NULL,
    marca VARCHAR(255) NOT NULL,
    nroserie VARCHAR(255) NOT NULL,
    fechaingreso DATE NOT NULL,
    fechaingresostr VARCHAR(20) NULL,
    moneda VARCHAR(10) NOT NULL,
    importe NUMERIC NOT NULL,
    FOREIGN KEY (grupoId) REFERENCES grupo(id),
    FOREIGN KEY (articuloId) REFERENCES articulo(id),
    FOREIGN KEY (responsableId) REFERENCES responsable(id),
    FOREIGN KEY (tipoId) REFERENCES tipo(id),
    FOREIGN KEY (proveedorId) REFERENCES proveedor(id)
);

CREATE TABLE parametros (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    descripcion VARCHAR(255) NOT NULL
);
