package com.fufu.cabbagejellypack.Hud;

import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;

public class AlphaSlider extends SliderWidget {

    private final String translationKey;
    private final DoubleSupplier getter;
    private final DoubleConsumer setter;

    public AlphaSlider(int x, int y, int width, int height,
                       String translationKey,
                       DoubleSupplier getter,
                       DoubleConsumer setter) {

        super(x, y, width, height, Text.literal(""), getter.getAsDouble());

        this.translationKey = translationKey;
        this.getter = getter;
        this.setter = setter;

        updateMessage();
    }

    @Override
    protected void updateMessage() {
        this.setMessage(Text.translatable(
                translationKey,
                String.format("%.2f", value)
        ));
    }

    @Override
    protected void applyValue() {
        double rounded = Math.round((float) this.value * 100) / 100.0;
        setter.accept(rounded);
    }

    public void syncFromSource() {
        this.value = getter.getAsDouble();
        updateMessage();
    }
}
