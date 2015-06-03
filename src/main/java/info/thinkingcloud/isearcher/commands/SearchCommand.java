package info.thinkingcloud.isearcher.commands;

import info.thinkingcloud.isearcher.Application;
import info.thinkingcloud.isearcher.services.ConfigService;
import info.thinkingcloud.isearcher.services.LuceneService;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.document.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("search")
public class SearchCommand extends BaseCommand {

	private static final int DEFAULT_SEARCH_LIMIT = 20;

	private static final Logger logger = LoggerFactory
			.getLogger(SearchCommand.class);

	@Autowired
	private ConfigService configService;

	@Autowired
	private LuceneService lucene;

	@Override
	public int priority() {
		return LOWEST;
	}

	@Override
	public void execute() throws Exception {
		String text = StringUtils.join(Application.get().getCmd().getArgList(),
				" ");
		String c = configService.config("application.case_insensitive", null);
		String search_limit = configService.config("application.search_limit",
				null);
		int limit = DEFAULT_SEARCH_LIMIT;
		if (search_limit != null)
			limit = Integer.parseInt(search_limit);
		String search_field = "text";
		if (c != null) {
			text = text.toLowerCase();
			search_field = "text_lower";
		}
		logger.trace(
				"Searching lucene using text {} though field {} using limit {}",
				new Object[] { text, search_field, limit });
		List<Document> docs = lucene.search(
				text,
				search_field,
				configService.config("application.index_dest",
						configService.getDefaultIndexDest()), limit);
		for (Document doc : docs) {
			logger.info(
					"{} - {}: {}",
					new Object[] { doc.get("path"), doc.get("line"),
							doc.get("orig_text") });
		}
	}
}
