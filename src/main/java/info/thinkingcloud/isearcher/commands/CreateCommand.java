package info.thinkingcloud.isearcher.commands;

import info.thinkingcloud.isearcher.services.ConfigService;
import info.thinkingcloud.isearcher.services.LuceneService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("create")
public class CreateCommand extends BaseCommand {

	private static Logger logger = LoggerFactory.getLogger(CreateCommand.class);

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
		String indexTarget = configService.config("application.index_folder",
				configService.getDefaultIndexTarget());
		String indexDest = configService.config("application.index_dest",
				configService.getDefaultIndexDest());
		logger.debug("Storing index for folder {} to folder {}", indexTarget,
				indexDest);
		lucene.index(indexTarget, indexDest, "");
	}
}
