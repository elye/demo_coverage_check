package com.elyeproj.testingcoverage;

/**
 * Created by elisha.lye on 9/25/17.
 */

public class TestView implements MainView {

    public String result;

    @Override
    public void showResult(String result) {
        this.result = result;
    }

    @Override
    public String getIllegalResult() {
        return "Bad result";
    }
}
