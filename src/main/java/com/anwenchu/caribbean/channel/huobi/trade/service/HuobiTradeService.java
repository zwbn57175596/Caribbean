package com.anwenchu.caribbean.channel.huobi.trade.service;

import com.alibaba.fastjson.JSONObject;
import com.anwenchu.caribbean.channel.huobi.enums.OrderTypeEnum;
import com.anwenchu.caribbean.channel.huobi.http.HuobiRestConnection;
import com.anwenchu.caribbean.channel.huobi.signature.UrlParamsBuilder;
import com.anwenchu.caribbean.channel.huobi.trade.request.CreateOrderRequest;
import com.anwenchu.caribbean.utils.InputChecker;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HuobiTradeService implements TradeClient {


  public static final String CREATE_ORDER_PATH = "/v1/order/orders/place";
  public static final String CANCEL_ORDER_PATH = "/v1/order/orders/{order-id}/submitcancel";
  public static final String CANCEL_ORDER_BY_CLIENT_ORDER_ID_PATH = "/v1/order/orders/submitCancelClientOrder";
  public static final String BATCH_CANCEL_OPEN_ORDERS_PATH = "/v1/order/orders/batchCancelOpenOrders";
  public static final String BATCH_CANCEL_ORDERS_PATH = "/v1/order/orders/batchcancel";
  public static final String GET_OPEN_ORDERS_PATH = "/v1/order/openOrders";
  public static final String GET_ORDER_PATH = "/v1/order/orders/{order-id}";
  public static final String GET_ORDERS_PATH = "/v1/order/orders";
  public static final String GET_ORDERS_HISTORY_PATH = "/v1/order/history";
  public static final String GET_ORDER_BY_CLIENT_ORDER_ID_PATH = "/v1/order/orders/getClientOrder";
  public static final String GET_SINGLE_ORDER_MATCH_RESULT_PATH = "/v1/order/orders/{order-id}/matchresults";
  public static final String GET_MATCH_RESULT_PATH = "/v1/order/matchresults";
  public static final String GET_FEE_RATE_PATH = "/v2/reference/transact-fee-rate";

  public static final String WEBSOCKET_ORDER_UPDATE_V2_TOPIC = "orders#${symbol}";
  public static final String WEBSOCKET_TRADE_CLEARING_TOPIC = "trade.clearing#${symbol}";



  @Autowired
  private HuobiRestConnection restConnection;

//  private HuobiAccountService huobiAccountService;


  @SneakyThrows
  @Override
  public Long createOrder(CreateOrderRequest request) {

    InputChecker.checker().checkSymbol(request.getSymbol())
        .shouldNotNull(request.getAccountId(), "Account-Id")
        .shouldNotNull(request.getAmount(), "Amount")
        .shouldNotNull(request.getType(), "Type")
        .shouldNotNull(request.getOrderSource(), "order source");

    if (request.getType() == OrderTypeEnum.SELL_LIMIT
        || request.getType() == OrderTypeEnum.BUY_LIMIT
        || request.getType() == OrderTypeEnum.BUY_LIMIT_MAKER
        || request.getType() == OrderTypeEnum.SELL_LIMIT_MAKER) {
      InputChecker.checker()
          .shouldNotNull(request.getPrice(), "Price");
    }
    if (request.getType() == OrderTypeEnum.SELL_MARKET
        || request.getType() == OrderTypeEnum.BUY_MARKET) {
      InputChecker.checker()
          .shouldNull(request.getPrice(), "Price");
    }
    UrlParamsBuilder builder = UrlParamsBuilder.build()
        .putToPost("account-id", request.getAccountId())
        .putToPost("amount", request.getAmount())
        .putToPost("price", request.getPrice())
        .putToPost("symbol", request.getSymbol())
        .putToPost("type", request.getType().getCode())
        .putToPost("client-order-id", request.getClientOrderId())
        .putToPost("source", request.getOrderSource().getCode())
        .putToPost("stop-price", request.getStopPrice())
        .putToPost("operator", request.getOperator() == null ? null : request.getOperator().getOperator());

    JSONObject jsonObject = restConnection.executePostWithSignature(CREATE_ORDER_PATH, builder);
    return jsonObject.getLong("data");
  }
//
//  @Override
//  public Long cancelOrder(Long orderId) {
//
//    InputChecker.checker()
//        .shouldNotNull(orderId, "order-id");
//
//    String path = CANCEL_ORDER_PATH.replace("{order-id}", orderId + "");
//
//    JSONObject jsonObject = restConnection.executePostWithSignature(path, UrlParamsBuilder.build());
//    return jsonObject.getLong("data");
//  }
//
//  @Override
//  public Integer cancelOrder(String clientOrderId) {
//
//    InputChecker.checker()
//        .shouldNotNull(clientOrderId, "client-order-id");
//
//    UrlParamsBuilder builder = UrlParamsBuilder.build();
//    builder.putToPost("client-order-id", clientOrderId);
//
//    JSONObject jsonObject = restConnection.executePostWithSignature(CANCEL_ORDER_BY_CLIENT_ORDER_ID_PATH, builder);
//    return jsonObject.getInteger("data");
//  }
//
//  @Override
//  public BatchCancelOpenOrdersResult batchCancelOpenOrders(BatchCancelOpenOrdersRequest request) {
//
//    InputChecker.checker()
//        .shouldNotNull(request.getAccountId(), "account-id");
//    if (request.getSize() != null) {
//      InputChecker.checker()
//          .checkRange(request.getSize(), 1, 100, "size");
//    }
//
//    UrlParamsBuilder builder = UrlParamsBuilder.build()
//        .putToPost("account-id", request.getAccountId())
//        .putToPost("symbol", request.getSymbol())
//        .putToPost("side", request.getSide() == null ? null : request.getSide().getCode())
//        .putToPost("size", request.getSize());
//
//    JSONObject jsonObject = restConnection.executePostWithSignature(BATCH_CANCEL_OPEN_ORDERS_PATH, builder);
//    JSONObject data = jsonObject.getJSONObject("data");
//    return new BatchCancelOpenOrdersResultParser().parse(data);
//  }
//
//  @Override
//  public BatchCancelOrderResult batchCancelOrder(List<Long> ids) {
//
//    InputChecker.checker()
//        .shouldNotNull(ids, "order-ids")
//        .checkList(ids, 1, 50, "orderIds");
//
//    List<String> stringList = new LinkedList<>();
//    for (Object obj : ids) {
//      stringList.add(obj.toString());
//    }
//    UrlParamsBuilder builder = UrlParamsBuilder.build()
//        .putToPost("order-ids", stringList);
//
//    JSONObject jsonObject = restConnection.executePostWithSignature(BATCH_CANCEL_ORDERS_PATH, builder);
//    JSONObject data = jsonObject.getJSONObject("data");
//    return new BatchCancelOrderResultParser().parse(data);
//  }
//
//  @Override
//  public List<Order> getOpenOrders(OpenOrdersRequest request) {
//
//    InputChecker.checker()
//        .checkSymbol(request.getSymbol())
//        .shouldNotNull(request.getAccountId(), "account-id")
//        .checkRange(request.getSize(), 1, 500, "size");
//
//
//    UrlParamsBuilder builder = UrlParamsBuilder.build()
//        .putToUrl("account-id", request.getAccountId())
//        .putToUrl("symbol", request.getSymbol())
//        .putToUrl("side", request.getSide() == null ? null : request.getSide().getCode())
//        .putToUrl("size", request.getSize())
//        .putToUrl("direct", request.getDirect() == null ? null : request.getDirect().getCode())
//        .putToUrl("from", request.getFrom());
//
//    JSONObject jsonObject = restConnection.executeGetWithSignature(GET_OPEN_ORDERS_PATH, builder);
//    JSONArray data = jsonObject.getJSONArray("data");
//    return new OrderParser().parseArray(data);
//  }
//
//  @Override
//  public Order getOrder(Long orderId) {
//
//    InputChecker.checker()
//        .shouldNotNull(orderId, "order-id");
//
//    String path = GET_ORDER_PATH.replace("{order-id}", orderId + "");
//
//    JSONObject jsonObject = restConnection.executeGetWithSignature(path, UrlParamsBuilder.build());
//    JSONObject data = jsonObject.getJSONObject("data");
//    return new OrderParser().parse(data);
//  }
//
//  @Override
//  public Order getOrder(String clientOrderId) {
//
//    InputChecker.checker()
//        .shouldNotNull(clientOrderId, "client-order-id");
//
//    UrlParamsBuilder builder = UrlParamsBuilder.build();
//    builder.putToUrl("clientOrderId", clientOrderId);
//
//    JSONObject jsonObject = restConnection.executeGetWithSignature(GET_ORDER_BY_CLIENT_ORDER_ID_PATH, builder);
//    JSONObject data = jsonObject.getJSONObject("data");
//    return new OrderParser().parse(data);
//  }
//
//  @Override
//  public List<Order> getOrders(OrdersRequest request) {
//
//    InputChecker.checker()
//        .shouldNotNull(request.getSymbol(), "symbol")
//        .checkList(request.getStates(), 1, 100, "states");
//
//    UrlParamsBuilder builder = UrlParamsBuilder.build()
//        .putToUrl("symbol", request.getSymbol())
//        .putToUrl("types", request.getTypesString())
//        .putToUrl("start-date", request.getStartDate(), "yyyy-MM-dd")
//        .putToUrl("end-date", request.getEndDate(), "yyyy-MM-dd")
//        .putToUrl("from", request.getStartId())
//        .putToUrl("states", request.getStatesString())
//        .putToUrl("size", request.getSize())
//        .putToUrl("direct", request.getDirect() == null ? null : request.getDirect().getCode());
//
//    JSONObject jsonObject = restConnection.executeGetWithSignature(GET_ORDERS_PATH, builder);
//    JSONArray array = jsonObject.getJSONArray("data");
//    return new OrderParser().parseArray(array);
//  }
//
//  @Override
//  public List<Order> getOrdersHistory(OrderHistoryRequest request) {
//
//    UrlParamsBuilder builder = UrlParamsBuilder.build()
//        .putToUrl("symbol", request.getSymbol())
//        .putToUrl("start-time", request.getStartTime())
//        .putToUrl("end-time", request.getEndTime())
//        .putToUrl("size", request.getSize())
//        .putToUrl("direct", request.getDirection() == null ? null : request.getDirection().getCode());
//
//    JSONObject jsonObject = restConnection.executeGetWithSignature(GET_ORDERS_HISTORY_PATH, builder);
//    JSONArray array = jsonObject.getJSONArray("data");
//    return new OrderParser().parseArray(array);
//  }
//
//  @Override
//  public List<MatchResult> getMatchResult(Long orderId) {
//
//    InputChecker.checker()
//        .shouldNotNull(orderId, "order-id");
//
//    String path = GET_SINGLE_ORDER_MATCH_RESULT_PATH.replace("{order-id}", orderId + "");
//
//    JSONObject jsonObject = restConnection.executeGetWithSignature(path, UrlParamsBuilder.build());
//    JSONArray array = jsonObject.getJSONArray("data");
//    return new MatchResultParser().parseArray(array);
//  }
//
//  @Override
//  public List<MatchResult> getMatchResults(MatchResultRequest request) {
//
//    InputChecker.checker()
//        .shouldNotNull(request.getSymbol(), "symbol");
//
//    UrlParamsBuilder builder = UrlParamsBuilder.build()
//        .putToUrl("symbol", request.getSymbol())
//        .putToUrl("types", request.getTypeString())
//        .putToUrl("start-date", request.getStartDate(), "yyyy-MM-dd")
//        .putToUrl("end-date", request.getEndDate(), "yyyy-MM-dd")
//        .putToUrl("from", request.getFrom())
//        .putToUrl("direct", request.getDirection() == null ? null : request.getDirection().getCode())
//        .putToUrl("size", request.getSize());
//
//    JSONObject jsonObject = restConnection.executeGetWithSignature(GET_MATCH_RESULT_PATH, builder);
//    JSONArray array = jsonObject.getJSONArray("data");
//    return new MatchResultParser().parseArray(array);
//  }
//
//  @Override
//  public List<FeeRate> getFeeRate(FeeRateRequest request) {
//
//    InputChecker.checker()
//        .shouldNotNull(request.getSymbols(), "symbol");
//
//    UrlParamsBuilder builder = UrlParamsBuilder.build()
//        .putToUrl("symbols", request.getSymbols());
//
//    JSONObject jsonObject = restConnection.executeGetWithSignature(GET_FEE_RATE_PATH, builder);
//    JSONArray array = jsonObject.getJSONArray("data");
//    return new FeeRateParser().parseArray(array);
//  }
//
//
//  public void subOrderUpdateV2(SubOrderUpdateV2Request request, ResponseCallback<OrderUpdateV2Event> callback) {
//    // 检查参数
//    InputChecker.checker()
//        .shouldNotNull(request.getSymbols(), "symbols");
//
//    // 格式化symbol为数组
//    List<String> symbolList = SymbolUtils.parseSymbols(request.getSymbols());
//
//    // 检查数组
//    InputChecker.checker().checkSymbolList(symbolList);
//
//    List<String> commandList = new ArrayList<>(symbolList.size());
//    symbolList.forEach(symbol -> {
//
//      String topic = WEBSOCKET_ORDER_UPDATE_V2_TOPIC
//          .replace("${symbol}", symbol);
//
//      JSONObject command = new JSONObject();
//      command.put("action", WebSocketConstants.ACTION_SUB);
//      command.put("ch", topic);
//      command.put("id", System.nanoTime());
//      commandList.add(command.toJSONString());
//    });
//
//    HuobiWebSocketConnection.createAssetV2Connection(options, commandList, new OrderUpdateEventV2Parser(), callback, false);
//  }
//
//  public void subTradeClearing(SubTradeClearingRequest request, ResponseCallback<TradeClearingEvent> callback) {
//    // 检查参数
//    InputChecker.checker()
//        .shouldNotNull(request.getSymbols(), "symbols");
//
//    // 格式化symbol为数组
//    List<String> symbolList = SymbolUtils.parseSymbols(request.getSymbols());
//
//    // 检查数组
//    InputChecker.checker().checkSymbolList(symbolList);
//
//    List<String> commandList = new ArrayList<>(symbolList.size());
//    symbolList.forEach(symbol -> {
//
//      String topic = WEBSOCKET_TRADE_CLEARING_TOPIC
//          .replace("${symbol}", symbol);
//
//      JSONObject command = new JSONObject();
//      command.put("action", WebSocketConstants.ACTION_SUB);
//      command.put("ch", topic);
//      command.put("id", System.nanoTime());
//      commandList.add(command.toJSONString());
//    });
//
//    HuobiWebSocketConnection.createAssetV2Connection(options, commandList, new TradeClearingEventParser(), callback, false);
//
//  }

}
