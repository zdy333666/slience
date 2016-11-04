/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.webmagic;

import java.util.concurrent.atomic.AtomicInteger;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 *
 * @author slience
 */
public class MyPageProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(0);
    private static int MAX_CONTENT_LENGTH = 16000 * 1000;

    private static final AtomicInteger counter = new AtomicInteger(0);

    @Override
    public void process(Page page) {

        page.putField("id", counter.incrementAndGet());
        page.putField("fetcher", Thread.currentThread().getName());
        
        String url=page.getUrl().toString();
        page.putField("url", url);
        
        String title=page.getHtml().xpath("//title").toString().replaceAll("<title>", "").replaceAll("</title>", "");
        page.putField("title", title+"\n");
        
        //String content = page.getHtml().smartContent().toString();
        //page.putField("content", content);//.substring(0, Math.min(MAX_CONTENT_LENGTH, content.length())));

        if (title.isEmpty()) {
            page.setSkip(true);
        }
        
        String[] urlTips =url.split("/");
        
        //page.addTargetRequests(page.getHtml().links().regex("(http://news\\.sohu\\.com/[a-zA-Z0-9_-]+\\.shtml)").all());
        //page.addTargetRequests(page.getHtml().links().regex("("+urlTips[0]+"//"+urlTips[2]+"/.*\\.shtml)").all());
        
         page.addTargetRequests(page.getHtml().links().regex("("+urlTips[0]+"//"+urlTips[2]+"/.*\\.shtml)").all());
    }

    @Override
    public Site getSite() {
        return site;
    }

}
