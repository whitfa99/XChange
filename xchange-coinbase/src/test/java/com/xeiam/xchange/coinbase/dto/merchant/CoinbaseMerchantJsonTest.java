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
package com.xeiam.xchange.coinbase.dto.merchant;

import static org.fest.assertions.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xeiam.xchange.coinbase.dto.account.CoinbaseToken;
import com.xeiam.xchange.coinbase.dto.common.CoinbaseRecurringPaymentStatus;
import com.xeiam.xchange.coinbase.dto.marketdata.CoinbaseMoney;
import com.xeiam.xchange.coinbase.dto.merchant.CoinbaseOrder.CoinbaseOrderStatus;
import com.xeiam.xchange.coinbase.dto.merchant.CoinbaseOrder.CoinbaseOrderTransaction;
import com.xeiam.xchange.utils.DateUtils;

/**
 * @author jamespedwards42
 */
public class CoinbaseMerchantJsonTest {

  @Test
  public void testDeserializeButton() throws IOException {

    // Read in the JSON from the example resources
    InputStream is = CoinbaseMerchantJsonTest.class.getResourceAsStream("/merchant/example-create-button-data.json");

    // Use Jackson to parse it
    ObjectMapper mapper = new ObjectMapper();
    CoinbaseButton button = mapper.readValue(is, CoinbaseButton.class);

    assertThat(button.getCode()).isEqualTo("7e285152558af4ffdedf81c31c904119");
    assertThat(button.getType()).isEqualTo(CoinbaseButtonType.DONATION);
    assertThat(button.getStyle()).isEqualTo(CoinbaseButtonStyle.DONATION_LARGE);
    assertThat(button.getText()).isEqualTo("Donate Bitcoins");
    assertThat(button.getName()).isEqualTo("Demo Button");
    assertThat(button.getDescription()).isEqualTo("Coinbase button demo for Coinbase.");
    assertThat(button.getCustom()).isEmpty();
    assertThat(button.getCallbackUrl()).isNull();
    assertThat(button.getSuccessUrl()).isNull();
    assertThat(button.getCancelUrl()).isNull();
    assertThat(button.getInfoUrl()).isNull();
    assertThat(button.isAutoReDirect()).isFalse();
    assertThat(button.getPrice()).isEqualsToByComparingFields(new CoinbaseMoney("BTC", new BigDecimal(".00100000")));
    assertThat(button.isVariablePrice()).isTrue();
    assertThat(button.isChoosePrice()).isFalse();
    assertThat(button.isIncludeAddress()).isFalse();
    assertThat(button.isIncludeEmail()).isTrue();
  }

  @Test
  public void testDeserializeOrders() throws IOException {

    // Read in the JSON from the example resources
    InputStream is = CoinbaseMerchantJsonTest.class.getResourceAsStream("/merchant/example-orders-data.json");

    // Use Jackson to parse it
    ObjectMapper mapper = new ObjectMapper();
    CoinbaseOrders orders = mapper.readValue(is, CoinbaseOrders.class);

    List<CoinbaseOrder> orderList = orders.getOrders();
    assertThat(orderList.size()).isEqualTo(1);

    CoinbaseOrder order = orderList.get(0);
    assertThat(order.getId()).isEqualTo("ND4923CX");
    assertThat(order.getCreatedAt()).isEqualTo(DateUtils.fromISO8601DateString("2014-02-19T13:30:50-08:00"));
    assertThat(order.getStatus()).isEqualTo(CoinbaseOrderStatus.COMPLETED);
    assertThat(order.getTotalBTC()).isEqualsToByComparingFields(new CoinbaseMoney("BTC", new BigDecimal(".00157800")));
    assertThat(order.getTotalNative()).isEqualsToByComparingFields(new CoinbaseMoney("USD", new BigDecimal("1.00")));
    assertThat(order.getCustom()).isEmpty();
    assertThat(order.getReceiveAddress()).isEqualTo("1DkHhHANFeZmJL4p6HXzGDHbvxHT8DzQgW");

    CoinbaseButton button = order.getButton();
    assertThat(button.getType()).isEqualTo(CoinbaseButtonType.SUBSCRIPTION);
    assertThat(button.getName()).isEqualTo("Rent");
    assertThat(button.getDescription()).isEqualTo("Rent Test");
    assertThat(button.getId()).isEqualTo("efb115b9c24100b59a4977c4dbcc5ca1");

    CoinbaseOrderTransaction transaction = order.getTransaction();
    assertThat(transaction.getId()).isEqualTo("530522c5610d7c296200015c");
    assertThat(transaction.getHash()).isNull();
    assertThat(transaction.getConfirmations()).isZero();
  }

  @Test
  public void testDeserializeRecurringPayments() throws IOException {

    // Read in the JSON from the example resources
    InputStream is = CoinbaseMerchantJsonTest.class.getResourceAsStream("/merchant/example-subscribers-data.json");

    // Use Jackson to parse it
    ObjectMapper mapper = new ObjectMapper();
    CoinbaseSubscriptions subscriptions = mapper.readValue(is, CoinbaseSubscriptions.class);

    List<CoinbaseSubscription> subscriptionList = subscriptions.getSubscriptions();
    assertThat(subscriptionList.size()).isEqualTo(3);

    CoinbaseSubscription subscription = subscriptionList.get(0);
    assertThat(subscription.getId()).isEqualTo("530522c5610d7c296200015b");
    assertThat(subscription.getCreatedAt()).isEqualTo(DateUtils.fromISO8601DateString("2014-02-19T13:31:49-08:00"));
    assertThat(subscription.getStatus()).isEqualTo(CoinbaseRecurringPaymentStatus.ACTIVE);
    assertThat(subscription.getCustom()).isEmpty();

    CoinbaseButton button = subscription.getButton();
    assertThat(button.getType()).isEqualTo(CoinbaseButtonType.SUBSCRIPTION);
    assertThat(button.getName()).isEqualTo("Rent");
    assertThat(button.getDescription()).isEqualTo("Rent Test");
    assertThat(button.getId()).isEqualTo("efb115b9c24100b59a4977c4dbcc5ca1");
  }

  @Test
  public void testDeserializeToken() throws IOException {

    // Read in the JSON from the example resources
    InputStream is = CoinbaseMerchantJsonTest.class.getResourceAsStream("/merchant/example-token-data.json");

    // Use Jackson to parse it
    ObjectMapper mapper = new ObjectMapper();
    CoinbaseToken token = mapper.readValue(is, CoinbaseToken.class);

    assertThat(token.getTokenId()).isEqualTo("4193b86c7c7d54b32a7ca0974a97d8e21793713478162bb88834993be24dda40");
    assertThat(token.getAddress()).isEqualTo("12uXAka4ncEcVTWfvifPzq3n4kFC9qyvNz");
  }
}
