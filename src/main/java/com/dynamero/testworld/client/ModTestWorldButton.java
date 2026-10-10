package com.dynamero.testworld.client;

import com.dynamero.shared.annotations.*;
import com.dynamero.shared.exceptions.Exceptions;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;

import java.util.Comparator;
import java.util.List;

@Developer("TurtyWurty")
@CreatedAt("2026-10-10")
@ModifiedAt("2026-10-10")
@ModifiedBy("TheMentor")
@Website("https://dynamero.com")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
@Modrinth("https://modrinth.com/user/jiraiyah")
public class ModTestWorldButton
{
    public ModTestWorldButton()
    {
        Exceptions.throwCtorAssertion();
    }

    public static void init() {
        ScreenEvents.AFTER_INIT.register((_, screen, _, _) -> {
            if (!(screen instanceof TitleScreen))
                return;

            var widgets = Screens.getWidgets(screen);
            Button original = widgets.stream()
                                     .filter(Button.class::isInstance)
                                     .map(Button.class::cast)
                                     .filter(TestWorldLauncher::isVanillaTestWorldButton)
                                     .findFirst()
                                     .orElse(null);
            if (original == null) {
                addFallbackButton((TitleScreen) screen, widgets);
                return;
            }

            AbstractWidget replacement = TestWorldLauncher.replacementFor(original, screen);
            widgets.remove(original);
            widgets.add(replacement);
        });
    }

    private static void addFallbackButton(TitleScreen screen, List<AbstractWidget> widgets) {
        Button realms = findButton(widgets, "menu.online");
        Button options = findButton(widgets, "menu.options");
        if (realms == null || options == null)
            return;

        // Detect the row containing the small icon buttons (between realms and options)
        int smallButtonsY = realms.getY() + 24;
        List<AbstractWidget> smallIconButtons = widgets.stream()
                                                       .filter(w -> w.getY() == smallButtonsY && w.getWidth() == 20)
                                                       .sorted(Comparator.comparingInt(AbstractWidget::getX))
                                                       .toList();

        if (!smallIconButtons.isEmpty()) {
            // Option A: Put Mod Test to the left, icon buttons aligned to the right edge
            int rowRight = realms.getX() + realms.getWidth();
            int totalIconWidth = (smallIconButtons.size() * 20) + ((smallIconButtons.size() - 1) * 4);

            // Shift icon buttons to the right
            int iconStartX = rowRight - totalIconWidth;
            for (int i = 0; i < smallIconButtons.size(); i++) {
                smallIconButtons.get(i).setPosition(iconStartX + i * 24, smallButtonsY);
            }

            // Add Mod Test to the left side of the icons
            int buttonWidth = (iconStartX - 4) - realms.getX();
            widgets.add(TestWorldLauncher.createButton(
                    screen,
                    realms.getX(),
                    smallButtonsY,
                    buttonWidth,
                    20));
        }
    }

    private static Button findButton(List<AbstractWidget> widgets, String translationKey) {
        Component label = Component.translatable(translationKey);
        return widgets.stream()
                      .filter(Button.class::isInstance)
                      .map(Button.class::cast)
                      .filter(button -> button.getMessage().equals(label))
                      .findFirst()
                      .orElse(null);
    }
}