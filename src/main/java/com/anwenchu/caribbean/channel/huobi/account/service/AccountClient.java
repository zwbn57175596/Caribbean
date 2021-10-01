package com.anwenchu.caribbean.channel.huobi.account.service;


import com.anwenchu.caribbean.channel.huobi.account.request.AccountBalanceRequest;
import com.anwenchu.caribbean.channel.huobi.account.response.AccountBalance;

public interface AccountClient {

//  /**
//   * Get User Account List
//   * @return
//   */
//  List<Account> getAccounts();

  /**
   * Get User Account Balance
   * @param request
   * @return
   */
  AccountBalance getAccountBalance(AccountBalanceRequest request);

//  List<AccountHistory> getAccountHistory(AccountHistoryRequest request);
//
//  AccountLedgerResult getAccountLedger(AccountLedgerRequest request);
//
//  AccountTransferResult accountTransfer(AccountTransferRequest request);
//
//  AccountFuturesTransferResult accountFuturesTransfer(AccountFuturesTransferRequest request);
//
//  Point getPoint(PointRequest request);
//
//  PointTransferResult pointTransfer(PointTransferRequest request);
//
//  AccountAssetValuationResult accountAssetValuation(AccountAssetValuationRequest request);
//
//  void subAccountsUpdate(SubAccountUpdateRequest request, ResponseCallback<AccountUpdateEvent> callback);
//
//  static AccountClient create(Options options) {
//
//    if (options.getExchange().equals(ExchangeEnum.HUOBI)) {
//      return new HuobiAccountService(options);
//    }
//    throw new SDKException(SDKException.INPUT_ERROR, "Unsupport Exchange.");
//  }
}
