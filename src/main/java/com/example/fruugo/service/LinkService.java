package com.example.fruugo.service;

import com.example.fruugo.dataobject.Link;
import com.example.fruugo.model.Result;
import com.example.fruugo.vo.LinkListVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LinkService {
    Result<LinkListVO> insertLink(List<Link> links);

    Result<LinkListVO> getAllLinks();

    Result<LinkListVO> updateLinks(List<Link> links);

    Result<LinkListVO> deleteLinks(List<Link> links);
}
