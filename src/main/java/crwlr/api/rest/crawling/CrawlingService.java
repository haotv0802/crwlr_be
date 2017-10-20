package crwlr.api.rest.crawling;

import crwlr.api.rest.crawling.beans.Vendor;
import crwlr.api.rest.crawling.beans.VendorProduct;
import crwlr.api.rest.crawling.interfaces.ICrawlingDao;
import crwlr.api.rest.crawling.interfaces.ICrawlingService;
import org.apache.commons.lang3.StringUtils;
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
 * Date: 10/19/2017 Time: 4:56 PM
 *
 * @author haho
 */
@Service("crawlingService")
public class CrawlingService implements ICrawlingService {

  private final ICrawlingDao crawlingDao;

  private Map<String, Vendor> vendorMap = new HashMap<>();

  private final Logger logger = LogManager.getLogger(getClass());

  @Autowired
  public CrawlingService(@Qualifier("crawlingDao") ICrawlingDao crawlingDao) {
    Assert.notNull(crawlingDao);

    this.crawlingDao = crawlingDao;
  }

  @Override
  public Map<String, Vendor> saveCrawledData(List<String> pages) {
    for (String page : pages) {
      getVendorProduct(page);
    }

    Set<String> keys = vendorMap.keySet();
    for (String key : keys) {
      Vendor vendor = vendorMap.get(key);

      // Saving vendor
      if (crawlingDao.isVendorExisting(vendor.getName())) {
        crawlingDao.updateVendor(vendor);
      } else {
        crawlingDao.addVendor(vendor);
      }

      Set<VendorProduct> products = vendor.getProducts();
      for (VendorProduct product: products) {

        // Saving Product
        if (crawlingDao.isProductExisting(product.getName(), vendor.getName())) {
          crawlingDao.updateVendorProduct(product, vendor.getName());
        } else {
          crawlingDao.addVendorProduct(product, vendor.getName());
        }
      }
    }
    return vendorMap;
  }

  /**
   * Get list of products from given Vendor.
   * @param vendorLink
   */
  private void getVendorProduct(String vendorLink) {
    int number = 0;
    try {
      Document document = Jsoup.connect(vendorLink).get();

      Elements content = document.select(".c-product-list");

      Elements productLinks = content.select("a[href]");

      for (Element link : productLinks) {
        if (number++ > 20) {
          break;
        }
        String productLink = link.attr("abs:href");
        getProductDetails(productLink);
      }

    } catch (IOException e) {
      System.err.println("For '" + vendorLink + "': " + e.getMessage());
    }
  }

  /**
   * Get product details
   * @param productLink
   */
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
        vendor.setRating(StringUtils.isEmpty(rating) ? null : Float.valueOf(rating));

        Elements timeOnLazada = document.select(".time-on-lazada__value");
        vendor.setTimeOnLazada(timeOnLazada.size() > 0 ? Integer.valueOf(timeOnLazada.get(0).text()) : null);

        String size = document.select(".seller-size__content").select(".seller-size-icon").attr("data-level");
        vendor.setSize(StringUtils.isEmpty(size) ? null : Integer.valueOf(size));

        vendorMap.put(vendorName, vendor);
      }

      VendorProduct vendorProduct = new VendorProduct();
      String productName = document.select("#prod_title").text();
      vendorProduct.setName(productName);
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