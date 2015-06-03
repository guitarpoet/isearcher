package info.thinkingcloud.isearcher.interfaces;

import java.nio.file.Path;

/**
 * The plugin interface
 * 
 * @author jack
 *
 */
public interface Plugin {
	public boolean accept(Path path);
}
