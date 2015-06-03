package info.thinkingcloud.isearcher.commands;

import info.thinkingcloud.isearcher.services.ConfigService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("version")
public class VersionCommand extends BaseCommand {

	private static final Logger logger = LoggerFactory
			.getLogger(VersionCommand.class);

	@Autowired
	private ConfigService config;

	@Override
	public void execute() {
		logger.info("{}({})", config.config("application.name"),
				config.config("application.version"));
	}
}