package team.imcc.ltbl;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ParticleUtil {

    private static Class<?> packetClass = null;
    private static Class<Enum> enumParticle = null;
    private static Constructor<?> packetConstructor = null;

    // @note (jyfeng) only compatible in 1.8 or above
    static {
        try {
            packetClass = getNmsClass("PacketPlayOutWorldParticles");
            enumParticle = (Class<Enum>) getNmsClass("EnumParticle");
            packetConstructor = packetClass.getDeclaredConstructor(enumParticle, boolean.class,
                    float.class,    float.class,   float.class,
                    float.class,    float.class,   float.class,
                    float.class,    int.class,     int[].class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Object createPacket(int typeId, float x, float y, float z, float radiusX, float radiusY, float radiusZ, float speed, int count) {
        Object packet = null;
        try {
            Object particleType = enumParticle.getEnumConstants()[typeId];
            packet = packetConstructor.newInstance(particleType, true,
                    x, y, z, radiusX, radiusY, radiusZ, speed, count, new int[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return packet;
    }

    private static void sendPacket(Player player, Object packet) {
        try {
            Method player_getHandle = player.getClass().getMethod("getHandle");
            Field player_connection  = player_getHandle.invoke(player).getClass().getField("playerConnection");

            Method player_sendPacket = null;
            for (Method m : player_connection.get(player_getHandle.invoke(player)).getClass().getMethods()) {
                if (m.getName().equalsIgnoreCase("sendPacket")){
                    player_sendPacket = m;
                }
            }
            player_sendPacket.invoke(player_connection.get(player_getHandle.invoke(player)), packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendParticle(Player player, Location location, int typeId, float speed, int count) {
        try {
            Object packet = createPacket(typeId,
                    (float) location.getX(), (float) location.getY(), (float) location.getZ(),
                    0, 0, 0, speed, count);
            sendPacket(player, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getVersion(){
        String[] array = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",");
        if (array.length == 4) {
            return array[3] + ".";
        }
        return "";
    }

    private static Class<?> getNmsClass(String name){
        String version = getVersion();
        String className = "net.minecraft.server." + version + name;
        Class<?> target = null;
        try {
            target = Class.forName(className);
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return target;
    }
}
