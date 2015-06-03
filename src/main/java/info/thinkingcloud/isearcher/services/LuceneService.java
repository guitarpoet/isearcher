package info.thinkingcloud.isearcher.services;

import info.thinkingcloud.isearcher.interfaces.FileProcessor;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import javax.annotation.PostConstruct;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LuceneService implements FileProcessor {

	private static final Logger logger = LoggerFactory
			.getLogger(LuceneService.class);

	@Autowired
	private ConfigService config;

	private Analyzer analyzer;

	private IndexWriter indexWriter;

	@Autowired
	private DirectoryWalkService directoryWalker;

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() throws ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		String analyzer = config.getConfig("lucene.analyzer");
		Class<? extends Analyzer> analyzerClass = null;
		if (analyzer == null) {
			analyzerClass = StandardAnalyzer.class;
		} else {
			analyzerClass = (Class<? extends Analyzer>) Thread.currentThread()
					.getContextClassLoader().loadClass(analyzer);
		}

		logger.trace("Initializing lucene using analyzer class {}",
				analyzerClass);

		if (analyzerClass != null) {
			this.analyzer = analyzerClass.newInstance();
		} else {
			logger.error(
					"Wrong analyzer configuartion, no analyzer named {} found!",
					analyzer);
			throw new IllegalArgumentException(
					"Wrong analyzer configuartion, no analyzer named "
							+ analyzer + " found!");
		}
		directoryWalker.addFileProcessor(this);
	}

	public void index(String indexTarget, String indexDest, String exclude)
			throws IOException {

		Directory index = new NIOFSDirectory(FileSystems.getDefault().getPath(
				indexTarget));
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		indexWriter = new IndexWriter(index, config);
		directoryWalker.walk(indexTarget);
	}

	@Override
	public void process(Path path) {
		System.out.println(path);
	}

	@Override
	public boolean accept(Path path) {
		return true;
	}
}