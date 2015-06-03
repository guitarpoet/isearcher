package info.thinkingcloud.isearcher.plugins;

import java.nio.file.Path;

import org.springframework.stereotype.Component;

@Component("md_plugin")
public class MarkdownIndexPlugin extends IndexPlugin {

	@Override
	public boolean accept(Path path) {
		return hasExtension(path, ".md");
	}

}
