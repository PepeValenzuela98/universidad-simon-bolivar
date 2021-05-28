
CREATE DATABASE UniversidadSimonBolivar
WITH OWNER = Postgres
ENCODING = 'UTF8';

\c universidadsimonbolivar

SET client_encoding = 'UTF8';

CREATE SCHEMA universidad;

SET search_path TO universidad;

DROP SCHEMA public;

CREATE DOMAIN numcontrolalumnos AS VARCHAR (13) NOT NULL CHECK (VALUE ~ '^\d{4}\d{6}ES$');
CREATE DOMAIN numcontroldocentes AS VARCHAR (13) NOT NULL CHECK (VALUE ~ '^\d{4}\d{6}DO$');
CREATE DOMAIN numcontrolsecretarias AS VARCHAR (13) NOT NULL CHECK (VALUE ~ '^\d{4}\d{6}SE$');
CREATE DOMAIN numcontroljefes AS VARCHAR (13) NOT NULL CHECK (VALUE ~ '^\d{4}\d{6}JD$');
CREATE DOMAIN codigocarreras AS VARCHAR(3) NOT NULL CHECK (VALUE ~ '^[A-Z]{3}$');
CREATE DOMAIN nombres AS VARCHAR (50) NOT NULL CHECK (VALUE ~ '^[A-Za-z\s]+$');
CREATE DOMAIN telefonos AS VARCHAR (12) NOT NULL CHECK (VALUE ~ '^\d{3}-\d{3}-\d{4}$');
CREATE DOMAIN correos AS VARCHAR NOT NULL CHECK (VALUE ~ '^[\w_-]+(@){1}[a-z]+\.{1}[a-z]{2,3}$');
CREATE DOMAIN contraseñas AS VARCHAR NOT NULL;
CREATE DOMAIN codigomaterias AS VARCHAR(5) NOT NULL CHECK (VALUE ~ '^[A-Z]{3}[A-Z]\d+$');
CREATE DOMAIN codigogrupo AS VARCHAR(7) NOT NULL CHECK (VALUE ~ '^[A-Z]{3}[A-Z]\d-\d+$');
CREATE DOMAIN codigogrupoalumno AS VARCHAR(18) NOT NULL CHECK(VALUE ~ '^[A-Z]{3}[A-Z]\d\/\d{4}\d{6}ES$');
CREATE DOMAIN dias AS VARCHAR(10) NOT NULL CHECK (VALUE ~ '^(Lunes|Martes|Miercoles|Jueves|Viernes)$');
CREATE DOMAIN horas AS VARCHAR(5) NOT NULL CHECK (VALUE ~ '^\d{2}:\d{2}$');
CREATE DOMAIN fecha AS DATE NOT NULL;
CREATE DOMAIN salones AS VARCHAR(2) NOT NULL CHECK (VALUE ~ '^[A-Z]\d$');

CREATE TABLE jefes_departamento(
	Pk_Numero_Control numcontroljefes,
	Nombre nombres,
	Apellido nombres,
	Telefono telefonos,
	Correo_electronico correos,
	contraseña contraseñas,
	PRIMARY KEY(Pk_Numero_Control)
);

CREATE TABLE secretarias(
	Pk_Numero_Control numcontrolsecretarias,
	Nombre nombres,
	Apellido nombres,
	Telefono telefonos,
	Correo_electronico correos,
	contraseña contraseñas,
	PRIMARY KEY(Pk_Numero_Control)
);

CREATE TABLE carreras(
	Pk_Codigo_Carrera codigocarreras,
	Fk_NumControl_Jefe numcontroljefes UNIQUE,
	Fk_NumControl_Secretaria numcontrolsecretarias UNIQUE,
	Nombre nombres UNIQUE,
	Semestre INT NOT NULL,
	PRIMARY KEY(Pk_Codigo_Carrera),
	FOREIGN KEY (Fk_NumControl_Jefe) REFERENCES jefes_departamento (Pk_Numero_Control)
	ON UPDATE CASCADE
	ON DELETE CASCADE,
	FOREIGN KEY (Fk_NumControl_Secretaria) REFERENCES secretarias (Pk_Numero_Control)
	ON UPDATE CASCADE
	ON DELETE CASCADE
);

