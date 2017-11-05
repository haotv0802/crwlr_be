package crwlr.api.rest.crawling

import java.util
import java.util.List
import java.util.ArrayList
import java.util.Map

import crwlr.api.rest.crawling.beans.VendorProductPresenter
import crwlr.api.rest.crawling.beans.VendorPresenter
import crwlr.api.rest.crawling.beans.Vendor
import crwlr.api.rest.crawling.interfaces.{ICrawledDataService, ICrawlingService}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.ComponentScan
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.{GetMapping, RequestMapping, RequestParam, RestController}

@RestController("crawlingResource")
@ComponentScan
@RequestMapping(path = Array("/svc"))
class CrawlingResource2(
                   @Autowired val crawlingService: ICrawlingService,
                   @Autowired val crawledService: ICrawledDataService
                 ) {

  @GetMapping(path = Array("/crawler/crawledData"))
  def getCrawledData(): List[VendorProductPresenter] = {
    crawledService.getAllVendorProducts
  }

  @GetMapping(path = Array("/crawler/vendors"))
  def getAllVendors(): List[VendorPresenter] = {
    crawledService.getAllVendors
  }

  @GetMapping(path = Array("/crawler/vendors"))
  def crawlingData(
                  @RequestParam(value = "link", required = false) link: String,
                  @RequestParam(value = "numberOfProductsCrawled", required = false, defaultValue = "5") numberOfProductsCrawled: Integer
                  ): Map[String, Vendor] = {
    val pages: List[String] = new ArrayList[String]()

    if (StringUtils.isEmpty(link)) {
      pages.add("https://www.lazada.sg/value-market");
      pages.add("https://www.lazada.sg/empire-13");
      pages.add("https://www.lazada.sg/boom_");
      pages.add("https://www.lazada.sg/the-bro-store");
      pages.add("https://www.lazada.sg/taka-jewellery1");
      pages.add("https://www.lazada.sg/crystalawaking");
      pages.add("https://www.lazada.sg/nicee-shop");
      pages.add("https://www.lazada.sg/itechcool");
      pages.add("https://www.lazada.sg/selffix-pte-ltd");
      pages.add("https://www.lazada.sg/originalfook");
    } else {
      pages.add(link)
    }
    crawlingService.saveCrawledData(pages, numberOfProductsCrawled)
  }
}

object CrawlingResource2 {
  def main(args: Array[String]) {
    println("Hello, world!")
  }
}