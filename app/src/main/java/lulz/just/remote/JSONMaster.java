package lulz.just.remote;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class JSONMaster {
    private JSONObject mJSON;
    private Context mContext;

    JSONMaster(Context context) {
        mJSON = new JSONObject();
        mContext = context;
    }

    static String makeJSONString(JSONObject jSystem, Context activityContext) {
        return makeJSONString(jSystem, null, activityContext);
    }

    static String makeJSONString(Context context, String[]... array) {
        JSONObject json = new JSONObject();
        for (String[] item : array) {
            try {
                json.put(item[0], item[1]);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
        return makeJSONString(json, null, context);
    }

    static String makeJSONString(JSONObject jSystem, JSONArray jItems, Context activityContext) {
        try {
            JSONObject jData = new JSONObject();
            final String app_ver = activityContext.getPackageManager().getPackageInfo(activityContext.getPackageName(), 0).versionName;
            jData.put("ClientApp", app_ver);
            jData.put("ClientType", "1");

            if (null == jSystem)
                jSystem = new JSONObject();

            jData.put("System", jSystem);

            if (null == jItems)
                jItems = new JSONArray();

            jData.put("Items", jItems);

            return jData.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    static String getFieldFromJSON(String sFieldName, String json) {
        JSONObject dataJsonObj;
        String sFieldValue = "";
        try {
            dataJsonObj = new JSONObject(json);
            try {
                sFieldValue = dataJsonObj.getString(sFieldName);
                if (!sFieldValue.equals("")) return sFieldValue;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONArray arrayJSON = dataJsonObj.getJSONArray("Items");
            for (int i = 0; i < arrayJSON.length(); i++) {
                JSONObject obj = arrayJSON.getJSONObject(i);
                sFieldValue = obj.getString(sFieldName);
            }

            return sFieldValue.trim();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return sFieldValue;
    }

    void put(String field, String value) {
        try {
            mJSON.put(field, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    String makeString() {
        return makeJSONString(mJSON, mContext);
    }
}