CREATE TABLE alumnos(
	Pk_Numero_Control numcontrolalumnos,
	Fk_Carrera codigocarreras,
	Nombre nombres,
	Apellido nombres,
	Telefono telefonos,
	Correo_electronico correos,
	Nombre_Emergencia nombres,
	Apellido_Emergencia nombres,
	Telefono_Emergencia telefonos,
	Semestre INT NOT NULL,
	Contraseña contraseñas,
	PRIMARY KEY(Pk_Numero_Control),
	FOREIGN KEY (Fk_Carrera) REFERENCES carreras (Pk_Codigo_Carrera)
	ON UPDATE CASCADE
	ON DELETE CASCADE
);

CREATE TABLE docentes(
	Pk_Numero_Control numcontroldocentes,
	Nombre nombres,
	Apellido nombres,
	Telefono telefonos,
	Correo_electronico correos,
	contraseña contraseñas,
	PRIMARY KEY(Pk_Numero_Control)
);

CREATE TABLE materias(
	Pk_Codigo_Materia codigomaterias,
	Nombre nombres,
	Numero_Unidades INT NOT NULL,
	Semestre INT NOT NULL,
	PRIMARY KEY(Pk_Codigo_Materia)
);

CREATE TABLE grupos(
	Pk_Grupo codigogrupo UNIQUE,
	Fk_Codigo_Materia codigomaterias,
	Fk_NumControl_Docente numcontroldocentes,
	PRIMARY KEY(Pk_Grupo),
	FOREIGN KEY (Fk_Codigo_Materia) REFERENCES materias (Pk_Codigo_Materia)
	ON UPDATE CASCADE
	ON DELETE CASCADE,
	FOREIGN KEY (Fk_NumControl_Docente) REFERENCES docentes (Pk_Numero_Control)
	ON UPDATE CASCADE
	ON DELETE CASCADE
);

CREATE TABLE horarios_de_grupo(
	Pk_dia dias,
	Fk_Grupo codigogrupo,
	hora_inicio horas,
	hora_final horas,
	salon salones,
	PRIMARY KEY(Pk_dia,Fk_Grupo),
	FOREIGN KEY (Fk_Grupo) REFERENCES grupos (Pk_Grupo)
	ON UPDATE CASCADE
	ON DELETE CASCADE	
);

CREATE TABLE grupo_alumnos(
	Pk_Codigo_Grupo_Alumno codigogrupoalumno UNIQUE,
	Fk_NumControl_Alumno numcontrolalumnos,
	Fk_Grupo codigogrupo,
	promedio_calificacion DECIMAL NOT NULL,
	PRIMARY KEY(Pk_Codigo_Grupo_Alumno),
	FOREIGN KEY (Fk_NumControl_Alumno) REFERENCES alumnos (Pk_Numero_Control)
	ON UPDATE CASCADE
	ON DELETE CASCADE,
	FOREIGN KEY (Fk_Grupo) REFERENCES grupos (Pk_Grupo)
	ON UPDATE CASCADE
	ON DELETE CASCADE
);

CREATE TABLE calificaciones_parciales(
	Fk_Codigo_Grupo_Alumno codigogrupoalumno,
	calificacion INT NOT NULL,
	unidad INT NOT NULL,
	PRIMARY KEY(Fk_Codigo_Grupo_Alumno,unidad),
	FOREIGN KEY (Fk_Codigo_Grupo_Alumno) REFERENCES grupo_alumnos (Pk_Codigo_Grupo_Alumno)
	ON UPDATE CASCADE
	ON DELETE CASCADE
);

CREATE SEQUENCE justificante_seq;

CREATE TABLE justificantes(
	Pk_Codigo_Justificante INT NOT NULL,
	Fk_NumControl_Alumno numcontrolalumnos,
	Fecha_Inicio_Valida fecha,
	Fecha_Fin_Valida fecha,
	Motivo VARCHAR NOT NULL,
	PRIMARY KEY(Pk_Codigo_Justificante),
	FOREIGN KEY (Fk_NumControl_Alumno) REFERENCES alumnos (Pk_Numero_Control)
	ON UPDATE CASCADE
	ON DELETE CASCADE
);

ALTER TABLE universidad.justificantes ALTER COLUMN pk_codigo_justificante SET DEFAULT nextval('justificante_seq');

CREATE TABLE kardex(
	Pk_kardex INT NOT NULL UNIQUE,
	NumControl_Alumno numcontrolalumnos,
	Codigo_Materia codigomaterias,
	promedio_calificacion DECIMAL NOT NULL,
	veces_cursada INT NOT NULL, --Numero de veces que se curso
	semestre_curso INT NOT NULL, --Semestre en el que se curso
	PRIMARY KEY(Pk_kardex)
);