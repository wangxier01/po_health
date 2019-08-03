package com.itheima.service;

import com.itheima.pojo.Member;

import java.util.Map;

public interface MemberService {


     Member doLogin(String telephone);

    void addMember(Member mem);

    Map<String, Object> getCountMember();



}

