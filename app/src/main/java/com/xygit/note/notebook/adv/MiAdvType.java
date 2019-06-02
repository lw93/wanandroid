package com.xygit.note.notebook.adv;

/**
 * @author Created by xiuyaun
 * @time on 2019/6/1
 */

public enum MiAdvType {
    banner("802e356f1726f9ff39c69308bfd6f06a"),//62b4f114b36e136ab31feee0beb37186
    horizontalInsertScreen("1d576761b7701d436f5a9253e7cf9572"),
    verticalInsertScreen("67b05e7cc9533510d4b8d9d4d78d0ae9"),//cfffd807e8d8597107cddc3bd2269591
    horizontalOpenScreen("94f4805a2d50ba6e853340f9035fda18"),
    verticalOpenScreen("b373ee903da0c6fc9c9da202df95a500"),//1153a6f497afb1f2e6a8a3c9ff479867
    informationFlowBigMap("2cae1a1f63f60185630f78a1d63923b0"),
    informationFlowSmallMap("0c220d9bf7029e71461f247485696d07"),//b6535105e07950f74a4f16b28180944e
    informationFlowGroupMap("b38f454156852941f3883c736c79e7e1"),
    informationFlowVideoMap("0bd963002ece0fca0bfff800fb6beca5"),
    patchAdvertising("20c070adf42787a99f8146881a640306"),
    floatingBallAdvertising("20c070adf42787a99f8146881a640306"),//3e2702e44a1b42aaaa352effd4bb9577
    incentiveAdvertising("6d089fcf31523ea73ca94138571ed31e");//50f3b88e194e2f3b60d5b468bc93e98f

    public String getAdvId() {
        return advId;
    }

    private String advId;

    MiAdvType(String id) {
        this.advId = id;
    }
}
