package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.server.entity.grottol.EntityGrottol;
import com.google.common.collect.Sets;
import net.minecraft.entity.ai.controller.LookController;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class ItemCapturedGrottol extends Item {
    public ItemCapturedGrottol(Item.Properties properties) {
        super(properties);
        setMaxStackSize(1);
    }

    @Override
    public ActionResultType onItemUse(PlayerEntity player, World world, BlockPos pos, Hand hand, Direction facing, float hitX, float hitY, float hitZ) {
        if (facing == Direction.DOWN) {
            return ActionResultType.FAIL;
        }
        BlockPos location = pos.offset(facing);
        ItemStack stack = player.getHeldItem(hand);
        if (!player.canPlayerEdit(location, facing, stack)) {
            return ActionResultType.FAIL;
        }
        if (!world.isRemote) {
            EntityGrottol grottol = new EntityGrottol(world);
            CompoundNBT compound = stack.getSubCompound("EntityTag");
            if (compound != null) {
                setData(grottol, compound);
            }
            grottol.moveToBlockPosAndAngles(location, 0, 0);
            lookAtPlayer(grottol, player);
            grottol.onInitialSpawn(world.getDifficultyForLocation(location), null);
            world.spawnEntity(grottol);
            if (!player.capabilities.isCreativeMode) {
                stack.shrink(1);
            }
        }
        return ActionResultType.SUCCESS;
    }

    private void setData(EntityGrottol grottol, CompoundNBT compound) {
        CompoundNBT data = grottol.writeToNBT(new CompoundNBT());
        UUID id = grottol.getUniqueID();
        data.merge(compound);
        grottol.readFromNBT(data);
        grottol.setUniqueId(id);
    }

    private void lookAtPlayer(EntityGrottol grottol, PlayerEntity player) {
        LookController helper = new LookController(grottol);
        helper.setLookPositionWithEntity(player, 180, 90);
        helper.onUpdateLook();
        GoalSelector ai = grottol.tasks;
        Set<GoalSelector.EntityAITaskEntry> tasks = Sets.newLinkedHashSet(ai.taskEntries);
        ai.taskEntries.removeIf(entry -> !(entry.action instanceof LookAtGoal));
        grottol.getRNG().setSeed("IS MATh RElatEd tO ScIENCe?".hashCode());
        ai.onUpdateTasks();
        grottol.getRNG().setSeed(new Random().nextLong());
        ai.taskEntries.clear();
        ai.taskEntries.addAll(tasks);
    }

    public ItemStack create(EntityGrottol grottol) {
        ItemStack stack = new ItemStack(this);
        stack.setTagInfo("EntityTag", grottol.writeToNBT(new CompoundNBT()));
        return stack;
    }
}
