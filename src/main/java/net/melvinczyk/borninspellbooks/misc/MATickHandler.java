package net.melvinczyk.borninspellbooks.misc;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.TickEvent;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Mod.EventBusSubscriber
public class MATickHandler {
    private static final List<DelayedTask> tasks = new ArrayList<>();

    public static void delay(int ticks, Runnable task) {
        tasks.add(new DelayedTask(ticks, task));
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        Iterator<DelayedTask> iterator = tasks.iterator();
        while (iterator.hasNext()) {
            DelayedTask delayedTask = iterator.next();
            delayedTask.ticksRemaining--;

            if (delayedTask.ticksRemaining <= 0) {
                delayedTask.task.run();
                iterator.remove();
            }
        }
    }

    private static class DelayedTask {
        int ticksRemaining;
        Runnable task;

        public DelayedTask(int ticks, Runnable task) {
            this.ticksRemaining = ticks;
            this.task = task;
        }
    }
}
