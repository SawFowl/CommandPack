package sawfowl.commandpack.mixins.neoforge.network;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.network.Connection;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;

@Mixin(value = {ServerCommonPacketListenerImpl.class})
public interface MixinAccessorServerCommonPacketListener {

	@Accessor(value = "connection")
	public Connection accessor$connection();

}
