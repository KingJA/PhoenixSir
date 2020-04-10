package sample.kingja.phoenixsir;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Description:TODO
 * Create Time:2020/4/10 0010 下午 2:42
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
public class ResultData {

    /**
     * status_code : 200
     * message : 成功
     * data : {"status":1,"type":1,"download_url":"http://www.chinafwcq.com/app/20191101172309.apk"}
     */

    private int status_code;
    private String message;
    private DataBean data;

    /**
     * status : 1
     * type : 0
     * download_url : https://www.baidu.com/
     */


    public static ResultData parse(String reponse) {
        try {
            JSONObject jsonObject = new JSONObject(reponse);

            JSONObject data = jsonObject.optJSONObject("data");

            int status = data.optInt("status");
            int type = data.optInt("type");
            String downloadUrl = data.optString("download_url");


            ResultData resultData = new ResultData();

            DataBean dataBean = new DataBean();
            dataBean.setType(type);
            dataBean.setStatus(status);
            dataBean.setDownload_url(downloadUrl);
            resultData.setData(dataBean);
            return resultData;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getStatus_code() {
        return status_code;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }


    public static class DataBean {
        /**
         * status : 1
         * type : 1
         * download_url : http://www.chinafwcq.com/app/20191101172309.apk
         */

        private int status;
        private int type;
        private String download_url;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getDownload_url() {
            return download_url;
        }

        public void setDownload_url(String download_url) {
            this.download_url = download_url;
        }
    }
}
