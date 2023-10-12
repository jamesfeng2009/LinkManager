package com.example.fruugo.dao;

import com.example.fruugo.dataobject.Link;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LinkDAO {
    int add(Link link);
    int update(Link link);
    int delete(Link link);
    List<Link> getAll();
}

