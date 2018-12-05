# LET THERE BE LIGHT (LTBL)

This is a bukkit plugin use to show where should be lighted. :)

## Usage

### Step 0: Install Java 8 and Maven 2 (lastest version recommanded).

### Step 1: Clone this project and enter its directory.
```
git clone git@github.com:fjysmall/minecraft_ltbl.git
cd ./minecraft_ltbl/
```

### Step 2: Download CraftBukkit jar file.
```
wget https://cdn.getbukkit.org/craftbukkit/craftbukkit-1.12.jar --directory-prefix=./lib/
```

### Step 3: Compile & Package.
```
mvn clean install
```

### Step 4: Drag ltbl jar file to plugins directory.
```
cp target/minecraft_ltbl*.jar ${YOUR-MINECRAFT-DIRECTORY}/plugins/
```
At last, Reloading or Restart your minecraft server. Enter game server and enter command */ltbl*, see if anything changes.

## Know Issues
- Only tested in minecraft version 1.12.
- Only working in Over Wolrd for now.

Good luck and have fun~
