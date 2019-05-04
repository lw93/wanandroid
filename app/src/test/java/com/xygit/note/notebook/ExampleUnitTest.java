package com.xygit.note.notebook;

import android.test.ApplicationTestCase;

import com.xygit.note.notebook.base.BaseApplication;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest extends ApplicationTestCase<BaseApplication>{
    public ExampleUnitTest(Class<BaseApplication> applicationClass) {
        super(applicationClass);
    }

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void getSing(){

    }
}