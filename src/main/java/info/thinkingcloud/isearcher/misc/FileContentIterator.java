package info.thinkingcloud.isearcher.misc;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Iterator;

import org.apache.commons.lang.NotImplementedException;

public class FileContentIterator implements Iterator<String> {

	private Path path;

	private BufferedReader reader;

	private String current;

	public FileContentIterator(Path path) throws FileNotFoundException {
		this.path = path;
		if (this.path != null && this.path.toFile().exists()) {
			reader = new BufferedReader(new FileReader(this.path.toFile()));
		}
	}

	@Override
	public boolean hasNext() {
		if (this.reader != null) {
			try {
				current = reader.readLine();
			} catch (IOException e) {
				current = null;
			}
			return current != null;
		}
		return false;
	}

	@Override
	public String next() {
		if (this.reader != null) {
			return current;
		}
		return null;
	}

	@Override
	public void remove() {
		throw new NotImplementedException();
	}

}
