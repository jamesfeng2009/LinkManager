package com.example.fruugo.service.impl;

import com.example.fruugo.dao.LinkDAO;
import com.example.fruugo.dataobject.Link;
import com.example.fruugo.model.Result;
import com.example.fruugo.service.LinkService;
import com.example.fruugo.vo.LinkListVO;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LinkServiceImpl implements LinkService {

    @Autowired
    private LinkDAO linkDAO;

    @Override
    public Result<LinkListVO> insertLink(List<Link> links) {
        Result<LinkListVO> result = new Result<>();
        if (ObjectUtils.isEmpty(links) || links.size() == 0) {
            result.setResultFailed("Link cannot be empty!");
            return result;
        }
        LinkListVO listVOs = new LinkListVO();
        for (Link link : links) {
            link.setStatus(1);
            linkDAO.add(link);
        }
        listVOs.setLinks(links);
        result.setResultSuccess("Jump link inserted successfully!", listVOs);
        return result;
    }

    @Override
    public Result<LinkListVO> getAllLinks() {
        LinkListVO listVO = new LinkListVO();
        Result<LinkListVO> result = new Result<>();
        List<Link> list = linkDAO.getAll();
        listVO.setLinks(list);
        result.setResultSuccess("Search successful!", listVO);
        return result;
    }

    @Override
    public Result<LinkListVO> updateLinks(List<Link> links) {
        Result<LinkListVO> result = new Result<>();
        if (ObjectUtils.isEmpty(links) || links.size() == 0) {
            result.setResultFailed("Input can not be empty!");
            return result;
        }
        LinkListVO listVO = new LinkListVO();
        for (Link link : links) {
            linkDAO.update(link);
        }
        listVO.setLinks(links);
        result.setResultSuccess("Update completed!", listVO);
        return result;
    }

    @Override
    public Result<LinkListVO> deleteLinks(List<Link> links) {
        Result<LinkListVO> result = new Result<>();
        if (ObjectUtils.isEmpty(links) || links.size() == 0) {
            result.setResultFailed("Input can not be empty!");
            return result;
        }
        LinkListVO listVO = new LinkListVO();
        for (Link link : links) {
            link.setStatus(0);
            linkDAO.delete(link);
        }
        listVO.setLinks(links);
        result.setResultSuccess("Delete completed!", listVO);
        return result;
    }
}
