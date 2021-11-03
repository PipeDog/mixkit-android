package com.pipedog.mixkit.autoregister

// 已扫描到接口或者 codeInsertToClassName jar的信息
class ScanJarHarvest {
    List<Harvest> harvestList = new ArrayList<>()
    class Harvest {
        String className
        String interfaceName
        boolean isInitClass
    }
}