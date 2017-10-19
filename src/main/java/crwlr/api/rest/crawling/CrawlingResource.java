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

  private HashSet<String> links = new HashSet<>();

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
    String url = "http://www.lazada.sg";
    return getPageLinks(url);
//    return "Hello";
  }

  public String getPageLinks(String URL) {

    StringBuilder stringBuilder = new StringBuilder();

    //4. Check if you have already crawled the URLs
    //(we are intentionally not checking for duplicate content in this example)
    if (!links.contains(URL)) {
      try {
        //4. (i) If not add it to the index
        if (links.add(URL)) {
          System.out.println(URL);
        }

        //2. Fetch the HTML code
        Document document = Jsoup.connect(URL).get();
        //3. Parse the HTML to extract links to other URLs
        Elements linksOnPage = document.select("a[href]");

        //5. For each extracted URL... go back to Step 4.
        for (Element page : linksOnPage) {
//          getPageLinks(page.attr("abs:href"));
          stringBuilder.append(page.attr("abs:href"));
        }
      } catch (IOException e) {
        System.err.println("For '" + URL + "': " + e.getMessage());
      }
    }
    return stringBuilder.toString();
  }
}