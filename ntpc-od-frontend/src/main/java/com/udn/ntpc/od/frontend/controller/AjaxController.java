package com.udn.ntpc.od.frontend.controller;

import com.udn.ntpc.od.frontend.entity.domain.MockUser;
import com.udn.ntpc.od.frontend.entity.repository.MockUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
public class AjaxController {

    @Autowired
    private MockUserRepository userRepository;

    // https://docs.spring.io/spring-data/jpa/docs/2.1.5.RELEASE/reference/html/#core.web.basic
    @PostMapping("/userList")
    public Page<MockUser> findUsers(@RequestBody MockUser example, Pageable pageable) {
        log.info("{}", example);
        log.info("{}", pageable);
        // https://docs.spring.io/spring-data/jpa/docs/2.1.5.RELEASE/reference/html/#query-by-example.matchers
        ExampleMatcher matcher = ExampleMatcher.matching()
                                               // 忽略大小寫
                                               .withIgnoreCase(true)
                                               // 忽略空值欄位
                                               .withIgnoreNullValues();
        // https://docs.spring.io/spring-data/jpa/docs/2.1.5.RELEASE/reference/html/#repositories.limit-query-result
        // https://docs.spring.io/spring-data/jpa/docs/2.1.5.RELEASE/reference/html/#query-by-example
        return userRepository.findAll(Example.of(example, matcher), pageable);
    }

    @PostMapping("/user")
    public String saveUser(@RequestBody MockUser entity) {
        log.info("{}", entity);
        userRepository.saveAndFlush(entity);
        return "success";
    }

    @DeleteMapping("/user/{userId}")
    public String saveUser(@PathVariable Integer userId) {
        log.info("{}", userId);
        userRepository.deleteById(userId);
        return "success";
    }
}
