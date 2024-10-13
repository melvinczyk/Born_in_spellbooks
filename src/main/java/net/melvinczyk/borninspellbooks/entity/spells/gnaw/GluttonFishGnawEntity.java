package net.melvinczyk.borninspellbooks.entity.spells.gnaw;

import net.mcreator.borninchaosv.entity.GluttonFishEntity;
import net.melvinczyk.borninspellbooks.registry.MAEntityRegistry;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;
import software.bernie.geckolib.core.animation.AnimationController;


import java.util.Collections;
import java.util.List;

public class GluttonFishGnawEntity extends LivingEntity implements GeoEntity {

    private Player owner;
    private boolean playSwingAnimation = true;


    public GluttonFishGnawEntity(EntityType<? extends GluttonFishGnawEntity> entityType, Level level)
    {
        super(entityType, level);
        this.setNoGravity(true);
        this.setInvulnerable(true);
    }

    public GluttonFishGnawEntity(Level levelIn, LivingEntity owner)
    {
        this(MAEntityRegistry.GLUTTON_FISH_GNAW.get(), levelIn);
        if (owner instanceof Player player)
        {
            this.owner = player;
        }
    }

    public Player getOwner()
    {
        return this.owner;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean isInvulnerable() {
        return true;
    }

    protected float getStandingEyeHeight(Pose pPose, EntityDimensions pDimensions) {
        return pDimensions.height * 0.6F;
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    public static AttributeSupplier.Builder prepareAttributes() {
        return LivingEntity.createLivingAttributes();
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return Collections.singleton(ItemStack.EMPTY);
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot pSlot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot pSlot, ItemStack pStack) {

    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.LEFT;
    }


    private final RawAnimation animationBuilder = RawAnimation.begin().thenPlay("attack");
    private final AnimationController animationController = new AnimationController(this, "controller", 0, this::predicate);

    private PlayState predicate(software.bernie.geckolib.core.animation.AnimationState event) {

        if (event.getController().getAnimationState() == AnimationController.State.STOPPED) {
            if (playSwingAnimation) {
                event.getController().setAnimation(animationBuilder);
                event.getController().setAnimationSpeed(0.75F); // Normal animation speed
                playSwingAnimation = false;
            }
        }

        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(animationController);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
}
