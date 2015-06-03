package info.thinkingcloud.isearcher;

import info.thinkingcloud.isearcher.interfaces.Command;
import info.thinkingcloud.isearcher.services.ConfigService;

import java.util.Arrays;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * The application for iSearcher
 * 
 * @author jack
 */
public class Application {

	private static final Logger logger = LoggerFactory
			.getLogger(Application.class);

	private static final Application instance = new Application();

	private CommandLine cmd;

	private ConfigurableApplicationContext context;

	private Application() {
		context = new ClassPathXmlApplicationContext("config/context.xml");
	}

	public CommandLine getCmd() {
		return cmd;
	}

	public static Application get() {
		return instance;
	}

	public static <T> T getService(Class<T> clazz) {
		return get().context.getBean(clazz);
	}

	public static void main(String[] args) {
		logger.trace("Starting application using args {}",
				Arrays.toString(args));

		CommandLineParser parser = new DefaultParser();
		try {
			get().cmd = parser.parse(getService(ConfigService.class)
					.getOptions(), args);

			for (Command command : getService(ConfigService.class)
					.getCommands()) {
				command.execute();
			}
		} catch (ParseException e) {
			logger.error(e.getMessage());
		}
	}

}