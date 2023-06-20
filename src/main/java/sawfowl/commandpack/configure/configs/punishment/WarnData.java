package sawfowl.commandpack.configure.configs.punishment;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

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
	@Setting("Expired")
	private Long expired;
	@Setting("Source")
	private String source;
	@Setting("Reason")
	private String reason;
	@Setting("WarnUUID")
	private UUID uuid;

	@Override
	public Instant getCreationDate() {
		return Instant.ofEpochSecond(creationDate);
	}

	@Override
	public boolean isIndefinitely() {
		return expired != null || expired <= 0;
	}

	@Override
	public Optional<Instant> getExpirationDate() {
		return Optional.ofNullable(expired).map(e -> Instant.ofEpochSecond(e));
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
	public UUID getUniqueId() {
		return uuid;
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
		return Objects.hash(uuid);
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null || getClass() != obj.getClass()) return false;
		if(this == obj) return true;
		return obj instanceof WarnData && Objects.equals(uuid, ((WarnData) obj).uuid);
	}

	private class Builder implements Warn.Builder {

		@Override
		public @NotNull Warn build() {
			if(creationDate == 0) creationDate = System.currentTimeMillis() / 1000;
			uuid = UUID.randomUUID();
			return WarnData.this;
		}

		@Override
		public Warn.Builder creationDate(Instant value) {
			creationDate = value.getEpochSecond();
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
			expired = value.getEpochSecond();
			return this;
		}

		@Override
		public Warn.Builder reason(Component value) {
			reason = TextUtils.serializeJson(value);
			return this;
		}

		@Override
		public Warn from(Warn warn) {
			creationDate = warn.getCreationDate().getEpochSecond();
			warn.getExpirationDate().ifPresent(e -> {
				expired = e.getEpochSecond();
			});
			warn.getReason().ifPresent(r -> {
				reason = TextUtils.serializeJson(r);
			});
			warn.getSource().ifPresent(s -> {
				source = TextUtils.serializeJson(s);
			});
			uuid = warn.getUniqueId();
			return WarnData.this;
		}
		
	}

}
