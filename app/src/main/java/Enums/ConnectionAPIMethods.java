package Enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by User on 18/6/14.
 */
public enum ConnectionAPIMethods {

    couriersList(0),modifyCourier(1),couriersDetect(2),createTrackings(3),getTrackings(4),modifyInfo(5),deleteTrackings(6),stopUpdate(7),remote(8),status(9),transitTime(10),realtime(11),getUserInfo(12),airCargo(13);
    private final int numberMethod;

    ConnectionAPIMethods(int numberMethod){
        this.numberMethod = numberMethod;
    }

    public int getNumberMethod(){

        return this.numberMethod;
    }
    /**
     * key:枚举值name，即业务标识。
     * value：枚举值对象。
     */
    private static final Map<String, ConnectionAPIMethods> mapping = new HashMap<>(16);
    static {
        for (ConnectionAPIMethods apiMethodsEnum : values()) {
            mapping.put(apiMethodsEnum.name(), apiMethodsEnum);
        }
    }
    /**
     * 根据枚举对象名 获取泛枚举值对象(线程安全)
     *
     * @param enumName 枚举值的名字，不区分大小写
     * @return 若上送参数为null，则返回null，
     * 若上送的业务标识在mapping缓存，返回对应的枚举值对象。
     * 否则返回缺省 枚举值对象。
     */
    public static ConnectionAPIMethods resolve(String enumName) {
        return enumName != null ? mapping.getOrDefault(enumName, couriersList) : null;
    }

}
