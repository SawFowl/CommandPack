package sawfowl.commandpack.mixins.forge.network;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

import net.minecraftforge.network.packets.ModVersions.Info;

@Mixin(Info.class)
public abstract class MixinModInfoImpl extends ModIdInfo {

	@Shadow abstract String name();

	@Shadow abstract String version();

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
