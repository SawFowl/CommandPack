package sawfowl.commandpack.mixins.forge.game;

import org.jetbrains.annotations.Contract;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerConnectionListener;

@Mixin(value = MinecraftServer.class, remap = false)
public interface MinecraftServerAccessor {

	@Accessor
	@Contract("-> _")
	static ServerConnectionListener getconnection() {
		throw new AssertionError();
	}
}
