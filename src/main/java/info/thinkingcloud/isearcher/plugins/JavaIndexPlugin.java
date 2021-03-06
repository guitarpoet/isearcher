package info.thinkingcloud.isearcher.plugins;

import java.nio.file.Path;

import org.springframework.stereotype.Component;

@Component("java_plugin")
public class JavaIndexPlugin extends ProgrammingLanguageIndexPlugin {

	@Override
	public boolean accept(Path path) {
		return hasExtension(path, ".java");
	}
}
