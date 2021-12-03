package com.bloomhousemc.thermus.common.blocks.boiler;

import com.bloomhousemc.thermus.common.registry.ThermusObjects;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import static com.bloomhousemc.thermus.common.blocks.boiler.BoilerBlock.COAL;
import static com.bloomhousemc.thermus.common.blocks.boiler.BoilerBlock.LIT;

public class BoilerBlockEntity extends BlockEntity implements IAnimatable, ImplementedInventory, SidedInventory {
    protected final DefaultedList<ItemStack> items = DefaultedList.ofSize(2, ItemStack.EMPTY);
    private final AnimationFactory factory = new AnimationFactory(this);
    private boolean loaded = false;
    public int active = 0;

    public BoilerBlockEntity(BlockPos pos, BlockState state) {
        super(ThermusObjects.BOILER_BLOCK_ENTITY, pos, state);
    }

    public static void decrementBoiler(World world, BoilerBlockEntity blockEntity){
        if((blockEntity.getStack(0).getCount() > 0 && blockEntity.getStack(0).getCount() > 0)){
            int rand = MathHelper.nextInt(world.random, 0,1);
            blockEntity.getStack(rand).decrement(1);
        }else if (blockEntity.getStack(0).getCount() > 0 && blockEntity.getStack(0).isOf(Items.COAL)){
            blockEntity.getStack(0).decrement(1);
        }else if (blockEntity.getStack(1).getCount() > 0 && blockEntity.getStack(1).isOf(Items.CHARCOAL)){
            blockEntity.getStack(1).decrement(1);
        }
    }

    public static void tick(World world, BlockPos pos, BlockState state, BoilerBlockEntity blockEntity) {
        if (world != null) {
            if (!blockEntity.loaded) {
                blockEntity.markDirty();
                blockEntity.loaded = true;
            }
            if (!world.isClient) {
                if(state.get(LIT)){
                    if (world.random.nextFloat() <= 0.075f) {
                        world.playSound(null, pos, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1 / 3f, 1);
                    }
                    blockEntity.active++;
                    if(blockEntity.active > 100){
                        blockEntity.active=0;
                        decrementBoiler(world, blockEntity);
                    }


                }
                if((blockEntity.getStack(0).getCount() + blockEntity.getStack(0).getCount() <= 0)){
                    world.setBlockState(pos, state.with(LIT, false));
                    world.setBlockState(pos, state.with(COAL, 0));
                    blockEntity.setStack(0, new ItemStack(Items.AIR));
                    blockEntity.setStack(1, new ItemStack(Items.AIR));
                }
            }
        }
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
