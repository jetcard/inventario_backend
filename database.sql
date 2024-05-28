CREATE TABLE proveedor (
    id SERIAL PRIMARY KEY,
    ruc VARCHAR(18) NOT NULL,
    razonsocial VARCHAR(255) NOT NULL
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
    descripcortacomun VARCHAR(255) NOT NULL,
    FOREIGN KEY (responsableId) REFERENCES responsable(id),
    FOREIGN KEY (tipoId) REFERENCES tipo(id),
    FOREIGN KEY (grupoId) REFERENCES grupo(id)
);

CREATE TABLE atributo (
    id SERIAL PRIMARY KEY,
    responsableId INTEGER NOT NULL,
    articuloId INTEGER NOT NULL,
    FOREIGN KEY (responsableId) REFERENCES responsable(id),
    FOREIGN KEY (articuloId) REFERENCES articulo(id)
);

CREATE TABLE atributos (
    id SERIAL PRIMARY KEY,
    atributoid INTEGER NULL,
    nombreatributo VARCHAR(255) NOT NULL,
    FOREIGN KEY (atributoid) REFERENCES atributo(id)
);

CREATE TABLE activo (
    id SERIAL PRIMARY KEY,
    codinventario VARCHAR(50)  NULL,
    modelo VARCHAR(255)  NULL,
    marca VARCHAR(255) NOT NULL,
    nroserie VARCHAR(255) NOT NULL,
    fechaingreso DATE NOT NULL,
    importe NUMERIC NOT NULL,
    moneda VARCHAR(10) NOT NULL,
    grupoId INTEGER NOT NULL,
    articuloId INTEGER NOT NULL,
    responsableId INTEGER NOT NULL,
    tipoId INTEGER NOT NULL,
    FOREIGN KEY (grupoId) REFERENCES grupo(id),
    FOREIGN KEY (articuloId) REFERENCES articulo(id),
    FOREIGN KEY (responsableId) REFERENCES responsable(id),
    FOREIGN KEY (tipoId) REFERENCES tipo(id)
);
