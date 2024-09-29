package net.melvinczyk.melsadditions.entity.mobs.render;

import com.github.alexthe666.alexsmobs.client.render.RenderBoneSerpent;
import com.github.alexthe666.alexsmobs.entity.EntityBoneSerpent;
import net.melvinczyk.melsadditions.entity.mobs.SummonedBoneSerpent;
import net.minecraft.client.model.RavagerModel;
import net.minecraft.client.renderer.entity.*;
import com.github.alexthe666.alexsmobs.client.render.RenderBoneSerpent;
import net.minecraft.world.entity.monster.Ravager;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SummonedBoneSerpentRenderer extends RenderBoneSerpent {
    public SummonedBoneSerpentRenderer(EntityRendererProvider.Context context)
    {
        super(context);
    }
}
