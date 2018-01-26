package betterwithmods.manual.client.manual.segment;

import betterwithmods.client.gui.GuiManual;
import com.mojang.realmsclient.gui.ChatFormatting;
import joptsimple.internal.Strings;

import java.util.Optional;

public final class HeaderSegment extends TextSegment {
    private final int level;
    private final float fontScale;

    public HeaderSegment(final Segment parent, final String text, final int level) {
        super(parent, text);
        this.level = level;
        fontScale = Math.max(2, 5 - level) / 2f;
    }

    @Override
    protected Optional<Float> scale() {
        return Optional.of(fontScale * GuiManual.FONT_SCALE);
    }

    @Override
    protected String format() {
        return ChatFormatting.UNDERLINE.toString();
    }

    @Override
    public String toString() {
        return String.format("%s %s", Strings.repeat('#', level), text());
    }
}
