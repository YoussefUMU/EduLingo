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
   - [Caso de Uso 1: Realizar Curso](#caso-de-uso-1-realizar-curso)  
   - [Caso de Uso 2: Crear un nuevo curso](#caso-de-uso-2-crear-un-nuevo-curso)  
   - [Caso de Uso 3: Importar un curso](#caso-de-uso-3-importar-un-curso)  
   - [Caso de Uso 4: Activar Premium](#caso-de-uso-4-activar-premium)  
   - [Caso de Uso 5: Publicar un mensaje en la comunidad](#caso-de-uso-5-publicar-un-mensaje-en-la-comunidad)  
   - [Caso de Uso 6: Editar o eliminar mensaje en la comunidad](#caso-de-uso-6-editar-o-eliminar-mensaje-en-la-comunidad)  
   - [Caso de Uso 7: Ver estadísticas del usuario](#caso-de-uso-7-ver-estadísticas-del-usuario)  
   - [Caso de Uso 8: Ver rangos y logros del usuario](#caso-de-uso-8-ver-rangos-y-logros-del-usuario)  
   - [Caso de Uso 9: Recomendar cursos](#caso-de-uso-9-recomendar-cursos)  
3. [Modelado de dominio](#modelado-de-dominio)  

---

## Identificación de casos de uso  

### Actor y Objetivo  

| Actor   | Objetivo |
|---------|---------|
| **Autor** | - Crear cursos e importarlos|
| **Consumidor** | - Realizar curso <br>- Importar un curso <br>- Activar Premium <br>- Publicar mensajes en la comunidad <br>- Editar/Eliminar mensajes de la comunidad <br>- Ver sus estadísticas <br>- Ver sus rangos y logros  |
| **Sistema** | - Recomendar cursos a los usuarios |
---

## Desarrollo de casos de uso  

### Caso de Uso 1: Realizar curso  

**Resumen:**  
Un usuario deberá poder realizar cursos creados por otros usuarios.

**Actor Principal:** Consumidor  

**Precondición**: Usuario registrado y logueado

**Postcondición**: Curso completado o progreso guardado

**Flujo Básico:**  
1. El usuario inicia la app y accede a la ventana login. El sistema tiene en cuenta el dia del acceso a la app y empieza a contar el tiempo de uso en la app.
2. El usuario introduce su nombre de usuario y contraseña.
3. El usuario le da al botón *Aceptar*.
4. El usuario accede a la ventana principal de la app.
5. El usuario que desea inicar un nuevo curso, pulsa el botón *Explorar Cursos*.
6. El usuario accede la Ventana de cursos sin empezar.
7. El usuario selecciona un curso de los que le aperecen en la lista, selecciona una estrategia y pulsa el botón *Empezar*. 
8. Una vez seleccionada la estrategia, el usuario pulsa el botón *Iniciar*.
9. El usuario irá completando las preguntas conforme a la estrategia seleccionada.
10. El usuario finaliza el curso sin gastar sus vidas.

**Extensiones (Flujos Alternativos):**  
- **3a** El usuario no está registrado en la app.
   - El sistema le muestra un mensaje de error.
   - El usuario procede a pulsar el botón *Registro*.
   - El usuario accede a la Ventana de Registro.
   - El usuario introduce los siguientes datos: nombre, contraseña, correo, fecha de nacimiento, nombre de usuario y apellidos.
   - El usuario pulsa el botón *Aceptar* y queda registrado en el sistema.
   - Vuelve al punto 2.
- **5a** El ususario desa continuar con un curso ya empezado.
   - El ususario pulsa el botón *Mis Cursos* de la Ventana Principal.
   - El usuario accede a la Ventana de cursos empezados.
   - El usuario selecciona uno de sus Cursos empezados y pulsa el botón *Continuar*.
   - Vuelve al punto 10.
- **8a** El usuario ha seleccionado la misma estrategia en un curso que ya tenia empezado con esa estrategia.
   - El sistema le muestra un mensaje de error al usuario indicando que no puede hacer el mismo curso a la vez con la misma estrategia.
- **7a.** El usuario no tiene cursos disponibles en su biblioteca interna.  
  - Se notifica al usuario que no hay cursos para seleccionar.  
- **10a** El ususario ha gastado todas sus vidas.
  - Se le notifica al usuario que ha gastado todas sus vidas.
  - Se le notifica al ususaio que con la funcionalidad premium, tendrá vidas infinitas.
  - El usuario tiene que volver a hacer el curso desde el principio.
---
### Caso de Uso 2: Crear un nuevo curso

**Resumen:**
Un autor quiere crear un nuevo curso e imporatrlo.

**Actor Principal:** Autor

**Precondición**: Autor logueado

**Postcondición**: Curso agregado a la biblioteca interna

**Flujo Básico:**
1. Un autor crea su curso en un archivo .yml.
2. El autor inicia la app.
3. El autor ya logueado y dentro de la ventana principal pulsa el botón *Explorar Cursos*.
4. Dentro de la ventana de cursos sin empezar, el usuario seleccionara el botón *Añadir Curso*.
5. El sistema abrirá una ventana de dialogo donde el usuario podrá seleccionar el archivo del curso pertinente.
6. El curso se agregará a su biblioteca interna.
---
### Caso de Uso 3: Importar un curso
- Subsumido en el caso de uso anterior a partir del punto 2 en el flujo principal.
---
### Caso de Uso 4: Activar Premium

**Resumen:**
Un usuario quiere acceder a los servicios premium

**Actor Principal:** Consumidor 

**Precondición**: Usuario logueado

**Postcondición**: Suscripción Premium activa

**Flujo Básico:**

1. Un usuario logueado y dentro de la ventana principal, pulsará el botón *Perfil* y seleccionara la opción *Edulingo Premium*.
2. En la nueva ventana, el usuario selccionara si prefiere suscribirse a un plan mensual o anual.
3. Una vez seleecionado el plan, el usuario pulsará el botón *Activar Premium* y confirmará su suscripción.
4. El sistema procesará el pago.
5. Ahora como usuario premium, el usuario tendrá diversas ventajas como vidas infinitas, también podrá cancelar o renovar su suscripción desde los botones *Renovar Premium* y *Cancelar suscripción*.
---
### Caso de Uso 5: Publicar un mensaje en la comunidad

**Resumen:**
Un usuario quiere publicar un mensaje en la comunidad de Edulingo

**Actor Principal:** Consumidor 

**Precondición**: Usuario logueado

**Postcondición**: Mensaje publicado

**Flujo Básico:**
1. Un usuario logueado y dentro de la ventana principal, pulsará el botón *Comunidad*.
2. En la nueva ventana, el usuario seleccionrá la etiqueta del nuevo mensaje, habiendo tres opciones: Sugerencia, Problema o Crítica.
3. El usuario ahora escribirá su mensaje en el cuadro de texto.
4. Una vez escrito el mensaje, pulsará el botón *Publicar*.
---
### Caso de Uso 6: Buscar un mensaje en la comunidad para editarlo o eliminarlo.

**Resumen:**
Un usuario quiere buscar un mensaje en la comunidad de Edulingo para editarlo o eliminarlo.

**Actor Principal:** Consumidor 

**Precondición**: Usuario logueado

**Postcondición**: Mensaje actualizado o eliminado

**Flujo Básico:**
1. Un usuario logueado y dentro de la ventana principal, pulsará el botón *Comunidad*.
2. En la nueva ventana, el usuario puede o bien filtrar los mensajes según la etiqueta o bien escribir un texto por el que buscarlo en el cuadro de texto supeior y después pulsando el botón *Buscar*.
3. El usuario ahora puede opcionalmente decidir si quiere editar o eliminar el mensaje mediante los botones *editar* y *eliminar* del mensaje.
---
### Caso de Uso 7: Ver estadisticas del usuario

**Resumen:**
Un usuario quiere ver sus estadísticas.

**Actor Principal:** Consumidor 

**Precondición**: Usuario logueado

**Postcondición**: Estadísticas mostradas

**Flujo Básico:**
1. Un usuario logueado y dentro de la ventana principal, pulsará el botón *Perfil* y seleccionará *Mis Estadísticas*.
2. En la nueva ventana, el usuario podrá visualizar sus estadíticas.
---
### Caso de Uso 8: Ver rangos y logros del usuario

**Resumen:**
Un usuario quiere ver sus rangos y logros.

**Actor Principal:** Consumidor 

**Precondición**: Usuario logueado

**Postcondición**: Rangos y logros mostrados

**Flujo Básico:**
1. Un usuario logueado y dentro de la ventana principal, pulsará el botón *Perfil* y seleccionará mis *Mis Rangos y Logros*.
2. En la nueva ventana, el usuario podrá visualizar sus Rangos y Logros
---
### Caso de Uso 9: Recomendar cursos

**Resumen:**
El sistema quiere recomendar cursos a los usuarios

**Actor Principal:** Sistema 

**Precondición**: Se detecta un usuario conectado

**Postcondición**: Se muestran los cursos recomendados

**Flujo Básico:**
1. El sistema detecta que un usuario ha inicado sesión.
2. El sistema accede a la librería interna y muestra en la ventana principal los cursos recomendados para dicho usuario.
---

## Modelado de dominio  
![Modelado de dominio](https://github.com/YoussefUMU/proyecto-pds-24-25/blob/main/Recursos%20Entrega%20Opcional/modelado/ModeladoDominio.jpg?raw=true)

El modelo de dominio refleja las relaciones entre los **usuarios**, los **cursos**, los **bloques**, las **preguntas** y las **estrategias de aprendizaje**. 

- Un **repositorio de usuarios** gestiona a los usuarios, quienes tienen atributos como id, nombre, correo, contraseña, y la fecha de registro.
- Los **cursos** están compuestos por **bloques**, que organizan diferentes **preguntas**. Cada bloque tiene un título y puede contener varias preguntas, las cuales se organizan en tipos como **PreguntaTest** o **PreguntaRellenarHueco**.
- Los **cursos en marcha** mantienen el progreso del curso, indicando el bloque actual y la pregunta actual.
- Los usuarios pueden seleccionar estrategias de aprendizaje, implementadas mediante la interfaz Estrategia, entre estas estrategias 
 se incluyen **RepeticiónEspaciada**, **Secuencial** y **Aleatorio**.
- Además, el sistema tiene un componente de **estadísticas** que registra datos sobre el tiempo de uso y la mejor racha de los usuarios, permitiendo un seguimiento detallado del rendimiento de los mismos.  

---
