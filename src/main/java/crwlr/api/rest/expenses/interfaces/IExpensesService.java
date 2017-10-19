package crwlr.api.rest.expenses.interfaces;

import crwlr.api.rest.expenses.ExpensePresenter;
import crwlr.api.rest.expenses.Expense;
import crwlr.api.rest.expenses.ExpensesDetailsPresenter;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by haho on 6/22/2017.
 */
public interface IExpensesService {
  List<ExpensePresenter> getExpenses(int userId);

  ExpensesDetailsPresenter getExpensesDetails(int userId);

  List<ExpensesDetailsPresenter> getPreviousExpensesDetails(int userId);

  Long addExpense(Expense expense, int userId);

  void updateExpense(Expense expense);

  void updateExpense(BigDecimal amount, int userId, int expenseId);

  void updateAmount(int expenseId);

  void deleteExpense(int expenseId);
}
