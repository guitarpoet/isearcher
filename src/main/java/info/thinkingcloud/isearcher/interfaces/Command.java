package info.thinkingcloud.isearcher.interfaces;

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
	public void execute();

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
}
