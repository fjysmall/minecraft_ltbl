package team.imcc.ltbl;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class ParticleUtil {

    public static void sendParticle(Player player, Location location) {
        try {
            Particle particle = Particle.BARRIER;
            player.spawnParticle(particle, location, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
