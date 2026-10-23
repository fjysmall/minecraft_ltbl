package team.imcc.ltbl;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LtblMod implements ModInitializer {
	public static final String MOD_ID = "ltbl";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LtblConfig.load(LOGGER);
		LtblConfig config = LtblConfig.get();

		CommandRegistrationCallback.EVENT.register(
				(dispatcher, registryAccess, environment) -> LtblCommand.register(dispatcher));
		ServerTickEvents.END_SERVER_TICK.register(OverlayManager::onServerTick);
		ServerLifecycleEvents.SERVER_STOPPED.register(server -> OverlayManager.clear());

		LOGGER.info("Let There Be Light ready: radius {}, particle {}, {} sections/tick",
				config.radius, config.particle, config.sectionsPerTick);
	}
}

