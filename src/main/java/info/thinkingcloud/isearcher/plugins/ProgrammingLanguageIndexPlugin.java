package info.thinkingcloud.isearcher.plugins;

import java.util.Arrays;
import java.util.List;

/**
 * This is the programming language index plugin
 * 
 * @author jack
 *
 */
public abstract class ProgrammingLanguageIndexPlugin extends IndexPlugin {

	private static final List<String> opers = Arrays.asList(",", ".", ";", "!",
			"~", "@", "#", "$", "%", "^", "&", "*", "(", ")", "_", "+", "=",
			"\\", "[", "]", "/");

	@Override
	protected String filterText(String text) {
		for (String o : opers) {
			text = text.replace(o, " ");
		}
		return text;
	}
}
