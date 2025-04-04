package modelado;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

public class ManejadorCursos {
	public List<Path> obtenerFicherosLocales() {
		List<Path> ficheros = new LinkedList<Path>();
		try {
			ficheros = 
				Files.walk(Paths.get("libreria"))
		        .filter(Files::isRegularFile)
		        .filter(path -> path.toString().endsWith(".yaml"))
		        .collect(Collectors.toList());
		} catch (IOException e) {
            e.printStackTrace();
		}
		return ficheros;
	}
	
	public List<Curso> obtenerCursosLocales() {
		List<Path> ficheros = obtenerFicherosLocales();
		List<Curso> cursos = new LinkedList<Curso>();
		for (Path path: ficheros) {
			try {
				cursos.add(parseYAML(path));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return cursos;
	}
	public Curso parseYAML(Path path) throws FileNotFoundException {
		LoaderOptions options = new LoaderOptions();
		Constructor constructor = new Constructor(Curso.class, options);

		TypeDescription bloqueDesc = new TypeDescription(Bloque.class);
		bloqueDesc.addPropertyParameters("preguntas", TestPregunta.class);
		constructor.addTypeDescription(bloqueDesc);

		Yaml yaml = new Yaml(constructor);
		InputStream input = new FileInputStream(path.toFile());
		return yaml.loadAs(input, Curso.class);
	}
	
}
