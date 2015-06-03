package info.thinkingcloud.isearcher.services;

import static java.nio.file.FileVisitResult.CONTINUE;
import info.thinkingcloud.isearcher.interfaces.FileProcessor;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The service that will walk every file in the directory and callback when file
 * found.
 * 
 * @author jack
 *
 */
@Service
public class DirectoryWalkService extends SimpleFileVisitor<Path> {
	private static final Logger logger = LoggerFactory
			.getLogger(DirectoryWalkService.class);
	private ArrayList<FileProcessor> processors = new ArrayList<FileProcessor>();
	private PathMatcher matcher;

	@Autowired
	private ConfigService config;

	protected void fire(Path file) {
		for (FileProcessor p : processors) {
			if (p.accept(file))
				p.process(file);
		}
	}

	protected void find(Path file) {
		Path name = file.getFileName();
		if (name != null && matcher.matches(name)) {
			fire(file);
		}
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
		find(file);
		return CONTINUE;
	}

	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
		find(dir);
		return CONTINUE;
	}

	@Override
	public FileVisitResult visitFileFailed(Path file, IOException exc) {
		logger.warn("Failed to visit file {} with reason: {}", file,
				exc.getMessage());
		return CONTINUE;
	}

	public void walk(String directory) throws IOException {
		matcher = FileSystems.getDefault().getPathMatcher(
				"glob:" + config.config("application.file_name", "*.*"));
		Path path = Paths.get(directory);
		if (!path.toFile().exists()) {
			logger.warn("Directory {} is not exist! Failed to walk!", directory);
			return;
		}

		Files.walkFileTree(path, this);
	}

	public void addFileProcessor(FileProcessor processor) {
		processors.add(processor);
	}
}
