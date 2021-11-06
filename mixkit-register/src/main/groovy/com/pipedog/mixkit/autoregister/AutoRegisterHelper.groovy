package com.pipedog.mixkit.autoregister

import com.google.gson.Gson
import org.gradle.api.Project

import java.lang.reflect.Type

import static com.android.builder.model.AndroidProject.FD_INTERMEDIATES

/* 文件操作辅助类 */
class AutoRegisterHelper {

    private static final String CACHE_INFO_DIR = "auto-register"

    /* 缓存自动注册配置的文件 */
    static File getRegisterInfoCacheFile(Project project) {
        String baseDir = getCacheFileDir(project)
        if (!mkdirs(baseDir)) {
            throw new FileNotFoundException("Not found  path:" + baseDir)
        }

        return new File(baseDir + "register-info.config")
    }

    /* 缓存扫描到结果的文件 */
    static File getRegisterCacheFile(Project project) {
        String baseDir = getCacheFileDir(project)
        if (!mkdirs(baseDir)) {
            throw new FileNotFoundException("Not found path: " + baseDir)
        }

        return new File(baseDir + "register-cache.json")
    }

    /* 将扫描到的结果缓存起来 */
    static void cacheRegisterHarvest(File cacheFile, String harvests) {
        if (cacheFile == null || harvests == null) {
            return
        }

        cacheFile.getParentFile().mkdirs()
        if (!cacheFile.exists()) {
            cacheFile.createNewFile()
        }

        cacheFile.write(harvests)
    }

    private static String getCacheFileDir(Project project) {
        return project.getBuildDir().absolutePath +
            File.separator +
            FD_INTERMEDIATES +
            File.separator +
            CACHE_INFO_DIR +
            File.separator
    }

    /**
     * 读取文件内容并创建 Map
     * @param file 缓存文件
     * @param type map 的类型
     */
    static Map readToMap(File file, Type type) {
        Map map = new HashMap()

        if (!file.exists()) { return map }
        if (type == null) { return map }
        if (file.text == null) { return map }

        try {
            map = new Gson().fromJson(file.text, type)
        } catch (Exception e) {
            e.printStackTrace()
            return new HashMap()
        }

        return map
    }

    /* 创建文件夹 */
    static boolean mkdirs(String dirPath) {
        def baseDirFile = new File(dirPath)
        if (baseDirFile.isDirectory()) {
            return true
        }

        return baseDirFile.mkdirs()
    }

}