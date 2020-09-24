//package com.iflytek.gulimall.autherserver;
//
//import com.iflytek.common.utils.RsaUtils;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.security.PrivateKey;
//import java.security.PublicKey;
//import java.util.HashMap;
//import java.util.Map;
//
//@SpringBootTest
//class GulimallAutherServerApplicationTests {
//
//    // 别忘了创建D:\\project\rsa目录
//    private static final String pubKeyPath = "D:\\project\\rsa\\rsa.pub";
//    private static final String priKeyPath = "D:\\project\\rsa\\rsa.pri";
//    private PublicKey publicKey;
//    private PrivateKey privateKey;
//
//
//
//    @Test
//   public void contextLoads() {
//    }
//    //生存rsa公钥和私钥,并写入指定文件
//    @Test
//    public void testRsa() throws Exception {
//        RsaUtils.generateKey(pubKeyPath, priKeyPath, "234");
//    }
//    @BeforeEach
//    public void testGetRsa() throws Exception {
//        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
//        this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
//    }
//
//    /**
//     * 私钥加密
//     * @throws Exception
//     */
//    @Test
//    public void testGenerateToken() throws Exception {
//        Map<String, Object> map = new HashMap<>();
//        map.put("id", "11");
//        map.put("username", "liuyan");
//        // 生成token
//        String token = JwtUtils.generateToken(map, privateKey, 5);
//        System.out.println("token = " + token);
//
//    }
//
//    /**
//     * 公钥解析
//     * @throws Exception
//     */
//    @Test
//    public void testParseToken() throws Exception {
//        String token = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6IjExIiwidXNlcm5hbWUiOiJsaXV5YW4iLCJleHAiOjE1OTc3Mzk5NTV9.FmUBsugmZUiWL-Hj-Q5yLfPumSjGg9oSDGVeGy6iNklWWo6ECLl1eBlx_TaETOjleLu377xOSrVQkqUfn1tdSvmRL1-uMJftOigFauk3Qo_ryW7AOXUOPWtXd4bTcWEH7EoKLigyBGCHssLX41Cth4ov0i1pA7BRcGHScUzc0DOoozyIqgwC-uMvv8xRwOxZXU_QRjU6GVzSsvenaGn2_dtyYS1vZgy3Kzni7qQXo0gmzJ6Oa4n7xRHHIxY-WiOoOrtKCzMNYhUh7UVB-DYTOL9YBkFqoiQpKjDbcm8FtUrXEzfWWM3vLwY_O7v-pi1xvjXt5c0WAW1iBRsBgxqPig";
//        // 解析token
//        Map<String, Object> map = JwtUtils.getInfoFromToken(token, publicKey);
//        System.out.println("id: " + map.get("id"));
//        System.out.println("userName: " + map.get("username"));
//    }
//
//
//
//}
