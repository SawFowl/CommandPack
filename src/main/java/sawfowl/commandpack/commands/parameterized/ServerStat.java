package sawfowl.commandpack.commands.parameterized;

import java.util.List;
import java.util.Locale;

import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;

import net.kyori.adventure.audience.Audience;
import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.data.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractParameterizedCommand;

public class ServerStat extends AbstractParameterizedCommand {

	public ServerStat(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		/**Калька с Nucleus
		 * 
		 * Хедер
		 * Текущий тпс: значение/20
		 * Средний тпс: значение/20 //Можно заменить на варианты по отрезкам времени.
		 * Аптайм
		 * Аптайм JVM // Можно совместить с предыдущим.
		 * 													Пустая строка
		 * Максимум доступной памяти:
		 * Занято памяти:
		 * Используется памяти: значение (% от занятой, % от максимума)
		 * Доступная, но занятая память:
		 * 													Пустая строка
		 * Инфа по мирам
		 * 
		 * 
		 * 
		 * Можно добавить вывод текущего времени на сервере.
		 * Можно дополнить кнопками сверху, которые будут выводить списки плагинов, модов, а так же общую информацию по ОС.
		 * 
		 */
	}

	@Override
	public Parameterized build() {
		return fastBuild();
	}

	@Override
	public String permission() {
		return Permissions.SERVER_STAT;
	}

	@Override
	public String command() {
		return "serverstat";
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return null;
	}

}
