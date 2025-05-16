# 🎓 EduLingo - Plataforma de Aprendizaje Interactiva

![Java](https://img.shields.io/badge/Java-17%2B-orange)
![Swing](https://img.shields.io/badge/GUI-Swing-blue)
![Hibernate](https://img.shields.io/badge/ORM-Hibernate-green)
![SQLite](https://img.shields.io/badge/DB-SQLite-lightblue)
![Maven](https://img.shields.io/badge/Build-Maven-red)

## 📖 Descripción

**EduLingo** es una aplicación de aprendizaje diseñada para ofrecer una experiencia interactiva y efectiva basada en ejercicios rápidos, flashcards y juegos educativos. Inspirada en plataformas como Duolingo y Mochi, la aplicación permite a los usuarios reforzar sus conocimientos en diferentes áreas como idiomas, programación, cultura general, entre otros.

## 👥 Equipo de Desarrollo

### Realizado por:
- **Joaquín Manuel Redón Sánchez**
- **Marcos Davidson Antón**  
- **Youssef Bouaouiouich Souidi**

### Profesor Responsable:
**Jesús Sánchez Cuadrado**

## ✨ Características Principales

- 🎮 **Sistema de Gamificación**: Niveles, experiencia, rangos y logros
- 📚 **Cursos Interactivos**: Preguntas tipo test, drag & drop e imágenes
- 📊 **Estadísticas Detalladas**: Tiempo de estudio, rachas, porcentaje de aciertos
- 🏆 **Sistema de Logros**: Desbloquea logros según tu progreso
- 👑 **Suscripción Premium**: Vidas infinitas y contenido exclusivo
- 🎯 **Estrategias de Aprendizaje**: Secuencial, aleatoria y espaciada
- 💬 **Comunidad**: Sistema de comentarios entre usuarios
- 🤖 **Asistente Virtual**: Consejos personalizados y seguimiento

## 🎯 Funcionalidad Implementada

### Funcionalidad Principal:
- Sistema completo de registro y autenticación de usuarios
- Gestión de cursos con múltiples tipos de preguntas
- Estrategias de aprendizaje personalizables
- Sistema de vidas y progreso
- Persistencia de datos con Hibernate y SQLite

### Funcionalidad Extra:
- **Sistema de Gamificación Completo**: Experiencia, niveles, rangos y logros
- **Suscripción Premium**: Vidas infinitas y planes mensual/anual
- **Comunidad de Usuarios**: Publicación, edición y eliminación de comentarios
- **Asistente Virtual**: Consejos automáticos y ayuda personalizada
- **Sistema de Estadísticas Avanzado**: Métricas detalladas de rendimiento
- **Importación de Cursos YAML**: Carga dinámica de contenido educativo

## 🚀 Instalación


### Instalación y Ejecución:

1. **Clonar el repositorio**
```bash
git clone https://github.com/tuusuario/edulingo.git
cd edulingo
```

2. **Compilar el proyecto**
```bash
mvn clean compile
```

3. **Ejecutar la aplicación**
```bash
Abrir proyecto en Eclipse -> Ejecutar Lanzador
```



## 📋 Casos de Uso

### Actores del Sistema

| Actor | Objetivos |
|-------|-----------|
| **Autor** | - Crear cursos e importarlos |
| **Consumidor** | - Realizar curso<br>- Importar un curso<br>- Activar Premium<br>- Publicar mensajes en la comunidad<br>- Editar/Eliminar mensajes de la comunidad<br>- Ver sus estadísticas<br>- Ver sus rangos y logros |
| **Sistema** | - Recomendar cursos a los usuarios |

### Principales Casos de Uso

#### CU-01: Realizar Curso
**Actor Principal:** Consumidor  
**Precondición:** Usuario registrado y logueado  
**Postcondición:** Curso completado o progreso guardado

**Flujo Básico:**
1. El usuario inicia la app y accede a la ventana login
2. El usuario introduce su nombre de usuario y contraseña
3. El usuario pulsa el botón *Aceptar*
4. El usuario accede a la ventana principal de la app
5. El usuario pulsa el botón *Explorar Cursos*
6. El usuario selecciona un curso, selecciona una estrategia y pulsa *Empezar*
7. El usuario irá completando las preguntas conforme a la estrategia seleccionada
8. El usuario finaliza el curso sin gastar sus vidas

#### CU-02: Importar un Curso
**Actor Principal:** Autor/Consumidor  
**Precondición:** Usuario logueado  
**Postcondición:** Curso agregado a la biblioteca interna

**Flujo Básico:**
1. Un autor crea su curso en un archivo .yml
2. El usuario accede a *Explorar Cursos*
3. Selecciona el botón *Añadir Curso*
4. El sistema abre una ventana de diálogo para seleccionar el archivo
5. El curso se agrega a la biblioteca interna

#### CU-03: Activar Premium
**Actor Principal:** Consumidor  
**Precondición:** Usuario logueado  
**Postcondición:** Suscripción Premium activa

**Flujo Básico:**
1. El usuario pulsa *Perfil* y selecciona *Edulingo Premium*
2. Selecciona plan mensual o anual
3. Pulsa *Activar Premium* y confirma suscripción
4. El sistema procesa el pago
5. Usuario obtiene beneficios premium (vidas infinitas)

#### CU-04: Publicar Mensaje en Comunidad
**Actor Principal:** Consumidor  
**Precondición:** Usuario logueado  
**Postcondición:** Mensaje publicado

**Flujo Básico:**
1. El usuario pulsa el botón *Comunidad*
2. Selecciona etiqueta (Sugerencia/Problema/Crítica)
3. Escribe mensaje en el cuadro de texto
4. Pulsa *Publicar*

#### CU-05: Ver Estadísticas
**Actor Principal:** Consumidor  
**Precondición:** Usuario logueado  
**Postcondición:** Estadísticas mostradas

**Flujo Básico:**
1. Usuario pulsa *Perfil* y selecciona *Mis Estadísticas*
2. Sistema muestra estadísticas detalladas del usuario

#### CU-06: Ver Rangos y Logros
**Actor Principal:** Consumidor  
**Precondición:** Usuario logueado  
**Postcondición:** Rangos y logros mostrados

**Flujo Básico:**
1. Usuario pulsa *Perfil* y selecciona *Mis Rangos y Logros*
2. Sistema muestra rangos y logros del usuario

## 📊 Modelo de Dominio

![Modelado de dominio](https://github.com/YoussefUMU/proyecto-pds-24-25/blob/62eaff7b3c3beaf37e54e21d80181c3da2cc6798/Recursos/ModeladoDominioEduLingo.jpg)

### Descripción del Modelo

El modelo de dominio refleja las relaciones entre los **usuarios**, los **cursos**, los **bloques**, las **preguntas** y las **estrategias de aprendizaje**.

- **Repositorio de usuarios** gestiona a los usuarios con atributos como id, nombre, correo, contraseña y fecha de registro
- **Cursos** están compuestos por **bloques**, que organizan diferentes **preguntas**
- **Preguntas** se organizan en tipos como **PreguntaTest**, **PreguntaImagen** o **PreguntaArrastrar**
- **Cursos en marcha** mantienen el progreso del curso (bloque actual, pregunta actual)
- **Estrategias de aprendizaje** implementadas: **Secuencial**, **Aleatoria** y **Espaciada**
- **Sistema de estadísticas** registra tiempo de uso, racha y rendimiento del usuario
- 
## 🏗️ Arquitectura

El proyecto sigue el patrón **MVC (Modelo-Vista-Controlador)** con las siguientes capas:

```
src/
├── main/java/
│   ├── controlador/     # Lógica de control (ControladorPDS)
│   ├── modelado/        # Entidades del dominio
│   ├── vista/           # Interfaces de usuario (Swing)
│   └── lanzador/        # Punto de entrada de la aplicación
├── main/resources/
│   ├── META-INF/        # Configuración JPA/Hibernate
│   └── recursos/        # Imágenes y recursos
└── test/java/           # Tests unitarios
```

## 🧪 Testing

Ejecutar todos los tests:
```bash
mvn test
```

Ejecutar tests específicos:
```bash
mvn test -Dtest=ControladorPDSTest
```

## 📄 Configuración de Base de Datos

La aplicación utiliza **SQLite** con **Hibernate** como ORM. La base de datos se crea automáticamente en:
```
ProyectoPDS/basedatos.db
```
