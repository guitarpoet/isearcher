package info.thinkingcloud.isearcher.commands;

import info.thinkingcloud.isearcher.services.ConfigService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("set")
public class SetCommand extends BaseCommand {

	@Autowired
	private ConfigService configService;

	@Override
	public int priority() {
		return HIGHEST;
	}

	@Override
	public void execute() {
		configService.setConfig(
				"application." + (String) this.config.get("option"), this.arg);
	}
}
