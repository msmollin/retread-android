package com.treadly.client.lib.sdk.Model;

import com.polidea.rxandroidble2.RxBleDevice;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* loaded from: classes2.dex */
public class DeviceInfo {
    private static final List<String> gen2List = Arrays.asList("d8a01d42ddfa", "d8a01d42ddca", "d8a01d42dc86", "d8a01d42dda2", "d8a01d42de1e", "d8a01d42dc0a", "d8a01d42dc12", "d8a01d42dbaa", "d8a01d42dc5e", "d8a01d42dbd6", "d8a01d42de02", "d8a01d42dbd2", "d8a01d42dac6", "d8a01d42dd2e", "d8a01d42edce", "d8a01d6292f6", "d8a01d42dc6a", "d8a01d42dae2", "d8a01d42dc02", "d8a01d42ddfe", "d8a01d42dada", "d8a01d4da012", "d8a01d4a3ada", "d8a01d4d9e1a", "d8a01d4da91a", "d8a01d4dafda", "d8a01d4db07a", "d8a01d4da826", "d8a01d4d9cee", "d8a01d4d9ca2", "d8a01d4db142", "d8a01d4d9686", "d8a01d4d9e86", "d8a01d4d9d86", "d8a01d4db026", "d8a01d4d9cf6", "D8A01D42DE06", "D8A01D4D9E5A", "D8A01D4DA7DA", "D8A01D4DA16A", "D8A01D4D9FDE", "D8A01D4DA866", "D8A01D4DA9B6", "D8A01D4D9E7E", "D8A01D4DA93E", "D8A01D4DA092", "D8A01D4DB6FA", "D8A01D4DA85A", "D8A01D4DA1E6", "D8A01D4DAC16", "D8A01D4DB85E", "D8A01D4DAE86", "D8A01D4D9F82", "D8A01D4DAC2E", "D8A01D4D9C1E", "D8A01D4D9E12", "D8A01D4D96B2", "D8A01D4D9BBA", "D8A01D4DABDE", "D8A01D4DA9E2", "D8A01D4DA6B6", "D8A01D42DC86", "D8A01D4DB142", "D8A01D42DAE2", "D8A01D628B0E", "D8A01D42DC5E", "D8A01D4DA7D6", "D8A01D4DA826", "D8A01D4D9E1A", "D8A01D4DB07A", "D8A01D42DE02", "D8A01D4D9E86", "D8A01D4A3ADA", "D8A01D4DB026", "D8A01D4D9CF6", "D8A01D4DAAEE");
    public List<ComponentInfo> components = new ArrayList();
    public String name;
    public RxBleDevice peripheral;
    public int priority;
    public double rssi;

    /* loaded from: classes2.dex */
    public enum DeviceGen {
        gen1,
        gen2,
        gen3
    }

    public DeviceInfo(String str, int i, double d) {
        this.name = str;
        this.priority = i;
        this.rssi = d;
    }

    public String getName() {
        return this.name;
    }

    public int getPriority() {
        return this.priority;
    }

    public double getRssi() {
        return this.rssi;
    }

    public String getDeviceId() {
        if (this.name.startsWith("Treadly_2_") || this.name.startsWith("Treadly2_") || this.name.startsWith("Treadly2-")) {
            return parseDeviceId(this.name);
        }
        if (this.name.startsWith("Treadly_")) {
            return parseDeviceId(this.name);
        }
        return null;
    }

    public static String parseDeviceId(String str) {
        if (str.length() >= 6) {
            return str.substring(str.length() - 6);
        }
        return null;
    }

    public ComponentInfo[] getComponents() {
        ComponentInfo[] componentInfoArr = new ComponentInfo[this.components.size()];
        this.components.toArray(componentInfoArr);
        return componentInfoArr;
    }

    public void addComponent(ComponentInfo componentInfo) {
        this.components.add(componentInfo);
    }

    public void removeComponent(ComponentInfo componentInfo) {
        for (ComponentInfo componentInfo2 : this.components) {
            if (componentInfo2.getType() == componentInfo.getType() && componentInfo2.getId().equals(componentInfo.getId())) {
                this.components.remove(componentInfo2);
                return;
            }
        }
    }

    public boolean containsComponent(ComponentType componentType, String str) {
        if (componentType == null || str == null) {
            return false;
        }
        for (ComponentInfo componentInfo : this.components) {
            if (componentInfo.getType() == componentType && componentInfo.getId().equals(str)) {
                return true;
            }
        }
        return false;
    }

    public DeviceGen getGen() {
        if (isGen2Type()) {
            return DeviceGen.gen2;
        }
        if (isGen1Type()) {
            return DeviceGen.gen1;
        }
        return DeviceGen.gen3;
    }

    public boolean isGen1Type() {
        return this.name.startsWith("Treadly_");
    }

    public boolean isGen2Type() {
        if (this.name.startsWith("Treadly_2_") || this.name.startsWith("Treadly2_") || this.name.startsWith("Treadly2-")) {
            return isGen2DeviceId(getDeviceId());
        }
        return false;
    }

    private boolean isGen2DeviceId(String str) {
        return gen2List.contains(str.toLowerCase());
    }
}
