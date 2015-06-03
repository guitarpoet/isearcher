package info.thinkingcloud.isearcher.services;

import info.thinkingcloud.isearcher.Application;
import info.thinkingcloud.isearcher.interfaces.Command;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

@Service
@SuppressWarnings("unchecked")
public class ConfigService {
	private static final Logger logger = LoggerFactory
			.getLogger(ConfigService.class);

	private static final String CONFIG_PATH = "config/options.yml";

	private ArrayList<HashMap<String, Object>> optionGroups;

	private HashMap<String, HashMap<String, Object>> optionMap = new HashMap<String, HashMap<String, Object>>();

	private HashMap<String, Object> config = new HashMap<String, Object>();

	private Options options;

	public Set<Entry<String, Object>> entrySet() {
		return config.entrySet();
	}

	@Autowired
	private ApplicationContext context;

	public String config(String config) {
		return getConfig(config);
	}

	public String config(String config, String defaultValue) {
		String ret = config(config);
		if (ret == null)
			return defaultValue;
		return ret;
	}

	public String getConfig(String key) {
		return getConfig(key, String.class);
	}

	public <T> T getConfig(String key, Class<T> clazz) {
		return (T) config.get(key);
	}

	public void setConfig(String key, Object value) {
		config.put(key, value);
	}

	public String getDefaultIndexTarget() {
		return SystemUtils.getUserDir().getAbsolutePath();
	}

	public String getDefaultIndexDest() {
		return FilenameUtils.concat(SystemUtils.getUserDir().getAbsolutePath(),
				".index");
	}

	@PostConstruct
	public void init() {
		logger.debug("Reading the options configuration from {}", CONFIG_PATH);
		optionGroups = (ArrayList<HashMap<String, Object>>) new Yaml()
				.load(new InputStreamReader(Thread.currentThread()
						.getContextClassLoader()
						.getResourceAsStream(CONFIG_PATH)));

		// Trying to locate configuration file
		String[] paths = {
				"/etc/isearch",
				FilenameUtils.concat(SystemUtils.getUserHome()
						.getAbsolutePath(), ".isearch"),
				SystemUtils.getUserDir().getAbsolutePath() };
		for (String path : paths) {
			try {
				// Load all the system properties to configuration
				loadPropertiesToConfig(System.getProperties());

				// Load the default configuration
				Properties properties = new Properties();
				properties.load(Thread.currentThread().getContextClassLoader()
						.getResourceAsStream("config/default.properties"));

				loadPropertiesToConfig(properties);
				loadConfig(path);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	public Command[] getCommands() {
		ArrayList<Command> commands = new ArrayList<Command>();
		CommandLine cmd = Application.get().getCmd();
		for (Option opt : cmd.getOptions()) {
			HashMap<String, Object> o = optionMap.get(opt.getOpt());
			String c = (String) o.get("command");
			Command command = getCommand(c);
			if (command != null) {
				command.init(o);
				if (opt.hasArg()) {
					command.setArg(cmd.getOptionValue(opt.getOpt()));
				}
				commands.add(command);
			}
		}
		Collections.sort(commands, new Comparator<Command>() {
			@Override
			public int compare(Command o1, Command o2) {
				return o1.priority() - o2.priority();
			}
		});
		return commands.toArray(new Command[0]);
	}

	public Command getCommand(String c) {
		return context.containsBean(c) ? context.getBean(c, Command.class)
				: null;
	}

	private void loadConfig(String path) throws FileNotFoundException,
			IOException {
		File f = new File(FilenameUtils.concat(path, "config.properties"));
		if (f.exists()) {
			Properties p = new Properties();
			p.load(new FileReader(f));
			loadPropertiesToConfig(p);
		}
	}

	private void loadPropertiesToConfig(Properties properties) {
		for (Map.Entry<Object, Object> e : properties.entrySet()) {
			config.put(String.valueOf(e.getKey()), e.getValue());
		}
	}

	public ArrayList<HashMap<String, Object>> getOptionGroups() {
		return optionGroups;
	}

	public Options getOptions() {
		if (this.options != null)
			return options;

		Options ops = new Options();
		for (HashMap<String, Object> group : getOptionGroups()) {
			OptionGroup og = new OptionGroup();

			ArrayList<Object> options = (ArrayList<Object>) group
					.get("options");

			if (options != null) {
				for (Object o : options) {
					HashMap<String, Object> m = (HashMap<String, Object>) o;
					String doc = (String) m.get("doc");
					String sh = (String) m.get("short");
					String opt = (String) m.get("option");
					boolean as = m.containsKey("args") ? (Boolean) m
							.get("args") : false;
					optionMap.put(sh, m);
					og.addOption(new Option(sh, opt, as, doc));
				}

				ops.addOptionGroup(og);
			}
		}
		this.options = ops;
		return ops;
	}
}
