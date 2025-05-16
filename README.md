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

## 🚀 Requisitos y Instalación

### Requisitos Previos:
- **Java 17** o superior
- **Apache Maven 3.8+**
- **4GB RAM** mínimo
- **500MB** espacio en disco

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
Abrir proyecto en Eclipse --> Ejecutar "Lanzador"
```


## 📋 Documentación

- **[Casos de Uso](documentacion/casos-de-uso.md)**: Descripción detallada de todas las funcionalidades
- **[Modelo de Dominio](documentacion/modelo-dominio.md)**: Estructura de datos y relaciones del sistema
- **[Arquitectura](documentacion/arquitectura.md)**: Documentación técnica del proyecto

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

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Ver [LICENSE](LICENSE) para más detalles.

---
