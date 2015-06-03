package info.thinkingcloud.isearcher.interfaces;

import java.util.HashMap;

/**
 * The command interface
 * 
 * @author jack
 *
 */
public interface Command {

	public static final int HIGHEST = -10;

	public static final int HIGH = -1;

	public static final int DEFAULT = 0;

	public static final int LOW = 1;

	public static final int LOWEST = 10;

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
