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
package com.xeiam.xchange.bter.dto.marketdata;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.xeiam.xchange.bter.dto.BTERBaseResponse;
import com.xeiam.xchange.bter.dto.marketdata.BTERTicker.BTERTickerTickerDeserializer;

@JsonDeserialize(using = BTERTickerTickerDeserializer.class)
public class BTERTicker extends BTERBaseResponse {

  private final BigDecimal last;
  private final BigDecimal high;
  private final BigDecimal low;
  private final BigDecimal avg;
  private final BigDecimal sell;
  private final BigDecimal buy;
  private final Map<String, BigDecimal> volumes;

  private BTERTicker(final BigDecimal last, final BigDecimal high, final BigDecimal low, final BigDecimal average, final BigDecimal sell, final BigDecimal buy, final Map<String, BigDecimal> volumes,
      final boolean result, final String message) {

    super(result, message);
    this.last = last;
    this.high = high;
    this.low = low;
    this.avg = average;
    this.sell = sell;
    this.buy = buy;
    this.volumes = volumes;
  }

  public BigDecimal getLast() {

    return last;
  }

  public BigDecimal getHigh() {

    return high;
  }

  public BigDecimal getLow() {

    return low;
  }

  public BigDecimal getAvg() {

    return avg;
  }

  public BigDecimal getSell() {

    return sell;
  }

  public BigDecimal getBuy() {

    return buy;
  }

  public BigDecimal getVolume(final String currency) {

    return volumes.get(currency.toUpperCase());
  }

  @Override
  public String toString() {

    return "BTERTicker [last=" + last + ", high=" + high + ", low=" + low + ", avg=" + avg + ", sell=" + sell + ", buy=" + buy + ", volumes=" + volumes + "]";
  }

  static class BTERTickerTickerDeserializer extends JsonDeserializer<BTERTicker> {

    private static BigDecimal getNumberIfPresent(final JsonNode numberNode) {

      final String numberString = numberNode.asText();
      return numberString.isEmpty() ? null : new BigDecimal(numberString);
    }

    public static BTERTicker deserializeFromNode(JsonNode tickerNode) {

      final BigDecimal last = getNumberIfPresent(tickerNode.path("last"));
      final BigDecimal high = getNumberIfPresent(tickerNode.path("high"));
      final BigDecimal average = getNumberIfPresent(tickerNode.path("avg"));
      final BigDecimal low = getNumberIfPresent(tickerNode.path("low"));
      final BigDecimal sell = getNumberIfPresent(tickerNode.path("sell"));
      final BigDecimal buy = getNumberIfPresent(tickerNode.path("buy"));
      final Map<String, BigDecimal> volumes = new HashMap<String, BigDecimal>();

      if (tickerNode instanceof ObjectNode) {
        final ObjectNode tickerDataObjectNode = (ObjectNode) tickerNode;
        final Iterator<Entry<String, JsonNode>> tickerDataFields = tickerDataObjectNode.fields();
        while (tickerDataFields.hasNext()) {
          final Entry<String, JsonNode> tickerDataEntry = tickerDataFields.next();
          final String volumeEntryKey = tickerDataEntry.getKey();
          if (volumeEntryKey.startsWith("vol_")) {
            final BigDecimal volumeAmount = getNumberIfPresent(tickerDataEntry.getValue());
            if (volumeAmount != null) {
              final String[] volumeKeyParts = volumeEntryKey.split("_");
              if (volumeKeyParts != null && volumeKeyParts.length == 2) {
                volumes.put(volumeKeyParts[1].toUpperCase(), volumeAmount);
              }
            }
          }
        }
      }

      final boolean result = tickerNode.path("result").asBoolean();
      final String message = tickerNode.path("msg").asText();

      return new BTERTicker(last, high, low, average, sell, buy, volumes, result, message);
    }

    @Override
    public BTERTicker deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {

      final ObjectCodec oc = jp.getCodec();
      final JsonNode tickerNode = oc.readTree(jp);

      return deserializeFromNode(tickerNode);
    }

  }
}
