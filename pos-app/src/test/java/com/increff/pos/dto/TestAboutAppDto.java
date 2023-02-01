package com.increff.pos.dto;

import com.increff.pos.model.AboutAppData;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestAboutAppDto extends AbstractUnitTest{

    @Autowired
    private final AboutAppDto aboutAppDto = new AboutAppDto();

    @Test
    public void testAboutApp(){
        AboutAppData data = aboutAppDto.getDetails();
        Assert.assertEquals(data.getName(),"Pos Application");
        Assert.assertEquals(data.getVersion(),"1.0");
    }
}
