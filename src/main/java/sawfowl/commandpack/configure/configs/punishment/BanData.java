package sawfowl.commandpack.configure.configs.punishment;

import java.net.InetAddress;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.service.ban.Ban;
import org.spongepowered.api.service.ban.Ban.Builder;
import org.spongepowered.api.service.ban.BanTypes;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;

import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class BanData {

	public BanData() {
		if(uuid == null) return;
		Sponge.server().gameProfileManager().cache().findById(uuid).ifPresent(p -> {
			gameProfile = p;
		});
		if(gameProfile == null) {
			Sponge.server().gameProfileManager().profile(uuid).thenAccept(p -> {
				gameProfile = p;
			});
		}
	}
	public BanData(Ban.Profile ban) {
		creationDate = ban.creationDate().getEpochSecond();
		uuid = ban.profile().uniqueId();
		name = ban.profile().name().orElse(ban.profile().examinableName());
		ban.expirationDate().ifPresent(date -> {
			expired = date.getEpochSecond();
		});
		ban.banSource().ifPresent(s -> {
			source = TextUtils.serializeJson(s);
		});
		ban.reason().ifPresent(r -> {
			reason = TextUtils.serializeJson(r);
		});
	}
	public BanData(Ban.IP ban) {
		creationDate = ban.creationDate().getEpochSecond();
		ban.expirationDate().ifPresent(date -> {
			expired = date.getEpochSecond();
		});
		ban.banSource().ifPresent(s -> {
			source = TextUtils.serializeJson(s);
		});
		ban.reason().ifPresent(r -> {
			reason = TextUtils.serializeJson(r);
		});
		inetAddress = ban.address();
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
	@Setting("Address")
	private InetAddress inetAddress;
	private GameProfile gameProfile;

	public Instant getCreationDate() {
		return Instant.ofEpochSecond(creationDate);
	}

	public boolean isIndefinitely() {
		return expired != null || expired <= 0;
	}

	public Optional<UUID> getUniqueId() {
		return Optional.ofNullable(uuid);
	}

	public Optional<String> getName() {
		return Optional.ofNullable(name);
	}

	public Optional<Instant> getExpirationDate() {
		return Optional.ofNullable(expired).map(e -> Instant.ofEpochSecond(e));
	}

	public Optional<Component> getSource() {
		return Optional.ofNullable(source).map(s -> TextUtils.deserializeJson(s));
	}

	public Optional<Component> getReason() {
		return Optional.ofNullable(reason).map(r -> TextUtils.deserializeJson(r));
	}

	public GameProfile getGameProfile() {
		return gameProfile == null ? gameProfile = Sponge.server().gameProfileManager().cache().findById(uuid).orElse(GameProfile.of(uuid, name)) : gameProfile;
	}

	public Optional<InetAddress> getInetAddress() {
		return Optional.ofNullable(inetAddress);
	}

	public org.spongepowered.api.service.ban.Ban getBan() {
		Builder builder = Ban.builder().startDate(getCreationDate());
		Optional<Instant> expirationDate = getExpirationDate();
		if(expirationDate.isPresent()) builder = builder.expirationDate(expirationDate.get());
		Optional<Component> source = getSource();
		if(source.isPresent()) builder = builder.source(source.get());
		Optional<Component> reason = getReason();
		if(reason.isPresent()) builder = builder.source(reason.get());
		if(inetAddress == null) {
			builder.type(BanTypes.PROFILE);
			builder = builder.profile(getGameProfile());
		} else {
			builder.type(BanTypes.IP);
			builder = builder.address(inetAddress);
		}
		return builder.build();
	}

}
