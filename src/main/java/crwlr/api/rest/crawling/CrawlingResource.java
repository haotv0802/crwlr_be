package crwlr.api.rest.crawling;

import crwlr.api.rest.crawling.interfaces.ICrawlingService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashSet;

/**
 * Created by haho on 19/10/2017.
 */
@RestController("crawlingResource")
@RequestMapping(path = "/svc")
public class CrawlingResource {

  private final Logger logger = LogManager.getLogger(getClass());

  private final ICrawlingService crawlingService;

  private HashSet<String> vendorLinks = new HashSet<>();

  private int pageLevel = 0;

  @Autowired
  public CrawlingResource(@Qualifier("crawlingService") ICrawlingService crawlingService) {
    Assert.notNull(crawlingService);

    this.crawlingService = crawlingService;
  }

  @PostMapping("/crawler/crawlingData")
  public String crawlData(
  ) {
    return "Hello";
  }

  @GetMapping("/crawler/getDataCrawled")
  public String getData(
  ) {
    String url = "https://www.lazada.sg/xiaomi-redmi-note-4x-3gb32gb-gold-exportgold-32gb-13521233.html?spm=a2o42.category-010100000000.0.0.5aIjzG&ff=1&sc=EQM=";
    getPageLinks(url);
    url = "https://www.lazada.sg/google-pixel-xl-international-version-export-10051700.html?ff=1&sc=EQM=";
    getPageLinks(url);
    return vendorLinks.toString();
  }

  public void getPageLinks(String URL) {
//    if (pageLevel++ > 0) {
//      return;
//    }
    if (!vendorLinks.contains(URL)) {
      try {
        Document document = Jsoup.connect(URL).get();

        String vendorLink = document.select(".basic-info__name").attr("abs:href");

        if (!StringUtils.isEmpty(vendorLink)) {
          if (vendorLinks.add(vendorLink)) {
            System.out.println(vendorLink);
          }
        } else {
          Elements linksOnPage = document.select("a[href]");

          for (Element page : linksOnPage) {
            String tempURL = page.attr("abs:href");
            logger.info("processing: " + tempURL);
            if (tempURL.startsWith("http://www.lazada.sg")) {
              getPageLinks(tempURL);
            } else {
              logger.warn(tempURL);
            }
//            pageLevel = 0;
          }
        }

        //5. For each extracted URL... go back to Step 4.
      } catch (IOException e) {
        System.err.println("For '" + URL + "': " + e.getMessage());
      }
    }
  }
}