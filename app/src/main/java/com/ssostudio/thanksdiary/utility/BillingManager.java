package com.ssostudio.thanksdiary.utility;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.ssostudio.thanksdiary.preferences.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

public class BillingManager implements PurchasesUpdatedListener {

    final String TAG = "IN-APP-BILLING";

    private BillingClient _billingClient;
    private Activity _activity;
    private ConsumeResponseListener _consumeResListener;
    private AcknowledgePurchaseResponseListener _AcknowledgePurchaseResponseListener;

    public enum connectStatusTypes {waiting, connected, fail, disconnected}

    public connectStatusTypes connectStatus = connectStatusTypes.waiting;
    public List<SkuDetails> _skuDetailsList;

    private SkuDetails premiumUpgradeSku;
    private SkuDetails sponsor1000Sku;
    private String premiumUpgradePrice;
    private String sponsor1000Price;

    public BillingManager(Activity activity) {
        _activity = activity;

        // 결제를 위한 , 빌링 클라이언트를 생성
        _billingClient = BillingClient.newBuilder(_activity).setListener(this).enablePendingPurchases().build();

        // 구글 플레이와 연결을 시도
        _billingClient.startConnection(new BillingClientStateListener() {

            // 결제 작업 완료 가능한 상태.
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // 접속이 성공한 경우, 처리
                    connectStatus = connectStatusTypes.connected;
                    Log.d(TAG, "구글 결제 서버에 접속 성공");
                    getSkuDetailList();
                } else {
                    // 접속이 실패한 경우, 처리
                    connectStatus = connectStatusTypes.fail;
                    Log.d(TAG, "구글 결제 서버에 접속 실패 : " + billingResult.getResponseCode());
                }
            }

            //결제 작업 중, 구글 서버와 연결이 끊어진 상태.
            @Override
            public void onBillingServiceDisconnected() {
                connectStatus = connectStatusTypes.disconnected;
                Log.d(TAG, "구글 결제 서버에 접속이 끊어졌습니다.");
            }
        });

        // 소모성 상품을 소모한 후, 응답 받는 메소드
        _consumeResListener = new ConsumeResponseListener() {
            @Override
            public void onConsumeResponse(BillingResult billingResult, String s) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    //성공적으로 아이템 소모
                    Log.d(TAG, "상품을 성공적으로 소모하였습니다. 소모된 상품 : " + s);
                    return;
                } else {
                    //상품 소모에 실패
                    Log.d(TAG, "상품 소모에 실패하였습니다. 오류코드 : " + billingResult.getResponseCode() + " : " + s);
                    return;
                }
            }
        };

        _AcknowledgePurchaseResponseListener = new AcknowledgePurchaseResponseListener() {
            @Override
            public void onAcknowledgePurchaseResponse(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    //구매 확인 완료
                    setBillingIsPremium();
                    Log.d(TAG, "구매 확인 ");
                    return;
                } else {
                    //구매 확인 실패
                    Log.d(TAG, "구매 확인에 실패하였습니다. 오류코드 : " + billingResult.getResponseCode());
                    return;
                }
            }
        };

    }

    public void getSkuDetailList() {
        // 구글 삼품 정보들의 ID를 만들어 준다.
        final List<String> skuIdList = new ArrayList<>();
        skuIdList.add("premium");
        skuIdList.add("sponsor1000");

        //SkuDetailsParam 객체를 만들어 줍니다. (1회성 소모품에 대한)
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuIdList).setType(BillingClient.SkuType.INAPP);

        // 비동기 상태로 앱의. 정보를 가지고 옵니다.
        _billingClient.querySkuDetailsAsync(params.build(), new SkuDetailsResponseListener() {
            @Override
            public void onSkuDetailsResponse(BillingResult billingResult, List<SkuDetails> list) {
                //상품 정보를 가지고 오지 못한 경우, 오류를 반환하고 종료
                if (billingResult.getResponseCode() != BillingClient.BillingResponseCode.OK) {
                    Log.d(TAG, "상품 정보를 가지고 오던 중 오류를 만났습니다. 오류코드 : " + billingResult.getResponseCode());
                    return;
                }

                //하나의 상품 정보도 가지고 오지 못했습니다.
                if (list == null || list.size() == 0) {
                    Log.d(TAG, "상품 정보가 존재하지 않습니다.");
                    return;
                }

                //응답 받은 데이터들의 숫자를 출력합니다.
                Log.d(TAG, "응답 받은 데이터 숫자: " + list.size());

                // 해당 인덱스의 상품 정보를 출력하도록 합니다. 확인 용
                for (int i = 0; i < list.size(); i++) {
                    SkuDetails skuDetails = list.get(i);
                    String sku = skuDetails.getSku();
                    String price = skuDetails.getPrice();
                    if ("premium".equals(sku)) {
                        premiumUpgradeSku = skuDetails;
                        premiumUpgradePrice = price;
                    } else if ("sponsor1000".equals(sku)) {
                        sponsor1000Sku = skuDetails;
                        sponsor1000Price = price;
                    }

                    Log.d(TAG, skuDetails.getSku() + " : " + skuDetails.getTitle() + " : " + skuDetails.getPrice());
                    Log.d(TAG, skuDetails.getOriginalJson());
                }

                // 받은 값을 멤버 변수로 저장합니다.
                _skuDetailsList = list;
            }
        });
    }

    public SkuDetails getPremiumUpgradeSku() {
        return premiumUpgradeSku;
    }

    public SkuDetails getSponsor1000Sku() {
        return sponsor1000Sku;
    }

    public String getPremiumUpgradePrice() {
        return premiumUpgradePrice;
    }

    public String getSponsor1000Price() {
        return sponsor1000Price;
    }

    public void onPurchaseHistoryRestored() {
        Purchase.PurchasesResult purchasesResult = _billingClient.queryPurchases(BillingClient.SkuType.INAPP);
        Log.d(TAG, "구매 내역 복원 : " + purchasesResult.getPurchasesList().size());
        for (Purchase result : purchasesResult.getPurchasesList()) {
            setBillingIsPremium(result);
            Log.d(TAG, "구매 내역 : " + result.getSku() + " : " + result.getPurchaseToken());
        }
    }

    public boolean isPremiumCheck(){
        boolean isPremium = false;
        Purchase.PurchasesResult purchasesResult = _billingClient.queryPurchases(BillingClient.SkuType.INAPP);
        Log.d(TAG, "isPremiumCheck : " + purchasesResult.getPurchasesList().size());
        for (Purchase result : purchasesResult.getPurchasesList()) {
            if (result.getSku().equals("premium")){
                isPremium = true;
            }
            Log.d(TAG, "구매 내역 : " + result.getSku() + " : " + result.getPurchaseToken());
        }
        return isPremium;
    }

    public BillingResult purchase(SkuDetails skuDetails) {
        BillingFlowParams flowParams = BillingFlowParams.newBuilder().setSkuDetails(skuDetails).build();
        return _billingClient.launchBillingFlow(_activity, flowParams);
    }

    //결제 처리를 하는 메소드
    @Override
    public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> purchases) {

        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
            Log.d(TAG, "결제 성공");

            for (Purchase pur : purchases) {
                Log.d(TAG, "구매 상품 내역 : " + pur.getOriginalJson());

                if (pur.getSku().equals("premium")){
                    AcknowledgePurchaseParams.Builder params = AcknowledgePurchaseParams.newBuilder();
                    params.setPurchaseToken(pur.getPurchaseToken());
                    params.setDeveloperPayload(pur.getDeveloperPayload());
                    _billingClient.acknowledgePurchase(params.build(), _AcknowledgePurchaseResponseListener);
                }else{
                    ConsumeParams.Builder params = ConsumeParams.newBuilder();
                    params.setPurchaseToken(pur.getPurchaseToken());
                    params.setDeveloperPayload(pur.getDeveloperPayload());
                    _billingClient.consumeAsync(params.build(), _consumeResListener);
                }
            }
        } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
            // 사용자 결제 취소
            Log.d(TAG, "사용자에 의해 결제가 취소");
        } else {
            //그 외에 다른 결제 실패
            Log.d(TAG, "결제가 취소 되었습니다. 종료 코드 : " + billingResult.getResponseCode());
        }

    }

    private void setBillingIsPremium(Purchase purchase){
        Context context = _activity.getApplicationContext();
        if (purchase.getSku().equals("premium") && !PreferenceManager.getIsPremium(context)){
            PreferenceManager.setIsPremium(context, true);
            AppUtility.restartApp(context);
        }
    }

    private void setBillingIsPremium(){
        Context context = _activity.getApplicationContext();
        if (!PreferenceManager.getIsPremium(context)){
            PreferenceManager.setIsPremium(context, true);
            AppUtility.restartApp(context);
        }
    }

}
