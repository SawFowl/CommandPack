package sawfowl.commandpack.utils;

import org.apache.logging.log4j.LogManager;

import net.kyori.adventure.text.Component;
import sawfowl.localeapi.api.TextUtils;

public class Logger {

	private org.apache.logging.log4j.Logger logger;
	public Logger() {
		logger = LogManager.getLogger("\033[33mCommandPack\033[0m");
	}

	public void info(Object object) {
		logger.info(object);
	}

	public void info(Object object, Throwable throwable) {
		logger.info(object, throwable);
	}

	public void info(String string) {
		logger.info(string);
	}

	public void info(String string, Throwable throwable) {
		logger.info(string, throwable);
	}

	public void info(Component component) {
		info(TextUtils.clearDecorations(component));
	}

	public void info(Component component, Throwable throwable) {
		info(TextUtils.clearDecorations(component), throwable);
	}

	public void warn(Object object) {
		logger.warn(object);
	}

	public void warn(Object object, Throwable throwable) {
		logger.warn(object, throwable);
	}

	public void warn(String string) {
		logger.warn(string);
	}

	public void warn(String string, Throwable throwable) {
		logger.warn(string, throwable);
	}

	public void warn(Component component) {
		warn(TextUtils.clearDecorations(component));
	}

	public void warn(Component component, Throwable throwable) {
		warn(TextUtils.clearDecorations(component), throwable);
	}

	public void error(Object object) {
		logger.error(object);
	}

	public void error(Object object, Throwable throwable) {
		logger.error(object, throwable);
	}

	public void error(String string) {
		logger.error(string);
	}

	public void error(String string, Throwable throwable) {
		logger.error(string, throwable);
	}

	public void error(Component component) {
		error(TextUtils.clearDecorations(component));
	}

	public void error(Component component, Throwable throwable) {
		error(TextUtils.clearDecorations(component), throwable);
	}

	public void debug(Object object) {
		logger.debug(object);
	}

	public void debug(Object object, Throwable throwable) {
		logger.debug(object, throwable);
	}

	public void debug(String string) {
		logger.debug(string);
	}

	public void debug(String string, Throwable throwable) {
		logger.debug(string, throwable);
	}

	public void debug(Component component) {
		debug(TextUtils.clearDecorations(component));
	}

	public void debug(Component component, Throwable throwable) {
		debug(TextUtils.clearDecorations(component), throwable);
	}

}
