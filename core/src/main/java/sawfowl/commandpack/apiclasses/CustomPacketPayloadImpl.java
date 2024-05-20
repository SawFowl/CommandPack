package sawfowl.commandpack.apiclasses;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record CustomPacketPayloadImpl(Type<? extends CustomPacketPayload> id, FriendlyByteBuf buf) implements CustomPacketPayload{
	
	public static StreamCodec<FriendlyByteBuf, CustomPacketPayloadImpl> streamCodec(final Type<? extends CustomPacketPayload> type, final int maxPayloadSize) {
		return CustomPacketPayload.codec(
				CustomPacketPayloadImpl::write, (b) -> {
					int readableBytes = b.readableBytes();
					if (readableBytes >= 0 && readableBytes <= maxPayloadSize) {
						return new CustomPacketPayloadImpl(type, new FriendlyByteBuf(b.readBytes(b.readableBytes())));
					}
					throw new IllegalArgumentException("Payload may not be larger than " + maxPayloadSize + " bytes");
				});
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return id;
	}

	public FriendlyByteBuf getData() {
		return buf;
	}

	public void write(FriendlyByteBuf buffer) {
		buffer.writeBytes(buf);
	}

}
