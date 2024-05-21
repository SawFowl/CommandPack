package sawfowl.commandpack.mixins.forge.network;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

import net.minecraftforge.network.packets.ModVersions.Info;
import sawfowl.commandpack.api.mixin.network.PlayerModInfo;

@Mixin(Info.class)
public abstract class MixinModInfoImpl implements ModIdInfo {

	@Shadow public abstract String name();
	@Shadow public abstract String version();
	private String id;

	@Override
	public String getId() {
		return id;
	}

	@Override
	public PlayerModInfo setId(String string) {
		id = string;
		return this;
	}

	private Component asComponent = Component.text(name()).color(TextColor.color(255, 137, 0))
		.append(Component.text(" - ").color(TextColor.color(255, 255, 255)))
		.append(Component.text(version()).color(TextColor.color(78, 0, 245)));

	@Override
	public Component asComponent() {
		return asComponent;
	}

	@Override
	public String getName() {
		return name();
	}

	@Override
	public String getVersion() {
		return version();
	}

}
