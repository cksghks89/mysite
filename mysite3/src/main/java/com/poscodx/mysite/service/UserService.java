package com.poscodx.mysite.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poscodx.mysite.repository.UserRepository;
import com.poscodx.mysite.vo.UserVo;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;
	
	public void join(UserVo vo) {
		userRepository.insert(vo);
	}

	public UserVo getuser(String email, String password) {
		return userRepository.findByEmailAndPassword(email, password);
	}

	public UserVo getuser(Long no) {
		return userRepository.findByNo(no);
	}

	public void update(UserVo vo) {
		userRepository.update(vo);
	}
	
}
