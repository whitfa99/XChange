package com.xeiam.xchange.dto.trade;

import java.math.BigDecimal;
import java.util.Date;

import com.xeiam.xchange.currency.CurrencyPair;
import com.xeiam.xchange.dto.Order.OrderType;
import com.xeiam.xchange.dto.marketdata.Trade;

/**
 * Data object representing a user trade
 */
public class UserTrade extends Trade {

  /**
   * The id of the order responsible for execution of this trade
   */
  private final String orderId;

  /**
   * The fee that was charged by the exchange for this trade.
   */
  private final BigDecimal feeAmount;

  /**
   * The symbol of the currency in which the fee was charged.
   */
  private final String feeCurrency;

  /**
   * This constructor is called to construct user's trade objects (in
   * {@link com.xeiam.xchange.service.polling.trade.PollingTradeService#getTradeHistory(Object...)} implementations).
   *
   * @param type The trade type (BID side or ASK side)
   * @param tradableAmount The depth of this trade
   * @param tradableIdentifier The exchange identifier (e.g. "BTC/USD")
   * @param transactionCurrency The transaction currency (e.g. USD in BTC/USD)
   * @param price The price (either the bid or the ask)
   * @param timestamp The timestamp of the trade
   * @param id The id of the trade
   * @param orderId The id of the order responsible for execution of this trade
   * @param feeAmount The fee that was charged by the exchange for this trade
   * @param feeCurrency The symbol of the currency in which the fee was charged
   */
  public UserTrade(OrderType type, BigDecimal tradableAmount, CurrencyPair currencyPair, BigDecimal price, Date timestamp, String id, String orderId,
      BigDecimal feeAmount, String feeCurrency) {

    super(type, tradableAmount, currencyPair, price, timestamp, id);

    this.orderId = orderId;
    this.feeAmount = feeAmount;
    this.feeCurrency = feeCurrency;
  }

  public String getOrderId() {

    return orderId;
  }

  public BigDecimal getFeeAmount() {

    return feeAmount;
  }

  public String getFeeCurrency() {

    return feeCurrency;
  }

  @Override
  public String toString() {
    return "UserTrade[type=" + type + ", tradableAmount=" + tradableAmount + ", currencyPair=" + currencyPair + ", price=" + price + ", "
        + "timestamp=" + timestamp + ", id=" + id + ", orderId='" + orderId + '\'' + ", feeAmount=" + feeAmount + ", feeCurrency='" + feeCurrency
        + '\'' + "]";
  }

  public static class Builder extends Trade.Builder {

    protected String orderId;
    protected BigDecimal feeAmount;
    protected String feeCurrency;

    public static Builder from(UserTrade trade) {
      return new Builder().type(trade.getType()).tradableAmount(trade.getTradableAmount()).currencyPair(trade.getCurrencyPair())
          .price(trade.getPrice()).timestamp(trade.getTimestamp()).id(trade.getId()).orderId(trade.getOrderId()).feeAmount(trade.getFeeAmount())
          .feeCurrency(trade.getFeeCurrency());
    }

    public Builder type(OrderType type) {
      return (Builder) super.type(type);
    }

    public Builder tradableAmount(BigDecimal tradableAmount) {
      return (Builder) super.tradableAmount(tradableAmount);
    }

    public Builder currencyPair(CurrencyPair currencyPair) {
      return (Builder) super.currencyPair(currencyPair);
    }

    public Builder price(BigDecimal price) {
      return (Builder) super.price(price);
    }

    public Builder timestamp(Date timestamp) {
      return (Builder) super.timestamp(timestamp);
    }

    public Builder id(String id) {
      return (Builder) super.id(id);
    }

    public Builder orderId(String orderId) {
      this.orderId = orderId;
      return this;
    }

    public Builder feeAmount(BigDecimal feeAmount) {
      this.feeAmount = feeAmount;
      return this;
    }

    public Builder feeCurrency(String feeCurrency) {
      this.feeCurrency = feeCurrency;
      return this;
    }

    public UserTrade build() {
      return new UserTrade(type, tradableAmount, currencyPair, price, timestamp, id, orderId, feeAmount, feeCurrency);
    }
  }
}
