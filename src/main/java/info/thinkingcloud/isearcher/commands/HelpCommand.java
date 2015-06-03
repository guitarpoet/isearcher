package info.thinkingcloud.isearcher.commands;

import info.thinkingcloud.isearcher.services.ConfigService;

import org.apache.commons.cli.HelpFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("help")
public class HelpCommand extends BaseCommand {

	@Autowired
	private ConfigService config;

	@Override
	public void execute() {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp(config.config("application.name")
				+ " [options] [text]", config.getOptions());
	}
}