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
package com.xeiam.xchange.btctrade.service.polling;

import si.mazi.rescu.ParamsDigest;

import com.xeiam.xchange.ExchangeSpecification;
import com.xeiam.xchange.btctrade.service.BTCTradeDigest;
import com.xeiam.xchange.btctrade.service.BTCTradeSession;
import com.xeiam.xchange.btctrade.service.BTCTradeSessionFactory;

public class BTCTradeBaseTradePollingService extends BTCTradeBasePollingService {

  protected final String publicKey;
  protected final BTCTradeSession session;

  /**
   * @param exchangeSpecification
   */
  protected BTCTradeBaseTradePollingService(ExchangeSpecification exchangeSpecification) {

    super(exchangeSpecification);
    session = BTCTradeSessionFactory.INSTANCE.getSession(exchangeSpecification);
    publicKey = session.getKey();
  }

  /**
   * Returns the next nonce.
   *
   * @return the next nonce.
   */
  public long nextNonce() {

    return session.nextNonce();
  }

  /**
   * Returns the {@link BTCTradeDigest}.
   *
   * @return the {@link BTCTradeDigest}.
   */
  public ParamsDigest getSignatureCreator() {

    return session.getSignatureCreator();
  }

}
