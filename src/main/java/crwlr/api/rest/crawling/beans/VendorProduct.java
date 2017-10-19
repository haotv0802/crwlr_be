package crwlr.api.rest.crawling.beans;

/**
 * Date: 10/19/2017 Time: 4:56 PM
 *
 * @author haho
 */
public class VendorProduct {
  private String productName;
  private String category;
  private VendorInfo vendorInfo; /// **

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public VendorInfo getVendorInfo() {
    return vendorInfo;
  }

  public void setVendorInfo(VendorInfo vendorInfo) {
    this.vendorInfo = vendorInfo;
  }
}
