package info.thinkingcloud.isearcher.services;

import info.thinkingcloud.isearcher.interfaces.FileProcessor;
import info.thinkingcloud.isearcher.plugins.IndexPlugin;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.util.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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
	private ApplicationContext context;

	@Autowired
	private DirectoryWalkService directoryWalker;

	private int count;

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() throws ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		String analyzer = config.getConfig("lucene.analyzer");
		Class<? extends Analyzer> analyzerClass = null;
		if (analyzer == null) {
			analyzerClass = WhitespaceAnalyzer.class;
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
				indexDest));
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		indexWriter = new IndexWriter(index, config);
		count = 0;
		directoryWalker.walk(indexTarget);
		indexWriter.commit();
		indexWriter.close();
		logger.info("{} files indexed...", count);
	}

	public List<Document> search(String text, String field, String indexDest,
			int limit) throws IOException {
		Directory index = new NIOFSDirectory(FileSystems.getDefault().getPath(
				indexDest));
		Query q = new QueryBuilder(analyzer).createBooleanQuery(field, text);
		DirectoryReader reader = DirectoryReader.open(index);
		IndexSearcher searcher = new IndexSearcher(reader);
		ScoreDoc[] hits = null;

		TopScoreDocCollector collector = TopScoreDocCollector.create(limit);
		searcher.search(q, collector);
		hits = collector.topDocs().scoreDocs;

		List<Document> docs = new ArrayList<Document>();
		for (ScoreDoc hit : hits) {
			docs.add(searcher.doc(hit.doc));
		}
		return docs;
	}

	@Override
	public void process(Path path) {
		for (IndexPlugin ip : context.getBeansOfType(IndexPlugin.class)
				.values()) {
			try {
				if (ip.accept(path)) {
					ip.index(path, indexWriter);
					count++;
				}
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
	}

	@Override
	public boolean accept(Path path) {
		return true;
	}
}