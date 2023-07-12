package com.facebook.appevents.ml;

import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import com.facebook.appevents.ml.ModelManager;
import com.facebook.internal.instrument.crashshield.CrashShieldHandler;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
/* loaded from: classes.dex */
public final class Model {
    private static final int SEQ_LEN = 128;
    private MTensor convs_0_bias;
    private MTensor convs_0_weight;
    private MTensor convs_1_bias;
    private MTensor convs_1_weight;
    private MTensor convs_2_bias;
    private MTensor convs_2_weight;
    private MTensor embedding;
    private MTensor fc1_bias;
    private MTensor fc1_weight;
    private MTensor fc2_bias;
    private MTensor fc2_weight;
    private final Map<String, MTensor> final_weights = new HashMap();

    private Model(Map<String, MTensor> map) {
        this.embedding = map.get("embed.weight");
        this.convs_0_weight = Operator.transpose3D(map.get("convs.0.weight"));
        this.convs_1_weight = Operator.transpose3D(map.get("convs.1.weight"));
        this.convs_2_weight = Operator.transpose3D(map.get("convs.2.weight"));
        this.convs_0_bias = map.get("convs.0.bias");
        this.convs_1_bias = map.get("convs.1.bias");
        this.convs_2_bias = map.get("convs.2.bias");
        this.fc1_weight = Operator.transpose2D(map.get("fc1.weight"));
        this.fc2_weight = Operator.transpose2D(map.get("fc2.weight"));
        this.fc1_bias = map.get("fc1.bias");
        this.fc2_bias = map.get("fc2.bias");
        for (String str : new HashSet<String>() { // from class: com.facebook.appevents.ml.Model.1
            {
                add(ModelManager.Task.MTML_INTEGRITY_DETECT.toKey());
                add(ModelManager.Task.MTML_APP_EVENT_PREDICTION.toKey());
            }
        }) {
            String str2 = str + ".weight";
            String str3 = str + ".bias";
            MTensor mTensor = map.get(str2);
            MTensor mTensor2 = map.get(str3);
            if (mTensor != null) {
                this.final_weights.put(str2, Operator.transpose2D(mTensor));
            }
            if (mTensor2 != null) {
                this.final_weights.put(str3, mTensor2);
            }
        }
    }

    @Nullable
    public MTensor predictOnMTML(MTensor mTensor, String[] strArr, String str) {
        if (CrashShieldHandler.isObjectCrashing(this)) {
            return null;
        }
        try {
            MTensor conv1D = Operator.conv1D(Operator.embedding(strArr, 128, this.embedding), this.convs_0_weight);
            Operator.addmv(conv1D, this.convs_0_bias);
            Operator.relu(conv1D);
            MTensor conv1D2 = Operator.conv1D(conv1D, this.convs_1_weight);
            Operator.addmv(conv1D2, this.convs_1_bias);
            Operator.relu(conv1D2);
            MTensor maxPool1D = Operator.maxPool1D(conv1D2, 2);
            MTensor conv1D3 = Operator.conv1D(maxPool1D, this.convs_2_weight);
            Operator.addmv(conv1D3, this.convs_2_bias);
            Operator.relu(conv1D3);
            MTensor maxPool1D2 = Operator.maxPool1D(conv1D, conv1D.getShape(1));
            MTensor maxPool1D3 = Operator.maxPool1D(maxPool1D, maxPool1D.getShape(1));
            MTensor maxPool1D4 = Operator.maxPool1D(conv1D3, conv1D3.getShape(1));
            Operator.flatten(maxPool1D2, 1);
            Operator.flatten(maxPool1D3, 1);
            Operator.flatten(maxPool1D4, 1);
            MTensor dense = Operator.dense(Operator.concatenate(new MTensor[]{maxPool1D2, maxPool1D3, maxPool1D4, mTensor}), this.fc1_weight, this.fc1_bias);
            Operator.relu(dense);
            MTensor dense2 = Operator.dense(dense, this.fc2_weight, this.fc2_bias);
            Operator.relu(dense2);
            Map<String, MTensor> map = this.final_weights;
            MTensor mTensor2 = map.get(str + ".weight");
            Map<String, MTensor> map2 = this.final_weights;
            MTensor mTensor3 = map2.get(str + ".bias");
            if (mTensor2 != null && mTensor3 != null) {
                MTensor dense3 = Operator.dense(dense2, mTensor2, mTensor3);
                Operator.softmax(dense3);
                return dense3;
            }
            return null;
        } catch (Throwable th) {
            CrashShieldHandler.handleThrowable(th, this);
            return null;
        }
    }

