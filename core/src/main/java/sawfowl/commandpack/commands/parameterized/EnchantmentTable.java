package sawfowl.commandpack.commands.parameterized;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Cause;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.item.inventory.EnchantItemEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Container;
import org.spongepowered.api.item.inventory.ContainerTypes;
import org.spongepowered.api.item.inventory.menu.InventoryMenu;
import org.spongepowered.api.item.inventory.menu.handler.CloseHandler;
import org.spongepowered.api.item.inventory.type.ViewableInventory;

import net.kyori.adventure.audience.Audience;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.commands.parameterized.ParameterSettings;
import sawfowl.commandpack.commands.abstractcommands.parameterized.AbstractParameterizedCommand;
import sawfowl.commandpack.commands.settings.CommandParameters;
import sawfowl.commandpack.commands.settings.Register;

@Register
public class EnchantmentTable extends AbstractParameterizedCommand {

	private Map<UUID, Integer> levels = new HashMap<>();
	public EnchantmentTable(CommandPackInstance plugin) {
		super(plugin);
		Sponge.eventManager().registerListeners(getContainer(), this);
	}

	@Override
	public void execute(CommandContext context, Audience src, Locale locale, boolean isPlayer) throws CommandException {
		if(isPlayer) {
			Optional<ServerPlayer> target = getPlayer(context);
			InventoryMenu menu = ViewableInventory.builder().type(ContainerTypes.ENCHANTMENT).completeStructure().carrier(target.isPresent() ? target.get() : (ServerPlayer) src).plugin(plugin.getPluginContainer()).build().asMenu();
			menu.setTitle(ItemTypes.ENCHANTING_TABLE.get().asComponent());
			menu.registerClose(new CloseHandler() {
				@Override
				public void handle(Cause cause, Container container) {
					if(target.isPresent()) {
						levels.remove(target.get().uniqueId());
					} else levels.remove(((ServerPlayer) src).uniqueId());
					menu.unregisterAll();
				}
			});
			int level = getArgument(context, Integer.class, "Level").orElse(Permissions.getEnchantmentTableLimit((ServerPlayer) src));
			if(level < 2) level = 2;
			if(level > 40) level = 40;
			final int finalLevel = level;
			if(target.isPresent()) {
				levels.put(target.get().uniqueId(), level);
				menu.open(target.get());
				src.sendMessage(plugin.getLocales().getLocale(locale).getCommands().getEnchantmentTable().getSuccessStaff(target.get()));
			} else {
				delay((ServerPlayer) src, locale, consumer -> {
					levels.put(((ServerPlayer) src).uniqueId(), finalLevel);
					menu.open((ServerPlayer) src);
				});
			}
		} else {
			ServerPlayer target = getPlayer(context).get();
			int level = getArgument(context, Integer.class, "Level").orElse(1);
			levels.put(target.uniqueId(), level);
			InventoryMenu menu = ViewableInventory.builder().type(ContainerTypes.ENCHANTMENT).completeStructure().carrier(target).plugin(plugin.getPluginContainer()).build().asMenu();
			menu.setTitle(ItemTypes.CRAFTING_TABLE.get().asComponent());
			menu.open(target);
			src.sendMessage(plugin.getLocales().getLocale(locale).getCommands().getEnchantmentTable().getSuccessStaff(target));
		}
	}

	@Override
	public Parameterized build() {
		return fastBuild();
	}

	@Override
	public String permission() {
		return Permissions.ENCHANTING_TABLE;
	}

	@Override
	public List<ParameterSettings> getParameterSettings() {
		return Arrays.asList(
			ParameterSettings.of(CommandParameters.createPlayer(Permissions.ENCHANTMENT_TABLE_STAFF, true), false, locale -> getExceptions(locale).getPlayerNotPresent()),
			ParameterSettings.of(CommandParameters.createRangedInteger("Level", Permissions.ENCHANTMENT_TABLE_STAFF, 2, 40, true), true, locale -> getExceptions(locale).getValueNotPresent())
		);
	}

	@Override
	public String command() {
		return "enchantmenttable";
	}

	@Listener
	public void onEnchant(EnchantItemEvent.CalculateLevelRequirement event, @First ServerPlayer player) {
		if(!levels.containsKey(player.uniqueId())) return;
		int level = levels.get(player.uniqueId());
		switch (event.option()) {
		case 0:
			event.setLevelRequirement(random((level / 3), (level / 3) - random(3, 5)));
			break;
		case 1:
			int set = random((level / 2) - 2, (level / 2) - random(1, 5));
			event.setLevelRequirement(set < 3 ? 3 : set);
			break;
		case 2:
			event.setLevelRequirement(level);
			break;
		default:
			break;
		}
	}

	private int random(int first, int second) {
		if(first < 2) first = 2;
		if(second < 2) second = 2;
		if(first == second) return first;
		return first > second ? ThreadLocalRandom.current().nextInt(second, first) : ThreadLocalRandom.current().nextInt(first, second);
	}

}
