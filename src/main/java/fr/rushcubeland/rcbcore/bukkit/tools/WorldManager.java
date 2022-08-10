package fr.rushcubeland.rcbcore.bukkit.tools;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;

import java.io.*;

public class WorldManager {

    public static void replaceWorld(String worldName, boolean active){
        if(active){
            WorldManager.deleteWorld(worldName);
            File from = new File(worldName + "_default");
            File to = new File(worldName);
            try {
                WorldManager.copyFolder(from, to);
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public static void copyFolder(File src, File dest) throws IOException {
        if(src.isDirectory()){
            if(!dest.exists()){
                dest.mkdir();
            }
            String[] files = src.list();
            if(files == null){
                return;
            }
            String[] arrayOfString;
            int j = (arrayOfString = files).length;
            for(int i = 0; i < j; i++){
                String file = arrayOfString[i];
                File srcFile = new File(src, file);
                File destFile = new File(dest, file);
                copyFolder(srcFile, destFile);
            }
        }
        else
        {
            InputStream in = new FileInputStream(src);
            OutputStream out = new FileOutputStream(dest);
            byte[] buffer = new byte['?'];
            int length;
            while((length = in.read(buffer)) > 0){
                out.write(buffer, 0, length);
            }
            in.close();
            out.close();
        }
    }

    public static void deleteWorld(String worldName){
        World world = Bukkit.getWorld(worldName);
        File file = new File(worldName);
        if(file == null || world == null){
            return;
        }
        Bukkit.unloadWorld(worldName, false);
        try {
            FileUtils.deleteDirectory(file);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public static boolean deleteWorld(File path){
        if(path.exists()){
            File[] files = path.listFiles();
            for(int x = 0; x < files.length; x++){
                if(files[x].isDirectory()){
                    deleteWorld(files[x]);
                }
                else
                {
                    files[x].delete();
                }
            }
        }
        return path.delete();
    }
}
