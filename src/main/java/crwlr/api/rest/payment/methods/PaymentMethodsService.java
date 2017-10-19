package crwlr.api.rest.payment.methods;

import crwlr.api.rest.payment.methods.beans.CardInformation;
import crwlr.api.rest.payment.methods.interfaces.IPaymentMethodsDao;
import crwlr.api.rest.payment.methods.interfaces.IPaymentMethodsService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Created by haho on 6/25/2017.
 */
@Service("paymentMethodsService")
public class PaymentMethodsService implements IPaymentMethodsService {

  private final IPaymentMethodsDao paymentMethodsDao;

  public PaymentMethodsService(@Qualifier("paymentMethodsDao") IPaymentMethodsDao paymentMethodsDao) {
    Assert.notNull(paymentMethodsDao);

    this.paymentMethodsDao = paymentMethodsDao;
  }

  @Override
  public List<CardInformation> getCardsInformation(int userId) {
    return this.paymentMethodsDao.getCardsInformation(userId);
  }
}