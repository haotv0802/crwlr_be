package crwlr.api.rest.crawling;

import crwlr.api.rest.crawling.beans.Vendor;
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

import java.io.IOException;
import java.util.*;

/**
 * Created by haho on 19/10/2017.
 */
@Service("crawlingService")
public class CrawlingService implements ICrawlingService {

  private final ICrawlingDao crawlingDao;

  private Map<String, Set<VendorProduct>> vendorsProductMap = new HashMap<>();

  private Map<String, Vendor> vendorMap = new HashMap<>();

  private final Logger logger = LogManager.getLogger(getClass());

  @Autowired
  public CrawlingService(@Qualifier("messagesDao") ICrawlingDao crawlingDao) {
    Assert.notNull(crawlingDao);

    this.crawlingDao = crawlingDao;
  }

  @Override
  public Map<String, Vendor> saveCrawledData(List<String> pages) {
    for (String page : pages) {
      getVendorProduct(page);
    }
    return vendorMap;
  }

  private void getVendorProduct(String vendorLink) {
    int number = 0;
    try {
      Document document = Jsoup.connect(vendorLink).get();

      Elements content = document.select(".c-product-list");

      Elements productLinks = content.select("a[href]");

      for (Element link : productLinks) {
        if (number++ > 5) {
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

      String vendorName = document.select(".basic-info__name").get(0).text();

      Vendor vendor = vendorMap.get(vendorName);
      if (null == vendor) {
        vendor = new Vendor();
        vendor.setName(vendorName);

        String rating = document.select("div.seller-rating").attr("data-tooltip-header");
        rating = rating.substring(0, rating.indexOf("/"));
        vendor.setRating(Float.valueOf(rating));

        int timeOnLazada = Integer.valueOf(document.select(".time-on-lazada__value").get(0).text());
        vendor.setTimeOnLazada(timeOnLazada);

        int size = Integer.valueOf(document.select(".seller-size__content").select(".seller-size-icon").attr("data-level"));
        vendor.setSize(size);

        vendorMap.put(vendorName, vendor);
      }

      VendorProduct vendorProduct = new VendorProduct();
      String productName = document.select("#prod_title").text();
      vendorProduct.setProductName(productName);
      String category = document.select(".breadcrumb__list").select(".breadcrumb__item-text").select("a[title]").get(0).select("span").text();
      vendorProduct.setCategory(category);

      Set<VendorProduct> products = vendor.getProducts();
      if (null == products) {
        products = new HashSet<>();
        vendor.setProducts(products);
      }
      products.add(vendorProduct);


    } catch (IOException e) {
      System.err.println("For '" + productLink + "': " + e.getMessage());
    }
  }
}