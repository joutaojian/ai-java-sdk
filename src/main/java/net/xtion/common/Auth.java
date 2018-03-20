package net.xtion.common;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Setter
@Getter
public final class Auth {

    private final String host;
    private final String accessKey;
    private final String secretKey;

    private Auth(String host, String accessKey, String secretKey) {
        this.host = host;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    public static Auth create(String host, String accessKey, String secretKey) {
        if (!StringUtils.isBlank(accessKey) && !StringUtils.isBlank(secretKey)) {
            return new Auth(host,accessKey, secretKey);
        } else {
            throw new IllegalArgumentException("empty key");
        }
    }

}