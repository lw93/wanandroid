package com.xygit.note.notebook.adv;

/**
 * @author Created by xiuyaun
 * @time on 2019/6/1
 */

public enum MiAdvType {
    banner("802e356f1726f9ff39c69308bfd6f06a"),
    horizontalInsertScreen("1d576761b7701d436f5a9253e7cf9572"),
    verticalInsertScreen("67b05e7cc9533510d4b8d9d4d78d0ae9"),
    horizontalOpenScreen("94f4805a2d50ba6e853340f9035fda18"),
    verticalOpenScreen("b373ee903da0c6fc9c9da202df95a500"),
    informationFlowBigMap("2cae1a1f63f60185630f78a1d63923b0"),
    informationFlowSmallMap("0c220d9bf7029e71461f247485696d07"),
    informationFlowGroupMap("b38f454156852941f3883c736c79e7e1"),
    informationFlowVideoMap("0bd963002ece0fca0bfff800fb6beca5"),
    patchAdvertising("20c070adf42787a99f8146881a640306"),
    floatingBallAdvertising("20c070adf42787a99f8146881a640306"),
    incentiveAdvertising("6d089fcf31523ea73ca94138571ed31e");

    private String advId;

    MiAdvType(String id) {
        this.advId = id;
    }
}
