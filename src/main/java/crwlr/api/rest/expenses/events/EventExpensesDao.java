package crwlr.api.rest.expenses.events;

import crwlr.api.rest.expenses.events.beans.EventExpensePresenter;
import crwlr.api.rest.expenses.events.beans.EventPresenter;
import crwlr.api.rest.expenses.events.beans.Expense;
import crwlr.api.rest.expenses.events.interfaces.IEventExpensesDao;
import crwlr.common.JdbcUtils;
import crwlr.common.dao.DaoUtils;
import io.jsonwebtoken.lang.Assert;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Date: 7/19/2017 Time: 11:00 AM
 * <p>
 * TODO: WRITE THE DESCRIPTION HERE
 *
 * @author haho
 */
@Repository("eventExpensesDao")
public class EventExpensesDao implements IEventExpensesDao {

  private static final Logger LOGGER = LogManager.getLogger(EventExpensesDao.class);

  private final NamedParameterJdbcOperations namedTemplate;

  @Autowired
  public EventExpensesDao(NamedParameterJdbcTemplate namedTemplate) {
    Assert.notNull(namedTemplate);
    this.namedTemplate = namedTemplate;
  }

  @Override
  public Boolean isEventExisting(int expenseId) {
    final String sql = "SELECT COUNT(*) FROM fm_event_expenses WHERE expense_id = :expenseId"
        ;

    final MapSqlParameterSource paramsMap = new MapSqlParameterSource();
    paramsMap.addValue("expenseId", expenseId);

    DaoUtils.debugQuery(LOGGER, sql, paramsMap.getValues());

    return namedTemplate.queryForObject(sql, paramsMap, Integer.class) > 0;
  }

  @Override
  public EventPresenter getEvent(int userId, int expenseId) {
    final String sql = "SELECT                                                   "
                     + "    e.id,                                                "
                     + "    e.user_id,                                           "
                     + "    e.amount,                                            "
                     + "    e.date,                                              "
                     + "    e.place,                                             "
                     + "    e.for_person,                                        "
                     + "    e.is_an_event,                                       "
                     + "    e.event_name,                                        "
                     + "    c.card_number,                                       "
                     + "    c.name card_info,                                    "
                     + "    p.name payment_method                                "
                     + "FROM                                                     "
                     + "    (fm_expenses e                                       "
                     + "    LEFT JOIN fm_cards_information c ON e.card_id = c.id)"
                     + "        LEFT JOIN                                        "
                     + "    fm_payment_methods p ON c.card_type_id = p.id        "
                     + "WHERE                                                    "
                     + "    e.user_id = :userId AND is_deleted = FALSE           "
                     + "        AND e.id = :expenseId                            "
        ;

    final MapSqlParameterSource paramsMap = new MapSqlParameterSource();
    paramsMap.addValue("expenseId", expenseId);
    paramsMap.addValue("userId", userId);

    DaoUtils.debugQuery(LOGGER, sql, paramsMap.getValues());

    EventPresenter eventPresenter = namedTemplate.queryForObject(sql, paramsMap, (rs, rowNum) -> {
      EventPresenter event = new EventPresenter();
      event.setId(rs.getInt("id"));
      event.setUserId(rs.getInt("user_id"));
      event.setAmount(rs.getBigDecimal("amount"));
      event.setDate(JdbcUtils.toUtilDate(rs.getDate("date")));
      event.setForPerson(rs.getString("for_person"));
      event.setName(rs.getString("event_name"));
      return event;
    });

    List<EventExpensePresenter> eventExpenses = getExpenses(expenseId);
    if (!CollectionUtils.isEmpty(eventExpenses)) {
      eventPresenter.setExpenses(eventExpenses);
      BigDecimal totalSpendings = BigDecimal.ZERO;
      for (int i = 0; i < eventExpenses.size(); i++) {
        totalSpendings = totalSpendings.add(eventExpenses.get(i).getAmount());
      }
      eventPresenter.setTotal(totalSpendings);
    }
    return eventPresenter;
  }

  @Override
  public Long addExpense(Expense expense, int expenseId) {
    final String sql =
              "INSERT INTO fm_event_expenses (amount, date, place, for_person, card_id, expense_id) "
            + "VALUES (:amount, :date, :place, :forPerson, :cardId, :expenseId)                     "
        ;

    final MapSqlParameterSource paramsMap = new MapSqlParameterSource();
    paramsMap.addValue("amount", expense.getAmount());
    paramsMap.addValue("date", expense.getDate());
    paramsMap.addValue("place", expense.getPlace());
    paramsMap.addValue("forPerson", expense.getForPerson());
    Integer cardId = null;
    if (null != expense.getCardId()) {
      cardId = expense.getCardId() <= 0 ? null : expense.getCardId();
    }
    paramsMap.addValue("cardId", cardId);
    paramsMap.addValue("expenseId", expenseId);

    DaoUtils.debugQuery(LOGGER, sql, paramsMap.getValues());

    KeyHolder keyHolder = new GeneratedKeyHolder();
    namedTemplate.update(sql, paramsMap, keyHolder);
    final Long id = keyHolder.getKey().longValue();
    return id;
  }

