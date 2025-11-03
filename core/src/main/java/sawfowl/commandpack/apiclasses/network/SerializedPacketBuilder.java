package sawfowl.commandpack.apiclasses.network;

import java.util.function.Function;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.data.persistence.DataContainer;

import sawfowl.commandpack.api.network.packets.SerializedPacket;

public class SerializedPacketBuilder<T> implements SerializedPacket.Builder<T> {

	@SuppressWarnings("hiding")
	public class SerializedPacketImpl<T> implements SerializedPacket<T> {

		private ResourceKey channel;
		private Function<String, T> function;
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

		public void apply(String raw) {
			data = function.apply(raw);
		}

		@Override
		public ResourceKey channel() {
			return channel;
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
	
}
