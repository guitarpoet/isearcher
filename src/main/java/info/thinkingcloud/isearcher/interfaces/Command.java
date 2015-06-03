package info.thinkingcloud.isearcher.interfaces;

import java.util.HashMap;

/**
 * The command interface
 * 
 * @author jack
 *
 */
public interface Command {

	/**
	 * Execute the command
	 * 
	 * @param args
	 */
	public void execute() throws Exception;

	/**
	 * the priority of this command
	 * 
	 * @return
	 */
	public int priority();

	/**
	 * Set the argument for this command if needed
	 * 
	 * @param arg
	 */
	public void setArg(String arg);

	/**
	 * Initialize this command using the configuration
	 * 
	 * @param config
	 */
	public void init(HashMap<String, Object> config);
}
