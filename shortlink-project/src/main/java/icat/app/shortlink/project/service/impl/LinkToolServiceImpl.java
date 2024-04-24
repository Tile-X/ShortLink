package icat.app.shortlink.project.service.impl;

import icat.app.shortlink.project.service.LinkToolService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

@Service
public class LinkToolServiceImpl implements LinkToolService {

    @Override
    public String getTitleByUrl(String url) {
        try {
            Document document = Jsoup.connect(url).get();
            return document.title();
        } catch (Exception e) {
            return "Title Fetch Failed";
        }
    }



}
