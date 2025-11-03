package sawfowl.commandpack.apiclasses.network;

import java.util.function.Function;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.data.persistence.DataContainer;

import sawfowl.commandpack.api.network.packets.SerializedPacket;

public class SerializedPacketBuilder<T> implements SerializedPacket.Builder<T> {

	@SuppressWarnings("hiding")
	public class SerializedPacketImpl<T> implements SerializedPacket<T> {

		Function<String, T> function;
		private SerializedPacketImpl(Function<String, T> function) {
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

	}

	@Override
	public @NotNull SerializedPacket<T> build() {
		throw new NullPointerException();
	}

	@Override
	public SerializedPacket<T> build(Function<String, T> function) {
		return new SerializedPacketImpl<T>(function);
	}
	
}
