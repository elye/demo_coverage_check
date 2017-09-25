package com.elyeproj.testingcoverage;

import java.util.Locale;

class MainPresenter {
    private MainView view;

    MainPresenter(MainView view) {
        this.view = view;
    }

    void add(String num1, String num2) {
        if (isNumeric(num1) && isNumeric(num2)) {
            view.showResult(addValueToString(Integer.parseInt(num1), Integer.parseInt(num2)));
        } else {
            view.showResult(view.getIllegalResult());
        }
    }

    private String addValueToString(int num1, int num2) {
        return String.format(Locale.US, "%d", num1 + num2);
    }

    private boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
        }
        catch(NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
