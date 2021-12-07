package com.pipedog.mixkit.plugin.utils

class PerfMonitor {

    private Map<String, Long> recordMap
    private static final PerfMonitor sPerfMonitor = new PerfMonitor();


    // PUBLIC METHODS

    static void startPerf(String key) {
        getInstance()._startPerf(key)
    }

    static void endPerf(String key) {
        getInstance()._endPerf(key)
    }


    // PRIVATE METHODS

    private static PerfMonitor getInstance() {
        return sPerfMonitor
    }

    private PerfMonitor() {
        recordMap = new HashMap<>()
    }

    void _startPerf(String key) {
        Long startTime = System.currentTimeMillis()
        recordMap.put(key, startTime)
    }

    void _endPerf(String key) {
        Long startTime = recordMap.get(key)
        Long endTime = System.currentTimeMillis()
        Long cost = endTime - startTime

        recordMap.remove(key)
        Logger.i("[" + key + "] " + "cost time : " + cost + "ms")
    }

}