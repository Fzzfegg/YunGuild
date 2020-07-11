package fzzfegg.effectbuff;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.scheduler.BukkitTask;


import java.util.Set;

public class Eccet implements Listener {
    BukkitTask a;
    Set con = EffectBuff.instance.getConfig().getKeys(false);

    @EventHandler
    public void Lightning(BlockIgniteEvent e) {
        if (!EffectBuff.instance.getConfig().getBoolean("LightningFire"))
            if (e.getCause() == BlockIgniteEvent.IgniteCause.LIGHTNING)
                e.setCancelled(true);
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onnDamage(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && (e.getDamager().getType().equals((Object) EntityType.ENDER_PEARL))) {
            e.setCancelled(true);
        }

    }



    @EventHandler
    public void DamageEvent(EntityDamageEvent e){
        for (Object each : con){
            if (e.getCause().toString().equals(each.toString())){
                if (EffectBuff.instance.getConfig().get(each.toString()).toString().equalsIgnoreCase("false")){
                    e.setCancelled(true);
                }else if(EffectBuff.instance.getConfig().get(each.toString()).toString().contains("%")){
                    if (e.getEntity() instanceof Player) {
                        Damageable damageable = (Damageable) e.getEntity();
                        AttributeInstance a = ((Player) e.getEntity()).getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH);
                        double sh = (Double.parseDouble(String.valueOf(a.getValue()))) * (Double.parseDouble(EffectBuff.instance.getConfig().get(each.toString()).toString().split("%")[0]));
                        damageable.damage(sh);
                    }
                }else if(EffectBuff.instance.getConfig().get(each.toString()).toString().contains("true")){
                    if (e.getEntity() instanceof Player) {
                        a = Bukkit.getScheduler().runTaskLater(EffectBuff.instance, () -> {
                            Damageable damageable = (Damageable) e.getEntity();
                            damageable.damage(Double.parseDouble(EffectBuff.instance.getConfig().get(each.toString()).toString().split(":")[0]));
                        },20L);
                    }
                }else {
                    e.setDamage(EffectBuff.instance.getConfig().getInt(each.toString()));
                }
            }
        }
    }








}
