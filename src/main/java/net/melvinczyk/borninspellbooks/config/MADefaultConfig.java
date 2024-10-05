package net.melvinczyk.borninspellbooks.config;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import net.melvinczyk.borninspellbooks.spells.MASpellRarity;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

public class MADefaultConfig extends DefaultConfig {
    public MASpellRarity minRarity;
    public ResourceLocation schoolResource;
    public int maxLevel = -1;
    public boolean enabled = true;
    public double cooldownInSeconds = -1;
    public boolean allowCrafting = true;

    public MADefaultConfig(Consumer<MADefaultConfig> intialize) throws RuntimeException {
        intialize.accept(this);
        build();
    }

    public MADefaultConfig() {
    }

    public MADefaultConfig setMaxLevel(int i) {
        this.maxLevel = i;
        return this;
    }

    public MADefaultConfig setDeprecated(boolean deprecated) {
        this.enabled = !deprecated;
        return this;
    }

    public MADefaultConfig setMinRarity(MASpellRarity i) {
        this.minRarity = i;
        return this;
    }

    public MADefaultConfig setCooldownSeconds(double i) {
        this.cooldownInSeconds = i;
        return this;
    }

    public MADefaultConfig setSchoolResource(ResourceLocation schoolResource) {
        this.schoolResource = schoolResource;
        return this;
    }

    public MADefaultConfig setAllowCrafting(boolean allowCrafting) {
        this.allowCrafting = allowCrafting;
        return this;
    }

    public MADefaultConfig build() throws RuntimeException {
        if (!this.validate())
            throw new RuntimeException("You didn't define all config attributes!");

        return this;
    }

    private boolean validate() {
        return minRarity != null && maxLevel >= 0 && schoolResource != null && cooldownInSeconds >= 0;
    }
}

