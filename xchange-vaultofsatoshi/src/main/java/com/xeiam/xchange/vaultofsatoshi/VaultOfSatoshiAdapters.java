/**
 * Copyright (C) 2012 - 2014 Xeiam LLC http://xeiam.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.xeiam.xchange.vaultofsatoshi;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.xeiam.xchange.currency.CurrencyPair;
import com.xeiam.xchange.dto.Order.OrderType;
import com.xeiam.xchange.dto.marketdata.Ticker;
import com.xeiam.xchange.dto.marketdata.Ticker.TickerBuilder;
import com.xeiam.xchange.dto.marketdata.Trade;
import com.xeiam.xchange.dto.marketdata.Trades;
import com.xeiam.xchange.dto.marketdata.Trades.TradeSortType;
import com.xeiam.xchange.dto.trade.LimitOrder;
import com.xeiam.xchange.utils.DateUtils;
import com.xeiam.xchange.vaultofsatoshi.dto.marketdata.VaultOfSatoshiTicker;
import com.xeiam.xchange.vaultofsatoshi.dto.marketdata.VaultOfSatoshiTrade;
import com.xeiam.xchange.vaultofsatoshi.dto.marketdata.VosOrder;

/**
 * Various adapters for converting from VaultOfSatoshi DTOs to XChange DTOs
 */
public final class VaultOfSatoshiAdapters {

  /**
   * private Constructor
   */
  private VaultOfSatoshiAdapters() {

  }

  /**
   * Adapts a vosOrder to a LimitOrder
   * 
   * @param amount
   * @param price
   * @param pair
   * @param orderTypeString
   * @param id
   * @return
   */
  public static LimitOrder adaptOrder(VosOrder vosOrder, CurrencyPair pair, String orderTypeString, String id) {

    // place a limit order
    OrderType orderType = orderTypeString.equalsIgnoreCase("bid") ? OrderType.BID : OrderType.ASK;

    return new LimitOrder(orderType, vosOrder.getAmount().getValue(), pair, id, null, vosOrder.getPrice().getValue());
  }

  /**
   * Adapts a List of vosOrders to a List of LimitOrders
   * 
   * @param vosOrders
   * @param currency
   * @param orderType
   * @param id
   * @return
   */
  public static List<LimitOrder> adaptOrders(List<VosOrder> vosOrders, CurrencyPair pair, String orderType, String id) {

    List<LimitOrder> limitOrders = new ArrayList<LimitOrder>();

    for (VosOrder vosOrder : vosOrders)
      limitOrders.add(adaptOrder(vosOrder, pair, orderType, id));

    return limitOrders;
  }

  /**
   * Adapts a VaultOfSatoshiTrade to a Trade Object
   * 
   * @param vosTrade A VaultOfSatoshi trade
   * @return The XChange Trade
   */
  public static Trade adaptTrade(VaultOfSatoshiTrade vosTrade, CurrencyPair currencyPair) {

    BigDecimal amount = vosTrade.getUnitsTraded().getValue();
    Date date = DateUtils.fromMillisUtc(vosTrade.getTimestamp() / 1000L);
    final String tradeId = String.valueOf(vosTrade.getTransactionId());
    return new Trade(null, amount, currencyPair, vosTrade.getPrice().getValue(), date, tradeId);
  }

  /**
   * Adapts a VaultOfSatoshiTrade[] to a Trades Object
   * 
   * @param vosTrades The VaultOfSatoshi trade data
   * @return The trades
   */
  public static Trades adaptTrades(List<VaultOfSatoshiTrade> vosTrades, CurrencyPair currencyPair) {

    List<Trade> tradesList = new ArrayList<Trade>();
    long lastTradeId = 0;
    for (VaultOfSatoshiTrade vosTrade : vosTrades) {
      long tradeId = vosTrade.getTransactionId();
      if (tradeId > lastTradeId)
        lastTradeId = tradeId;
      tradesList.add(adaptTrade(vosTrade, currencyPair));
    }
    return new Trades(tradesList, lastTradeId, TradeSortType.SortByID);
  }

  /**
   * Adapts a VaultOfSatoshiTicker to a Ticker Object
   * 
   * @param vosTicker
   * @return
   */
  public static Ticker adaptTicker(VaultOfSatoshiTicker vosTicker, CurrencyPair currencyPair) {

    BigDecimal last = vosTicker.getLast();
    BigDecimal high = vosTicker.getHigh();
    BigDecimal low = vosTicker.getLow();
    BigDecimal volume = vosTicker.getVolume();

    return TickerBuilder.newInstance().withCurrencyPair(currencyPair).withLast(last).withHigh(high).withLow(low).withVolume(volume).build();
  }

}
