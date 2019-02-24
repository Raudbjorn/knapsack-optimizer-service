package database.util;

public class Numbers {

    private Numbers(){}

    public static Long toLong(Object object){
        if(null == object) return null;

        return ((Number)object).longValue();
    }

}
