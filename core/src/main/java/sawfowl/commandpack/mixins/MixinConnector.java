package sawfowl.commandpack.mixins;

import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.connect.IMixinConnector;

public class MixinConnector implements IMixinConnector {

	@Override
	public void connect() {
		Mixins.addConfiguration(
			checkNeo()
			?
			"commandpack.mixins.neoforge.json"
			:
			checkForge()
			?
			"commandpack.mixins.forge.json"
			:
			"commandpack.mixins.vanilla.json"
		);
	}

	private boolean checkForge() {
		try {
			Class.forName("net.minecraftforge.fml.javafmlmod.FMLModContainer");
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private boolean checkNeo() {
		try {
			Class.forName("net.neoforged.neoforge.common.NeoForge");
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
