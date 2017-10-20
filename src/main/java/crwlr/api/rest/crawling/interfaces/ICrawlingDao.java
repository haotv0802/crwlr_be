package crwlr.api.rest.crawling.interfaces;

import crwlr.api.rest.crawling.beans.Vendor;
import crwlr.api.rest.crawling.beans.VendorProduct;

/**
 * Created by haho on 19/10/2017.
 */
public interface ICrawlingDao {
  void saveVendor(Vendor vendor);
  void saveVendorProduct(VendorProduct product, String vendorName);
}
