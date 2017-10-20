package crwlr.api.rest.crawling;

import crwlr.api.rest.crawling.beans.Vendor;
import crwlr.api.rest.crawling.beans.VendorProduct;
import crwlr.api.rest.crawling.interfaces.ICrawlingDao;
import crwlr.common.dao.DaoUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

/**
 * Created by haho on 19/10/2017.
 */
@Repository("crawlingDao")
public class CrawlingDao implements ICrawlingDao {

  private static final Logger LOGGER = LogManager.getLogger(CrawlingDao.class);

  private final NamedParameterJdbcTemplate namedTemplate;

  @Autowired
  public CrawlingDao(NamedParameterJdbcTemplate namedTemplate) {
    Assert.notNull(namedTemplate);

    this.namedTemplate = namedTemplate;
  }

  @Override
  public void saveVendor(Vendor vendor) {
    final String sql =
      "INSERT INTO crwlr_vendors (name, location, positive, neutral, negative, link, timeOnLazada, rating, size, shipOnTime)"
    + "VALUE (:name, :location, :positive, :neutral, :negative, :link, :timeOnLazada, :rating, :size, :shipOnTime)          "
    ;

    final MapSqlParameterSource paramsMap = new MapSqlParameterSource();
    paramsMap.addValue("name", vendor.getName());
    paramsMap.addValue("link", vendor.getLink());
    paramsMap.addValue("location", vendor.getLocation());
    paramsMap.addValue("positive", vendor.getPositive());
    paramsMap.addValue("neutral", vendor.getNeutral());
    paramsMap.addValue("negative", vendor.getNegative());
    paramsMap.addValue("timeOnLazada", vendor.getTimeOnLazada());
    paramsMap.addValue("rating", vendor.getRating());
    paramsMap.addValue("size", vendor.getSize());
    paramsMap.addValue("shipOnTime", vendor.getShipOnTime());

    DaoUtils.debugQuery(LOGGER, sql, paramsMap.getValues());

    namedTemplate.update(sql, paramsMap);
  }

  @Override
  public void saveVendorProduct(VendorProduct product, String vendorName) {
    final String sql =
      "INSERT INTO crwlr_products (name, category, vendor_name) "
    + "VALUE (:name, :category, :vendor_name)                   "
        ;
    final MapSqlParameterSource paramsMap = new MapSqlParameterSource();
    paramsMap.addValue("name", product.getName());
    paramsMap.addValue("category", product.getCategory());
    paramsMap.addValue("vendor_name", vendorName);

    DaoUtils.debugQuery(LOGGER, sql, paramsMap.getValues());

    namedTemplate.update(sql, paramsMap);
  }
}
