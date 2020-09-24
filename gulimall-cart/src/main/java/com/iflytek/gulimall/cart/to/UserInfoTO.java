package com.iflytek.gulimall.cart.to;

        import lombok.Data;
        import lombok.ToString;

        import java.io.Serializable;

@Data
@ToString
public class UserInfoTO implements Serializable {

    private String userId;
    private String userKey;

}
