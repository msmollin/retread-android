package com.treadly.client.lib.sdk.Model;

import java.util.Date;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class FactoryTestResults {
    public String appVersion;
    public String customBoardMacAddress;
    public long customBoardSerialNumber;
    public String customBoardVersion;
    public boolean pass;
    public FactoryTestCaseResults testCaseResults;
    public Date time;
    public String treadlyBarcode;
    public String uuid;

    public FactoryTestResults(Date date, String str, String str2, long j, String str3, String str4, String str5, FactoryTestCaseResults factoryTestCaseResults, boolean z) {
        this.time = date;
        this.uuid = str;
        this.appVersion = str2;
        this.customBoardSerialNumber = j;
        this.customBoardMacAddress = str3;
        this.customBoardVersion = str4;
        this.treadlyBarcode = str5;
        this.testCaseResults = factoryTestCaseResults;
        this.pass = z;
    }

    public FactoryTestResults() {
    }

    public static FactoryTestResults deserialize(String str) {
        try {
            JSONObject jSONObject = new JSONObject(str);
            FactoryTestResults factoryTestResults = new FactoryTestResults();
            factoryTestResults.time = new Date(jSONObject.optInt("time", 0));
            factoryTestResults.uuid = jSONObject.optString("uuid");
            factoryTestResults.appVersion = jSONObject.optString("app_version");
            factoryTestResults.customBoardSerialNumber = jSONObject.optLong("serial_number");
            factoryTestResults.customBoardVersion = jSONObject.optString("version");
            factoryTestResults.customBoardMacAddress = jSONObject.optString("mac_address");
            factoryTestResults.treadlyBarcode = jSONObject.optString("barcode");
            factoryTestResults.pass = jSONObject.optBoolean("pass");
            factoryTestResults.testCaseResults = new FactoryTestCaseResults();
            factoryTestResults.testCaseResults.start = jSONObject.optBoolean("start_test");
            factoryTestResults.testCaseResults.accelerateDown = jSONObject.optBoolean("accelerate");
            factoryTestResults.testCaseResults.accelerateUp = jSONObject.optBoolean("auto_accelerate");
            factoryTestResults.testCaseResults.startUp = jSONObject.optBoolean("auto_start");
            factoryTestResults.testCaseResults.stopUp = jSONObject.optBoolean("auto_stop");
            factoryTestResults.testCaseResults.factoryReset = jSONObject.optBoolean("factory_reset");
            factoryTestResults.testCaseResults.handrailDown = jSONObject.optBoolean("handrail_down");
            factoryTestResults.testCaseResults.handrailUp = jSONObject.optBoolean("handrail_up");
            factoryTestResults.testCaseResults.startDown = jSONObject.optBoolean("start");
            factoryTestResults.testCaseResults.stopDown = jSONObject.optBoolean("stop");
            factoryTestResults.testCaseResults.temperature = jSONObject.optBoolean("temperature");
            return factoryTestResults;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String serialize() {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("time", this.time.getTime());
            jSONObject.put("uuid", this.uuid);
            jSONObject.put("app_version", this.appVersion);
            jSONObject.put("serial_number", this.customBoardSerialNumber);
            jSONObject.put("version", this.customBoardVersion);
            jSONObject.put("mac_address", this.customBoardMacAddress);
            jSONObject.put("barcode", this.treadlyBarcode);
            jSONObject.put("pass", this.pass);
            jSONObject.put("start_test", this.testCaseResults.start);
            jSONObject.put("accelerate", this.testCaseResults.accelerateDown);
            jSONObject.put("auto_accelerate", this.testCaseResults.accelerateUp);
            jSONObject.put("auto_start", this.testCaseResults.startUp);
            jSONObject.put("auto_stop", this.testCaseResults.stopUp);
            jSONObject.put("factory_reset", this.testCaseResults.factoryReset);
            jSONObject.put("handrail_down", this.testCaseResults.handrailDown);
            jSONObject.put("handrail_up", this.testCaseResults.handrailUp);
            jSONObject.put("start", this.testCaseResults.startDown);
            jSONObject.put("stop", this.testCaseResults.stopDown);
            jSONObject.put("temperature", this.testCaseResults.temperature);
            return jSONObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
