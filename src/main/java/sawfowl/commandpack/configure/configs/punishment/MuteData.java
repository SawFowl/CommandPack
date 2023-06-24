package sawfowl.commandpack.configure.configs.punishment;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.data.persistence.Queries;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.api.data.punishment.Mute;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class MuteData implements Mute {

	public MuteData(){}

	public Builder builder() {
		return new Builder();
	}

	@Setting("CreationDate")
	private long creationDate;
	@Setting("UUID")
	private UUID uuid;
	@Setting("Name")
	private String name;
	@Setting("Expired")
	private Long expired;
	@Setting("Source")
	private String source;
	@Setting("Reason")
	private String reason;

	@Override
	public Instant getCreationDate() {
		return Instant.ofEpochSecond(creationDate);
	}

	@Override
	public boolean isIndefinitely() {
		return expired != null || expired <= 0;
	}

	@Override
	public UUID getUniqueId() {
		return uuid;
	}

	@Override
	public String getName() {
		return name;
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
	public boolean isExpired() {
		return expired != null && expired >= Instant.now().getEpochSecond();
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

	private class Builder implements Mute.Builder {

		@Override
		public @NotNull Mute build() {
			if(creationDate == 0) creationDate = System.currentTimeMillis() / 1000;
			return MuteData.this;
		}

		@Override
		public Mute.Builder creationDate(Instant value) {
			creationDate = value.getEpochSecond();
			return this;
		}

		@Override
		public Mute.Builder target(ServerPlayer value) {
			uuid = value.uniqueId();
			name = value.name();
			return this;
		}

		@Override
		public sawfowl.commandpack.api.data.punishment.Mute.Builder target(User value) {
			uuid = value.uniqueId();
			name = value.name();
			return this;
		}

		@Override
		public Mute.Builder source(Component name) {
			source = TextUtils.serializeJson(name);
			return this;
		}

		@Override
		public Mute.Builder source(ServerPlayer value) {
			source = TextUtils.serializeJson(value.get(Keys.DISPLAY_NAME).orElse(Component.text(value.name())));
			return this;
		}

		@Override
		public Mute.Builder expirationDate(Instant value) {
			expired = value.getEpochSecond();
			return this;
		}

		@Override
		public Mute.Builder reason(Component value) {
			reason = TextUtils.serializeJson(value);
			return this;
		}

		@Override
		public Mute from(Mute mute) {
			creationDate = mute.getCreationDate().getEpochSecond();
			uuid = mute.getUniqueId();
			name = mute.getName();
			mute.getExpirationDate().ifPresent(e -> {
				expired = e.getEpochSecond();
			});
			mute.getReason().ifPresent(r -> {
				reason = TextUtils.serializeJson(r);
			});
			mute.getSource().ifPresent(s -> {
				source = TextUtils.serializeJson(s);
			});
			uuid = mute.getUniqueId();
			return MuteData.this;
		}
		
	}

}
