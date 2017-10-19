package fm.api.rest.expenses.events;

import fm.api.rest.expenses.events.beans.EventPresenter;
import fm.api.rest.expenses.events.beans.Expense;
import fm.api.rest.expenses.events.interfaces.IEventExpensesDao;
import fm.api.rest.expenses.events.interfaces.IEventExpensesService;
import fm.api.rest.expenses.interfaces.IExpensesDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Date: 7/19/2017 Time: 10:56 AM
 * <p>
 * TODO: WRITE THE DESCRIPTION HERE
 *
 * @author haho
 */
@Service("eventExpensesService")
public class EventExpensesService implements IEventExpensesService {

  private final IEventExpensesDao eventExpensesDao;

  private final IExpensesDao expensesDao;

  @Autowired
  public EventExpensesService(
      @Qualifier("eventExpensesDao") IEventExpensesDao eventExpensesDao,
      @Qualifier("expensesDao") IExpensesDao expensesDao
  ) {
    this.expensesDao = expensesDao;
    Assert.notNull(eventExpensesDao);

    this.eventExpensesDao = eventExpensesDao;
  }

  @Override
  public Boolean isEventExisting(int expenseId) {
    return eventExpensesDao.isEventExisting(expenseId);
  }

  @Override
  public EventPresenter getEvent(int userId, int expenseId) {
    return eventExpensesDao.getEvent(userId, expenseId);
  }

  @Override
  public Long addExpense(Expense expense, int expenseId) {
    long id = this.eventExpensesDao.addExpense(expense, expenseId);
    this.expensesDao.updateAmount(expenseId);
    return id;
  }

  @Override
  public void updateExpense(Expense expense, int eventExpenseId) {
    this.eventExpensesDao.updateExpense(expense);
    this.expensesDao.updateAmount(eventExpenseId);
  }

  @Override
  public void deleteExpense(int expenseId, int eventExpenseId) {
    this.eventExpensesDao.deleteExpense(eventExpenseId);
    this.expensesDao.updateAmount(expenseId);
  }
}