    @Nullable
    public static Model build(File file) {
        if (CrashShieldHandler.isObjectCrashing(Model.class)) {
            return null;
        }
        try {
            try {
                return new Model(parse(file));
            } catch (Exception unused) {
                return null;
            }
        } catch (Throwable th) {
            CrashShieldHandler.handleThrowable(th, Model.class);
            return null;
        }
    }

    @Nullable
    private static Map<String, MTensor> parse(File file) {
        if (CrashShieldHandler.isObjectCrashing(Model.class)) {
            return null;
        }
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            int available = fileInputStream.available();
            DataInputStream dataInputStream = new DataInputStream(fileInputStream);
            byte[] bArr = new byte[available];
            dataInputStream.readFully(bArr);
            dataInputStream.close();
            if (available < 4) {
                return null;
            }
            int i = 0;
            ByteBuffer wrap = ByteBuffer.wrap(bArr, 0, 4);
            wrap.order(ByteOrder.LITTLE_ENDIAN);
            int i2 = wrap.getInt();
            int i3 = i2 + 4;
            if (available < i3) {
                return null;
            }
            JSONObject jSONObject = new JSONObject(new String(bArr, 4, i2));
            JSONArray names = jSONObject.names();
            String[] strArr = new String[names.length()];
            for (int i4 = 0; i4 < strArr.length; i4++) {
                strArr[i4] = names.getString(i4);
            }
            Arrays.sort(strArr);
            HashMap hashMap = new HashMap();
            Map<String, String> mapping = getMapping();
            int length = strArr.length;
            int i5 = i3;
            int i6 = 0;
            while (i6 < length) {
                String str = strArr[i6];
                JSONArray jSONArray = jSONObject.getJSONArray(str);
                int[] iArr = new int[jSONArray.length()];
                int i7 = 1;
                for (int i8 = i; i8 < iArr.length; i8++) {
                    iArr[i8] = jSONArray.getInt(i8);
                    i7 *= iArr[i8];
                }
                int i9 = i7 * 4;
                int i10 = i5 + i9;
                if (i10 > available) {
                    return null;
                }
                ByteBuffer wrap2 = ByteBuffer.wrap(bArr, i5, i9);
                wrap2.order(ByteOrder.LITTLE_ENDIAN);
                MTensor mTensor = new MTensor(iArr);
                wrap2.asFloatBuffer().get(mTensor.getData(), 0, i7);
                if (mapping.containsKey(str)) {
                    str = mapping.get(str);
                }
                hashMap.put(str, mTensor);
                i6++;
                i5 = i10;
                i = 0;
            }
            return hashMap;
        } catch (Exception unused) {
            return null;
        } catch (Throwable th) {
            CrashShieldHandler.handleThrowable(th, Model.class);
            return null;
        }
    }

    private static Map<String, String> getMapping() {
        if (CrashShieldHandler.isObjectCrashing(Model.class)) {
            return null;
        }
        try {
            return new HashMap<String, String>() { // from class: com.facebook.appevents.ml.Model.2
                {
                    put("embedding.weight", "embed.weight");
                    put("dense1.weight", "fc1.weight");
                    put("dense2.weight", "fc2.weight");
                    put("dense3.weight", "fc3.weight");
                    put("dense1.bias", "fc1.bias");
                    put("dense2.bias", "fc2.bias");
                    put("dense3.bias", "fc3.bias");
                }
            };
        } catch (Throwable th) {
            CrashShieldHandler.handleThrowable(th, Model.class);
            return null;
        }
    }
}
