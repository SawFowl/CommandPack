package sawfowl.commandpack.commands.parameterized.serverstat;

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
import sawfowl.commandpack.utils.ModContainer;

public class ModInfo extends AbstractInfoCommand {

	public ModInfo(CommandPack plugin) {
		super(plugin);
		this.CHOICES = null;
	}

	public final Value<String> CHOICES;
	public ModInfo(CommandPack plugin, List<ModContainer> mods) {
		super(plugin);
		super.mods = mods;
		CHOICES = Parameter.choices(mods.stream().map(container -> container.getModId()).toArray(String[]::new)).key("Mod").build();
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		if(!getString(context, "Mod").isPresent()) exception(locale, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT);
		if(isPlayer) {
			delay((ServerPlayer) src, locale, consumer -> {
				sendModInfo(src, locale, mods.stream().filter(mod -> mod.getModId().equalsIgnoreCase(getString(context, "Mod").get())).findFirst().orElse(null));
			});
		} else {
			sendModInfo(src, locale, mods.stream().filter(mod -> mod.getModId().equalsIgnoreCase(getString(context, "Mod").get())).findFirst().orElse(null));
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
		return "info";
	}

	@Override
	public String trackingName() {
		return "serverstat";
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return CHOICES == null ? null : Arrays.asList(ParameterSettings.of(CHOICES, false, LocalesPaths.COMMANDS_EXCEPTION_VALUE_NOT_PRESENT));
	}

}
