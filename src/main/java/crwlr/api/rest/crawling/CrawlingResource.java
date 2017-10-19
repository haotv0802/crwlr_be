package crwlr.api.rest.crawling;

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

import java.util.Map;
import java.util.Set;

/**
 * Created by haho on 19/10/2017.
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
  public Map<String, Set<VendorProduct>> getData(
  ) {
    return this.crawlingService.saveCrawledData();
  }

}