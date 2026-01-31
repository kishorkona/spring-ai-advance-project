package com.work.data;

import lombok.Data;

import java.util.Map;

@Data
public class PostDocuments {
    public String uniqueId;
    public String content;
    public Map<String, String> metadata;
}
