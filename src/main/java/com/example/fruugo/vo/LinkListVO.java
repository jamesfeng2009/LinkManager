package com.example.fruugo.vo;

import com.example.fruugo.dataobject.Link;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class LinkListVO {
    private List<Link> links;
}
