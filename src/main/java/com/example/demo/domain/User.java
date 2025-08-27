package com.example.demo.domain;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {
    @Id @GeneratedValue(strategy = 	GenerationType.IDENTITY) @EqualsAndHashCode.Include
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    /**
     * 已加密后的密码（如 BCrypt、PBKDF2、argon2 等）。
     * 记得在注册/修改密码时使用同一个 PasswordEncoder 进行加密。
     */
    @Column(nullable = false)
    private String password;   // BCrypt 暗号化

    /**
     * 与角色的多对多关系
     *
     * 说明:
     *   - fetch = FetchType.EAGER 让 Spring Security 在加载 User 时立即拿到权限列表，
     *     这样就不必手动再发一次查询。
     *   - cascade = {} 这里只做读操作，写时手动维护关系即可。
     */
    @ManyToMany(fetch = FetchType.EAGER, cascade = {})
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
        )
    private Set<Role> roles;
    
    /* ---------- 重要：返回 GrantedAuthority 所需的角色集合 ---------- */

    /**
     * Spring Security 需要一个 List<GrantedAuthority>，
     * 这里我们把 Role 名称直接映射为 SimpleGrantedAuthority。
     *
     * @return 角色集合
     */
    public Set<Role> getRoles() {
        return this.roles;
    }
}