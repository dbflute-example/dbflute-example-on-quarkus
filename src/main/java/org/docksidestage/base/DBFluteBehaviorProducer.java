package org.docksidestage.base;

import com.google.inject.Inject;
import jakarta.enterprise.context.ApplicationScoped;
import org.docksidestage.dbflute.exbhv.*;

/**
 * Class that allows Google Guice to DI and generate Behavior instances and DI them to CDI
 */
public class DBFluteBehaviorProducer {

  @Inject MemberBhv memberBhv;

  @Inject MemberAddressBhv memberAddressBhv;

  @Inject MemberFollowingBhv memberFollowingBhv;

  @Inject MemberLoginBhv memberLoginBhv;

  @Inject MemberSecurityBhv memberSecurityBhv;

  @Inject MemberServiceBhv memberServiceBhv;

  @Inject MemberStatusBhv memberStatusBhv;

  @Inject MemberWithdrawalBhv memberWithdrawalBhv;

  @Inject ProductBhv productBhv;

  @Inject ProductCategoryBhv productCategoryBhv;

  @Inject ProductStatusBhv productStatusBhv;

  @Inject PurchaseBhv purchaseBhv;

  @Inject PurchasePaymentBhv purchasePaymentBhv;

  @Inject RegionBhv regionBhv;

  @Inject ServiceRankBhv serviceRankBhv;

  @Inject WithdrawalReasonBhv withdrawalReasonBhv;

  @ApplicationScoped
  public MemberBhv getMemberBhv() {
    return GuiceComponents.find(this.getClass()).memberBhv;
  }

  @ApplicationScoped
  public MemberAddressBhv getMemberAddressBhv() {
    return GuiceComponents.find(this.getClass()).memberAddressBhv;
  }

  @ApplicationScoped
  public MemberFollowingBhv getMemberFollowingBhv() {
    return GuiceComponents.find(this.getClass()).memberFollowingBhv;
  }

  @ApplicationScoped
  public MemberLoginBhv getMemberLoginBhv() {
    return GuiceComponents.find(this.getClass()).memberLoginBhv;
  }

  @ApplicationScoped
  public MemberSecurityBhv getMemberSecurityBhv() {
    return GuiceComponents.find(this.getClass()).memberSecurityBhv;
  }

  @ApplicationScoped
  public MemberServiceBhv getMemberServiceBhv() {
    return GuiceComponents.find(this.getClass()).memberServiceBhv;
  }

  @ApplicationScoped
  public MemberStatusBhv getMemberStatusBhv() {
    return GuiceComponents.find(this.getClass()).memberStatusBhv;
  }

  @ApplicationScoped
  public MemberWithdrawalBhv getMemberWithdrawalBhv() {
    return GuiceComponents.find(this.getClass()).memberWithdrawalBhv;
  }

  @ApplicationScoped
  public ProductBhv getProductBhv() {
    return GuiceComponents.find(this.getClass()).productBhv;
  }

  @ApplicationScoped
  public ProductCategoryBhv getProductCategoryBhv() {
    return GuiceComponents.find(this.getClass()).productCategoryBhv;
  }

  @ApplicationScoped
  public ProductStatusBhv getProductStatusBhv() {
    return GuiceComponents.find(this.getClass()).productStatusBhv;
  }

  @ApplicationScoped
  public PurchaseBhv getPurchaseBhv() {
    return GuiceComponents.find(this.getClass()).purchaseBhv;
  }

  @ApplicationScoped
  public PurchasePaymentBhv getPurchasePaymentBhv() {
    return GuiceComponents.find(this.getClass()).purchasePaymentBhv;
  }

  @ApplicationScoped
  public RegionBhv getRegionBhv() {
    return GuiceComponents.find(this.getClass()).regionBhv;
  }

  @ApplicationScoped
  public ServiceRankBhv getServiceRankBhv() {
    return GuiceComponents.find(this.getClass()).serviceRankBhv;
  }

  @ApplicationScoped
  public WithdrawalReasonBhv getWithdrawalReasonBhv() {
    return GuiceComponents.find(this.getClass()).withdrawalReasonBhv;
  }

}