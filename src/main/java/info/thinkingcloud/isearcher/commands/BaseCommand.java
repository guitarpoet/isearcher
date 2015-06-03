package info.thinkingcloud.isearcher.commands;

import info.thinkingcloud.isearcher.interfaces.Command;

public abstract class BaseCommand implements Command {

	protected String arg;

	@Override
	public int priority() {
		return 0;
	}

	@Override
	public void setArg(String arg) {
		this.arg = arg;
	}

}
