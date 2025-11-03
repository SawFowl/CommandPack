package sawfowl.commandpack.mixins.neoforge.plugin;

import org.spongepowered.api.ResourceKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.minecraft.network.ConnectionProtocol;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistration;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.api.mixin.network.MixinServerPlayer;
import sawfowl.commandpack.api.network.packets.RawPacket;
import sawfowl.commandpack.api.network.packets.SerializedPacket;
import sawfowl.commandpack.apiclasses.network.CustomPayloadsServiceImpl;
import sawfowl.commandpack.apiclasses.network.SerializedPacketBuilder.SerializedPacketImpl;
import sawfowl.commandpack.mixins.neoforge.network.NetworkRegistryAccessor;

@Mixin(CustomPayloadsServiceImpl.class)
public class MixinRegisterCustomPayloads {

	private ModContainer modContainer;
	private CommandPackInstance plugin = CommandPackInstance.getInstance();

	@Overwrite
	private void init() {
		modContainer = ModList.get().getModContainerById("commandpack").get();
		modContainer.getEventBus().register(this);
	}
	
	@SuppressWarnings("unchecked")
	@SubscribeEvent
	public void register(RegisterPayloadHandlersEvent event) {
		var registrations = NetworkRegistryAccessor.getPAYLOAD_REGISTRATIONS();
		plugin.getPayloadsService().getCodecs().forEach((type, codec) -> {
			if (registrations.get(ConnectionProtocol.PLAY).containsKey(type.id())) {
				PayloadRegistration<?> existingHandler = registrations.get(ConnectionProtocol.PLAY).get(type.id());
				@SuppressWarnings({ "rawtypes" })
				PayloadRegistration<?> newHandler = new PayloadRegistration(existingHandler.type(), existingHandler.codec(), (payload, context) -> {
					if (context.player() instanceof MixinServerPlayer player && payload instanceof RawPacket rawPacket) {
						// Server-side packet, let plugin handle it
						plugin.getPayloadsService().getRawListeners((ResourceKey) (Object) payload.type().id()).forEach(listener -> listener.read(player, rawPacket));
						if(plugin.getPayloadsService().containsSerializer((ResourceKey) (Object) payload.type().id())) plugin.getPayloadsService().getListeners((ResourceKey) (Object) payload.type().id()).forEach(listener -> {
							SerializedPacketImpl packet = (SerializedPacketImpl) SerializedPacket.of((ResourceKey) (Object) payload.type().id(), plugin.getPayloadsService().getSerializer((ResourceKey) (Object) payload.type().id()));
							packet.apply(rawPacket.data());
							listener.read(player, packet);
						});
						((IPayloadHandler)existingHandler.handler()).handle(payload, context);
						return;
					}
				}, existingHandler.protocols(), existingHandler.flow(), existingHandler.version(), existingHandler.optional());
				registrations.get(ConnectionProtocol.PLAY).put(type.id(), newHandler);
			} else {
				event.registrar("1")
					.optional()
					.playBidirectional(
						type,
						codec,
						(payload, context) -> {
							if (context.player() instanceof MixinServerPlayer player && payload instanceof RawPacket rawPacket) {
								// Server-side packet, let WE handle it
								plugin.getPayloadsService().getRawListeners((ResourceKey) (Object) payload.type().id()).forEach(listener -> {
										listener.read(player, rawPacket);
									}
								);
								if(plugin.getPayloadsService().containsSerializer((ResourceKey) (Object) payload.type().id())) plugin.getPayloadsService().getListeners((ResourceKey) (Object) payload.type().id()).forEach(listener -> {
									@SuppressWarnings("rawtypes")
									SerializedPacketImpl packet = (SerializedPacketImpl) SerializedPacket.of((ResourceKey) (Object) payload.type().id(), plugin.getPayloadsService().getSerializer((ResourceKey) (Object) payload.type().id()));
									packet.apply(rawPacket.data());
									listener.read(player, packet);
								});
								return;
							}
						}
				);
			}
		});
	}

}
