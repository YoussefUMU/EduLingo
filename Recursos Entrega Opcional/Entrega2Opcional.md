# Entrega 2 PDS

## Entregable de Prácticas 2  
**Proceso de Desarrollo Software**

### Realizado por:  
- **Youssef Bouaouiouich Souidi** (G2.1)  
- **Joaquín Manuel Redón Sánchez** (G2.1)  
- **Marcos Davidson Antón** (G2.1)  

**Profesor:**  
Jesús Sánchez Cuadrado  

---

## Índice  
1. [Identificación de casos de uso](#identificación-de-casos-de-uso)  
2. [Desarrollo de casos de uso](#desarrollo-de-casos-de-uso)  
   - [Caso de Uso 1: Realizar Cueso](#caso-de-uso-1-seleccionar-la-estrategia-de-aprendizaje)  
   - [Caso de Uso 2: Cancelar un curso en marcha](#caso-de-uso-2-guardar-las-estadísticas-de-uso)
   - [Caso de Uso 3: Agregar un nuevo curso.](#)
3. [Modelado de dominio](#modelado-de-dominio)  

---

## Identificación de casos de uso  

### Actor y Objetivo  

| Actor   | Objetivo |
|---------|---------|
| **Creador** | - Añadir nuevos tipos de preguntas  <br>- Crear cursos  <br>- Compartir cursos creados |
| **Consumidor** | - Realizar curso |
| **Sistema** | - Ofrecer distintos tipos de estrategias  <br>- Guardar estado actual del curso  <br>- Restaurar estado previo del curso  <br>- Guardar las estadísticas de uso  <br>- Permitir la instalación de nuevos cursos en su biblioteca interna |

---

## Desarrollo de casos de uso  

### Caso de Uso 1: Realizar curso  

**Resumen:**  
Un usuario deberá poder realizar cursos creados por otros usuarios.

**Actor Principal:** Consumidor  

**Flujo Básico:**  
1. El usuario inicia la app y accede a la ventana login. El sistema tiene en cuenta el dia del acceso a la app y empieza a contar el tiempo de uso en la app.
2. El usuario introduce su nombre de usuario y contraseña.
3. El usuario le da al botón *Aceptar*.
4. El usuario accede a la ventana principal de la app.
5. El usuario que desea inicar un nuevo curso, pulsa el botón *Cursos*.
6. El usuario accede la Ventana de cursos sin empezar.
7. El ususario selecciona un curso de los que le aperecen en la lista y pulsa el botón *Empezar*. 
8. El usuario accede a la Ventana Iniciar Curso, donde deberá seleccionar una estrategia a seguir de las ofertadas.
9. Una vez seleccionada la estrategia, el usuario pulsa el botón *Iniciar*.
10. El usuario irá completando las preguntas conforme a la estrategia seleccionada.
11. El usuario finaliza el curso sin gastar sus vidas.

**Extensiones (Flujos Alternativos):**  
- **3a** El usuario no está registrado en la app.
   - El sistema le muestra un mensaje de error.
   - El usuario procede a pulsar el botón *Registro*.
   - El usuario accede a la Ventana de Registro.
   - El usuario introduce los siguientes datos: nombre, contraseña, correo, fecha de nacimiento, nombre de usuario y apellidos.
   - El usuario pulsa el botón *Aceptar* y queda registrado en el sistema.
   - Vuelve al punto 2.
- **5a** El ususario desa continuar con un curso ya empezado.
   - El ususario pulsa el botón *Continuar Cursos* de la Ventana Principal.
   - El usuario accede a la Ventana de cursos empezados.
   - El ususario selecciona uno de sus Cursos empezados y pulsa el botón *Continuar*.
   - Vuelve al punto 10.
- **9a** El usuario ha seleccionado la misma estrategia en un curso que ya tenia empezado con esa estrategia.
   - El sistema le muestra un mensaje de error al usuario indicando que no puede hacer el mismo curso a la vez con la misma estrategia.
- **7a.** El usuario no tiene cursos disponibles en su biblioteca interna.  
  - Se notifica al usuario que no hay cursos para seleccionar.  
- **11a** El ususario ha gastado todas sus vidas.
  - Se le notifica al usuario que ha gastado todas sus vidas.
  - Se le notifica al ususaio que con la funcionalidad premium, tendrá vidas infinitas.
  - El usuario tiene que volver el curso desde el principio.
---

### Caso de Uso 2: Cancelar un curso en marcha
**Resumen:**  
Un usuario deberá poder cancelar un curso ya empezado.

**Actor Principal:** Consumidor  

**Flujo Básico:**
1. Un usuario ya logueado y dentro de la ventana principal pulsa el botón *Continuar Cursos*.
2. En la ventana de cursos empezados, el usuario selecciona el curso que desea abandonar, y pulsa el botón *Abandonar*.
3. El sistema le muestra al usuario un mensaje sobre la importancia del esfuerzo.
4. El curso desaparece la ventana de cursos empezados.

---

### Caso de Uso 3: Agregar un nuevo curso

**Resumen:**
Un usuario podrá importar cursos a si biblioteca interna.

**Actor Principal:** Consumidor

**Flujo Básico:**
1. Un usuario ya logueado y dentro de la ventana principal pulsa el botón *Cursos*.
2. Dentro de la ventana de cursos sin empezar, el usuario seleccionara el botón *Añadir Curso*.
3. El sistema abrirá una ventana de dialogo donde el usuario podrá seleccionar el archivo del curso pertinente.
4. El curso se agregará a su biblioteca interna.
---


---

## Modelado de dominio  
![Modelado de dominio](https://github.com/YoussefUMU/proyecto-pds-24-25/blob/90fbfc3be5f7380470d30521d0d30e1a68d188ca/Recursos%20Entrega%20Opcional/modelado/Modelado.drawio.jpg)

El modelo de dominio representa la relación entre **usuarios**, **cursos**, **preguntas** y **estrategias de aprendizaje**.  

- Un **usuario** puede **crear, realizar y compartir cursos**.  
- Los **cursos** contienen diferentes **preguntas** organizadas por tipo.  
- Los **usuarios pueden seleccionar estrategias de aprendizaje** (como repetición o repaso espaciado) para mejorar la memorización.  
- Cada curso **mantiene un estado**, permitiendo **reanudar el progreso en cualquier momento**.  
- La aplicación **registra estadísticas** sobre el uso (como tiempo de estudio o rachas de actividad), ofreciendo un sistema flexible y escalable para el aprendizaje interactivo.  

---
