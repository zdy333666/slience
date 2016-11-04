/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.webmagic;

import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.scheduler.QueueScheduler;

/**
 *
 * @author slience
 */
public class Application {

    public static void main(String[] args) {

        //Spider.create(new GithubRepoPageProcessor()).addUrl("https://github.com/code4craft").thread(5).run();
        
        Spider.create(new MyPageProcessor()).addUrl("http://news.sohu.com") //http://news.sohu.com
                .setScheduler(new QueueScheduler())
                .thread(60)
                .run();
    }

}
