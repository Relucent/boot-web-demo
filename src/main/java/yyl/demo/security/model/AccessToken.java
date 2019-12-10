package yyl.demo.security.model;

import java.io.Serializable;

import lombok.Data;

@SuppressWarnings("serial")
@Data
public class AccessToken implements Serializable {
    private String value;
}
