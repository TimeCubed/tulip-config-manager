package io.github.timecubed.tulip;

import net.fabricmc.api.ClientModInitializer;

public class MainClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		MainServer.LOGGER.info("Initialized tulip successfully!");
	}
}