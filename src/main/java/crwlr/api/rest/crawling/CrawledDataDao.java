package crwlr.api.rest.crawling;

import crwlr.api.rest.crawling.beans.VendorPresenter;
import crwlr.api.rest.crawling.beans.VendorProductPresenter;
import crwlr.api.rest.crawling.interfaces.ICrawledDataDao;
import crwlr.common.dao.DaoUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: 10/20/2017 Time: 9:56 AM
 * The DAO is responsible for querying data including vendors and products crawled from given pages.
 *
 * @author haho
 */
@Repository("crawledDataDao")
public class CrawledDataDao implements ICrawledDataDao {
  private static final Logger LOGGER = LogManager.getLogger(CrawledDataDao.class);

  private final NamedParameterJdbcTemplate namedTemplate;

  @Autowired
  public CrawledDataDao(NamedParameterJdbcTemplate namedTemplate) {
    Assert.notNull(namedTemplate);

    this.namedTemplate = namedTemplate;
  }

  @Override
  public List<VendorProductPresenter> getAllVendorProducts() {
    List<VendorPresenter> vendors = getAllVendors();
    List<VendorProductPresenter> products = new ArrayList<>();
    for (VendorPresenter vendor : vendors) {
      products.addAll(getVendorProductsByVendorName(vendor));
    }
    return products;
  }

  private List<VendorProductPresenter> getVendorProductsByVendorName(VendorPresenter vendor) {
    final String sql = "SELECT name, category FROM crwlr_products WHERE vendor_name = :vendorName";
    final MapSqlParameterSource paramsMap = new MapSqlParameterSource();
    paramsMap.addValue("vendorName", vendor.getName());

    DaoUtils.debugQuery(LOGGER, sql, paramsMap.getValues());

    return namedTemplate.query(sql, paramsMap, (rs, rowNum) -> {
      VendorProductPresenter vendorProduct = new VendorProductPresenter();
      vendorProduct.setName(rs.getString("name"));
      vendorProduct.setCategory(rs.getString("category"));
      vendorProduct.setVendor(vendor);

      return vendorProduct;
    });
  }

  private List<VendorPresenter> getAllVendors() {
    final String sql = "SELECT name, location, positive, neutral, negative, link, timeOnLazada, rating, size, shipOnTime FROM crwlr_vendors";
    final MapSqlParameterSource paramsMap = new MapSqlParameterSource();

    DaoUtils.debugQuery(LOGGER, sql, paramsMap.getValues());

    return namedTemplate.query(sql, paramsMap, (rs, rowNum) -> {
      VendorPresenter vendorPresenter = new VendorPresenter();
      vendorPresenter.setLink(rs.getString("link"));
      vendorPresenter.setLocation(rs.getString("location"));
      vendorPresenter.setName(rs.getString("name"));
      vendorPresenter.setPositive(rs.getInt("positive"));
      vendorPresenter.setNeutral(rs.getInt("neutral"));
      vendorPresenter.setNegative(rs.getInt("negative"));
      vendorPresenter.setTimeOnLazada(rs.getInt("timeOnLazada"));
      vendorPresenter.setRating(rs.getFloat("rating"));
      vendorPresenter.setSize(rs.getInt("size"));
      vendorPresenter.setShipOnTime(rs.getInt("shipOnTime"));

      return vendorPresenter;
    });
  }
}
