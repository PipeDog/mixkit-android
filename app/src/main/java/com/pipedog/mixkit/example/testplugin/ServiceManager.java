package com.pipedog.mixkit.example.testplugin;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ServiceManager {

    private static final String TAG = ServiceManager.class.getSimpleName();
    private static ServiceManager sServiceManager = new ServiceManager();
    private List<IService> mServices;

    public static ServiceManager getInstance() {
        return sServiceManager;
    }

    private ServiceManager() {
        mServices = new ArrayList<>();
        autoCallRegisterService();
    }

    private void autoCallRegisterService() {
        // insert code that call `registerService()` method here
    }

    private void registerService(IService service) {
        Log.i(TAG, "register service : " + service.getClass().toString());
        mServices.add(service);
    }

    public void printAllServices() {
        Log.i(TAG, "services : " + mServices.toString());
    }


    // STATIC METHODS TEST

    private static List<IService> sServices = new ArrayList<>();

    public static void staticRegister(IService service) {
        Log.i("TAG", "register service : " + service.getClass().toString());
        sServices.add(service);
    }

    public static void printAllStaticServices() {
        Log.i("TAG", "services : " + sServices.toString());
    }

}
