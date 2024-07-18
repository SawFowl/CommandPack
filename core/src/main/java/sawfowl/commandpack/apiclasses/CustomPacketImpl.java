package sawfowl.commandpack.apiclasses;

import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import sawfowl.commandpack.api.mixin.network.CustomPacket;
import sawfowl.commandpack.api.mixin.network.MixinServerPlayer;

public class CustomPacketImpl implements CustomPacket {

	private String loc;
	private String data;

	public String getLocation() {
		return loc;
	}

	public String getData() {
		return data;
	}

	public Builder builder() {
		return new Builder() {
			@Override
			public CustomPacket create(String resourceLocation, String data) {
				CustomPacketImpl.this.loc = resourceLocation;
				CustomPacketImpl.this.data = data;
				return build();
			}
			@Override
			public CustomPacket build() {
				return CustomPacketImpl.this;
			}
		};
	}

	@Override
	public void sendTo(ServerPlayer player) {
		MixinServerPlayer.cast(player).sendPacket(this);
	}

	@Override
	public int contentVersion() {
		return 0;
	}

	@Override
	public DataContainer toContainer() {
		return null;
	}

}
