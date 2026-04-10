package sawfowl.commandpack.mixins.neoforge.network;

import java.util.Map;

import org.jetbrains.annotations.Contract;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.network.ConnectionProtocol;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.registration.NetworkRegistry;
import net.neoforged.neoforge.network.registration.PayloadRegistration;

@Mixin(NetworkRegistry.class)
public interface NetworkRegistryAccessor {

	@Accessor
	@Contract("-> _")
	static Map<ConnectionProtocol, Map<Identifier, PayloadRegistration<?>>> getPAYLOAD_REGISTRATIONS() {
		throw new AssertionError();
	}

}
