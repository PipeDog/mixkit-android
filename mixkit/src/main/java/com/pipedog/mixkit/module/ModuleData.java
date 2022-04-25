package com.pipedog.mixkit.module;

import com.pipedog.mixkit.kernel.IBridgeModule;
import com.pipedog.mixkit.tool.MixLogger;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自定义 module 信息描述（允许 class 不同但 module 名相同，也就是说一个 MixModuleData 实例中可能包含多个 class 的导出信息）
 * @author liang
 */
public class ModuleData implements Serializable {

    /**
     * 类名列表（所有被导出为这个 module 名称的 class 都会出现在这个列表内）
     */
    public List<String> classes;

    /**
     * 方法信息表，key 为导出函数名，value 为对应方法描述信息
     */
    public Map<String, ModuleMethod> methods;

    /**
     * 常量表（运行时动态生成），Key - 常量名，Value - 常量值
     * 注意：
     *      Value 只支持原始数据类型，包括：int、float、double、String、Map、List 等，不支持自定义类型
     */
    public Map<String, Object> constantsTable;


    // PUBLIC METHODS

    /**
     * 动态整合并注册本地常量表到当前类的属性 `constantsTable`
     */
    public void dynamicRegisterConstantsTable() {
        if (classes == null) {
            return;
        }

        Map<String, Object> mergeTable = new HashMap<>();

        for (String className : classes) {
            Class aClass = null;
            try {
                aClass = Class.forName(className);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            if (!IBridgeModule.class.isAssignableFrom(aClass)) {
                MixLogger.error(String.format("Class %s does not comply with" +
                        " the interface %s", className, IBridgeModule.class));
                continue;
            }

            Method constantsToExportMethod = null;
            try {
                constantsToExportMethod = aClass.getMethod("constantsToExport");
                constantsToExportMethod.setAccessible(true);
                Map<String, Object> moduleConstantsTable =
                        (Map<String, Object>) constantsToExportMethod.invoke(aClass);
                mergeTable.putAll(moduleConstantsTable);
            } catch (Exception e) {
                e.printStackTrace();
                MixLogger.error("Invoke static method failed, class = %s!", className);
            }
        }

        this.constantsTable = mergeTable;
    }


    // OVERRIDE METHODS

    @Override
    public String toString() {
        return "ModuleData{" +
                "classes=" + classes +
                ", methods=" + methods +
                ", constantsTable=" + constantsTable +
                '}';
    }

}
