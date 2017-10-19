package crwlr.api.rest.crawling;

import crwlr.api.rest.crawling.interfaces.ICrawlingService;
import crwlr.common.messages.interfaces.IMessagesDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * Created by haho on 6/7/2017.
 */
@Service("crawlingService")
public class CrawlingService implements ICrawlingService {

  private final IMessagesDao messagesDao;

  public CrawlingService(@Qualifier("messagesDao") IMessagesDao messagesDao) {
    Assert.notNull(messagesDao);

    this.messagesDao = messagesDao;
  }

  @Override
  public Map<String, Map<String, String>> getMessages(String lang) {
    return this.messagesDao.getMessages(lang);
  }
}