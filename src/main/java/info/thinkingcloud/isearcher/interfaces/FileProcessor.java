package info.thinkingcloud.isearcher.interfaces;

import java.nio.file.Path;

public interface FileProcessor {
	public boolean accept(Path path);

	public void process(Path path);
}
