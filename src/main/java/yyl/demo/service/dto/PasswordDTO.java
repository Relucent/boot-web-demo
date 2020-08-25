package yyl.demo.service.dto;

import java.io.Serializable;

import lombok.Data;

/**
 * 修改密码
 */
@SuppressWarnings("serial")
@Data
public class PasswordDTO implements Serializable {
    private String oldPassword;
    private String newPassword;
}