  @Override
  public void updateExpense(Expense expense) {
    final String sql =
        "UPDATE fm_event_expenses   "
      + "SET                        "
      + "	amount = :amount,         "
      + "	date = :date,             "
      + "	place = :place,           "
      + "	for_person = :forPerson,  "
      + "	card_id = :cardId         "
      + "WHERE                      "
      + "	id = :id                  "
      ;

    final MapSqlParameterSource paramsMap = new MapSqlParameterSource();
    paramsMap.addValue("amount", expense.getAmount());
    paramsMap.addValue("date", expense.getDate());
    paramsMap.addValue("place", expense.getPlace());
    paramsMap.addValue("forPerson", expense.getForPerson());
    Integer cardId = null;
    if (null != expense.getCardId()) {
      cardId = expense.getCardId() <= 0 ? null : expense.getCardId();
    }
    paramsMap.addValue("cardId", cardId);
    paramsMap.addValue("id", expense.getId());

    DaoUtils.debugQuery(LOGGER, sql, paramsMap.getValues());

    namedTemplate.update(sql, paramsMap);
  }

  @Override
  public void deleteExpense(int eventId) {
    final String sql =
        "UPDATE fm_event_expenses   "
      + "SET                        "
      + "	is_deleted = TRUE         "
      + "WHERE                      "
      + "	id = :id                  "
        ;

    final MapSqlParameterSource paramsMap = new MapSqlParameterSource();
    paramsMap.addValue("id", eventId);

    DaoUtils.debugQuery(LOGGER, sql, paramsMap.getValues());

    namedTemplate.update(sql, paramsMap);
  }

  private List<EventExpensePresenter> getExpenses(int expenseId) {
    final String sql =
        "SELECT                                                "
      + "	e.id,                                                "
      + "	e.amount,                                            "
      + "	e.date,                                              "
      + "	e.place,                                             "
      + "	e.for_person,                                        "
      + "	e.card_id,                                           "
      + "	c.card_number,                                       "
      + "	c.name card_info,                                    "
      + "	p.name payment_method                                "
      + "FROM                                                  "
      + "	(fm_event_expenses e                                 "
      + "	LEFT JOIN fm_cards_information c ON e.card_id = c.id)"
      + "		LEFT JOIN                                          "
      + "	fm_payment_methods p ON c.card_type_id = p.id        "
      + "WHERE                                                 "
      + "	is_deleted = FALSE AND e.expense_id = :expenseId     "
      + "ORDER BY e.date DESC                                  "
        ;

    final MapSqlParameterSource paramsMap = new MapSqlParameterSource();
    paramsMap.addValue("expenseId", expenseId);

    DaoUtils.debugQuery(LOGGER, sql, paramsMap.getValues());

    List<EventExpensePresenter> expensesList = namedTemplate.query(sql, paramsMap, new RowMapper<EventExpensePresenter>() {
      @Override
      public EventExpensePresenter mapRow(ResultSet rs, int rowNum) throws SQLException {
        EventExpensePresenter eventExpensePresenter = new EventExpensePresenter();
        eventExpensePresenter.setId(rs.getInt("id"));
        eventExpensePresenter.setAmount(rs.getBigDecimal("amount"));
        eventExpensePresenter.setDate(JdbcUtils.toUtilDate(rs.getDate("date")));
        eventExpensePresenter.setPlace(rs.getString("place"));
        eventExpensePresenter.setForPerson(rs.getString("for_person"));
        eventExpensePresenter.setCardId(rs.getInt("card_id"));

        if (null == eventExpensePresenter.getCardId() || eventExpensePresenter.getCardId() == 0) {
          eventExpensePresenter.setPaymentMethod("CASH");
        } else {
          eventExpensePresenter.setPaymentMethod(rs.getString("payment_method"));
          eventExpensePresenter.setCardInfo(rs.getString("card_info"));
          eventExpensePresenter.setCardNumber(rs.getString("card_number"));
        }
        return eventExpensePresenter;
      }
    });

    return expensesList;
  }
}
