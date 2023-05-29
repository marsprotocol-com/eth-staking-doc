package zh.developers.util;

import com.alibaba.fastjson.JSONObject;
import org.springframework.util.DigestUtils;

import java.util.Arrays;

/**
 * @author scr
 */
class Eth2ApiHeader {

    private String apiId;

    private String apiTs;

    private String sign;

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public String getApiTs() {
        return apiTs;
    }

    public void setApiTs(String apiTs) {
        this.apiTs = apiTs;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}

public class ApiSignUtil {

    private static String md5Str(String paramStr){
        return DigestUtils.md5DigestAsHex(paramStr.getBytes());
    }

    public static String signReq(Eth2ApiHeader header, JSONObject body, String secret){
        String bodyStr = body.toJSONString();
        char[] bodyToChar = bodyStr.toCharArray();
        Arrays.sort(bodyToChar);
        String bodySort = new String(bodyToChar);
        String originStr = bodySort + "&apiId=" + header.getApiId()
                + "&apiTs=" + header.getApiTs()
                + "&secret=" + secret;

        return md5Str(originStr);
    }

    public static void main(String[] args) {
        Eth2ApiHeader header = new Eth2ApiHeader();
        header.setApiId("a1");
        header.setApiTs("1685346885");
        header.setSign("46d2d7ccd318f307a9b62d9e72287541");
        JSONObject body = JSONObject.parseObject("{\n" +
                "  \"num\": 1,\n" +
                "  \"rewardAddress\": \"0x555d46a4670b380473e5aedbac41487b2aadc050\"\n" +
                "}");
        String secret = "aaabbb";
        String sign = signReq(header, body, secret);
        assert header.getSign().equals(sign);
    }
}
