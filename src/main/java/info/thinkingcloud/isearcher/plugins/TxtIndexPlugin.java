package info.thinkingcloud.isearcher.plugins;

import java.nio.file.Path;

import org.springframework.stereotype.Component;

@Component("txt_plugin")
public class TxtIndexPlugin extends IndexPlugin {

	@Override
	public boolean accept(Path path) {
		return hasExtension(path, ".txt");
	}

}
