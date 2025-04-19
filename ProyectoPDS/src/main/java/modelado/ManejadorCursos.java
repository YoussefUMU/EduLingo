package modelado;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

public class ManejadorCursos {
	public List<Path> obtenerFicherosLocales() {
		List<Path> ficheros = new LinkedList<Path>();
		try {
			ficheros = Files.walk(Paths.get("libreria")).filter(Files::isRegularFile)
					.filter(path -> path.toString().endsWith(".yaml") || path.toString().endsWith(".yml"))
					.collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ficheros;
	}

	public List<Curso> obtenerCursosLocales() {
		List<Path> ficheros = obtenerFicherosLocales();
		List<Curso> cursos = new LinkedList<Curso>();
		for (Path path : ficheros) {
			try {
				cursos.add(parseYAML(path));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return cursos;
	}

	public Curso parseYAML(Path path) throws FileNotFoundException {
		// Usamos el parser básico de SnakeYAML para mayor flexibilidad
		Yaml yaml = new Yaml();
		InputStream input = new FileInputStream(path.toFile());

		// Cargamos el archivo como un Map de Java
		Map<String, Object> yamlMap = yaml.load(input);

		// Creamos el objeto Curso manualmente
		Curso curso = new Curso();

		// Configuramos los atributos básicos del curso
		curso.setId((String) yamlMap.get("id"));
		curso.setNombre((String) yamlMap.get("nombre"));
		curso.setAutor((String) yamlMap.get("autor"));
		curso.setDescripcion((String) yamlMap.get("descripcion"));
		curso.setImagenCurso((String) yamlMap.get("imagenCurso"));
		curso.setCategoria((String) yamlMap.getOrDefault("categoria", "General"));

		// Procesamos los bloques
		List<Map<String, Object>> bloquesMap = (List<Map<String, Object>>) yamlMap.get("bloques");
		List<Bloque> bloques = new ArrayList<>();

		for (Map<String, Object> bloqueMap : bloquesMap) {
			String titulo = (String) bloqueMap.get("titulo");
			List<Map<String, Object>> preguntasMap = (List<Map<String, Object>>) bloqueMap.get("preguntas");
			List<Pregunta> preguntas = procesarPreguntas(preguntasMap);

			Bloque bloque = new Bloque(titulo, preguntas);
			bloques.add(bloque);
		}

		curso.setBloques(bloques);
		return curso;
	}

	private List<Pregunta> procesarPreguntas(List<Map<String, Object>> preguntasMap) {
		List<Pregunta> preguntas = new ArrayList<>();

		for (Map<String, Object> preguntaMap : preguntasMap) {
			String tipo = (String) preguntaMap.getOrDefault("tipo", "test");
			String enunciado = (String) preguntaMap.get("enunciado");

			Pregunta pregunta = null;

			switch (tipo.toLowerCase()) {
			case "test":
				pregunta = procesarPreguntaTest(enunciado, preguntaMap);
				break;
			case "imagen":
				pregunta = procesarPreguntaImagen(enunciado, preguntaMap);
				break;
			case "arrastrar":
				pregunta = procesarPreguntaArrastrar(enunciado, preguntaMap);
				break;
			default:
				pregunta = procesarPreguntaTest(enunciado, preguntaMap);
			}

			// Asignar numPregunta desde el YAML (debe estar definido como Integer)
			Integer numPregunta = (Integer) preguntaMap.get("numPregunta");
			if (numPregunta != null) {
				pregunta.setNumPregunta(numPregunta);
			} else {
				System.err.println("Advertencia: Pregunta sin 'numPregunta' en el YAML.");
				pregunta.setNumPregunta(0); // Valor por defecto o lanzar excepción si querés
			}

			preguntas.add(pregunta);
		}

		return preguntas;
	}

	private TestPregunta procesarPreguntaTest(String enunciado, Map<String, Object> preguntaMap) {
		TestPregunta pregunta = new TestPregunta();
		pregunta.setEnunciado(enunciado);

		List<String> opciones = (List<String>) preguntaMap.get("opciones");
		pregunta.setOpciones(opciones);

		String respuestaCorrecta = (String) preguntaMap.get("respuestaCorrecta");
		pregunta.setRespuestaCorrecta(respuestaCorrecta);

		return pregunta;
	}

	private PreguntaImagen procesarPreguntaImagen(String enunciado, Map<String, Object> preguntaMap) {
		PreguntaImagen pregunta = new PreguntaImagen();
		pregunta.setEnunciado(enunciado);

		List<String> imagenes = (List<String>) preguntaMap.get("imagenes");
		pregunta.setImagenes(imagenes);

		List<String> textos = (List<String>) preguntaMap.get("textos");
		pregunta.setTextos(textos);

		Integer imagenCorrecta = (Integer) preguntaMap.get("imagenCorrecta");
		pregunta.setImagenCorrecta(imagenCorrecta);

		return pregunta;
	}

	private PreguntaArrastrar procesarPreguntaArrastrar(String enunciado, Map<String, Object> preguntaMap) {
		PreguntaArrastrar pregunta = new PreguntaArrastrar();
		pregunta.setEnunciado(enunciado);

		String textoCompleto = (String) preguntaMap.get("textoCompleto");
		List<String> huecos = (List<String>) preguntaMap.get("huecos");

		pregunta.setTextoCompleto(textoCompleto);
		pregunta.setHuecos(huecos);

		return pregunta;
	}
}