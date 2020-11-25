package com.xymall.baby.Utils;

import java.util.LinkedHashMap;
import java.util.Map;

public class PageQueryUtil extends LinkedHashMap<String,Object> {
    int page;
    int limit;
    public PageQueryUtil(Map<String,Object> params){
        //注意这里使用的是putAll方法，将所有的键值对赋进去，很巧妙
        this.putAll(params);
        this.page=Integer.parseInt(params.get("page").toString());
        this.limit=Integer.parseInt(params.get("limit").toString());

        int start=(page-1)*limit;
        this.put("page",page);
        this.put("limit",limit);
        this.put("start",start);
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;

    }

    @Override
    public String toString() {
        return "PageUtil{" +
                "page=" + page +
                ", limit=" + limit +
                '}';
    }
}
