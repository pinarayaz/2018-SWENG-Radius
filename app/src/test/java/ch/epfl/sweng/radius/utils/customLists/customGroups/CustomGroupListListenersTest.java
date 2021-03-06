package ch.epfl.sweng.radius.utils.customLists.customGroups;

import android.view.View;
import android.widget.LinearLayout;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import ch.epfl.sweng.radius.utils.MapUtilityTest;

import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doAnswer;

public class CustomGroupListListenersTest {

    private LinearLayout mockedView = Mockito.mock(LinearLayout.class);
    private CustomGroupListListeners test;

    @Before
    public void setUp() throws Exception {

//        Database.activateDebugMode();

        final ArgumentCaptor<View.OnClickListener> argument = ArgumentCaptor.forClass(View.OnClickListener.class);

        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                verify(mockedView).setOnClickListener(argument.capture());
                View.OnClickListener answer = argument.getValue();
                answer.onClick(null);
                return null;
            }

        }).when(mockedView).setOnClickListener(argument.capture());

    }
    @After
    public void tearDown() throws Exception {
        test = new CustomGroupListListeners("EPFL", "EPFL", "EPFL");
        test.setCustomOnClick(mockedView, MapUtilityTest.context);
    }

    @Test
    public void onBindViewHolder() {
    }
}