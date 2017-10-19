package crwlr.api.rest.crawling;

import crwlr.api.rest.crawling.beans.VendorInfo;
import crwlr.api.rest.crawling.beans.VendorProduct;
import crwlr.api.rest.crawling.interfaces.ICrawlingDao;
import crwlr.api.rest.crawling.interfaces.ICrawlingService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;

/**
 * Created by haho on 19/10/2017.
 */
@Service("crawlingService")
public class CrawlingService implements ICrawlingService {

  private final ICrawlingDao crawlingDao;

  private Set<String> vendorLinks = new HashSet<>();

  private Set<VendorInfo> vendors = new HashSet<>();

  private Map<String, Set<VendorProduct>> vendorsProductMap = new HashMap<>();

  private final Logger logger = LogManager.getLogger(getClass());

  @Autowired
  public CrawlingService(@Qualifier("messagesDao") ICrawlingDao crawlingDao) {
    Assert.notNull(crawlingDao);

    this.crawlingDao = crawlingDao;
  }

  @Override
  public Map<String, Set<VendorProduct>> saveCrawledData() {
    String url = "https://www.lazada.sg/xiaomi-redmi-note-4x-3gb32gb-gold-exportgold-32gb-13521233.html?spm=a2o42.category-010100000000.0.0.5aIjzG&ff=1&sc=EQM=";
    getVendors(url);
    url = "https://www.lazada.sg/google-pixel-xl-international-version-export-10051700.html?ff=1&sc=EQM=";
    getVendors(url);

    Iterator<String> vendorIterator = vendorLinks.iterator();
    while (vendorIterator.hasNext()) {
      String vendor = vendorIterator.next();
      getVendorProduct(vendor);
    }
    return vendorsProductMap;
  }

  private void getVendorProduct(String vendorLink) {
    int number = 0;
    try {
      Document document = Jsoup.connect(vendorLink).get();

      Elements content = document.select(".c-product-list");

      Elements productLinks = content.select("a[href]");

      for (Element link : productLinks) {
        if (number++ > 10) {
          break;
        }
        String productLink = link.attr("abs:href");
        getProductDetails(productLink);
      }

    } catch (IOException e) {
      System.err.println("For '" + vendorLink + "': " + e.getMessage());
    }
  }

  private void getProductDetails(String productLink) {
    try {
      Document document = Jsoup.connect(productLink).get();
      VendorProduct vendorProduct = new VendorProduct();
      vendorProduct.setProductName(document.select("#prod_title").text());
      vendorProduct.setCategory(document.select(".breadcrumb__list").select(".breadcrumb__item-text").select("a[title]").get(0).select("span").text());

      VendorInfo vendorInfo = new VendorInfo();
      vendorInfo.setName(document.select(".basic-info__name").get(0).text());

      String rating = document.select("div.seller-rating").attr("data-tooltip-header");
      rating = rating.substring(0, rating.indexOf("/"));
      vendorInfo.setRating(Float.valueOf(rating));

      vendorInfo.setTimeOnLazada(Integer.valueOf(document.select(".time-on-lazada__value").get(0).text()));
      vendorInfo.setSize(Integer.valueOf(document.select(".seller-size__content").select(".seller-size-icon").attr("data-level")));

      vendorProduct.setVendorInfo(vendorInfo);

      vendors.add(vendorInfo);
      Set<VendorProduct> productList = vendorsProductMap.computeIfAbsent(vendorInfo.getName(), k -> new HashSet<>());
      productList.add(vendorProduct);

    } catch (IOException e) {
      System.err.println("For '" + productLink + "': " + e.getMessage());
    }
  }

  private void getVendors(String url) {
//    if (pageLevel++ > 0) {
//      return;
//    }
    if (!vendorLinks.contains(url)) {
      try {
        Document document = Jsoup.connect(url).get();

        String vendorLink = document.select(".basic-info__name").attr("abs:href");

        if (!StringUtils.isEmpty(vendorLink)) {
          if (vendorLinks.add(vendorLink)) {
            logger.info(vendorLink);
          }
        } else {
          Elements linksOnPage = document.select("a[href]");

          for (Element page : linksOnPage) {
            String tempURL = page.attr("abs:href");
            logger.info("processing: " + tempURL);
            if (tempURL.startsWith("http://www.lazada.sg")) {
              getVendors(tempURL);
            } else {
              logger.warn(tempURL);
            }
//            pageLevel = 0;
          }
        }

      } catch (IOException e) {
        System.err.println("For '" + url + "': " + e.getMessage());
      }
    }
  }
}