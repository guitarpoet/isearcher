package info.thinkingcloud.isearcher.plugins;

import static org.apache.lucene.document.Field.Store.YES;
import info.thinkingcloud.isearcher.DocumentModel;
import info.thinkingcloud.isearcher.interfaces.Plugin;
import info.thinkingcloud.isearcher.misc.FileContentIterator;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class IndexPlugin implements Plugin {

	private static final Logger logger = LoggerFactory
			.getLogger(IndexPlugin.class);

	public DocumentModel extract(Path path) {
		DocumentModel model = new DocumentModel();
		try {
			model.setPath(path);
		} catch (IOException e) {
			return null;
		}
		return model;
	}

	protected boolean hasExtension(Path path, String extension) {
		return path != null
				&& getMatcher(extension).matches(path.getFileName());
	}

	protected PathMatcher getMatcher(String extension) {
		return FileSystems.getDefault().getPathMatcher("glob:*" + extension);
	}

	public void index(Path path, IndexWriter writer) throws IOException {
		DocumentModel model = extract(path);
		FileContentIterator iter = model.iterate();
		int i = 1;
		while (iter.hasNext()) {
			Document doc = new Document();
			doc.add(new StringField("path", path.toFile().getAbsolutePath(),
					YES));
			doc.add(new LongField("creation_time", model.creationTime()
					.toMillis(), YES));
			doc.add(new LongField("last_modified_time", model
					.lastModifiedTime().toMillis(), YES));
			doc.add(new LongField("last_access_time", model.lastAccessTime()
					.toMillis(), YES));
			doc.add(new LongField("line", i++, YES));
			String text = iter.next();
			doc.add(new StringField("orig_text", text, YES));

			logger.trace("Indexing text {} using index plugin {}",
					filterText(text), this);
			doc.add(new TextField("text", filterText(text), YES));
			doc.add(new TextField("text_lower", filterText(text).toLowerCase(),
					YES));
			writer.addDocument(doc);
		}
	}

	protected String filterText(String text) {
		return text;
	}
}
