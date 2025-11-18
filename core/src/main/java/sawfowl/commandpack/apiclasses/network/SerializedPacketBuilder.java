package sawfowl.commandpack.apiclasses.network;

import java.util.function.Function;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.network.channel.ChannelBuf;

import sawfowl.commandpack.api.network.packets.SerializedPacket;

public class SerializedPacketBuilder<T> implements SerializedPacket.Builder<T> {

	@SuppressWarnings("hiding")
	public class SerializedPacketImpl<T> implements SerializedPacket<T> {

		private ResourceKey channel;
		private Function<String, T> function;
		private Function<ChannelBuf, T> bufferFunction;
		private SerializedPacketImpl(ResourceKey channel, Function<String, T> function) {
			this.channel = channel;
			this.function = function;
		}

		T data;

		@Override
		public int contentVersion() {
			return 1;
		}

		@Override
		public DataContainer toContainer() {
			return null;
		}

		@Override
		public T get() {
			return data;
		}

		public SerializedPacket<T> apply(ChannelBuf buffer, String raw) {
			if(function != null && raw != null) data = function.apply(raw);
			if(bufferFunction != null && buffer != null) data = bufferFunction.apply(buffer);
			return this;
		}

		@Override
		public ResourceKey channel() {
			return channel;
		}

		@SuppressWarnings("unchecked")
		private SerializedPacket<T> setBufferSerializer(Function<ChannelBuf, ?> function) {
			bufferFunction = (Function<ChannelBuf, T>) function;
			return this;
		}

	}

	@Override
	public @NotNull SerializedPacket<T> build() {
		throw new NullPointerException();
	}

	@Override
	public SerializedPacket<T> build(ResourceKey channel, Function<String, T> function) {
		return new SerializedPacketImpl<T>(channel, function);
	}

	@SuppressWarnings("unchecked")
	@Override
	public SerializedPacket<T> buildBuffer(ResourceKey channel, Function<ChannelBuf, T> function) {
		return (SerializedPacket<T>) new SerializedPacketImpl<>(channel, null).setBufferSerializer(function);
	}
	
}
