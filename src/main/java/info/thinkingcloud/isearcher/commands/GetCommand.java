package info.thinkingcloud.isearcher.commands;

import java.util.Map.Entry;

import info.thinkingcloud.isearcher.services.ConfigService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("get")
public class GetCommand extends BaseCommand {

	private static final Logger logger = LoggerFactory
			.getLogger(GetCommand.class);

	@Autowired
	private ConfigService configService;

	@Override
	public void execute() {
		if (this.arg.equals("all")) {
			for (Entry<String, Object> e : configService.entrySet()) {
				logger.info("{} = {}", e.getKey(), e.getValue());
			}
		} else {
			logger.info("{} = {}", arg, configService.getConfig(arg));

		}
	}
}
