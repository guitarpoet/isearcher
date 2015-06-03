package info.thinkingcloud.isearcher.commands;

import java.util.HashMap;

import info.thinkingcloud.isearcher.interfaces.Command;

public abstract class BaseCommand implements Command {

	protected String arg;

	protected HashMap<String, Object> config;

	@Override
	public void init(HashMap<String, Object> config) {
		this.config = config;
	}

	@Override
	public int priority() {
		return 0;
	}

	@Override
	public void setArg(String arg) {
		this.arg = arg;
	}

}
