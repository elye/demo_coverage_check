package com.elyeproj.testingcoverage;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MainPresenterTest {

    private MainPresenter presenter;
    private TestView testView = new TestView();

    @Before
    public void setup() {
        presenter = new MainPresenter(testView);
    }

    @ Test
    public void givenTwoNumbersWhenAddThenSumResult() {
        // Given
        String num1 = "10";
        String num2 = "17";

        // When
        presenter.add(num1, num2);

        // Then
        assertEquals(testView.result, "27");
    }

    @ Test
    public void givenFirstNonNumberWhenAddThenBadResult() {
        // Given
        String num1 = "";
        String num2 = "17";

        // When
        presenter.add(num1, num2);

        // Then
        assertEquals(testView.result, testView.getIllegalResult());
    }

    @ Test
    public void givenSecondNonNumberWhenAddThenBadResult() {
        // Given
        String num1 = "22";
        String num2 = "";

        // When
        presenter.add(num1, num2);

        // Then
        assertEquals(testView.result, testView.getIllegalResult());
    }
}