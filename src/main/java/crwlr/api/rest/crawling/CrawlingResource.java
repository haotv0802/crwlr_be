package crwlr.api.rest.crawling;

import crwlr.api.rest.crawling.beans.Vendor;
import crwlr.api.rest.crawling.beans.VendorProduct;
import crwlr.api.rest.crawling.interfaces.ICrawlingService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Date: 10/19/2017 Time: 4:56 PM
 *
 * @author haho
 */
@RestController("crawlingResource")
@RequestMapping(path = "/svc")
public class CrawlingResource {

  private final Logger logger = LogManager.getLogger(getClass());

  private final ICrawlingService crawlingService;

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
  public Map<String, Vendor> getData(
  ) {
    List<String> pages = new ArrayList<>();
    pages.add("https://www.lazada.sg/value-market/");
    pages.add("https://www.lazada.sg/empire-13");
    pages.add("https://www.lazada.sg/boom_");
//    pages.add("https://www.lazada.sg/the-bro-store");
//    pages.add("https://www.lazada.sg/dotec");
//    pages.add("https://www.lazada.sg/diotem");
//    pages.add("https://www.lazada.sg/taka-jewellery1");
//    pages.add("https://www.lazada.sg/crystalawaking");
//    pages.add("https://www.lazada.sg/nicee-shop");
//    pages.add("https://www.lazada.sg/itechcool");

    return this.crawlingService.saveCrawledData(pages);
  }

}