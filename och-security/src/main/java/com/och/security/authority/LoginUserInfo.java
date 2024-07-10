package com.och.security.authority;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author danmo
 * @date 2024-02-21 13:44
 **/
@Schema
@Data
public class LoginUserInfo implements UserDetails, CredentialsContainer {

    @Schema(description = "用户id")
    private Long userId;
    @Schema(description = "头像")
    private String avatar;
    @Schema(description = "性别")
    private Integer sex;
    @Schema(description = "手机号")
    private String phone;
    @Schema(description = "用户名")
    private String username;
    @Schema(description = "昵称")
    private String nickName;
    @Schema(description = "密码")
    private String password;
    @Schema(description = "角色id")
    private List<Long> roleIds;
    @Schema(description = "数据权限")
    private List<Integer> dataScope;
    @Schema(description = "权限")
    private Set<OchGrantedAuthority> authorities;
    @Schema(description = "是否过期",hidden = true)
    private boolean accountNonExpired;
    @Schema(description = "是否锁定",hidden = true)
    private boolean accountNonLocked;
    @Schema(description = "是否过期",hidden = true)
    private boolean credentialsNonExpired;
    @Schema(description = "是否启用",hidden = true)
    private boolean enabled;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public void eraseCredentials() {
        this.password = null;
    }

    public int hashCode() {
        return this.username.hashCode();
    }

    public boolean equals(Object obj) {
        if (obj instanceof User user) {
            return this.username.equals(user.getUsername());
        } else {
            return false;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getClass().getName()).append(" [");
        sb.append("Username=").append(this.username).append(", ");
        sb.append("Password=[PROTECTED], ");
        sb.append("RoleIds=").append(getRoleIds().toString()).append(", ");
        sb.append("DataScope=").append(getDataScope().toString()).append(", ");
        sb.append("Enabled=").append(this.enabled).append(", ");
        sb.append("AccountNonExpired=").append(this.accountNonExpired).append(", ");
        sb.append("CredentialsNonExpired=").append(this.credentialsNonExpired).append(", ");
        sb.append("AccountNonLocked=").append(this.accountNonLocked).append(", ");
        sb.append("Granted Authorities=").append(this.authorities).append("]");
        return sb.toString();
    }


}
