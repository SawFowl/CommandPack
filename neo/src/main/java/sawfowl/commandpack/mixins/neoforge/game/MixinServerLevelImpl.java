package sawfowl.commandpack.mixins.neoforge.game;

import java.util.Collection;
import java.util.function.BooleanSupplier;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.network.protocol.game.ClientboundTickingStatePacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.TickRateManager;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import sawfowl.commandpack.api.mixin.game.MixinServerWorld;

@Mixin(value = ServerLevel.class)
public abstract class MixinServerLevelImpl implements MixinServerWorld {

	@Shadow
	public abstract @NonNull MinecraftServer shadow$getServer();
	abstract long[] bridge$recentTickTimes();
	private TickRateManager ticksManager = new TickRateManager();

	public boolean isFreezeTicks() {
		return ticksManager.isFrozen();
	}

	public void setFreezeTicks(boolean enable) {
		ticksManager.setFrozen(enable);
		updateStateToClients(ClientboundTickingStatePacket.from(ticksManager));
	}

	@Override
	public double getTickTime() {
		long[] tickTimes = bridge$recentTickTimes();
		long $$1 = 0L;
		for(long $$2 : tickTimes) $$1 += $$2;
		return ((double)$$1 / (double)tickTimes.length) * 1.0E-6D;
	}

	@Override
	public double getTPS() {
		return Math.min(1000.0 / getTickTime(), 20.0);
	}

	public TickRateManager getTicksManager() {
		return ticksManager;
	}

	@SuppressWarnings("unchecked")
	private void updateStateToClients(ClientboundTickingStatePacket packet) {
		((Collection<ServerPlayer>) (Object) players()).forEach(player -> {
			player.connection.send(packet);
		});
	}

	@Inject(method = "addPlayer", at = @At("HEAD"))
	private void onAddPlayer(ServerPlayer $$0, CallbackInfo info) {
		$$0.connection.send(ClientboundTickingStatePacket.from(ticksManager));
	}

	@Inject(method = "tick", at = @At("HEAD"), cancellable = true)
	public void onTick(BooleanSupplier $$0, CallbackInfo info) {
		if(isFreezeTicks()) {
			bridge$recentTickTimes()[this.shadow$getServer().getTickCount() % 100] = 0;
			info.cancel();
		}
	}

}
