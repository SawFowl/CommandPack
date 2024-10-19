package sawfowl.commandpack.configure.locale.locales.ru;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.configure.locale.locales.AbstractLocale;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.Buttons;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.CommandExceptions;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.Commands;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.Comments;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.Debug;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.Other;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.Time;

@ConfigSerializable
public class ImplementRuPluginLocale implements AbstractLocale {

	public ImplementRuPluginLocale(){}

	@Setting("Buttons")
	private ImplementButtons buttons = new ImplementButtons();
	@Setting("CommandExceptions")
	private ImplementCommandExceptions exceptions = new ImplementCommandExceptions();
	@Setting("Commands")
	private ImplementCommands commands = new ImplementCommands();
	@Setting("Comments")
	private ImplementComments comments = new ImplementComments();
	@Setting("Debug")
	private ImplementDebug debug = new ImplementDebug();
	@Setting("Other")
	private ImplementOther other = new ImplementOther();
	@Setting("Time")
	private ImplementTime time = new ImplementTime();

	@Override
	public Buttons getButtons() {
		return buttons;
	}

	@Override
	public CommandExceptions getCommandExceptions() {
		return exceptions;
	}

	@Override
	public Commands getCommands() {
		return commands;
	}

	@Override
	public Comments getComments() {
		return comments;
	}

	@Override
	public Debug getDebug() {
		return debug;
	}

	@Override
	public Other getOther() {
		return other;
	}

	@Override
	public Time getTime() {
		return time;
	}

}
