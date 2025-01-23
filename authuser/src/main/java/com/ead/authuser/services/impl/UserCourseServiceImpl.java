package com.ead.authuser.services.impl;

import com.ead.authuser.repositories.UserRepository;
import com.ead.authuser.services.UserCourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCourseServiceImpl implements UserCourseService {

    private final UserRepository userRepository;
}
