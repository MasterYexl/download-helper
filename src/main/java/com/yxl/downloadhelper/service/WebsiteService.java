package com.yxl.downloadhelper.service;

import com.yxl.downloadhelper.common.model.ResponseTemplate;
import com.yxl.downloadhelper.common.model.jpa.CommonRepository;
import com.yxl.downloadhelper.common.service.CommonService;
import com.yxl.downloadhelper.component.searchengin.Spider;
import com.yxl.downloadhelper.model.entity.AuthorEntity;
import com.yxl.downloadhelper.model.entity.WebsiteEntity;
import com.yxl.downloadhelper.repository.AuthorRepository;
import com.yxl.downloadhelper.repository.WebsiteRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
public class WebsiteService extends CommonService<WebsiteEntity> {

    private final WebsiteRepository websiteRepository;
    private final Spider spider = new Spider();

    @Override
    public CommonRepository<WebsiteEntity> getRepository() {
        return websiteRepository;
    }

    public WebsiteEntity add(String url, String rule) {
        String indexUrl = parseUrl(url);
        WebsiteEntity entity = websiteRepository.getByUrl(indexUrl);
        if (entity != null) {
            entity.setRule(rule);
            ResponseTemplate<WebsiteEntity> save = save(entity);
            return save.getDetail();
        }
        WebsiteEntity website = createWebsite(indexUrl, rule);
        ResponseTemplate<WebsiteEntity> add = add(website);
        return add.getDetail();
    }

    public WebsiteEntity createWebsite(String url, String rule) {
        WebsiteEntity websiteEntity = new WebsiteEntity();
        websiteEntity.setUrl(url);
        websiteEntity.setRule(rule);
        try {
            websiteEntity.setName(getSiteName(websiteEntity.getUrl()));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return websiteEntity;
    }

    public String getSiteName(String url) throws IOException {
        Element element = spider.getElement(url);
        Element html = element.getElementsByTag("html").get(0);
        Element title = html.getElementsByTag("title").get(0);
        return title.text();
    }

    public String parseUrl(String url) {
        String regex = "(?i)^(https?|ftp)(://[^/]+)/[^/?]+.*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);
        if (!matcher.find()) {
            regex = "(.*//)?([^/]+).*";
            pattern = Pattern.compile(regex);
            matcher = pattern.matcher(url);
            if (!matcher.find()) {
                return url;
            }
        }
        return (matcher.group(1) == null? "" : matcher.group(1)) + matcher.group(2);
    }
}
