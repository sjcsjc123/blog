package com.example.myblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.myblog.domain.MyStar;
import com.example.myblog.mapper.MyStarMapper;
import com.example.myblog.service.MyStarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MyStarServiceImpl implements MyStarService {

    @Autowired
    private MyStarMapper myStarMapper;

    @Override
    public void star(String author, String username) {
        MyStar myStar = new MyStar();
        myStar.setAuthor(author);
        myStar.setUsername(username);
        myStarMapper.insert(myStar);
    }

    @Override
    public void cancel(String author, String username) {
        QueryWrapper<MyStar> wrapper = new QueryWrapper<>();
        wrapper.eq("author",author);
        wrapper.eq("username",username);
        myStarMapper.delete(wrapper);
    }

    @Override
    public List<MyStar> findByUsername(String username) {
        QueryWrapper<MyStar> wrapper = new QueryWrapper<>();
        wrapper.eq("username",username);
        return myStarMapper.selectList(wrapper);
    }
}
