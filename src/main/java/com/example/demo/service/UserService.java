package com.example.demo.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.domain.Role;
import com.example.demo.domain.User;
import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository; // User 表对应的 Repository

	public User register(String username, String rawPwd, Set<Role> Roles) {
		User u = new User(null, username, rawPwd, Roles);
		return userRepository.save(u);
	}

	/**
	 * 根据用户名查询用户信息，返回 Spring Security 需要的 UserDetails 对象
	 *
	 * @param username 登录时使用的用户名
	 * @return UserDetails，包含用户名、已加密密码以及权限列表
	 * @throws UsernameNotFoundException 若用户名不存在
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found" + username));

		// 認可があればここで設定できる
		// ②（可选）把角色/权限映射为 GrantedAuthority 列表
		List<GrantedAuthority> authorities = user.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority(role.getName()))
				.collect(Collectors.toList());

		// org.springframework.security.core.userdetails.Userにして返却する
		// パスワードエンコーダを利用してパスワードはエンコードをかける
		return new org.springframework.security.core.userdetails.User(
				user.getUsername(),
				user.getPassword(),
				authorities);
	}
}