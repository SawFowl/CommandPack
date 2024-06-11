package sawfowl.commandpack.configure.serializers;

import java.lang.reflect.Type;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import sawfowl.commandpack.CommandPack;
import sawfowl.commandpack.api.data.command.CancelRules;
import sawfowl.commandpack.configure.configs.commands.CancelRulesData;
import sawfowl.commandpack.configure.locale.locales.abstractlocale.comments.commandsconfig.DelayData;

public class CancelRulesSerializer implements TypeSerializer<CancelRules> {

	private final CommandPack INSTANCE = CommandPack.getInstance();

	@Override
	public CancelRules deserialize(Type type, ConfigurationNode node) throws SerializationException {
		return new CancelRulesData().builder().allowMoving(node.node("AllowMoving").getBoolean()).allowOtherCommand(node.node("AllowOtherCommand").getBoolean()).build();
	}

	@Override
	public void serialize(Type type, @Nullable CancelRules rules, ConfigurationNode node) throws SerializationException {
		node.node("AllowMoving").set(rules.isAllowMoving());
		node.node("AllowOtherCommand").set(rules.isAllowOtherCommand());
		if(node instanceof CommentedConfigurationNode commented) {
			commented.node("AllowMoving").comment(getCancelRules().getAllowMoving());
			commented.node("AllowOtherCommand").comment(getCancelRules().getAllowOtherCommand());
		}
	}

	private DelayData.CancelRules getCancelRules() {
		return INSTANCE.getLocales().getSystemLocale().getComments().getCommandsConfig().getDelayData().getCancelRules();
	}

}
