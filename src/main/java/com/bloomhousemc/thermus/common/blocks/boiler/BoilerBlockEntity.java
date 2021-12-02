package com.bloomhousemc.thermus.common.blocks.boiler;

import com.bloomhousemc.thermus.common.registry.ThermusObjects;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import static com.bloomhousemc.thermus.common.blocks.boiler.BoilerBlock.COAL;

public class BoilerBlockEntity extends BlockEntity implements IAnimatable, ImplementedInventory, SidedInventory {
    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(2, ItemStack.EMPTY);
    private final AnimationFactory factory = new AnimationFactory(this);
    public BoilerBlockEntity(BlockPos pos, BlockState state) {
        super(ThermusObjects.BOILER_BLOCK_ENTITY, pos, state);
    }



    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, items);
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        Inventories.writeNbt(nbt, items);
        super.writeNbt(nbt);
    }

    private <E extends BlockEntity & IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.boiler.idle", true));
        return PlayState.CONTINUE;
    }

    private <E extends BlockEntity & IAnimatable> PlayState coal(AnimationEvent<E> event) {
        if(event.getAnimatable().getWorld().getBlockState(event.getAnimatable().getPos()).getBlock() instanceof BoilerBlock){
            int coal = event.getAnimatable().getWorld().getBlockState(event.getAnimatable().getPos()).get(COAL);
            String animation = switch (coal) {
                case 1 -> "coal1";
                case 2 -> "coal2";
                case 3 -> "coal3";
                case 4 -> "coal4";
                default -> "nocoal";
            };
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.boiler."+animation, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
        animationData.addAnimationController(new AnimationController<>(this, "coal", 0, this::coal));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return items;
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        int[] result = new int[getItems().size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = i;
        }
        return result;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return dir == Direction.UP;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return false;
    }
}
