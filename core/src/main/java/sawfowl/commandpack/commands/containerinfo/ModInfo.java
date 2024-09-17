package sawfowl.commandpack.commands.containerinfo;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.Parameter.Value;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import net.kyori.adventure.audience.Audience;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractInfoCommand;
import sawfowl.commandpack.configure.locale.LocalesPaths;

public class ModInfo extends AbstractInfoCommand {

	public ModInfo(CommandPack plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		if(!getString(context, "Mod").isPresent()) exception(locale, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT);
		if(isPlayer) {
			delay((ServerPlayer) src, locale, consumer -> {
				sendModInfo(src, locale, plugin.getAPI().getModContainers().stream().filter(mod -> mod.getModId().equalsIgnoreCase(getString(context, "Mod").get())).findFirst().orElse(null));
			});
		} else {
			sendModInfo(src, locale, plugin.getAPI().getModContainers().stream().filter(mod -> mod.getModId().equalsIgnoreCase(getString(context, "Mod").get())).findFirst().orElse(null));
		}}

	@Override
	public Parameterized build() {
		return fastBuild();
	}

	@Override
	public String permission() {
		return Permissions.SERVER_STAT_STAFF_MODS_INFO;
	}

	@Override
	public String command() {
		return "modinfo";
	}

	@Override
	public String trackingName() {
		return "serverstat";
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		Value<String> CHOICES = Parameter.choices(plugin.getAPI().getModContainers().stream().map(container -> container.getModId()).toArray(String[]::new)).key("Mod").build();
		return plugin.getAPI().getModContainers().isEmpty() ? null : Arrays.asList(ParameterSettings.of(CHOICES, false, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT));
	}

}
