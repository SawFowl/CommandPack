package sawfowl.commandpack.configure.locales;

import sawfowl.commandpack.configure.locales.abstractlocale.Buttons;
import sawfowl.commandpack.configure.locales.abstractlocale.CommandExceptions;
import sawfowl.commandpack.configure.locales.abstractlocale.Commands;
import sawfowl.commandpack.configure.locales.abstractlocale.Comments;
import sawfowl.commandpack.configure.locales.abstractlocale.Debug;
import sawfowl.commandpack.configure.locales.abstractlocale.Other;
import sawfowl.commandpack.configure.locales.abstractlocale.Time;
import sawfowl.localeapi.api.Translation;

public interface AbstractLocale extends Translation {

	Buttons getButtons();

	CommandExceptions getCommandExceptions();

	Commands getCommands();

	Comments getComments();

	Debug getDebug();

	Other getOther();

	Time getTime();

}
