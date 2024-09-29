package net.melvinczyk.melsadditions.entity.mobs.render;

import com.github.alexthe666.alexsmobs.client.render.RenderBoneSerpent;
import com.github.alexthe666.alexsmobs.entity.EntityBoneSerpent;
import net.melvinczyk.melsadditions.entity.mobs.SummonedBoneSerpent;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import com.github.alexthe666.alexsmobs.client.model.ModelBoneSerpentHead;

public class SummonedBoneSerpentRenderer extends RenderBoneSerpent{
    public SummonedBoneSerpentRenderer(EntityRendererProvider.Context renderManager)
    {
        super(renderManager);
    }
}
