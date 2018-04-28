package com.schrondinger.quin.https.api;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import com.schrondinger.quin.bean.CountryRegionResult;
import com.schrondinger.quin.bean.Result;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * 此类是模拟网络请求的过程，有参数输入，以及输出
 * Created by HP on 2018/1/16.
 */

public class LocalApi {

    public static Observable<Result<CountryRegionResult>> getCountryRegionList(Context context) {
        try {
            InputStream inStream = context.getAssets().open("country_code.xml");

            CountryRegionResult.CountryRegionListBean countryRegion = null;
            List<CountryRegionResult.CountryRegionListBean> countryRegions = null;
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser pullParser = factory.newPullParser();

            pullParser.setInput(inStream, "UTF-8");
            int event = pullParser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:
                        countryRegions = new ArrayList<>();
                        break;
                    case XmlPullParser.START_TAG:
                        if ("Country_Region".equals(pullParser.getName())) {
                            countryRegion = new CountryRegionResult.CountryRegionListBean();
                            countryRegion.setE_Name(pullParser.getAttributeValue(0));
                            countryRegion.setC_Name(pullParser.getAttributeValue(1));
                            countryRegion.setIDNA(pullParser.getAttributeValue(2));
                            countryRegion.setCode(pullParser.getAttributeValue(3));
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        Log.i("pullParser", "END_TAG: "+pullParser.getName());
                        if ("Country_Region".equals(pullParser.getName())) {
                            countryRegions.add(countryRegion);
                            countryRegion = null;
                        }
                        break;
                }
                event = pullParser.next();
            }
            CountryRegionResult countryRegionResult = new CountryRegionResult();
            countryRegionResult.setCountryRegionList(countryRegions);
            Result<CountryRegionResult> result = new Result<>();
            result.setErrorcode("0");
            result.setErrormsg("");
            result.setData(countryRegionResult);
            return Observable.just(result);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return null;
    }
}
