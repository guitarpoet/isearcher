package info.thinkingcloud.isearcher.plugins;

import java.nio.file.Path;

import org.springframework.stereotype.Component;

@Component("php_plugin")
public class PhpIndexPlugin extends ProgrammingLanguageIndexPlugin {

	@Override
	public boolean accept(Path path) {
		return hasExtension(path, ".php");
	}

}
