package info.thinkingcloud.isearcher;

import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

import org.apache.commons.io.IOUtils;

public class DocumentModel implements Serializable {
	private static final long serialVersionUID = -8747224416107970623L;

	private Path path;

	private String content;

	private BasicFileAttributes attributes;

	public void setContent(String content) {
		this.content = content;
	}

	public void getContent() throws IOException {
		if (content == null && attributes != null) {
			StringWriter writer = new StringWriter();
			FileReader reader = new FileReader(path.toFile());
			IOUtils.copy(reader, writer);
			reader.close();
			writer.flush();
			writer.close();
		}
	}

	public void setPath(Path path) throws IOException {
		if (path.toFile().exists()) {
			this.path = path;
			this.attributes = Files.readAttributes(path,
					BasicFileAttributes.class);
		}
	}
}