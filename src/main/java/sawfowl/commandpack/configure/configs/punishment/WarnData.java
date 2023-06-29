package sawfowl.commandpack.configure.configs.punishment;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.data.persistence.Queries;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.api.data.punishment.Warn;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class WarnData implements Warn {

	public WarnData(){}

	public Builder builder() {
		return new Builder();
	}

	@Setting("CreationDate")
	private long creationDate;
	@Setting("ExpirationDate")
	private Long expired;
	@Setting("Source")
	private String source;
	@Setting("Reason")
	private String reason;

	@Override
	public Instant getCreationDate() {
		return Instant.ofEpochMilli(creationDate);
	}

	@Override
	public boolean isIndefinitely() {
		return expired != null || expired <= 0;
	}

	@Override
	public Optional<Instant> getExpirationDate() {
		return Optional.ofNullable(expired).map(e -> Instant.ofEpochMilli(e));
	}

	@Override
	public Optional<Component> getSource() {
		return Optional.ofNullable(source).map(s -> TextUtils.deserializeJson(s));
	}

	@Override
	public Optional<Component> getReason() {
		return Optional.ofNullable(reason).map(r -> TextUtils.deserializeJson(r));
	}

	@Override
	public int contentVersion() {
		return 1;
	}

	@Override
	public DataContainer toContainer() {
		return DataContainer.createNew()
				.set(Queries.CONTENT_VERSION, contentVersion());
	}

	@Override
	public int hashCode() {
		return Objects.hash(creationDate);
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null || getClass() != obj.getClass()) return false;
		if(this == obj) return true;
		return obj instanceof WarnData && creationDate == ((WarnData) obj).creationDate && source.equals(((WarnData) obj).source);
	}

	@Override
	public boolean isExpired() {
		return expired != null && expired <= System.currentTimeMillis();
	}

	private class Builder implements Warn.Builder {

		@Override
		public @NotNull Warn build() {
			if(creationDate == 0) creationDate = System.currentTimeMillis();
			return WarnData.this;
		}

		@Override
		public Warn.Builder creationDate(Instant value) {
			creationDate = value.toEpochMilli();
			return this;
		}

		@Override
		public Warn.Builder source(Component name) {
			source = TextUtils.serializeJson(name);
			return this;
		}

		@Override
		public Warn.Builder source(ServerPlayer value) {
			source = TextUtils.serializeJson(value.get(Keys.DISPLAY_NAME).orElse(Component.text(value.name())));
			return this;
		}

		@Override
		public Warn.Builder expirationDate(Instant value) {
			expired = value.toEpochMilli();
			return this;
		}

		@Override
		public Warn.Builder reason(Component value) {
			reason = TextUtils.serializeJson(value);
			return this;
		}

		@Override
		public Warn from(Warn warn) {
			creationDate = warn.getCreationDate().toEpochMilli();
			warn.getExpirationDate().ifPresent(e -> {
				expired = e.toEpochMilli();
			});
			warn.getReason().ifPresent(r -> {
				reason = TextUtils.serializeJson(r);
			});
			warn.getSource().ifPresent(s -> {
				source = TextUtils.serializeJson(s);
			});
			return WarnData.this;
		}
		
	}

}
